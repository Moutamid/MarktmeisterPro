package com.moutamid.marktmeisterpro.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.MainActivity;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.adapters.SavedMainAdapter;
import com.moutamid.marktmeisterpro.databinding.FragmentSavedBinding;
import com.moutamid.marktmeisterpro.models.Stall;
import com.moutamid.marktmeisterpro.models.StallModel;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class SavedFragment extends Fragment {
    FragmentSavedBinding binding;
    ArrayList<Stall> stalls;
    SavedMainAdapter adapter;

    public SavedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(getLayoutInflater(), container, false);

        binding.stallListRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.stallListRC.setHasFixedSize(false);

        binding.filter.setOnClickListener(v -> {
            showPopupMenu(v);
        });

        binding.search.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        stalls = Stash.getArrayList(Constants.STALL_LIST, Stall.class);
        Collections.reverse(stalls);
        adapter = new SavedMainAdapter(requireContext(), stalls);
        binding.stallListRC.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());

        // Set a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.filter_AZ) {
                stalls.sort((obj1, obj2) -> obj1.getName().compareToIgnoreCase(obj2.getName()));
                for (Stall s : stalls) {
                    s.getStall().sort((obj1, obj2) -> obj1.getItem().compareToIgnoreCase(obj2.getItem()));
                }
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.filter_RO) {
                if (item.getTitle().toString().equals("Recent To Old")) {
                    item.setTitle("Old To Recent");
                } else {
                    item.setTitle("Recent To Old");
                }
                Collections.reverse(stalls);
                for (Stall s : stalls) {
                    Collections.reverse(s.getStall());
                }
                adapter.notifyDataSetChanged();
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

}