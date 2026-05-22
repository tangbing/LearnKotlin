import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:webview_flutter/webview_flutter.dart';

class WinningPayWebViewPage extends StatefulWidget {

  static Future start(BuildContext context, {String? redirectUrl}) {
    return Navigator.push(context, MaterialPageRoute(builder: (context) {
      return WinningPayWebViewPage(
        paymentUrl: redirectUrl ?? '',
      );
    }));
  }

  final String paymentUrl;

  const WinningPayWebViewPage({
    super.key,
    required this.paymentUrl,
  });

  @override
  State<WinningPayWebViewPage> createState() => _WinningPayWebViewPageState();
}

class _WinningPayWebViewPageState extends State<WinningPayWebViewPage> {
  late final WebViewController _controller;

  bool _injectedWxReferer = false;

  static const String winningPayReferer = 'https://cashier.hkwinninggroup.com/';

  @override
  void initState() {
    super.initState();

    _controller = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setNavigationDelegate(
        NavigationDelegate(
          onNavigationRequest: (NavigationRequest request) async {
            final url = request.url;
            final uri = Uri.tryParse(url);

            if (uri == null) {
              return NavigationDecision.navigate;
            }

            debugPrint('WebView navigation: $url');

            // 1. 拦截微信支付 scheme，交给系统微信 App 打开
            if (url.startsWith('weixin://') ||
                url.startsWith('weixin:') ||
                uri.scheme == 'weixin') {
              final target = Uri.parse(url);

              if (await canLaunchUrl(target)) {
                await launchUrl(
                  target,
                  mode: LaunchMode.externalApplication,
                );
              }

              return NavigationDecision.prevent;
            }

            // 2. 如果 WebView 跳到微信 H5 支付中间地址，补 Referer
            // 常见地址：wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb
            if (uri.host == 'wx.tenpay.com' &&
                uri.path.contains('/cgi-bin/mmpayweb-bin/checkmweb') &&
                !_injectedWxReferer) {
              _injectedWxReferer = true;

              await _controller.loadRequest(
                uri,
                headers: const {
                  'Referer': winningPayReferer,
                },
              );

              return NavigationDecision.prevent;
            }

            return NavigationDecision.navigate;
          },
          onWebResourceError: (WebResourceError error) {
            debugPrint('''
WebView error:
code: ${error.errorCode}
type: ${error.errorType}
description: ${error.description}
url: ${error.url}
''');
          },
        ),
      );

    _controller.loadRequest(
      Uri.parse(widget.paymentUrl),
      headers: const {
        'Referer': winningPayReferer,
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('支付'),
      ),
      body: WebViewWidget(controller: _controller),
    );
  }
}