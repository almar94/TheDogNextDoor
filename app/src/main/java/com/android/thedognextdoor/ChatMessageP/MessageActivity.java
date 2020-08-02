package com.android.thedognextdoor.ChatMessageP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.thedognextdoor.ChatMessageP.Adapters.MessageAdapter;
import com.android.thedognextdoor.ProfileP.MyProfileDog;
import com.android.thedognextdoor.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    public TextView userName;
    public CircleImageView imageView;
    String userID;
    FirebaseUser fUser;
    DatabaseReference reference;
    Intent intent;

    EditText msg_editText;
    ImageButton send_btn;
    RecyclerView recyclerView;
    MessageAdapter adapter;
    List<ChatMessage> mMessage;

    public FirebaseDatabase firebaseDatabase;
    public StorageReference storageReference;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userName = findViewById(R.id.textViewUsers);
        imageView = findViewById(R.id.imageViewUsers);
        send_btn = findViewById(R.id.btn_send);
        msg_editText = findViewById(R.id.text_send);

        recyclerView = findViewById(R.id.recyclerView_fromToolbar);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+firebaseAuth.getUid()+"/MyProfileImage.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });

        intent = getIntent();
        userID = intent.getStringExtra("userID");
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyProfileDog myProfileDog = snapshot.getValue(MyProfileDog.class);
                userName.setText(myProfileDog.getMyName());
                if (myProfileDog.getImageURL() != null) {
                    if (myProfileDog.getImageURL().equals("default")) {
                        imageView.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Glide.with(MessageActivity.this).load(myProfileDog.getImageURL())
                                .into(imageView);
                    }
                }
                readMessage(fUser.getUid(), userID, myProfileDog.getImageURL());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }});

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msg_editText.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fUser.getUid(), userID, msg);
                }else {
                    Toast.makeText(MessageActivity.this, "Message is empty", Toast.LENGTH_SHORT).show();
                }
                msg_editText.setText("");
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message){
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        databaseReference.child("Chats").push().setValue(hashMap);
        ChatMessage chatMessage = new ChatMessage(sender, receiver, message);


        //Adding user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance()
                .getReference("ChatList").child(fUser.getUid()).child(userID);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatRef.child("id").setValue(userID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage(final String myID, final String userID, final String imgURL) {
        mMessage = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMessage.clear();
                for (DataSnapshot snapshotM : snapshot.getChildren()) {
                    ChatMessage chatMessage = snapshotM.getValue(ChatMessage.class);
                    if (chatMessage.getReceiver().equals(myID) &&
                            chatMessage.getSender().equals(userID) ||
                            chatMessage.getReceiver().equals(userID) &&
                                    chatMessage.getSender().equals(myID)) {
                        mMessage.add(chatMessage);
                    }
                    adapter = new MessageAdapter(MessageActivity.this, mMessage, imgURL);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
        private void CheckStatus (String status){
            reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(fUser.getUid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", status);
            reference.updateChildren(hashMap);
        }

        @Override
        protected void onResume() {
            super.onResume();
            CheckStatus("online");
        }

        @Override
        protected void onPause() {
            super.onPause();
            CheckStatus("offline");
        }
}