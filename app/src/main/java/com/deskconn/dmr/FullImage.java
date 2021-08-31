package com.deskconn.dmr;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class FullImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        ImageView imageView = findViewById(R.id.imageView);

        ImageView imageViewCross = findViewById(R.id.cross_image);

        Glide.with(this).load(getIntent().getStringExtra("url")).into(imageView);

        imageViewCross.setOnClickListener(v -> onBackPressed());
    }
}