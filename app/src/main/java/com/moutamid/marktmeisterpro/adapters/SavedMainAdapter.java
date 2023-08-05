package com.moutamid.marktmeisterpro.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.activities.SavedImagesActivity;
import com.moutamid.marktmeisterpro.models.Stall;
import com.moutamid.marktmeisterpro.models.StallModel;

import java.util.ArrayList;

public class SavedMainAdapter extends RecyclerView.Adapter<SavedMainAdapter.SavedVH> {

    Context context;
    ArrayList<Stall> list;

    public SavedMainAdapter(Context context, ArrayList<Stall> list) {
        this.context = context;
        this.list = list;
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

        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, SavedImagesActivity.class));
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.recyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        layoutManager.setInitialPrefetchItemCount(stall.getStall().size());

        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

        ImagesAdapter childItemAdapter = new ImagesAdapter(context, stall.getStall());
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setHasFixedSize(false);
        holder.recyclerView.setAdapter(childItemAdapter);
        holder.recyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SavedVH extends RecyclerView.ViewHolder {
        TextView name;
        RecyclerView recyclerView;

        public SavedVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cat);
            recyclerView = itemView.findViewById(R.id.stallListRC);
        }
    }

}
