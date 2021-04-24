package com.t7droid.enderecosdobrasil.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.sarnava.textwriter.TextWriter;
import com.t7droid.enderecosdobrasil.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView lt;
    TextWriter textWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getSupportActionBar().hide();

        textWriter = findViewById(R.id.textWriter);
        lt = findViewById(R.id.ltv);

        Handler handle = new Handler();
        handle.postDelayed(() -> textWriter
                .setWidth(15)
                .setDelay(4)
                .setColor(Color.parseColor("#FF3700B3"))
                .setConfig(TextWriter.Configuration.INTERMEDIATE)
                .setSizeFactor(35f)
                .setLetterSpacing(30f)
                .setText("CONSULTA CEP")
                .setListener(() -> {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                })
                .startAnimation(), 1500);
    }
}