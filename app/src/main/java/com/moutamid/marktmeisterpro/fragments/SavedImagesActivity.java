package com.moutamid.marktmeisterpro.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.adapters.ImagesSubAdapter;
import com.moutamid.marktmeisterpro.databinding.ActivitySavedImagesBinding;
import com.moutamid.marktmeisterpro.models.StallModel;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SavedImagesActivity extends Fragment {
    ActivitySavedImagesBinding binding;
    ImagesSubAdapter adapter;
    ArrayList<StallModel> mainlist;
    boolean az = true, ro = true;
    String ID;
    String NAME;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivitySavedImagesBinding.inflate(getLayoutInflater(), container, false);

        binding.savedRC.setHasFixedSize(false);
        NAME = Stash.getString(Constants.IMAGE);
        ID = Stash.getString(Constants.ID);

        Stash.put(Constants.isBACK, true);

//        Glide.with(this).load(R.drawable.markt_schwarz).into(binding.logo);

        binding.back.setOnClickListener(v -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new SavedFragment()).commit());

        binding.name.setText(NAME);
        binding.applicationID.setText("ID: " + ID);

        mainlist = new ArrayList<>();

        binding.sort.setOnClickListener(v -> {
            showPopupMenu(v);
        });
        binding.filter.setOnClickListener(v -> {
            showFilterMenu2(v);
        });


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mainlist = new ArrayList<>();
        mainlist.addAll(Stash.getArrayList(ID, StallModel.class));
        String s = mainlist.size() > 1 ? " Bilder" : " Bild";
        binding.totalSize.setText("Sie haben " + mainlist.size() + s + " gespeichert");
        adapter = new ImagesSubAdapter(requireContext(), mainlist);
        binding.savedRC.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showFilterMenu2(View v) {
        PopupMenu popupMenu = new PopupMenu(binding.getRoot().getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.filter_all_menu, popupMenu.getMenu());

        // Set a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            ArrayList<StallModel> list = Stash.getArrayList(ID, StallModel.class);
            // Tag  Geschäft
            if (itemId == R.id.nav_vorne_Tag) {
                geschaftFilter("Tag", "vorne", list);
                return true;
            } else if (itemId == R.id.nav_hinten_Tag) {
                geschaftFilter("Tag", "hinten", list);
                return true;
            } else if (itemId == R.id.nav_seite_links_Tag) {
                geschaftFilter("Tag", "Seite links", list);
                return true;
            } else if (itemId == R.id.nav_seite_rechts_Tag) {
                geschaftFilter("Tag", "Seite rechts", list);
                return true;
            } else if (itemId == R.id.nav_turen_Tag) {
                geschaftFilter("Tag", "Türen", list);
                return true;
            }
            // Nacht  Geschäft
            else if (itemId == R.id.nav_vorne_Nacht) {
                geschaftFilter("Nacht", "vorne", list);
                return true;
            } else if (itemId == R.id.nav_hinten_Nacht) {
                geschaftFilter("Nacht", "hinten", list);
                return true;
            } else if (itemId == R.id.nav_seite_links_Nacht) {
                geschaftFilter("Nacht", "Seite links", list);
                return true;
            } else if (itemId == R.id.nav_seite_rechts_Nacht) {
                geschaftFilter("Nacht", "Seite rechts", list);
                return true;
            } else if (itemId == R.id.nav_turen_Nacht) {
                geschaftFilter("Nacht", "Türen", list);
                return true;
            }
            // Anschluss
            else if (itemId == R.id.nav_Strom) {
                filterList("Strom", list);
                return true;
            } else if (itemId == R.id.nav_Wasser) {
                filterList("Wasser", list);
                return true;
            } else if (itemId == R.id.nav_Weitere) {
                filterList("Weitere", list);
                return true;
            }
            // Auslage
            else if (itemId == R.id.nav_Sortiment) {
                filterList("Sortiment", list);
                return true;
            } else if (itemId == R.id.nav_Highlights) {
                filterList("Highlights", list);
                return true;
            }
            // Dokumente
            else if (itemId == R.id.nav_Reisegewerbekarte) {
                filterList("Reisegewerbekarte", list);
                return true;
            } else if (itemId == R.id.nav_Prufbuch) {
                filterList("Prüfbuch", list);
                return true;
            } else if (itemId == R.id.nav_Ausführungsgenehmigung) {
                filterList("Ausführungsgenehmigung", list);
                return true;
            } else if (itemId == R.id.nav_Sonstige) {
                filterList("Sonstige", list);
                return true;
            } else if (itemId == R.id.reset) {
                Stash.put(Constants.isDAYNIGHT, false);
                adapter.getFilter().filter("");
            }

            return false;
        });

        popupMenu.show();
    }

    private void filterList(String beschreibung, ArrayList<StallModel> list) {
        Stash.put(Constants.isDAYNIGHT, false);
        adapter.getFilter().filter(beschreibung);
    }

    private void geschaftFilter(String tag, String cat, ArrayList<StallModel> list) {
        Stash.put(Constants.isDAYNIGHT, true);
        Stash.put(Constants.WHATDAY, tag);
        Stash.put(Constants.CAT, cat);
        adapter.getFilter().filter(cat);
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(binding.getRoot().getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());

        MenuItem atoz = popupMenu.getMenu().getItem(0);
        MenuItem rtoo = popupMenu.getMenu().getItem(1);

        if (!az) {
            atoz.setTitle(getResources().getString(R.string.sort_z_a));
        } else {
            atoz.setTitle(getResources().getString(R.string.sort_a_z));
        }

        if (!ro) {
            rtoo.setTitle(getResources().getString(R.string.sort_old_new));
        } else {
            rtoo.setTitle(getResources().getString(R.string.sort_new_old));
        }

        // Set a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.filter_AZ) {

                if (az) {
                    az = false;
                    item.setTitle(getResources().getString(R.string.sort_z_a));
                } else {
                    az = true;
                    item.setTitle(getResources().getString(R.string.sort_a_z));
                }

                List<StallModel> sublist = new ArrayList<>(mainlist);
                sublist.sort((obj1, obj2) -> obj1.getItem().compareToIgnoreCase(obj2.getItem()));
                if (az) {
                    Collections.reverse(sublist);
                }
                mainlist.clear();
                mainlist.addAll(sublist);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.filter_RO) {
                if (ro) {
                    ro = false;
                    item.setTitle(getResources().getString(R.string.sort_old_new));
                } else {
                    ro = true;
                    item.setTitle(getResources().getString(R.string.sort_new_old));
                }
                List<StallModel> sublist = new ArrayList<>(mainlist);
                Collections.reverse(sublist);
                mainlist.clear();
                mainlist.addAll(sublist);
                adapter.notifyDataSetChanged();
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

}