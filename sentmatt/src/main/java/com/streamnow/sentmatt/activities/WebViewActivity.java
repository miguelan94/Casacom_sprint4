package com.streamnow.sentmatt.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.streamnow.sentmatt.R;
import com.streamnow.sentmatt.utils.Lindau;

import java.util.Locale;


/**
 * !
 * Created by Miguel Estévez on 31/1/16.
 */
public class WebViewActivity extends BaseActivity {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private WebView webView;
    private static final String TAG = "WebViewActivity";
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale locale = new Locale(Lindau.getInstance().getCurrentSessionUser().userInfo.language);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        setContentView(R.layout.activity_web_view);
        final String webUrlString = getIntent().getStringExtra("web_url");
        String serviceId = getIntent().getStringExtra("service_id");
        String serviceName = getIntent().getStringExtra("service_name");
        String serviceType = getIntent().getStringExtra("type");
        if (webUrlString == null) {
            //finish();
        }
        ImageView shareIcon = (ImageView)findViewById(R.id.share_icon);
        shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, webUrlString);
                startActivity(intent);
            }
        });
        if(serviceType!=null && !serviceType.equals("")){
            if(getIntent().getStringExtra("type").equalsIgnoreCase("document")){
                shareIcon.setVisibility(View.VISIBLE);
                shareIcon.setColorFilter(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.fontColorSmartphone);
            }
        }
        else{
            shareIcon.setVisibility(View.GONE);
        }
        LinearLayout bgnd = (LinearLayout) findViewById(R.id.bar_bgnd);
        ImageView imageView = (ImageView) findViewById(R.id.bgnd_image);
        ImageView leftArrow = (ImageView) findViewById(R.id.image_left_arrow);
        leftArrow.setColorFilter(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.fontColorSmartphone);
        TextView textView_serviceName = (TextView)findViewById(R.id.name_service);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        System.out.println("document " + serviceName);
        this.webView = (WebView) findViewById(R.id.webView);

        if(serviceName!=null && !serviceName.equals("")){
        textView_serviceName.setTextColor(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.fontColorTop);
            textView_serviceName.setText(serviceName);
        }
        else{
            textView_serviceName.setVisibility(View.GONE);
        }
        if (serviceId != null && (serviceId.equals("29") || serviceId.equals("57"))) {
            FrameLayout topFrameLayout = (FrameLayout) findViewById(R.id.webview_top_frame);
            topFrameLayout.setVisibility(View.GONE);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.webView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            this.webView.setLayoutParams(params);
        } else {
            int colorTop = Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop;

            bgnd.setBackgroundColor(colorTop);
            imageView.setColorFilter(colorTop, PorterDuff.Mode.SRC_ATOP);
            imageView.invalidate();

            final GestureDetector gdt = new GestureDetector(new GestureListener());

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(final View view, final MotionEvent event) {
                    gdt.onTouchEvent(event);
                    return true;
                }
            });
        }

        String token = getIntent().getStringExtra("token");


        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setUseWideViewPort(true);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        if (webUrlString.contains("youtube")) {
            this.webView.getSettings().setUseWideViewPort(true);
            this.webView.getSettings().setLoadWithOverviewMode(true);
            this.webView.canGoBack();
            this.webView.setWebChromeClient(new WebChromeClient() {
            });
        }

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        progressDialog = ProgressDialog.show(this, getString(R.string.app_name), getString(R.string.please_wait), true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                if (url.contains("tel:")) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                    return true;
                }
                else if(url.contains("mailto:")){
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    System.out.println("url: ----> " + url);

                    MailTo mail = MailTo.parse(url);
                    System.out.println("mail: ----> " + mail.toString());
                    emailIntent.setType("message/rfc822");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String [] {mail.getTo()});
                    if(emailIntent.resolveActivity(WebViewActivity.this.getPackageManager()) != null) {
                        startActivity(emailIntent);
                        return true;
                    }
                }
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }


        });

        if (getIntent().getStringExtra("token") != null && serviceId!=null &&(serviceId.equals("29") || serviceId.equals("57") || serviceId.equals("59") || serviceId.equals("60") || serviceId.equals("27"))) {

            webView.loadUrl(webUrlString + "token=" + token);


        } else if(serviceType!=null && serviceType.equalsIgnoreCase("getTerms")){
            System.out.println("getTerms");
            this.webView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
            webView.loadData(webUrlString,"text/html; charset=utf-8", "UTF-8");
        }
        else {

            webView.loadUrl(webUrlString);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }


    public void onPause() {
        super.onPause();
        this.webView.onPause();
    }

    public void onResume() {
        super.onResume();
        this.webView.onResume();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false;
            }

            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false;
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                WebViewActivity.this.finish();
                return false;
            }
            return false;
        }
    }

}


