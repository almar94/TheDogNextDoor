package com.android.thedognextdoor.SearchMatchesP;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.thedognextdoor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import link.fls.swipestack.SwipeStack;

public class SearchFragment extends Fragment {

    public FirebaseAuth mAuth;
    public String currentUId;
    public DatabaseReference usersDb;


    private SwipeStack cardStack;
    public CardsAdapter cardsAdapter;
    public ArrayList<Cards> cardItems;
    public int currentPosition;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        usersDb = FirebaseDatabase.getInstance().getReference().child("MyUsers");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        cardStack = view.findViewById(R.id.container);
        setCardStackAdapter();

        currentPosition = 0;

        //Handling swipe event of Cards stack
        cardStack.setListener(new SwipeStack.SwipeStackListener() {
            @Override
            public void onViewSwipedToLeft(int position) {
                currentPosition = position + 1;
                Toast.makeText(getContext(), "Like", Toast.LENGTH_SHORT).show();
                cardStack.swipeTopViewToLeft();


            }

            @Override
            public void onViewSwipedToRight(int position) {
                currentPosition = position + 1;
                cardStack.swipeTopViewToRight();
            }

            @Override
            public void onStackEmpty() {
                Toast.makeText(getContext(), "No more people", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(getContext(), "new Connection", Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches").child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setCardStackAdapter() {
        cardItems = new ArrayList<>();
        cardItems.add(new Cards(R.drawable.dog2, "Huyen My", "Hanoi"));
        cardItems.add(new Cards(R.drawable.defavatar, "Do Ha", "Nghe An"));
        cardItems.add(new Cards(R.drawable.dog2, "Dong Nhi", "Hue"));
        cardItems.add(new Cards(R.drawable.background, "Le Quyen", "Sai Gon"));
        cardItems.add(new Cards(R.drawable.background, "Phuong Linh", "Thanh Hoa"));

        cardsAdapter = new CardsAdapter(getActivity(), cardItems);
        cardStack.setAdapter(cardsAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset) {
            cardStack.resetStack();
        }
        return super.onOptionsItemSelected(item);
    }
}
