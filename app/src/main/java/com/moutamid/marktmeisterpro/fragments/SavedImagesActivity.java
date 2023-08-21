package com.moutamid.marktmeisterpro.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.adapters.ImagesSubAdapter;
import com.moutamid.marktmeisterpro.adapters.ImagesSubAdapter;
import com.moutamid.marktmeisterpro.databinding.ActivitySavedImagesBinding;
import com.moutamid.marktmeisterpro.fragments.SavedFragment;
import com.moutamid.marktmeisterpro.fragments.ScanFragment;
import com.moutamid.marktmeisterpro.models.StallModel;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SavedImagesActivity extends Fragment {
    ActivitySavedImagesBinding binding;
    ImagesSubAdapter adapter;
    ArrayList<StallModel> list;
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

        list = new ArrayList<>();

        binding.sort.setOnClickListener(v -> {
            showPopupMenu(v);
        });
        binding.filter.setOnClickListener(v -> {
            showFilterMenu(v);
        });



        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        list = new ArrayList<>();
        list.addAll(Stash.getArrayList(ID, StallModel.class));

        binding.totalSize.setText("Sie haben " + list.size() + " Bild gespeichert");

        list.add(new StallModel(ID, NAME, "", "", "", "", "", "", true));
        adapter = new ImagesSubAdapter(requireContext(), list);
        binding.savedRC.setAdapter(adapter);
    }

    private void showFilterMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(binding.getRoot().getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.filter_all_menu, popupMenu.getMenu());

        // Set a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            // Tag  Geschäft
            if (itemId == R.id.nav_vorne_Tag) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getNight().equals("Tag") && stallModel.getBeschreibung().equals("vorne")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);
                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_hinten_Tag) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getNight().equals("Tag") && stallModel.getBeschreibung().equals("hinten")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_seite_links_Tag) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getNight().equals("Tag") && stallModel.getBeschreibung().equals("Seite links")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_seite_rechts_Tag) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getNight().equals("Tag") && stallModel.getBeschreibung().equals("Seite rechts")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_turen_Tag) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getNight().equals("Tag") && stallModel.getBeschreibung().equals("Türen")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
            // Nacht  Geschäft
            else if (itemId == R.id.nav_vorne_Nacht) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getNight().equals("Nacht") && stallModel.getBeschreibung().equals("vorne")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_hinten_Nacht) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getNight().equals("Nacht") && stallModel.getBeschreibung().equals("hinten")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_seite_links_Nacht) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getNight().equals("Nacht") && stallModel.getBeschreibung().equals("Seite links")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_seite_rechts_Nacht) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getNight().equals("Nacht") && stallModel.getBeschreibung().equals("Seite rechts")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_turen_Nacht) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getNight().equals("Nacht") && stallModel.getBeschreibung().equals("Türen")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
            // Anschluss
            else if (itemId == R.id.nav_Strom) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getBeschreibung().equals("Strom")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_Wasser) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getBeschreibung().equals("Wasser")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_Weitere) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getBeschreibung().equals("Weitere")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
            // Auslage
            else if (itemId == R.id.nav_Sortiment) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getBeschreibung().equals("Sortiment")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_Highlights) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getBeschreibung().equals("Highlights")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
            // Dokumente
            else if (itemId == R.id.nav_Reisegewerbekarte) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getBeschreibung().equals("Reisegewerbekarte")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);


                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_Prufbuch) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getBeschreibung().equals("Prüfbuch")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);


                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_Ausführungsgenehmigung) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getBeschreibung().equals("Ausführungsgenehmigung")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);


                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_Sonstige) {

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                ArrayList<StallModel> originalList = new ArrayList();
                originalList.addAll(sublist);
                StallModel lastItem = list.get(list.size() - 1);
                List<StallModel> filteredList = originalList.stream()
                        .filter(stallModel -> stallModel.getBeschreibung().equals("Sonstige")).collect(Collectors.toList());
                ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                finalList.add(lastItem);

                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), finalList);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.reset) {
                adapter = new ImagesSubAdapter(binding.getRoot().getContext(), list);
                binding.savedRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            return false;
        });

        popupMenu.show();
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

                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                sublist.sort((obj1, obj2) -> obj1.getItem().compareToIgnoreCase(obj2.getItem()));
                if (az) {
                    Collections.reverse(sublist);
                }
                StallModel lastItem = list.get(list.size() - 1);
                list.clear();
                list.addAll(sublist);
                list.add(lastItem);
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
                List<StallModel> sublist = new ArrayList<>(list.subList(0, list.size() - 1));
                Collections.reverse(sublist);

                StallModel lastItem = list.get(list.size() - 1);
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