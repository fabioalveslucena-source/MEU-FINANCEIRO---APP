package com.meufinanceiro.app

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    private val webAppUrl =
        "https://script.google.com/macros/s/AKfycbynigroKvhJdU7X_CpUzoSSxSk7p439FvrvzOJu19d4eK6AoLTDTUAr_JF72VfKa9-F/exec"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        configurarWebView()

        webView.loadUrl(webAppUrl)

        configurarBotaoVoltar()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configurarWebView() {

        webView.settings.apply {

            javaScriptEnabled = true

            domStorageEnabled = true

            databaseEnabled = true

            loadsImagesAutomatically = true

            allowFileAccess = true

            allowContentAccess = true

            mixedContentMode =
                WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

            setSupportZoom(false)

            builtInZoomControls = false

            displayZoomControls = false

            useWideViewPort = true

            loadWithOverviewMode = true
        }

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {

                val url = request.url.toString()

                return if (deveAbrirDentroDoApp(url)) {
                    false
                } else {
                    abrirNoNavegador(url)
                    true
                }
            }

            @Deprecated("Deprecated in API 24")
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {

                return if (deveAbrirDentroDoApp(url)) {
                    false
                } else {
                    abrirNoNavegador(url)
                    true
                }
            }
        }
    }

    private fun deveAbrirDentroDoApp(url: String): Boolean {

        return url.startsWith("https://script.google.com") ||
               url.startsWith("https://script.googleusercontent.com") ||
               url.startsWith("https://accounts.google.com")
    }

    private fun abrirNoNavegador(url: String) {

        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )

            startActivity(intent)

        } catch (_: Exception) {
            // Ignora caso não exista navegador disponível.
        }
    }

    private fun configurarBotaoVoltar() {

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {

                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        finish()
                    }
                }
            }
        )
    }

    override fun onDestroy() {

        webView.apply {
            stopLoading()
            clearHistory()
            removeAllViews()
            destroy()
        }

        super.onDestroy()
    }
}
