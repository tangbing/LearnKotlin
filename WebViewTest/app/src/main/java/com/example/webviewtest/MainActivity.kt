package com.example.webviewtest

import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"
private const val TARGET_URL = "https://juejin.cn/user/207173399875662"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        val webView = findViewById<WebView>(R.id.button)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                Log.e(
                    TAG,
                    "WebView error: code=${error?.errorCode}, description=${error?.description}, url=${request?.url}"
                )
            }
        }
        webView.loadUrl(TARGET_URL)
    }
}
