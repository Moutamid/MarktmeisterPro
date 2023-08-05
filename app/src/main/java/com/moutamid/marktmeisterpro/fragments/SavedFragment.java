package com.moutamid.marktmeisterpro.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.adapters.SavedMainAdapter;
import com.moutamid.marktmeisterpro.databinding.FragmentSavedBinding;
import com.moutamid.marktmeisterpro.models.Stall;
import com.moutamid.marktmeisterpro.models.StallModel;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.util.ArrayList;
import java.util.Date;

public class SavedFragment extends Fragment {
    FragmentSavedBinding binding;
    public SavedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(getLayoutInflater(), container, false);

        binding.stallListRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.stallListRC.setHasFixedSize(false);

        getList();

        return binding.getRoot();
    }

    private void getList() {
        ArrayList<Stall> stalls = new ArrayList<>();
        ArrayList<StallModel> Geschaft = Stash.getArrayList(Constants.Geschaft, StallModel.class);
        ArrayList<StallModel> Anschluss = Stash.getArrayList(Constants.Anschluss, StallModel.class);
        ArrayList<StallModel> Auslage = Stash.getArrayList(Constants.Auslage, StallModel.class);
        ArrayList<StallModel> Dokumente = Stash.getArrayList(Constants.Dokumente, StallModel.class);

        Log.d("CHECKIN123", "Geschäft size " + Geschaft.size() );
        Log.d("CHECKIN123", "Anschluss size " + Anschluss.size() );
        Log.d("CHECKIN123", "Auslage size " + Auslage.size() );
        Log.d("CHECKIN123", "Dokumente size " + Dokumente.size() );

        if (Geschaft.size() > 0) {
            stalls.add(new Stall("Geschäft", Geschaft));
        }  if (Anschluss.size() > 0) {
            stalls.add(new Stall("Anschlüsse", Anschluss));
        }  if (Auslage.size() > 0) {
            stalls.add(new Stall("Auslage", Auslage));
        }  if (Dokumente.size() > 0) {
            stalls.add(new Stall("Dokumente", Dokumente));
        }

        Log.d("CHECKIN123", "List size " + stalls.size() );

        SavedMainAdapter adapter = new SavedMainAdapter(requireContext(), stalls);
        binding.stallListRC.setAdapter(adapter);

    }
}