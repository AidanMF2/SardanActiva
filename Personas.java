package com.example.ushare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.example.ushare.adapters.AdapterPersona;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Personas extends AppCompatActivity implements Serializable, SearchView.OnQueryTextListener{

    String buscar_id;

    List<Personas_DDBB> elements;
    AdapterPersona listAdapter;

    boolean nadie = false;

    private SearchView sv_search;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_apuntados = database.getReference();
    DatabaseReference ref_users = database.getReference("users");
    DatabaseReference ref = ref_apuntados.child("users").child(user.getUid()).child("Apuntados");

    private String usuario;
    private List<String> id_usuario = new ArrayList<>();

    private List<Personas_DDBB> personas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personas);


        Model model = (Model) getIntent().getSerializableExtra("Model");
        buscar_id = model.getId().toString();
        Log.d("wuau", "El modelo que me llega es: " + model.toString());

        BottomNavigationView navigation = findViewById(R.id.Navegacion);
        navigation.setSelectedItemId(R.id.secondFragment);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemSelectedListener);

        sv_search = findViewById(R.id.searchview);

        /*Personas_DDBB p_model = (Personas_DDBB) getIntent().getSerializableExtra("Model2");
        Log.d("wuau", "El segundo modelo que me llega es: " + p_model.toString());*/

        initListener();
        elements = new ArrayList<>();
        ref_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {

                for(DataSnapshot datasnapshot: snapshot1.getChildren()){
                    Log.d("dentro", "dentro tenemos  " + datasnapshot.getKey());
                    if(!datasnapshot.getKey().equals(user.getUid())){
                        Log.d("dentro", "a ver depende de lo que me salga aquí  " + datasnapshot.getValue().toString());
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

                        DatabaseReference ref_dentro = ref_apuntados.child("users").child(datasnapshot.getKey()).child("Apuntados");
                        ref_dentro.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                //Log.d("dentro", "estoy dentro del for  " + snapshot2.getValue().toString());
                                if(snapshot2.child(buscar_id).exists()){
                                    if(snapshot2.child(buscar_id).getValue().equals("asiste")){
                                        //Log.d("dentro", "dentrísiiimo  " + snapshot2.child(buscar_id).getValue().toString());
                                        Personas_DDBB persona_nueva = new Personas_DDBB(foto,nombre, apellido, municipio, edat, id, actividad, cobles, texto, coche);
                                        elements.add(persona_nueva);
                                        lista();
                                        nadie = true;
                                    }
                                }
                                if(!nadie)Toast.makeText(getApplicationContext(),"Encara no hi ha cap participant!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
                nadie = false;
                init();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void lista() {
        //Log.d("hola","el nombre del afortunado es : " + elements.size());
        listAdapter = new AdapterPersona(elements, this, new AdapterPersona.OnItemClickListener() {
            @Override
            public void onItemClick(Personas_DDBB item) {
                moveToDescription(item);
            }
        });
        RecyclerView rv = findViewById(R.id.rv_sardanas);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(listAdapter);
    }

    private void LeerData() {
        for(int i=0;i<id_usuario.size();i++){
            Log.d("MyActivity", "los usuarios que estan son " + id_usuario);
        }
    }

    public void init(){
        Log.d("MyActivity", "aqui toy " + personas.size());
        for(int i =0;i<personas.size();i++)Log.d("MyActivity", "mecagonto " + personas.get(i));

        RecyclerView recyclerView = findViewById(R.id.rv_sardanas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(adapter);
    }

    public void moveToDescription(Personas_DDBB item) {
        Intent intent = new Intent(this, PerfilPublico.class);
        intent.putExtra("Model",item);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listAdapter.filter(newText);
        return false;
    }
    private void initListener(){
        sv_search.setOnQueryTextListener(this);
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