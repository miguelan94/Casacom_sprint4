package com.streamnow.sbb.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.streamnow.sbb.R;
import com.streamnow.sbb.datamodel.LDContact;
import com.streamnow.sbb.lib.LDConnection;
import com.streamnow.sbb.utils.Lindau;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by migue on 14/03/2017.
 */


public class FirstFragmentContact extends Fragment{

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














        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.content_contact, container, false);


            //////////////////////////////////////////////////////////////////////////////////////////


            Locale locale = new Locale(Lindau.getInstance().getCurrentSessionUser().userInfo.language);
            // Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,getResources().getDisplayMetrics());

            //setContentView(R.layout.activity_contact);

            String apiUrlString = getArguments().getString("api");
            String name_service = getArguments().getString("name");
            String response = getArguments().getString("contacts");
            JSONObject json ;
            int contactPosition = getArguments().getInt("contactPosition");
            if(response != null){
                try {
                    json = new JSONObject(response);
                    ArrayList<LDContact> contacts = LDContact.contactsFromArray(json.getJSONArray("contacts"));
                    //contact = contacts.get(0);
                    contact = contacts.get(contactPosition);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                getActivity().finish();
            }

           // ArrayList<LDContact> contacts = LDContact.contactsFromArray(json.getJSONArray("contacts"));





            // Instantiate a ViewPager
       /*ViewPager pager = (ViewPager) this.findViewById(R.id.pager);

        // Create an adapter with the fragments we show on the ViewPager
       ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());
        adapter.addFragment(FirstFragmentContact.newInstance("FirstFragment, Instance 1"));
        adapter.addFragment(SecondFragmentContact.newInstance("SecondFragment, Instance 1"));
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
           // LinearLayout bgnd = (LinearLayout)v.findViewById(R.id.bar_bgnd);
            //ImageView imageView = (ImageView) v.findViewById(R.id.contact_bgnd_image);
            //TextView nameService_textView = (TextView)v.findViewById(R.id.name_service);
            /*nameService_textView.setTextColor(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.fontColorTop);
            if(name_service!=null && !name_service.equals("")){
                nameService_textView.setText(name_service);
            }
            else{
                nameService_textView.setVisibility(View.GONE);
            }*/

            int colorTop = Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop;

           // bgnd.setBackgroundColor(colorTop);
            //imageView.setColorFilter(colorTop, PorterDuff.Mode.SRC_ATOP);
            //imageView.invalidate();

            final GestureDetector gdt = new GestureDetector(new GestureListener());

           /* imageView.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(final View view, final MotionEvent event)
                {
                    gdt.onTouchEvent(event);
                    return true;
                }
            });*/

            //ImageView leftArrow = (ImageView)v.findViewById(R.id.left_arrow_contact);
           // leftArrow.setColorFilter(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.fontColorSmartphone);
            /*leftArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });*/
            RelativeLayout relativeLayout = (RelativeLayout)v.findViewById(R.id.relativeLayout);
            TextView textView = (TextView)v.findViewById(R.id.title_main);
            textView.setText(contact.role);
            fullName = (TextView)v.findViewById(R.id.fullName_contact);
            FloatingActionButton floatingActionButton_call = (FloatingActionButton)v.findViewById(R.id.floating_button_call);
            FloatingActionButton floatingActionButton_room = (FloatingActionButton)v.findViewById(R.id.floating_button_room);
            FloatingActionButton floatingActionButton_earth = (FloatingActionButton)v.findViewById(R.id.floating_button_earth);
            FloatingActionButton floatingActionButton_email = (FloatingActionButton)v.findViewById(R.id.floating_button_email);
            final EditText message = (EditText)v.findViewById(R.id.contact_msg);
            message.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    message.setHint("");
                    return false;
                }
            });
            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(message.getHint().toString().equals("")){
                        message.setHint(getString(R.string.contactMessage));
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return false;
                }
            });
           /* message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b){
                        message.setHint("");
                    }else{
                        message.setHint(getString(R.string.contactMessage));
                    }
                }
            });*/
            Button button = (Button)v.findViewById(R.id.button);
            // button.setBackgroundColor(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                button.setBackgroundTintList(ColorStateList.valueOf(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop));
            }
            else{
                button.setBackgroundColor(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(message.getText().toString().equals("")){
                        showAlertDialog(getString(R.string.contactMessage));
                    }
                    else{
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("message/rfc822");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{contact.email});
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,getString(R.string.app_name) + " - " + contact.role);
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message.getText().toString());
                        if(emailIntent.resolveActivity(getActivity().getPackageManager()) != null){
                            startActivity(emailIntent);
                        }
                    }

                }
            });

            floatingActionButton_call.setBackgroundTintList(ColorStateList.valueOf(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop));
            floatingActionButton_room.setBackgroundTintList(ColorStateList.valueOf(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop));
            floatingActionButton_earth.setBackgroundTintList(ColorStateList.valueOf(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop));
            floatingActionButton_email.setBackgroundTintList(ColorStateList.valueOf(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop));
        /*this.avatarImageView = (ImageView) findViewById(R.id.contact_avatar);
        this.phoneTextView = (TextView) findViewById(R.id.contact_phone);
        this.emailTextView = (TextView) findViewById(R.id.contact_email);
        this.contactInfoTextView = (TextView) findViewById(R.id.contact_info);
        this.messageEditText = (EditText) findViewById(R.id.contact_msg_edittext);*/

            floatingActionButton_room.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(contact!=null){
                        if(contact.address.equals("")){
                            showAlertDialog(getString(R.string.emptyAddress_contact));
                        }
                        else{
                            String uri = "geo:0,0?q=" + contact.address + "," + contact.city + " " + contact.zip;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                                if(intent.setPackage("com.google.android.apps.maps")!=null){
                                    startActivity(intent);
                                }else{
                                    startActivity( new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=" + "com.google.android.apps.maps")));
                                }
                            }
                        }


                    }
                }
            });

            floatingActionButton_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(contact !=null ){
                        System.out.println("telephone--> " + contact.telephone);
                        if(!contact.telephone.contains("0123456789")){
                            AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance(contact.telephone);
                            dialogFragment.show(getActivity().getSupportFragmentManager(), "dialog");
                        }
                        else{
                            showAlertDialog(getString(R.string.emptyPhone_contact));
                        }



                       /* new AlertDialog.Builder(getActivity().getApplicationContext())
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
                                .show();*/
                    }
                }
            });

            floatingActionButton_earth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(contact != null){
                        if(contact.url.equals("")){
                            showAlertDialog(getString(R.string.emptyUrl_contact));
                        }
                        else{
                            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://"+contact.url));
                            System.out.println("parsed web: " + Uri.parse(contact.url));
                            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }

                    }

                }
            });

            floatingActionButton_email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(contact != null){
                        if(contact.email.equals("")){
                            showAlertDialog(getString(R.string.emptyEmail_contact));
                        }
                        else{
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("message/rfc822");
                            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{contact.email});
                            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,getString(R.string.app_name) + " - " + contact.role);
                            if(emailIntent.resolveActivity(getActivity().getPackageManager()) != null){
                                startActivity(emailIntent);
                            }
                        }
                    }


                }
            });
            this.avatarImageView = (ImageView)v.findViewById(R.id.contact_avatar);
            this.scheduleTitle = (TextView)v.findViewById(R.id.schedule);
            this.scheduleContact = (TextView)v.findViewById(R.id.scheduleContact);
            this.companyTextView = (TextView) v.findViewById(R.id.companyContact);


            //Button buttonSend = (Button) findViewById(R.id.button_send);
            //buttonSend.setBackgroundColor(colorTop);

           // progressDialog = ProgressDialog.show(getContext(), getString(R.string.app_name), getString(R.string.please_wait), true);

           // RequestParams requestParams = new RequestParams();

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
           // requestParams.add("access_token", Lindau.getInstance().getCurrentSessionUser().accessToken);
           // LDConnection.get("getContact", requestParams, new ResponseHandlerJson());





            ///////////////////////////////////////////////////////
















            setOutlets();
            return v;
        }

        public static FirstFragmentContact newInstance(String name, String api,String contacts,int pos) {

            FirstFragmentContact f = new FirstFragmentContact();
            Bundle b = new Bundle();
            b.putString("api", api);
            b.putString("name", name);
            b.putString("contacts",contacts);
            b.putInt("contactPosition",pos);
            f.setArguments(b);


            return f;
        }






    private void showAlertDialog(String msg)
    {
        new AlertDialog.Builder(getActivity())
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

        Picasso.with(getActivity().getApplicationContext())
                .load(avatarUrl)
                .placeholder(R.drawable.contact_placeholder)
                .into(this.avatarImageView);
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
                getActivity().finish();
                return false;
            }
            return false;
        }
    }


    /*public static class Fragment1 extends DialogFragment {

        private static LDContact contact;
            public static Fragment1 newInstance(LDContact ldc){
                contact = ldc;
            Fragment1 fragment = new Fragment1();

            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(contact.telephone)
                    .setPositiveButton(getString(R.string.call), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Intent callIntent =new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact.telephone, null));
                            startActivity(callIntent);
                        }
                    })
                    .setNegativeButton(getString(R.string.call_abort), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    })
                    .create();
        }
    }*/
    public static class AlertDialogFragment extends DialogFragment {

        public static AlertDialogFragment newInstance(String telephone) {
            AlertDialogFragment frag = new AlertDialogFragment();
            Bundle args = new Bundle();
            args.putString("tel", telephone);
            frag.setArguments(args);
            return frag;
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final String telephone = getArguments().getString("tel");
            return new AlertDialog.Builder(getActivity())
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(telephone)
                    .setPositiveButton(R.string.call,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent =new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", telephone, null));
                                    if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                                        startActivity(intent);
                                    }

                                }
                            }
                    )
                    .setNegativeButton(R.string.call_abort,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }
                    )
                    .create();
        }
    }
}


