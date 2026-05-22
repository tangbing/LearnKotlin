import 'dart:io';

import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart' as url_launcher;
import 'package:webview_flutter/webview_flutter.dart';
import 'package:webview_flutter_android/webview_flutter_android.dart';
import 'package:webview_flutter_wkwebview/webview_flutter_wkwebview.dart';

class WinPay extends StatefulWidget {
  static Future start(BuildContext context, {String? redirectUrl}) {
    return Navigator.push(context, MaterialPageRoute(builder: (context) {
      return WinPay(
        redirectUrl: redirectUrl,
      );
    }));
  }

  final String? redirectUrl;

  const WinPay({Key? key, this.redirectUrl}) : super(key: key);

  @override
  State<WinPay> createState() => _WinPayState();
}

class _WinPayState extends State<WinPay> {
  late final WebViewController _controller;
  final _WinPayLifecycleObserver _observer = _WinPayLifecycleObserver();
  int _progress = 0;
  bool _waitForReturnFromExternalPay = false;

  static const String _wechatHost = 'wx.tenpay.com';
  static const String _wechatH5Path = '/cgi-bin/mmpayweb-bin/checkmweb';
  static const String _paymentResultHost = 'cashier.hkwinninggroup.com';
  static const String _referer = 'https://cashier.hkwinninggroup.com';
  static const String _androidChromeUserAgent =
      'Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 '
      '(KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36';

