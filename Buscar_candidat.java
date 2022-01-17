package com.example.ushare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ushare.R;
import com.example.ushare.adapters.AdapterPersona;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Buscar_candidat extends AppCompatActivity {

    AutoCompleteTextView auto;
    ArrayAdapter<String> adapterItems;
    String [] items = {"Flabiol", "Tible", "Tenora", "Trompeta", "Trombo", "Fiscorn", "Contrabaix", "Ballar"};
    List<Personas_DDBB> elements;
    AdapterPersona listAdapter;
    boolean funciona = false;
    String item = "flabiol";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_apuntados = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_candidat);

        BottomNavigationView navigation = findViewById(R.id.Navegacion);
        navigation.setSelectedItemId(R.id.thirdFragment);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemSelectedListener);

        auto = findViewById(R.id.auto);

        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item, items);

        init(item);
        auto.setAdapter(adapterItems);
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(),"Item: " + item, Toast.LENGTH_LONG).show();
                item = item.toLowerCase();
                init(item);
                if(funciona) lista();
            }
        });



    }



    public void init(String item){
        elements = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("users");
        FirebaseUser yo = FirebaseAuth.getInstance().getCurrentUser();



        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    int i = 0;
                    for(DataSnapshot datasnapshot : snapshot.getChildren()){
                        if(!yo.getUid().equals(datasnapshot.getKey())){
                            Usuarios_DDBB info = datasnapshot.getValue(Usuarios_DDBB.class);

                            String nombre = datasnapshot.child("usuario").getValue().toString();
                            String apellido = datasnapshot.child("apellido").getValue().toString();
                            String municipio = datasnapshot.child("municipio").getValue().toString();
                            String edat = datasnapshot.child("edat").getValue().toString();
                            String id = datasnapshot.child("id").getValue().toString();
                            String actividad = datasnapshot.child("activitats").getValue().toString();
                            String cobles = datasnapshot.child("cobles").getValue().toString();
                            String texto = datasnapshot.child("texto").getValue().toString();
                            String coche = datasnapshot.child("coche").getValue().toString();
                            String foto = datasnapshot.child("imagen").getValue().toString();

                            DatabaseReference dentro = database.getReference("users").child(datasnapshot.getKey()).child("Instrumento").child(item);
                            dentro.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //Log.d("casa", "el key es " + snapshot.getKey());
                                    //Log.d("casa", "el valor es " + snapshot.getValue());
                                    if(snapshot.getValue().equals("si")){
                                        if (info.getUri() != null){

                                        }
                                        Personas_DDBB copiar = new Personas_DDBB(foto,nombre,apellido,municipio,edat,
                                                id, actividad, cobles, texto, coche);
                                        //Log.d("hola","Ojo cuidado : " + info.getMunicipio());
                                        elements.add(copiar);
                                        lista();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            i+=1;
                            Log.d("hola","se rellena de cum? : " + elements.size());
                            Log.d("hola","tengo que entrar unas 7 veces : " + i);
                        }
                    }
                }
            funciona = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void lista() {
        Log.d("hola","el nombre del afortunado es : " + elements.size());
        listAdapter = new AdapterPersona(elements, this, new AdapterPersona.OnItemClickListener() {
            @Override
            public void onItemClick(Personas_DDBB item) {
                moveToDescription(item);
            }
        });
                RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(listAdapter);
    }

    public void moveToDescription(Personas_DDBB item) {
        Intent intent = new Intent(this, PerfilPublico.class);
        intent.putExtra("Model", item);
        startActivity(intent);
    }

    private final BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.firstFragment:
                    if(item.getItemId() == R.id.firstFragment){
                        startActivity(new Intent(getApplicationContext(),Filtrar.class));
                        finish();
                        overridePendingTransition(0,0);
                    }
                case R.id.secondFragment:
                    if(item.getItemId() == R.id.secondFragment) {
                        startActivity(new Intent(getApplicationContext(), Sardanistas.class));
                        finish();
                        overridePendingTransition(0, 0);
                    }
                case R.id.thirdFragment:
                    if(item.getItemId() == R.id.thirdFragment) {
                        startActivity(new Intent(getApplicationContext(), Substitut.class));
                        finish();
                        overridePendingTransition(0, 0);
                    }
                case R.id.fourthFragment:
                    if(item.getItemId() == R.id.fourthFragment) {
                        startActivity(new Intent(getApplicationContext(), Chat.class));
                        finish();
                        overridePendingTransition(0, 0);
                    }
                case R.id.fifthFragment:
                    if(item.getItemId() == R.id.fifthFragment) {
                        startActivity(new Intent(getApplicationContext(), Perfil.class));
                        finish();
                        overridePendingTransition(0, 0);
                    }
            }
        }
    };
}