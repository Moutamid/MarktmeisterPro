package com.moutamid.marktmeisterpro.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SavedFragment extends Fragment {
    FragmentSavedBinding binding;
    ArrayList<Stall> stalls;
    SavedMainAdapter adapter;
    boolean az = true, ro = true;

    public SavedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(getLayoutInflater(), container, false);

        binding.stallListRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.stallListRC.setHasFixedSize(false);

        binding.sort.setOnClickListener(v -> {
            showPopupMenu(v);
        });
        binding.filter.setOnClickListener(v -> {
            showFilterMenu(v);
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

    private void showFilterMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.filter_all_menu, popupMenu.getMenu());

        // Set a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            // Tag  Geschäft
            if (itemId == R.id.nav_vorne_Tag) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getNight().equals("Tag") && stallModel.getBeschreibung().equals("vorne")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_hinten_Tag) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getNight().equals("Tag") && stallModel.getBeschreibung().equals("hinten")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_seite_links_Tag) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getNight().equals("Tag") && stallModel.getBeschreibung().equals("Seite links")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_seite_rechts_Tag) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getNight().equals("Tag") && stallModel.getBeschreibung().equals("Seite rechts")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_turen_Tag) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getNight().equals("Tag") && stallModel.getBeschreibung().equals("Türen")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
            // Nacht  Geschäft
            else if (itemId == R.id.nav_vorne_Nacht) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getNight().equals("Nacht") && stallModel.getBeschreibung().equals("vorne")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_hinten_Nacht) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getNight().equals("Nacht") && stallModel.getBeschreibung().equals("hinten")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_seite_links_Nacht) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getNight().equals("Nacht") && stallModel.getBeschreibung().equals("Seite links")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_seite_rechts_Nacht) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getNight().equals("Nacht") && stallModel.getBeschreibung().equals("Seite rechts")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_turen_Nacht) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getNight().equals("Nacht") && stallModel.getBeschreibung().equals("Türen")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
            // Anschluss
            else if (itemId == R.id.nav_Strom) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getBeschreibung().equals("Strom")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_Wasser) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getBeschreibung().equals("Wasser")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_Weitere) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getBeschreibung().equals("Weitere")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
            // Auslage
            else if (itemId == R.id.nav_Sortiment) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getBeschreibung().equals("Sortiment")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_Highlights) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getBeschreibung().equals("Highlights")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
            // Dokumente
            else if (itemId == R.id.nav_Reisegewerbekarte) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getBeschreibung().equals("Reisegewerbekarte")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_Prufbuch) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getBeschreibung().equals("Prüfbuch")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_Ausführungsgenehmigung) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getBeschreibung().equals("Ausführungsgenehmigung")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.nav_Sonstige) {
                for (int i = 0; i < stalls.size(); i++) {
                    for (int j = 0; j < stalls.get(i).getStall().size(); j++) {
                        ArrayList<StallModel> originalList = new ArrayList();
                        originalList.addAll(stalls.get(i).getStall());
                        List<StallModel> filteredList = originalList.stream()
                                .filter(stallModel -> stallModel.getBeschreibung().equals("Sonstige")).collect(Collectors.toList());
                        ArrayList<StallModel> finalList = new ArrayList<>(filteredList);
                        stalls.get(i).setStall(finalList);
                    }
                }
                adapter = new SavedMainAdapter(requireContext(), stalls);
                binding.stallListRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());

        MenuItem atoz = popupMenu.getMenu().getItem(0);
        MenuItem rtoo = popupMenu.getMenu().getItem(1);

        if (!az) {
            atoz.setTitle("Sort Z-A");
        } else {
            atoz.setTitle("Sort A-Z");
        }

        if (!ro) {
            rtoo.setTitle("Old To Recent");
        } else {
            rtoo.setTitle("Recent To Old");
        }

        // Set a listener for menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.filter_AZ) {

                stalls.sort((obj1, obj2) -> obj1.getName().compareToIgnoreCase(obj2.getName()));
                for (Stall s : stalls) {
                    s.getStall().sort((obj1, obj2) -> obj1.getItem().compareToIgnoreCase(obj2.getItem()));
                    if (!az){
                        Collections.reverse(s.getStall());
                    }
                }

                if (!az){
                    Collections.reverse(stalls);
                }

                if (az) {
                    az = false;
                    item.setTitle("Sort Z-A");
                } else {
                    az = true;
                    item.setTitle("Sort A-Z");
                }

                adapter.notifyDataSetChanged();
                return true;
            } else if (itemId == R.id.filter_RO) {
                if (ro) {
                    ro = false;
                    item.setTitle("Old To Recent");
                } else {
                    ro = true;
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