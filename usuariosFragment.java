package com.example.ushare.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ushare.Chat;
import com.example.ushare.R;
import com.example.ushare.Registrar;
import com.example.ushare.Usuarios_DDBB;
import com.example.ushare.adapters.AdapterUsuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class usuariosFragment extends Fragment{

    public usuariosFragment() {
        // Required empty public constructor
    }


    ArrayList<Usuarios_DDBB> usersArrayList;
    AdapterUsuarios adapter;
    LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ProgressBar progressBar;


        // Inflate the layout for this fragment
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);
        //TextView tv_user = view.findViewById(R.id.tv_user);
        //ImageView img_user = view.findViewById(R.id.img_user);

        //progressBar = view.findViewById(R.id.progressbar);



        assert user != null;
        //tv_user.setText(user.getDisplayName());
        //Glide.with(this).load(user.getPhotoUrl()).into(img_user);

        //RecyclerView rv;

       /* mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(mLayoutManager);

        usersArrayList = new ArrayList<>();
        adapter = new AdapterUsuarios(usersArrayList,getContext());
        rv.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("users");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //progressBar.setVisibility(View.GONE);
                    rv.setVisibility(View.GONE);

                    usersArrayList.removeAll(usersArrayList);
                    /*for(DataSnapshot datasnapshot : snapshot.getChildren()){
                        Usuarios_DDBB user = datasnapshot.getValue(Usuarios_DDBB.class);
                        usersArrayList.add(user);
                    }
                    adapter.notifyDataSetChanged();*/
             /*   }else{
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No existeixen usuaris", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        return view;
    }

}