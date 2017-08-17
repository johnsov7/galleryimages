package com.example.vincent.galleryimages.barcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.vincent.galleryimages.R;

public class Barcode extends AppCompatActivity {

    ImageView barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        barcode = (ImageView)findViewById(R.id.barcode);

        Intent i = getIntent();
        Intent r = getIntent();
        Intent t = getIntent();
        Intent a = getIntent();

    }

}
