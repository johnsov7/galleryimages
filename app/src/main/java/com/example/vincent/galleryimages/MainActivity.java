package com.example.vincent.galleryimages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.vincent.galleryimages.adapter.GalleryAdapter;
import com.example.vincent.galleryimages.app.AppController1;
import com.example.vincent.galleryimages.barcode.Barcode;
import com.example.vincent.galleryimages.model.Image;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private static final String endpoint = "http://cap-api.solutetech.io/recommended";
    private ArrayList<Image> images;
    private ProgressDialog pDialog;
    private GalleryAdapter mAdapter;
    private RecyclerView recyclerView;

    private View footer;

    private Slide slide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);



        // Click listener for nav footer.
        footer = findViewById(R.id.userProfilePage);
        footer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                footer = findViewById(R.id.userProfilePage);
                footer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent a = new Intent(getApplicationContext(), Barcode.class);
                        startActivity(a);
                    }
                });
                footer = findViewById(R.id.userCoupons);
                footer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Do footer action
                        Intent t = new Intent(getApplicationContext(), Barcode.class);
                        startActivity(t);
                    }
                });
                footer = findViewById(R.id.couponTimeline);
                footer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Do footer action
                        Intent r = new Intent(getApplicationContext(), Barcode.class);
                        startActivity(r);
                    }
                });
                footer = findViewById(R.id.userFindButton);
                footer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });




        Intent v = getIntent();
        Intent t = getIntent();
        Intent intent = getIntent();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        Cache cache = AppController1.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(endpoint);
        if (entry != null) {
            //fetch the data from the cache
            try {
                String data = new String(entry.data, "UTF-8");
                fetchImages(new JSONObject(data));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else {
            //Making fresh volley request and getting Json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    endpoint, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        fetchImages(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            //Adding request to volley request queue
            AppController1.getInstance().addToRequestQueue(jsonReq);
        }

//        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
//        tx.replace( R.id.recycler_view, new SlideshowDialogFragment() ).addToBackStack( "tag" ).commit();
//        recyclerView.getRootView().setFocusableInTouchMode(true);
//        recyclerView.getRootView().requestFocus();
//        recyclerView.getRootView().setOnKeyListener( new View.OnKeyListener()
//        {
//            @Override
//            public boolean onKey( View v, int keyCode, KeyEvent event )
//            {
//                if( keyCode == KeyEvent.KEYCODE_BACK )
//                {
//                    return true;
//                }
//                return false;
//            }
//        } );

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {


            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");


            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));

    }


    private void fetchImages(JSONObject response) {


        images.clear();

        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(response));

            JSONArray work = jsonObject.getJSONArray("data");
            for (int i = 0; i < work.length(); i++) {
                JSONObject time = work.getJSONObject(i);

                Image share = new Image();

                share.setCoupon_name(time.getString("coupon_name"));
                share.setCoupon_image(time.getString("coupon_image"));
                share.setCoupon_barcode(time.getString("coupon_barcode"));
                share.setCoupon_expiration(time.getString("coupon_expiration"));
                share.setCoupon_description(time.getString("coupon_description"));

                images.add(share);
            }
            mAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // Back?
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Back
            moveTaskToBack(true);
            return true;
        }
        else {
            // Return
            return super.onKeyDown(keyCode, event);
        }
    }
}
