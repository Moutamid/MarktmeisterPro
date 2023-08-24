package com.moutamid.marktmeisterpro.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.card.MaterialCardView;
import com.moutamid.marktmeisterpro.R;
import com.moutamid.marktmeisterpro.activities.SelectItemActivity;
import com.moutamid.marktmeisterpro.models.Stall;
import com.moutamid.marktmeisterpro.models.StallModel;
import com.moutamid.marktmeisterpro.utilis.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class ImagesSubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    Context context;
    ArrayList<StallModel> list;
    ArrayList<StallModel> listAll;

    private static final int VIEW_TYPE_IMAGE = 0;
    private static final int VIEW_TYPE_BUTTON = 1;

    public ImagesSubAdapter(Context context, ArrayList<StallModel> list) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_IMAGE) {
            View itemView = inflater.inflate(R.layout.image_layout2, parent, false);
            return new ImagesVH(itemView);
        } else {
            View itemView = inflater.inflate(R.layout.add_button, parent, false);
            return new ButtonVH(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ImagesVH) {
            StallModel model = list.get(holder.getAdapterPosition());

            GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    // Handle the double-click event here
                    showImage(model);
                    return true;
                }
            });

            ((ImagesVH) holder).image.setOnTouchListener((v, event) -> {
                gestureDetector.onTouchEvent(event);
                return true;
            });

            Glide.with(context).load(model.getImageURL()).into(((ImagesVH) holder).image);

            ((ImagesVH) holder).cat.setText(model.getItem());
            ((ImagesVH) holder).type.setText(model.getBeschreibung());

            ((ImagesVH) holder).delete.setOnClickListener(v -> {
                ArrayList<StallModel> stall = Stash.getArrayList(model.getApplicationID(), StallModel.class);
                ArrayList<Stall> allStalls = Stash.getArrayList(Constants.STALL_LIST, Stall.class);
                new AlertDialog.Builder(context)
                        .setMessage("Möchten Sie dieses Foto wirklich löschen?")
                        .setNegativeButton("Neín", ((dialog, which) -> dialog.dismiss()))
                        .setPositiveButton("Foto löschen", ((dialog, which) -> {
                            for (int i = 0; i < stall.size(); i++) {
                                if (stall.get(i).getImageURL().equals(model.getImageURL())) {

                                    for (int j = 0; j < allStalls.size(); j++) {
                                        if (allStalls.get(j).getApplicationID().equals(model.getApplicationID())) {
                                            allStalls.get(j).getStall().remove(i);
                                        }
                                        if (allStalls.get(j).getStall().size() == 0) {
                                            allStalls.remove(j);
                                        }
                                    }
                                    File fileToDelete = new File(stall.get(i).getImageURL());
                                    if (fileToDelete.exists()) {
                                        fileToDelete.delete();
                                    }
                                    stall.remove(i);
                                    list.remove(i);
                                    notifyItemRemoved(i);
                                    break;
                                }
                            }
                            Stash.put(model.getApplicationID(), stall);
                            Stash.put(Constants.STALL_LIST, allStalls);
                        }))
                        .show();
            });

        }



    }

    @Override
    public int getItemViewType(int position) {
        if (position < list.size()) {
            return VIEW_TYPE_IMAGE;
        } else {
            return VIEW_TYPE_BUTTON;
        }
    }

    private void showImage(StallModel model) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.big_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView image = dialog.findViewById(R.id.image);
        ImageView close = dialog.findViewById(R.id.close);
        Glide.with(context).load(model.getImageURL()).into(image);

        close.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<StallModel> filterList = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filterList.addAll(listAll);
            } else {
                if (Stash.getBoolean(Constants.isDAYNIGHT)) {
                    String tag = Stash.getString(Constants.WHATDAY);
                    for (StallModel listModel : listAll) {
                        if (
                                listModel.getBeschreibung().equalsIgnoreCase(charSequence.toString()) &&
                                        listModel.getNight().equalsIgnoreCase(tag)
                        ) {
                            filterList.add(listModel);
                        }
                    }
                } else {
                    for (StallModel listModel : listAll) {
                        if (
                                listModel.getBeschreibung().equalsIgnoreCase(charSequence.toString())
                        ) {
                            filterList.add(listModel);
                        }
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends StallModel>) results.values);
            notifyDataSetChanged();
        }
    };

public class ImagesVH extends RecyclerView.ViewHolder {
    ImageView image;
    TextView cat, type;
    RelativeLayout data;
    MaterialCardView mainCard, delete;

    public ImagesVH(@NonNull View itemView) {
        super(itemView);
        mainCard = itemView.findViewById(R.id.mainCard);
        data = itemView.findViewById(R.id.data);
        cat = itemView.findViewById(R.id.cat);
        type = itemView.findViewById(R.id.type);
        image = itemView.findViewById(R.id.image);
        delete = itemView.findViewById(R.id.delete);
    }
}

public class ButtonVH extends RecyclerView.ViewHolder {
    MaterialCardView mainCard;

    public ButtonVH(@NonNull View itemView) {
        super(itemView);
        mainCard = itemView.findViewById(R.id.mainCard);

        mainCard.setOnClickListener(v -> {
            int position = getAdapterPosition();

            // Ensure the position is valid and it's the last item
            if (position != RecyclerView.NO_POSITION && position == list.size()) {
                // Get the resource ID of the last image
                StallModel model = list.get(position - 1);
                // Open the new activity and pass the image resource ID
                Stash.put(Constants.NAME, model.getStallName());
                Stash.put(Constants.applicationID, model.getApplicationID());
                context.startActivity(new Intent(context, SelectItemActivity.class));
            }
        });

    }
}

}
