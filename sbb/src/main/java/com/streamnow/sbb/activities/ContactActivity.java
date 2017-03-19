package com.streamnow.sbb.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.streamnow.sbb.R;
import com.streamnow.sbb.datamodel.LDContact;
import com.streamnow.sbb.lib.LDConnection;
import com.streamnow.sbb.utils.Lindau;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class ContactActivity extends BaseActivity
{
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private ImageView avatarImageView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView contactInfoTextView;
    private EditText messageEditText;
    private TextView companyTextView;
    private TextView scheduleContact;
    private TextView scheduleTitle;
    private  TextView fullName;

    private LDContact contact;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Locale locale = new Locale(Lindau.getInstance().getCurrentSessionUser().userInfo.language);
        // Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

        setContentView(R.layout.activity_contact);
        String apiUrlString = getIntent().getStringExtra("api_url");
        String name_service = getIntent().getStringExtra("name_service");





        // Instantiate a ViewPager
       /*ViewPager pager = (ViewPager) this.findViewById(R.id.pager);

        // Create an adapter with the fragments we show on the ViewPager
       ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());
        adapter.addFragment(FirstFragmentContact.newInstance("FirstFragment, Instance 1"));
        pager.setAdapter(adapter);
*/
       /* ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ContactActivity.MyPagerAdapter(getSupportFragmentManager()));
*/


        /*SlidingSplashView slidingSplashView = (SlidingSplashView)findViewById(R.id.splash);
        slidingSplashView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //setContentView(R.layout.activity_event);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
*/
        LinearLayout bgnd = (LinearLayout)findViewById(R.id.bar_bgnd);
        ImageView imageView = (ImageView) findViewById(R.id.contact_bgnd_image);
        fullName = (TextView)findViewById(R.id.fullName_contact);
        TextView nameService_textView = (TextView)findViewById(R.id.name_service);
        nameService_textView.setTextColor(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.fontColorTop);
        if(name_service!=null && !name_service.equals("")){
            nameService_textView.setText(name_service);
        }
        else{
            nameService_textView.setVisibility(View.GONE);
        }

        int colorTop = Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop;

        bgnd.setBackgroundColor(colorTop);
        imageView.setColorFilter(colorTop, PorterDuff.Mode.SRC_ATOP);
        imageView.invalidate();

        final GestureDetector gdt = new GestureDetector(new GestureListener());

        imageView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(final View view, final MotionEvent event)
            {
                gdt.onTouchEvent(event);
                return true;
            }
        });

        ImageView leftArrow = (ImageView)findViewById(R.id.left_arrow_contact);
        leftArrow.setColorFilter(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.fontColorSmartphone);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FloatingActionButton floatingActionButton_call = (FloatingActionButton)findViewById(R.id.floating_button_call);
        FloatingActionButton floatingActionButton_room = (FloatingActionButton)findViewById(R.id.floating_button_room);
        FloatingActionButton floatingActionButton_earth = (FloatingActionButton)findViewById(R.id.floating_button_earth);
        FloatingActionButton floatingActionButton_email = (FloatingActionButton)findViewById(R.id.floating_button_email);
  //      Button button = (Button)findViewById(R.id.button);
        // button.setBackgroundColor(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop);
