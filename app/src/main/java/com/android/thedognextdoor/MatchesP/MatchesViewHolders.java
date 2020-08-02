package com.android.thedognextdoor.MatchesP;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.thedognextdoor.ChatMessageP.fragment.ChatsFragment;
import com.android.thedognextdoor.R;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
public TextView mMatchId, mMatchName;
public ImageView mMatchImage;
public MatchesViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMatchId =itemView.findViewById(R.id.Matchid);
        mMatchName = itemView.findViewById(R.id.textViewUsersFragment);

        mMatchImage =  itemView.findViewById(R.id.imageViewUsersFragment);
        }

@Override
public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatsFragment.class);
        Bundle b = new Bundle();
        b.putString("matchId", mMatchId.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
        }
}
