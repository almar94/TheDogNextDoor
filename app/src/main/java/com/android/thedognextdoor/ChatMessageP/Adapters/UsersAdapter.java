package com.android.thedognextdoor.ChatMessageP.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.thedognextdoor.ChatMessageP.MessageActivity;
import com.android.thedognextdoor.ProfileP.MyProfileDog;
import com.android.thedognextdoor.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private Context context;
    private List<MyProfileDog> mUsers;
    private boolean isChat;

    public FirebaseDatabase firebaseDatabase;
    public StorageReference storageReference;

    public UsersAdapter(Context context, List<MyProfileDog> mUsers, boolean isChat) {
        this.context = context;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_item, parent, false);
        return new UsersAdapter.UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersViewHolder holder, int position) {
        final MyProfileDog users = mUsers.get(position);
        holder.userName.setText(users.getMyName());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+firebaseAuth.getUid()+"/MyProfileImage.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.imageView);
            }
        });

//        String imageURL = users.getImageURL();
//        if (imageURL != null) {
//            if (users.getImageURL().equals("default")) {
//                holder.imageView.setImageResource(R.mipmap.ic_launcher);
//            } else
//                Glide.with(context).load(users.getImageURL())
//                        .into(holder.imageView);
//        }
        //status check
        if (isChat) {
            if (users.getStatus() != null) {
                if (users.getStatus().equals("online")) {
                    holder.img_status_on.setVisibility(View.VISIBLE);
                    holder.img_status_off.setVisibility(View.GONE);
                } else {
                    holder.img_status_on.setVisibility(View.GONE);
                    holder.img_status_off.setVisibility(View.VISIBLE);
                }
            } else {
                holder.img_status_on.setVisibility(View.GONE);
                holder.img_status_off.setVisibility(View.GONE);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userID", users.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class UsersViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public CircleImageView imageView, img_status_on, img_status_off;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.textViewUsersFragment);
            imageView = itemView.findViewById(R.id.imageViewUsersFragment);
            img_status_on = itemView.findViewById(R.id.img_status_on);
            img_status_off = itemView.findViewById(R.id.img_status_off);
        }
    }
}
