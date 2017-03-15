package com.streamnow.sbb.activities;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.streamnow.sbb.R;
import com.streamnow.sbb.utils.Lindau;

/**
 * Created by migue on 14/03/2017.
 */

public class ScreenSlidePageFragment extends FragmentActivity {


    String name_service;
    String apiUrl;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_slide_activity);
      //  intent.putExtra("name_service",service.name);
        //intent.putExtra("api_url", service.apiUrl);

        if (Build.VERSION.SDK_INT >= 21)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if(Lindau.getInstance().getCurrentSessionUser() != null)
            {
                window.setStatusBarColor(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.colorTop);
            }
            else
            {
                window.setStatusBarColor(getResources().getColor(R.color.appColor));
            }
        }
        name_service = getIntent().getStringExtra("name_service");
        apiUrl = getIntent().getStringExtra("api_url");

        pager = (ViewPager) findViewById(R.id.pagerView);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(pager);
        LinearLayout bgnd = (LinearLayout)findViewById(R.id.bar_bgnd);
        ImageView imageView = (ImageView) findViewById(R.id.contact_bgnd_image);
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



        ImageView leftArrow = (ImageView)findViewById(R.id.left_arrow_contact);
        leftArrow.setColorFilter(Lindau.getInstance().getCurrentSessionUser().userInfo.partner.fontColorSmartphone);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return FirstFragmentContact.newInstance(name_service,apiUrl);
                case 1: return SecondFragmentContact.newInstance("SecondFragment, Instance 1");

                default: return FirstFragmentContact.newInstance(name_service,apiUrl);
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

    @Override
    public void onBackPressed() {

        // Return to previous page when we press back button
        if (this.pager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);

    }







   /* public static ScreenSlidePageFragment newInstance(String n) {

        // Instantiate a new fragment
        System.out.println("instance---> " + n);
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();

        // Save the parameters

        fragment.setRetainInstance(true);

        return fragment;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load parameters when the initial creation of the fragment is done
        //String param =  getArguments().getString("1","");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_contact, container, false);

        return rootView;
    }*/
}
