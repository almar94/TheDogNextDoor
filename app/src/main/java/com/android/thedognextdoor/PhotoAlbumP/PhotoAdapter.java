package com.android.thedognextdoor.PhotoAlbumP;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.thedognextdoor.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private Context context;
    private List<Upload> mUpload;
    private onItemClickListener mListener;

    public PhotoAdapter(Context context, List<Upload> mUpload) {
        this.context = context;
        this.mUpload = mUpload;
    }
    public void setData(List<Upload> uploads){
        this.mUpload=uploads;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.cell_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Upload uploadCurrent = mUpload.get(position);
        Log.d("TAG", "onBindViewHolder: "+ uploadCurrent.getPhotoUrl());
        if (uploadCurrent.getPhotoUrl() != null) {
            Glide.with(context)
                    .load(uploadCurrent.getPhotoUrl())
                    .into(holder.imageViewUpload);
        }
//        Picasso.get()
//                .load(uploadCurrent.getPhotoUrl())
//                .fit().centerCrop().into(holder.imageViewUpload);
    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        ImageView imageViewUpload;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewUpload = itemView.findViewById(R.id.imageViewUpload);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("select");
            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");
            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                            mListener.onDeleteClick(position);
                            return true;
                }
            }
            return false;
        }

    }


    public interface onItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }
}