//        button.setBackgroundTintList(ColorStateList.valueOf(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop));

        floatingActionButton_call.setBackgroundTintList(ColorStateList.valueOf(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop));
        floatingActionButton_room.setBackgroundTintList(ColorStateList.valueOf(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop));
        floatingActionButton_earth.setBackgroundTintList(ColorStateList.valueOf(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop));
        floatingActionButton_email.setBackgroundTintList(ColorStateList.valueOf(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop));
        /*this.avatarImageView = (ImageView) findViewById(R.id.contact_avatar);
        this.phoneTextView = (TextView) findViewById(R.id.contact_phone);
        this.emailTextView = (TextView) findViewById(R.id.contact_email);
        this.contactInfoTextView = (TextView) findViewById(R.id.contact_info);
        this.messageEditText = (EditText) findViewById(R.id.contact_msg_edittext);*/


        this.avatarImageView = (ImageView) findViewById(R.id.contact_avatar);
        this.scheduleTitle = (TextView)findViewById(R.id.schedule);
        this.scheduleContact = (TextView)findViewById(R.id.scheduleContact);
        this.companyTextView = (TextView) findViewById(R.id.companyContact);


        //Button buttonSend = (Button) findViewById(R.id.button_send);
        //buttonSend.setBackgroundColor(colorTop);

        progressDialog = ProgressDialog.show(this, getString(R.string.app_name), getString(R.string.please_wait), true);

        RequestParams requestParams = new RequestParams();

        /*if(apiUrlString==null || apiUrlString.equals("")){

            requestParams.add("access_token", Lindau.getInstance().getCurrentSessionUser().accessToken);
            LDConnection.get("getContact", requestParams, new ResponseHandlerJson());
        }
        else{
            String endPoint = apiUrlString+"getContacts";
            AsyncHttpClient httpClient = new AsyncHttpClient();
            httpClient.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
            httpClient.setEnableRedirects(true);
            httpClient.get(endPoint,requestParams,new ResponseHandlerJson());
        }*/
        requestParams.add("access_token", Lindau.getInstance().getCurrentSessionUser().accessToken);
        LDConnection.get("getContact", requestParams, new ResponseHandlerJson());







    }
   /* private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return FirstFragmentContact.newInstance("FirstFragment, Instance 1");
                case 2: return ThirdFragment.newInstance("ThirdFragment, Instance 1");
                case 3: return ThirdFragment.newInstance("ThirdFragment, Instance 2");
                case 4: return ThirdFragment.newInstance("ThirdFragment, Instance 3");
                default: return FirstFragmentContact.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
*/
    private void showAlertDialog(String msg)
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .show();
    }

    private void setOutlets()
    {


        this.companyTextView.setText(this.contact.company);
        this.scheduleContact.setText(this.contact.schedule);
        this.scheduleTitle.setText(getString(R.string.opening_schedule));
/*
        this.phoneTextView.setText(this.contact.telephone);
        this.phoneTextView.setTextColor(Color.BLACK);

        if(contact.telephone!=null && !contact.telephone.equals("")){
            phoneTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(ContactActivity.this)
                            .setMessage(contact.telephone)
                            .setPositiveButton(getString(R.string.call), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent callIntent =new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact.telephone, null));
                                    startActivity(callIntent);
                                }
                            })
                            .setNegativeButton(getString(R.string.call_abort), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        }
        this.emailTextView.setText(this.contact.email);

        String spaceChar = "\n";
        String contactInfoString =  this.contact.company + spaceChar +
                                    this.contact.address + spaceChar +
                                    this.contact.zip + spaceChar +
                                    this.contact.city + spaceChar +
                                    getString(R.string.opening_schedule) + ":" + spaceChar +
                                    this.contact.schedule;
        this.contactInfoTextView.setText(contactInfoString);*/

        final String avatarUrl = LDConnection.getAbsoluteUrl("getContactAvatar") +
                "?access_token=" + Lindau.getInstance().getCurrentSessionUser().accessToken +
                "&id=" + this.contact.id;

        Picasso.with(this)
                .load(avatarUrl)
                .placeholder(R.drawable.contact_placeholder)
                .into(this.avatarImageView);

        System.out.println("FullName----> " + contact.fullname);
        fullName.setText(this.contact.fullname);
    }

    public void sendMessage(View v)
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{this.contact.email});
        i.putExtra(Intent.EXTRA_SUBJECT, this.contact.company + " " + getString(R.string.support));
        i.putExtra(Intent.EXTRA_TEXT, this.messageEditText.getText());
        try
        {
            startActivity(Intent.createChooser(i, getString(R.string.send_mail)));
        }
        catch (android.content.ActivityNotFoundException e)
        {
            e.printStackTrace();
            showAlertDialog(getString(R.string.no_mail_account));
        }
    }

    public void resetEditText(View v)
    {
        this.messageEditText.setText("");
    }

    private class ResponseHandlerJson extends JsonHttpResponseHandler
    {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response)
        {
            try
            {
                System.out.println("Contact---> " + response.toString());
                if( response.getString("status").equals("ok") )
                {
                    ArrayList<LDContact> contacts = LDContact.contactsFromArray(response.getJSONArray("contacts"));

                    if( contacts.size() > 0 )
                    {
                        contact = contacts.get(0);
                        setOutlets();
                    }
                    else
                    {
                        showAlertDialog(getString(R.string.network_error));
                    }
                }
                else
                {
                    showAlertDialog(getString(R.string.network_error));
                }
            }
            catch( Exception e )
            {
                e.printStackTrace();
                showAlertDialog(getString(R.string.network_error));
            }
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable)
        {
            showAlertDialog(getString(R.string.network_error));
            System.out.println("getContact onFailure throwable: " + throwable.toString() + " status code = " + statusCode);
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
        {
            showAlertDialog(getString(R.string.network_error));
            System.out.println("getContact onFailure json");
            progressDialog.dismiss();
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            {
                return false;
            }
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            {
                return false;
            }

            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
            {
                return false;
            }
            else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
            {
                ContactActivity.this.finish();
                return false;
            }
            return false;
        }
    }

}
