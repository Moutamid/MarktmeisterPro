package com.moutamid.marktmeisterpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moutamid.marktmeisterpro.databinding.ActivityMainBinding;
import com.moutamid.marktmeisterpro.fragments.ExportFragment;
import com.moutamid.marktmeisterpro.fragments.SavedFragment;
import com.moutamid.marktmeisterpro.fragments.ScanFragment;
import com.moutamid.marktmeisterpro.utilis.Constants;

public class MainActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener{
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);


        binding.bottomNav.setItemActiveIndicatorColor(ColorStateList.valueOf(getResources().getColor(R.color.bottomNavIndicator)));
        binding.bottomNav.setItemActiveIndicatorWidth(100);
        binding.bottomNav.setItemActiveIndicatorHeight(100);
        binding.bottomNav.setOnNavigationItemSelectedListener(this);
        binding.bottomNav.setSelectedItemId(R.id.nav_scan);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout , new ExportFragment()).commit();
            return true;
        }

        return false;
    }
}