  @override
  void initState() {
    super.initState();
    _observer.onResumed = _handleResume;
    WidgetsBinding.instance.addObserver(_observer);

    final PlatformWebViewControllerCreationParams params;
    if (Platform.isIOS) {
      params = WebKitWebViewControllerCreationParams(
        allowsInlineMediaPlayback: true,
        mediaTypesRequiringUserAction: const <PlaybackMediaTypes>{},
      );
    } else {
      params = const PlatformWebViewControllerCreationParams();
    }

    final controller = WebViewController.fromPlatformCreationParams(params)
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setBackgroundColor(Colors.white)
      ..setNavigationDelegate(
        NavigationDelegate(
          onProgress: (p) {
            setState(() {
              _progress = p;
            });
          },
          onPageStarted: (String url) {
            debugPrint('Page started loading: $url');
            _maybeCloseForResultUrl(url);
          },
          onPageFinished: (String url) {
            setState(() {
              _progress = 100;
            });
            debugPrint('Page finished loading: $url');
            _maybeCloseForResultUrl(url);
          },
          onHttpError: (HttpResponseError error) {
            debugPrint('Error occurred on page: ${error.response?.statusCode}');
          },
          onUrlChange: (UrlChange change) {
            debugPrint('url change to ${change.url}');
            _maybeCloseForResultUrl(change.url);
          },
          onNavigationRequest: (request) async {
            debugPrint('request.url---${request.url}');
            return _handleNavigationRequest(request.url);
          },
        ),
      )
      ..loadRequest(
        Uri.parse(widget.redirectUrl ?? ''),
        headers: const <String, String>{
          'Referer': _referer,
        },
      );

    _controller = controller;

    if (Platform.isAndroid) {
      _controller.setUserAgent(_androidChromeUserAgent);
    }

    if (_controller.platform is AndroidWebViewController) {
      AndroidWebViewController.enableDebugging(true);
      final androidController =
          _controller.platform as AndroidWebViewController;
      androidController.setMediaPlaybackRequiresUserGesture(false);
    }
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(_observer);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    bool isLoading = _progress >= 0 && _progress < 100;
    return Scaffold(
      resizeToAvoidBottomInset: true,
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Stack(
          children: [
            WebViewWidget(controller: _controller),
            isLoading
                ? const Center(
                    child: CircularProgressIndicator(strokeWidth: 3),
                  )
                : const SizedBox.shrink(),
          ],
        ),
      ),
    );
  }

  Future<NavigationDecision> _handleNavigationRequest(String url) async {
    // _maybeCloseForResultUrl(url);

    // if (_shouldLaunchWechatH5Externally(url)) {
    //   final launched = await _launchExternalUrl(url);
    //   if (launched) {
    //     _waitForReturnFromExternalPay = true;
    //     return NavigationDecision.prevent;
    //   }
    // }

    // if (_isWechatSchemeUrl(url)) {
    //   final launched = await _launchExternalUrl(url);
    //   if (launched) {
    //     _waitForReturnFromExternalPay = true;
    //   }
    //   return NavigationDecision.prevent;
    // }
    //
    // final externalDecision = await _handleExternalProtocol(url);
    // if (externalDecision == NavigationDecision.prevent) {
    //   _waitForReturnFromExternalPay = true;
    //   return externalDecision;
    // }
    //
    // return NavigationDecision.navigate;

    if (url.startsWith("weixin://")) {
      await url_launcher.launchUrl(
        Uri.parse(url),
        mode: url_launcher.LaunchMode.externalApplication,
      );
      return NavigationDecision.prevent; // 阻止 WebView 加载
    }
    return NavigationDecision.navigate;
  }

  bool _shouldLaunchWechatH5Externally(String url) {
    final uri = Uri.tryParse(url);
    return uri?.host == _wechatHost && uri?.path == _wechatH5Path;
  }

  bool _isWechatSchemeUrl(String url) {
    return url.startsWith('weixin://');
  }

  bool _isPaymentResultUrl(String? url) {
    final uri = Uri.tryParse(url ?? '');
    return uri?.host == _paymentResultHost &&
        (uri?.path.startsWith('/transaction/') ?? false);
  }

  void _maybeCloseForResultUrl(String? url) {
    if (!_isPaymentResultUrl(url) || !mounted) return;
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (mounted && Navigator.of(context).canPop()) {
        Navigator.of(context).pop();
      }
    });
  }

  Future<NavigationDecision> _handleExternalProtocol(String url) async {
    if (url.startsWith('http://') || url.startsWith('https://')) {
      return NavigationDecision.navigate;
    }

    final launched = await _launchExternalUrl(url);
    return launched ? NavigationDecision.prevent : NavigationDecision.navigate;
  }

  Future<bool> _launchExternalUrl(String url) async {
    try {
      final targetUri = Uri.parse(url);
      debugPrint('open app out web: $targetUri');
      final launched = await url_launcher.launchUrl(
        targetUri,
        mode: url_launcher.LaunchMode.externalApplication,
      );
      if (launched) {
        return true;
      }

      if (url.startsWith('intent://')) {
        return await _launchIntentFallback(url);
      }
    } catch (e) {
      debugPrint('唤起应用失败: $e');
    }
    return false;
  }

  Future<bool> _launchIntentFallback(String url) async {
    try {
      if (!url.contains('S.browser_fallback_url=')) return false;
      final parts = url.split('S.browser_fallback_url=');
      if (parts.length <= 1) return false;

      var fallbackUrl = parts[1].split(';')[0];
      fallbackUrl = Uri.decodeComponent(fallbackUrl);
      final fallbackUri = Uri.parse(fallbackUrl);
      debugPrint('open app out web: $fallbackUri');

      return await url_launcher.launchUrl(
        fallbackUri,
        mode: url_launcher.LaunchMode.externalApplication,
      );
    } catch (e) {
      debugPrint('解析 Intent 失败: $e');
    }
    return false;
  }

  void _handleResume() {
    if (!_waitForReturnFromExternalPay || !mounted) return;
    _waitForReturnFromExternalPay = false;
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (mounted && Navigator.of(context).canPop()) {
        Navigator.of(context).pop();
      }
    });
  }
}

class _WinPayLifecycleObserver extends WidgetsBindingObserver {
  _WinPayLifecycleObserver();

  VoidCallback? onResumed;

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
    if (state == AppLifecycleState.resumed) {
      onResumed?.call();
    }
  }
}
