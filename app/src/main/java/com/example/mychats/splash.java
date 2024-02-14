package com.example.mychats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash extends AppCompatActivity {
    ImageView logo;
    TextView from, owner, logoText;
    Animation topAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.logo);
        from = findViewById(R.id.from);
        owner = findViewById(R.id.owner);
        logoText = findViewById(R.id.logoText);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        logo.setAnimation(topAnim);
        logoText.setAnimation(topAnim);
        from.setAnimation(bottomAnim);
        owner.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this, Registration.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}