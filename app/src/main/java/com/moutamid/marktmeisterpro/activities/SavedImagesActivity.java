package com.moutamid.marktmeisterpro.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.adapters.ImagesSubAdapter;
import com.moutamid.marktmeisterpro.databinding.ActivitySavedImagesBinding;
import com.moutamid.marktmeisterpro.models.StallModel;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SavedImagesActivity extends AppCompatActivity {
    ActivitySavedImagesBinding binding;
    ImagesSubAdapter adapter;
    ArrayList<StallModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.savedRC.setHasFixedSize(false);
        String NAME = Stash.getString(Constants.IMAGE);

        Glide.with(this).load(R.drawable.markt_schwarz).into(binding.logo);

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.name.setText(NAME);

        list = new ArrayList<>();
        list.addAll(Stash.getArrayList(NAME, StallModel.class));

        binding.filter.setOnClickListener(v -> {
            showPopupMenu(v);
        });

        String im = list.size() > 1 ? " Images" : " Image";
        binding.totalSize.setText("You have saved "+ list.size() + im);

        list.add(new StallModel("", NAME, "", "", "", "", "", true));
        adapter = new ImagesSubAdapter(this, list);
        binding.savedRC.setAdapter(adapter);

    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(SavedImagesActivity.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());

        // Set a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.filter_AZ) {
                List<StallModel> sublist = list.subList(0, list.size()-1);
                sublist.sort((obj1, obj2) -> obj1.getStallName().compareToIgnoreCase(obj2.getStallName()));

                StallModel lastItem = list.get(list.size()-1);
                list.clear();
                list.addAll(sublist);
                list.add(lastItem);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.filter_RO) {
                if (item.getTitle().toString().equals("Recent To Old")) {
                    item.setTitle("Old To Recent");
                } else {
                    item.setTitle("Recent To Old");
                }
                List<StallModel> sublist = list.subList(0, list.size()-1);
                Collections.reverse(sublist);

                StallModel lastItem = list.get(list.size()-1);
                list.clear();
                list.addAll(sublist);
                list.add(lastItem);
                adapter.notifyDataSetChanged();
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

}