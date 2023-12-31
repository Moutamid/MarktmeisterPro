package com.moutamid.marktmeisterpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.databinding.ActivitySplashScreenBinding;
import com.moutamid.marktmeisterpro.utilis.Constants;

public class SplashScreenActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this).load(R.drawable.markt_schwarz).into(binding.logo);

        new Handler().postDelayed(() -> {
            Stash.put(Constants.From_Splash, true);
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        },2000);

    }
}