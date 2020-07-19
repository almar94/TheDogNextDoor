package com.android.thedognextdoor.ChatMessageP;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.thedognextdoor.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ConversationFragment extends Fragment {

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseAuth mAUth = FirebaseAuth.getInstance();
    final DatabaseReference mes = db.getReference().child("mes");
    static ChatMessageAdapter mAdapter = new ChatMessageAdapter();
    EditText msgEt;
    Button btn;

    TabLayout tabLayout;
    ViewPager viewPager;

    static List<Messages> list = new ArrayList<>();
    public ConversationFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conversation_fragment, container, false);
        return view;
//        RecyclerView recyclerView = view.findViewById(R.id.rv);
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(mAdapter);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.TabLayout);
        viewPager = view.findViewById(R.id.ViewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());


//        msgEt = view.findViewById(R.id.messageEt);
//        btn = view.findViewById(R.id.sendBtn);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String mes = msgEt.getText().toString();
//                db.getReference().child("messages").child(mAUth.getUid()).push().setValue(new Messages(mes));
//                list.add(new Messages(mes));
//                mAdapter.setArrayList(list);
//            }
//        });
    }
}

class ViewPagerAdapter extends FragmentPagerAdapter{

    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.titles = new ArrayList<>();

    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title){
        fragments.add(fragment);
        titles.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}

