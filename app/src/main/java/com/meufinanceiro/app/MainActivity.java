package com.meufinanceiro.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;

    private static final String WEB_APP_URL =
            "https://script.google.com/macros/s/AKfycbynigroKvhJdU7X_CpUzoSSxSk7p439FvrvzOJu19d4eK6AoLTDTUAr_JF72VfKa9-F/exec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);

        setContentView(webView);

        configurarWebView();

        webView.loadUrl(WEB_APP_URL);
    }

    private void configurarWebView() {

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        settings.setLoadsImagesAutomatically(true);

        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request) {

                String url = request.getUrl().toString();

                if (deveAbrirDentroDoApp(url)) {
                    return false;
                }

                abrirNoNavegador(url);

                return true;
            }

            @Override
            @SuppressWarnings("deprecation")
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    String url) {

                if (deveAbrirDentroDoApp(url)) {
                    return false;
                }

                abrirNoNavegador(url);

                return true;
            }
        });
    }

    private boolean deveAbrirDentroDoApp(String url) {

        return url.startsWith("https://script.google.com")
                || url.startsWith("https://script.googleusercontent.com")
                || url.startsWith("https://accounts.google.com");
    }

    private void abrirNoNavegador(String url) {

        try {

            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
            );

            startActivity(intent);

        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && webView.canGoBack()) {

            webView.goBack();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        if (webView != null) {

            webView.stopLoading();

            webView.clearHistory();

            webView.removeAllViews();

            webView.destroy();
        }

        super.onDestroy();
    }
}
