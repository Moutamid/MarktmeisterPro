package com.moutamid.marktmeisterpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.models.StallModel;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesVH> {
    Context context;
    ArrayList<StallModel> list;

    public ImagesAdapter(Context context, ArrayList<StallModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ImagesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImagesVH(LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesVH holder, int position) {
        StallModel model = list.get(holder.getAdapterPosition());

        Glide.with(context).load(model.getImageURL()).into(holder.image);
        int capturedImageOrientation = Constants.rotateImage(model.getImageURL());

        if (capturedImageOrientation == 90 || capturedImageOrientation == 270) {
            holder.image.setRotation(-90); // Rotate the ImageView for horizontal images
        } else {
            holder.image.setRotation(0);  // Reset rotation for portrait images
        }

        holder.cat.setText(model.getItem());
        holder.type.setText(model.getBeschreibung());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImagesVH extends RecyclerView.ViewHolder {
        ImageView image, add;
        TextView cat, type;
        LinearLayout data;
        MaterialCardView mainCard;

        public ImagesVH(@NonNull View itemView) {
            super(itemView);
            mainCard = itemView.findViewById(R.id.mainCard);
            data = itemView.findViewById(R.id.data);
            cat = itemView.findViewById(R.id.cat);
            type = itemView.findViewById(R.id.type);
            image = itemView.findViewById(R.id.image);
            add = itemView.findViewById(R.id.add);
        }
    }

}
