package com.moutamid.marktmeisterpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.interfaces.SavedClickListener;
import com.moutamid.marktmeisterpro.models.Stall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SavedMainAdapter extends RecyclerView.Adapter<SavedMainAdapter.SavedVH> implements Filterable {

    Context context;
    ArrayList<Stall> list;
    ArrayList<Stall> stallAll;
    ImagesAdapter childItemAdapter;
    SavedClickListener savedClickListener;

    public SavedMainAdapter(Context context, ArrayList<Stall> list, SavedClickListener savedClickListener) {
        this.context = context;
        this.list = list;
        this.stallAll = new ArrayList<>(list);
        this.savedClickListener = savedClickListener;
    }

    @NonNull
    @Override
    public SavedVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SavedVH(LayoutInflater.from(context).inflate(R.layout.saved_images, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SavedVH holder, int position) {

        Stall stall = list.get(holder.getAdapterPosition());

        holder.name.setText(stall.getName());

        holder.applicationID.setText("ID: " + stall.getApplicationID());

        holder.itemView.setOnClickListener(v -> savedClickListener.onClick(list.get(holder.getAdapterPosition())));

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        layoutManager.setInitialPrefetchItemCount(stall.getStall().size());

        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        Collections.reverse(stall.getStall());
        childItemAdapter = new ImagesAdapter(context, stall.getStall());
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setHasFixedSize(false);
        holder.recyclerView.setAdapter(childItemAdapter);
        holder.recyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Stall> filterList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filterList.addAll(stallAll);
            } else {
                for (Stall listModel : stallAll) {
                    if (
                            listModel.getName().toLowerCase().contains(charSequence.toString()) ||
                            listModel.getApplicationID().contains(charSequence.toString())
                    ) {
                        filterList.add(listModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        //run on Ui thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends Stall>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SavedVH extends RecyclerView.ViewHolder {
        TextView name, applicationID;
        RecyclerView recyclerView;

        public SavedVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cat);
            applicationID = itemView.findViewById(R.id.applicationID);
            recyclerView = itemView.findViewById(R.id.stallListRC);
        }
    }

}
