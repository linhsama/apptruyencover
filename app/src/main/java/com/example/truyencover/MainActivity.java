package com.example.truyencover;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private String baseUrl = "https://truyencover.herokuapp.com";
    private String key_title = "Truyện Cover";
    private String res = "";
    private WebView myWebView;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initConfig
        initConfig();

        // startWebView
        startWebView(baseUrl);

        // check connect
        checkConnect();

        // swipeRefreshLayout
        swipeRefreshLayout();
    }

    private void initConfig() {

        myWebView = (WebView) findViewById(R.id.webview);
        progressDialog = new ProgressDialog(MainActivity.this);

        Intent intent = getIntent();
        if (intent.hasExtra("IPSERVER")) {
            baseUrl = intent.getStringExtra("IPSERVER");
            Log.e("truyencoverLogcat", baseUrl); // => Mầm non Kim Đồng
        }
    }

    private void swipeRefreshLayout() {
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startWebView(baseUrl);
                swipeRefreshLayout.setRefreshing(false);
            }

        });
    }

    private void startWebView(String url) {

        // settings
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setAllowContentAccess(true);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setGeolocationEnabled(true);
        myWebView.getSettings().setUserAgentString(new WebView(this).getSettings().getUserAgentString());

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);
            }
        });

        myWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.setMessage("Chờ xíu nha...");
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                try {
                    progressDialog.dismiss();
                    res = view.getTitle();
                    if (res.contains(key_title)) {
                        saveIp();
                    }
                    Log.e("truyencoverLogcat", res); // => Kim Đồng

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        // load url in webview
        myWebView.loadUrl(url);
    }

    private void saveIp() {
        SharedPreferences prefs = getSharedPreferences("truyencover", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("key_ip", baseUrl); // Storing string
        editor.apply();
    }

    private void alertDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Lỗi kết nối Server")
                .setMessage("Vui lòng kiểm tra: \n 1.Kết nối WIFI \n 2.Địa chỉ IP ")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, ConnectActivity.class));
                        finish();
                    }
                })
                .setIcon(R.drawable.logo)
                .show();
    }

    private void checkConnect() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // kiểm tra có lưu prefs không
                SharedPreferences prefs = getSharedPreferences("truyencover", MODE_PRIVATE);
                String IPSERVER = prefs.getString("key_ip", null);
                if (IPSERVER == null) {
                    alertDialog();
                }
            }
        }, 5000);
    }
}