package com.example.vincent.galleryimages;

/**
 * Created by vincent on 12/8/16.
 */

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import com.example.vincent.galleryimages.adapter.GalleryAdapter;
import com.example.vincent.galleryimages.app.AppController1;
import com.example.vincent.galleryimages.barcode.Barcode;
import com.example.vincent.galleryimages.model.Image;
import com.example.vincent.galleryimages.SlideshowDialogFragment;
import com.example.vincent.galleryimages.MainActivity;

public class SlideshowDialogFragment extends DialogFragment {
    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<Image> images;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount, lblTitle, lblDate, lblName, lblTimestamp;
    private int selectedPosition = 0;
    private ImageView logo;
    private ImageLoader imageLoader = AppController1.getInstance().getImageLoader();

    private View footer;

    private ActionBar mActionBar;

    static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        lblTitle = (TextView) v.findViewById(R.id.title);
        lblDate = (TextView) v.findViewById(R.id.date);
        logo = (ImageView) v.findViewById(R.id.profilePic);
        lblName = (TextView) v.findViewById(R.id.name);
        lblTimestamp = (TextView) v.findViewById(R.id.timestamp);

//        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) v.findViewById(R.id.toolbar);
//        toolbar.setTitle("Explore");
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        SlideshowDialogFragment editor = new SlideshowDialogFragment();
//        FragmentTransaction transition = fragmentManager.beginTransaction();
//        transition.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transition.add(android.R.id.home, editor);
//
//
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setCancelable(false);





        images = (ArrayList<Image>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + images.size());


        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;

    }





    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {


        Image image = images.get(position);

        Glide.with(getActivity()).load(image.getCoupon_image())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(logo);

        lblName.setText(image.getCoupon_name());
        lblTimestamp.setText(image.getCoupon_expiration());
        lblTitle.setText(image.getCoupon_name());
        lblDate.setText(image.getCoupon_expiration());

        lblTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Barcode.class);
                startActivity(i);
            }
        });

        lblDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent v = new Intent(getActivity(), MainActivity.class);
                startActivity(v);
            }
        });




    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);




    }

    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;


        public MyViewPagerAdapter() {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);

            Image image = images.get(position);


            Glide.with(getActivity()).load(image.getCoupon_image())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//
//        } else  if (id == android.R.id.home) {
//            getActivity().onBackPressed();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)  {
//        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
//                && keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0) {
//            // Simply pop back your fragment stack here
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        // Simply pop back your fragment stack here
//    }
}