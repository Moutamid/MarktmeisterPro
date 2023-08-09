package com.moutamid.marktmeisterpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moutamid.marktmeisterpro.databinding.ActivityMainBinding;
import com.moutamid.marktmeisterpro.fragments.SettingFragment;
import com.moutamid.marktmeisterpro.fragments.SavedFragment;
import com.moutamid.marktmeisterpro.fragments.ScanFragment;
import com.moutamid.marktmeisterpro.utilis.Constants;

public class MainActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener{
    ActivityMainBinding binding;
    public BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        Glide.with(this).load(R.drawable.markt_schwarz).into(binding.logo);

        bottomNavigationView = binding.bottomNav;

        bottomNavigationView.setItemActiveIndicatorColor(ColorStateList.valueOf(getResources().getColor(R.color.bottomNavIndicator)));
        bottomNavigationView.setItemActiveIndicatorWidth(100);
        bottomNavigationView.setItemActiveIndicatorHeight(100);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_scan);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_scan ){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout , new ScanFragment()).commit();
            return true;
        } else  if (item.getItemId() == R.id.nav_saved ){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout , new SavedFragment()).commit();
            return true;
        } else  if (item.getItemId() == R.id.nav_export ){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout , new SettingFragment()).commit();
            return true;
        }

        return false;
    }
}