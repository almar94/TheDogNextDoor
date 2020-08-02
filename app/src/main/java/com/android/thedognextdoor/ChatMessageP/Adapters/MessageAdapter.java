package com.android.thedognextdoor.ChatMessageP.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.thedognextdoor.ChatMessageP.ChatMessage;
import com.android.thedognextdoor.ProfileP.MyProfileDog;
import com.android.thedognextdoor.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context context;
    private List<ChatMessage> mMessage;
    private String imgURL;

    FirebaseUser fUser;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public FirebaseDatabase firebaseDatabase;
    public StorageReference storageReference;

    public MessageAdapter(Context context, List<ChatMessage> mMessage, String imgURL) {
        this.context = context;
        this.mMessage = mMessage;
        this.imgURL = imgURL;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.MessageViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
        ChatMessage chatMessage = mMessage.get(position);
        holder.show_mag.setText(chatMessage.getMessage());

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/MyProfileImage.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.profile_img);
            }
        });

//        final MyProfileDog users = new MyProfileDog();
//        String imageURL = users.getImageURL();
//        if (imageURL != null) {
//            if (imgURL.equals("default")) {
//                holder.profile_img.setImageResource(R.mipmap.ic_launcher);
//            } else
//                Glide.with(context).load(imgURL)
//                        .into(holder.profile_img);
//        }
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView show_mag;
        public CircleImageView profile_img;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            show_mag = itemView.findViewById(R.id.textView_chat);
            profile_img = itemView.findViewById(R.id.profile_image_chat);

        }

    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mMessage.get(position).getSender().equals(fUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}

