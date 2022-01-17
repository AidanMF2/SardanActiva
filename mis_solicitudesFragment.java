package com.example.ushare.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ushare.adapters.AdapterMissolicitudes;
import com.example.ushare.R;
import com.example.ushare.Usuarios_DDBB;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class mis_solicitudesFragment extends Fragment {

    public mis_solicitudesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ProgressBar progressBar;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        View view = inflater.inflate(R.layout.fragment_solicitudes, container, false);


        progressBar = view.findViewById(R.id.progressbar);


        assert user != null;

        RecyclerView rv;
        ArrayList<Usuarios_DDBB> usersArrayList;
        AdapterMissolicitudes adapter;
        LinearLayoutManager mLayoutManager;

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(mLayoutManager);

        usersArrayList = new ArrayList<>();
        adapter = new AdapterMissolicitudes(usersArrayList,getContext());
        rv.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("users");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);

                    usersArrayList.removeAll(usersArrayList);
                    for(DataSnapshot datasnapshot : snapshot.getChildren()){
                        Usuarios_DDBB user = datasnapshot.getValue(Usuarios_DDBB.class);
                        usersArrayList.add(user);
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No existeixen usuaris", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }
}