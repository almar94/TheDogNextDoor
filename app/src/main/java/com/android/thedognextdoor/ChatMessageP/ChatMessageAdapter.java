package com.android.thedognextdoor.ChatMessageP;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.thedognextdoor.R;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {

    FirebaseDatabase db = FirebaseDatabase.getInstance();


    private List<Messages> ArrayList = new ArrayList<>();

    public List<Messages> getArrayList() {
        return ArrayList;
    }

    public void setArrayList(List<Messages> arrayList) {
        ArrayList = arrayList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_100, parent, false);
        return new ChatMessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        final Messages messages = ArrayList.get(position);
        holder.show.setText(messages.getMessages());

    }



    @Override
    public int getItemCount() {
        return ArrayList.size();
    }

    class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        TextView show;
        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            show = itemView.findViewById(R.id.tvShowMessage);


        }

    }


}
