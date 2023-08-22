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

public class ImagesSubAdapter extends RecyclerView.Adapter<ImagesSubAdapter.ImagesVH> {
    Context context;
    ArrayList<StallModel> list;

    public ImagesSubAdapter(Context context, ArrayList<StallModel> list) {
        this.context = context;
        this.list = list;
        Toast.makeText(context, "Listss   " + list.size(), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public ImagesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImagesVH(LayoutInflater.from(context).inflate(R.layout.image_layout2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesVH holder, int position) {
        StallModel model = list.get(holder.getAdapterPosition());

        GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                // Handle the double-click event here
                showImage(model);
                return true;
            }
        });

        holder.image.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });

        Log.d("PATH123" , "   " + model.isAdd());

        if (model.isAdd()) {
            holder.add.setVisibility(View.VISIBLE);
            holder.data.setVisibility(View.GONE);
            holder.mainCard.setCardBackgroundColor(context.getResources().getColor(R.color.background));

            holder.mainCard.setOnClickListener(v -> {
                Stash.put(Constants.NAME, model.getStallName());
                Stash.put(Constants.applicationID, model.getApplicationID());
                context.startActivity(new Intent(context, SelectItemActivity.class));
            });

        } else {
            Glide.with(context).load(model.getImageURL()).into(holder.image);

            int capturedImageOrientation = 0;
            try {
                ExifInterface exifInterface = new ExifInterface(model.getImageURL());
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_NORMAL:
                        capturedImageOrientation = 0;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        capturedImageOrientation = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        capturedImageOrientation = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        capturedImageOrientation = 270;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (capturedImageOrientation == 90 || capturedImageOrientation == 270) {
                holder.image.setRotation(-90); // Rotate the ImageView for horizontal images
            } else {
                holder.image.setRotation(0);  // Reset rotation for portrait images
            }

            holder.cat.setText(model.getItem());
            holder.type.setText(model.getBeschreibung());
        }

        holder.delete.setOnClickListener(v -> {
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

    public void updateData(ArrayList<StallModel> newData) {
        list.clear();
        list.addAll(newData);
        notifyDataSetChanged();
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

        int capturedImageOrientation = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(model.getImageURL());
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    capturedImageOrientation = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    capturedImageOrientation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    capturedImageOrientation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    capturedImageOrientation = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (capturedImageOrientation == 90 || capturedImageOrientation == 270) {
            image.setRotation(-90); // Rotate the ImageView for horizontal images
        } else {
            image.setRotation(0);  // Reset rotation for portrait images
        }


        close.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImagesVH extends RecyclerView.ViewHolder {
        ImageView image, add;
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
            add = itemView.findViewById(R.id.add);
        }
    }

}
