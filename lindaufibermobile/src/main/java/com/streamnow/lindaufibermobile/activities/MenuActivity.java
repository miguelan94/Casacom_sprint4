package com.streamnow.lindaufibermobile.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.streamnow.lindaufibermobile.R;
import com.streamnow.lindaufibermobile.datamodel.LDService;
import com.streamnow.lindaufibermobile.datamodel.LDSessionUser;
import com.streamnow.lindaufibermobile.interfaces.IMenuPrintable;
import com.streamnow.lindaufibermobile.lib.LDConnection;
import com.streamnow.lindaufibermobile.utils.Lindau;
import com.streamnow.lindaufibermobile.utils.MenuAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

public class    MenuActivity extends BaseActivity {
    protected final LDSessionUser sessionUser = Lindau.getInstance().getCurrentSessionUser();
    String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale locale = new Locale(Lindau.getInstance().getCurrentSessionUser().userInfo.language);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main_menu);


        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        List<Cookie> cookies = myCookieStore.getCookies();
        for (int c = 0; c < cookies.size(); c++) {
            System.out.println("COOKIES---" + cookies.get(c).getName() + " value" + cookies.get(c).getValue());
        }


        categoryId = this.getIntent().getStringExtra("category_id");
        String categoryName = getIntent().getStringExtra("category_name");
        ArrayList<? extends IMenuPrintable> adapterArray;

        if (categoryId == null) {
            adapterArray = sessionUser.getAvailableServicesForSession();
        } else {
            adapterArray = sessionUser.getAvailableServicesForCategoryId(categoryId);
        }


        RelativeLayout mainBackground = (RelativeLayout) findViewById(R.id.main_menu_background);
        mainBackground.setBackgroundColor(sessionUser.userInfo.partner.backgroundColorSmartphone);
        FloatingActionButton floating_button = (FloatingActionButton) findViewById(R.id.floating_button);
        System.out.println("button----> " + sessionUser.userInfo.partner.barButton);
        if(sessionUser.userInfo.partner.barButton!=null && (!sessionUser.userInfo.partner.barButton.equals("")|| !sessionUser.userInfo.partner.barButton.equals("null"))){
            floating_button.setBackgroundTintList(ColorStateList.valueOf(sessionUser.userInfo.partner.barButtonColor));
            floating_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MenuActivity.this, WebViewActivity.class);
                    intent.putExtra("service_name",sessionUser.userInfo.partner.barButtonText);
                    intent.putExtra("web_url", sessionUser.userInfo.partner.barButtonUrl);
                    startActivity(intent);

                }
            });
        }
        else{
            floating_button.setVisibility(View.GONE);
        }

        TextView textView = (TextView) findViewById(R.id.text_app_name);
        TextView categoryName_text = (TextView)findViewById(R.id.category_name);
        if (sessionUser.userInfo.partner.smartphoneAppName != null && sessionUser.userInfo.partner.smartphoneAppName.isEmpty()) {
            textView.setText(sessionUser.userInfo.partner.company);
        } else {
            textView.setText(sessionUser.userInfo.partner.smartphoneAppName);
        }

        ImageView smart_image = (ImageView) findViewById(R.id.smartphone_image);
        ImageView left_arrow = (ImageView) findViewById(R.id.left_arrow);
        left_arrow.setColorFilter(sessionUser.userInfo.partner.fontColorSmartphone);
        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        View dividerTop = findViewById(R.id.divider);
        ImageView imageView = (ImageView) findViewById(R.id.settings_ico); //settings
        if (!getIntent().getBooleanExtra("sub_menu", false)) {
            categoryName_text.setVisibility(View.GONE);
            System.out.println("Image: " + sessionUser.userInfo.partner.backgroundSmartphoneImage);
            dividerTop.setVisibility(View.GONE);
            Picasso.with(this)
                    .load(sessionUser.userInfo.partner.backgroundSmartphoneImage)
                    .into(smart_image);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MenuActivity.this, SettingsActivity.class);
                    i.putExtra("main_menu", true);
                    startActivity(i);

                }
            });
        } else {
            dividerTop.setBackgroundColor(sessionUser.userInfo.partner.lineColorSmartphone);
            dividerTop.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            if(categoryName!=null && !categoryName.equals("")){
                categoryName_text.setVisibility(View.VISIBLE);
                categoryName_text.setTextColor(sessionUser.userInfo.partner.fontColorTop);
                categoryName_text.setText(categoryName);
            }
            textView.setVisibility(View.GONE);
            floating_button.setVisibility(View.GONE);
            smart_image.setVisibility(View.GONE);
            left_arrow.setVisibility(View.VISIBLE);
        }

        final ListView listView = (ListView) findViewById(R.id.main_menu_list_view);
        listView.setDivider(new ColorDrawable(sessionUser.userInfo.partner.lineColorSmartphone));
        listView.setDividerHeight(1);
        listView.setAdapter(new MenuAdapter(this, adapterArray));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuItemClicked(position);
            }
        });
    }

    private void menuItemClicked(int position) {
        ArrayList<? extends IMenuPrintable> services;

        if (getIntent().getBooleanExtra("sub_menu", false)) //si true
        {

            services = sessionUser.getAvailableServicesForCategoryId(categoryId);
            final LDService service = (LDService) services.get(position);
            switch (service.type) {

                case "2": {
                    if (categoryId.equals("5")) {
                        System.out.println("AppID: " + service.secretId + " user: " + getIntent().getStringExtra("user_vodka") + " pass: " + getIntent().getStringExtra("pass_vodka"));
                        RequestParams requestParams = new RequestParams();
                        requestParams.add("appId", service.secretId);
                        requestParams.add("userId", getIntent().getStringExtra("user_vodka"));
                        requestParams.add("password", getIntent().getStringExtra("pass_vodka"));
                        //https://project-test.streamnow.ch/web/#/tv/livingServices?
                        System.out.println("api_url: " + service.apiUrl);
                        System.out.println("web_url: " + service.webviewUrl);
                        System.out.println("service_id: " + service.id);
                        AsyncHttpClient httpClient = new AsyncHttpClient();
                        httpClient.setUserAgent("Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
                        httpClient.setEnableRedirects(true);
                   /* KeyStore trustStore = null;
                    MySSLSocketFactory socketFactory = null;
                    try {
                        trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                        trustStore.load(null, null);
                        socketFactory = new MySSLSocketFactory(trustStore);
                        socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    httpClient.setSSLSocketFactory(socketFactory);
                    */
                        httpClient.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
                        httpClient.post(service.apiUrl, requestParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                try {
                                    System.out.println("response: " + response.toString());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(service.webviewUrl + "token=" + response.getString("token")));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setPackage("com.android.chrome");
                                        try {
                                            startActivity(intent);
                                        } catch (ActivityNotFoundException e) {
                                            // Chrome is probably not installed
                                            intent.setPackage(null);
                                            startActivity(intent);
                                        }
                                    } else {
                                        Intent intent = new Intent(MenuActivity.this, WebViewActivity.class);
                                        intent.putExtra("web_url", service.webviewUrl);
                                        intent.putExtra("service_id", service.id);
                                        intent.putExtra("token", response.getString("token"));
                                        intent.putExtra("service_name", service.name);
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                System.out.println("onFailure json" + errorResponse.toString());
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                System.out.println("onFailure array");
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                                System.out.println("get token KO: " + throwable.toString() + " status code = " + statusCode + " responseString = " + response);
                            }
                        });
                        break;
                    } else {
                        final Intent intent = new Intent(this, WebViewActivity.class);
                        intent.putExtra("web_url", service.webviewUrl);
                        intent.putExtra("service_id", service.id);
                        intent.putExtra("service_name", service.name);
                        startActivity(intent);
                        break;
                    }

                }
                case "3": {
                    // TODO Open youtube video here
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("service_name", service.name);
                    intent.putExtra("web_url", "https://m.youtube.com/watch?v=" + service.webviewUrl);
                    startActivity(intent);
                    break;
                }
                case "1": {


                        if (service.id.equals("22")) {//events
                            Intent i = new Intent(this, EventActivity.class);
                            startActivity(i);
                        } else if (service.id.equals("53") || service.id.equals("20")) {
                            Intent intent = new Intent(this, ContactActivity.class);
                            intent.putExtra("api_url", service.apiUrl);
                            startActivity(intent);
                        } else if (service.id.equals("3")) {
                            Intent intent = new Intent(this, DocmanMenuActivity.class);
                            intent.putExtra("root_menu", true);
                            intent.putExtras(new Bundle());
                            startActivity(intent);
                        }
                    break;
                }
                case "8": {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(service.webviewUrl);
                    if (intent != null) {
                        startActivity(intent);
                    } else {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + service.webviewUrl)));
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + service.webviewUrl)));
                        }

                    }
                    break;
                }
                case "9": {
                    Intent intent = new Intent(this, ContactActivity.class);
                    intent.putExtra("name_service", service.name);
                    intent.putExtra("api_url", service.apiUrl);
                    startActivity(intent);
                    break;
                }
                case "10": {
                    Intent intent = new Intent(this, DocmanMenuActivity.class);
                    intent.putExtra("name_service", service.name);
                    intent.putExtra("root_menu", true);
                    intent.putExtras(new Bundle());
                    startActivity(intent);
                    break;
                }
                case "11": {
                    Intent i = new Intent(this, EventActivity.class);
                    i.putExtra("name_service", service.name);
                    startActivity(i);
                    break;
                }
                case "12": {
                    RequestParams requestParams = new RequestParams();
                    requestParams.add("appId", service.secretId);
                    requestParams.add("userId", getIntent().getStringExtra("user_vodka"));
                    requestParams.add("password", getIntent().getStringExtra("pass_vodka"));
                    System.out.println("web_url: " + service.webviewUrl);
                    System.out.println("service_id: " + service.id);
                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    httpClient.setUserAgent("Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
                    httpClient.setEnableRedirects(true);
                   /* KeyStore trustStore = null;
                    MySSLSocketFactory socketFactory = null;
                    try {
                        trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                        trustStore.load(null, null);
                        socketFactory = new MySSLSocketFactory(trustStore);
                        socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    httpClient.setSSLSocketFactory(socketFactory);
                    */
                    httpClient.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
                    httpClient.post(service.apiUrl, requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(service.webviewUrl + "token=" + response.getString("token")));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setPackage("com.android.chrome");
                                    try {
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        // Chrome is probably not installed
                                        intent.setPackage(null);
                                        startActivity(intent);
                                    }
                                } else {
                                    Intent intent = new Intent(MenuActivity.this, WebViewActivity.class);
                                    intent.putExtra("web_url", service.webviewUrl);
                                    intent.putExtra("service_id", service.id);
                                    intent.putExtra("token", response.getString("token"));
                                    intent.putExtra("service_name", service.name);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            System.out.println("onFailure json" + errorResponse.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            System.out.println("onFailure array");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                            System.out.println("get token KO: " + throwable.toString() + " status code = " + statusCode + " responseString = " + response);
                        }
                    });
                }

            }
        } else {
            services = sessionUser.getAvailableServicesForCategoryId(sessionUser.categories.get(position).id);

            System.out.println("clicked on item with title " + sessionUser.categories.get(position).name + " it has " + services.size() + " services available");


            if (services.size() == 1) {

                LDService service = (LDService) services.get(0);
                System.out.println("service type: " + service.type);
                //check service type
                switch (service.type) {
                    case "1":
                            if (service.id.equals("53") || service.id.equals("20")) {
                                Intent intent = new Intent(this, ContactActivity.class);
                                intent.putExtra("api_url", service.apiUrl);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(this, DocmanMenuActivity.class);
                                intent.putExtra("root_menu", true);
                                intent.putExtras(new Bundle());
                                startActivity(intent);
                            }
                        break;
                    case "2": {
                        Intent intent = new Intent(this, WebViewActivity.class);
                        intent.putExtra("web_url", service.webviewUrl);
                        intent.putExtra("service_name",service.name);
                        startActivity(intent);
                        break;
                    }
                    case "3": {
                        // TODO Open youtube video here
                        Intent intent = new Intent(this, WebViewActivity.class);
                        intent.putExtra("web_url", "https://m.youtube.com/watch?v=" + service.webviewUrl);
                        intent.putExtra("service_name",service.name);
                        startActivity(intent);
                        break;
                    }
                    case "8": {
                        Intent intent = getPackageManager().getLaunchIntentForPackage(service.webviewUrl);
                        if (intent != null) {
                            startActivity(intent);
                        } else {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + service.webviewUrl)));
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + service.webviewUrl)));
                            }
                        }
                        break;
                    }
                    case  "9": {
                        Intent intent = new Intent(this, ContactActivity.class);
                        intent.putExtra("name_service",service.name);
                        intent.putExtra("api_url", service.apiUrl);
                        startActivity(intent);
                        break;
                    }
                    case  "10": {
                        Intent intent = new Intent(this, DocmanMenuActivity.class);
                        intent.putExtra("name_service",service.name);
                        intent.putExtra("root_menu", true);
                        intent.putExtras(new Bundle());
                        startActivity(intent);
                        break;
                    }
                    case "11": {
                        Intent i = new Intent(this, EventActivity.class);
                        i.putExtra("name_service",service.name);
                        startActivity(i);
                        break;
                    }
                }
            } else if (services.size() > 1) {
                final Intent intent = new Intent(this, MenuActivity.class);
                intent.putExtra("category_id", sessionUser.categories.get(position).id);
                intent.putExtra("category_name" , sessionUser.categories.get(position).name);
                intent.putExtra("sub_menu", true);
                if (sessionUser.categories.get(position).id.equals("5")) {//entertainment
                    final RequestParams requestParams = new RequestParams("access_token", sessionUser.accessToken);
                    LDConnection.get("myentertainment/getCredentials", requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                if (response.getJSONObject("status").getString("status").equals("ok")) {
                                    intent.putExtra("user_vodka", response.getJSONObject("status").getJSONObject("credentials").getString("username"));
                                    intent.putExtra("pass_vodka", response.getJSONObject("status").getJSONObject("credentials").getString("password"));
                                } else {
                                }
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            System.out.println("onFailure json");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            System.out.println("onFailure array");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                            System.out.println("getCredentials KO: " + throwable.toString() + " status code = " + statusCode + " responseString = " + response);
                        }
                    });
                } else {
                    startActivity(intent);
                }

            }
        }
    }

    private void showAlertDialog(String msg) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
