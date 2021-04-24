package com.t7droid.enderecosdobrasil.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.t7droid.enderecosdobrasil.R;

public class SplashActivity extends AppCompatActivity {

    LottieAnimationView lt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getSupportActionBar().hide();

        lt = findViewById(R.id.ltv);
        lt.setMinAndMaxProgress(0.0f, 0.5f);
        lt.setSpeed(0.7f);
        lt.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}