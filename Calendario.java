package com.example.ushare;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ushare.adapters.AdapterCalendario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Calendario extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ArrayList<String> actes = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_apuntados = database.getReference();

    DatabaseReference ref_contador = ref_apuntados.child("users").child(user.getUid()).child("Apuntados").child("contador");
    DatabaseReference ref_estado = database.getReference("Apuntados").child(user.getUid());

    private String actividad;

    private SearchView sv_search;
    AdapterCalendario adapter;

    private List<Model> sardanaSamples = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);


        sv_search = findViewById(R.id.searchview);

        BottomNavigationView navigation = findViewById(R.id.Navegacion);
        navigation.setSelectedItemId(R.id.firstFragment);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemSelectedListener);

        for(int i = 0;i<actes.size();++i){
            actes.remove(i);
        }

        DatabaseReference ref = ref_apuntados.child("users").child(user.getUid()).child("Apuntados");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot: snapshot.getChildren()){
                    actividad = datasnapshot.getKey().toString();
                    if(!actividad.equals("contador")) actes.add(actividad);
                    Log.d("MyActivity", "Entro a ver que tal " + datasnapshot.getKey());
                }
                for(int i=0;i<actes.size();i++){
                    Log.d("MyActivity", "sisii " + actes.get(i));
                }
                initListener();
                LeerData();
                init();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void onBackPressed(){
        Intent iniciar = new Intent(this, Filtrar.class);
        startActivity(iniciar);
        finish();
        super.onBackPressed();
    }

    private void LeerData() {
        InputStream is = getResources().openRawResource(R.raw.prueba);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line = "";
        try{
            reader.readLine();

            while((line = reader.readLine()) != null){
                //Dividido por comas
                String [] prueba = null;
                prueba = line.split(",");

                int borrar=1;
                boolean trobat = false;
                for(int i = 0;i<prueba.length;i++){
                    if(!prueba[i].isEmpty()){
                        char first = prueba[i].charAt(0);
                        if(first == ' '){
                            while(prueba[i-borrar].equals("eliminar")){
                                borrar=borrar+1;
                                trobat=true;
                            }

                            if(!trobat){
                                prueba[i-1]=prueba[i-1].concat(prueba[i]);
                                prueba[i] = "eliminar";
                            }else{
                                prueba[i-borrar-1]=prueba[i-borrar-1].concat(prueba[i]);
                                prueba[i] = "eliminar";
                            }
                            borrar=1;
                            trobat=false;
                        }
                    }
                }

                String [] tokens = new String[prueba.length];
                int contador=0;
                for(int i = 0;i<prueba.length;i++){
                    if(!prueba[i].equals("eliminar")){
                        String pillo = prueba[i];
                        tokens[contador] = pillo;
                        contador++;
                    }

                }
                //String [] tokens = line.split(",");
                //Leer la data
                Model sample = new Model();
                int id = Integer.parseInt(tokens[0]);
                String transformar = tokens[1];
                StringBuilder transformar2 = new StringBuilder(transformar);
                transformar2.deleteCharAt(0);
                transformar2.deleteCharAt(transformar2.length() -1);
                transformar = transformar2.toString();

                for(int i=0;i<actes.size();++i){
                    if(Integer.parseInt(actes.get(i)) == id){
                        sample.setId(id);
                        sample.setNomactivitat(transformar);
                        sample.setTipus(transformar);
                        sample.setHores(tokens[6]);
                        sample.setMunicipi(tokens[29]);
                        sample.setNomactivitat(tokens[10]);
                        sample.setAnul(tokens[8]);
                        sample.setMesdades(tokens[12]);
                        sample.setDia(tokens[2]);
                        if(!(tokens[17].equals(""))){
                            String quitar = tokens[17];
                            StringBuilder quitar2 = new StringBuilder(quitar);
                            quitar2.deleteCharAt(0);
                            quitar2.deleteCharAt(quitar2.length() -1);
                            quitar = quitar2.toString();

                            Double lat = Double.parseDouble(quitar);
                            sample.setLatitud(lat);

                            quitar = tokens[18];
                            quitar2 = new StringBuilder(quitar);
                            quitar2.deleteCharAt(0);
                            quitar2.deleteCharAt(quitar2.length() -1);
                            quitar = quitar2.toString();

                            Double lon = Double.parseDouble(quitar);

                            sample.setLongitud(lon);
                        }
                        else{
                            String quitar = tokens[19];
                            StringBuilder quitar2 = new StringBuilder(quitar);
                            quitar2.deleteCharAt(0);
                            quitar2.deleteCharAt(quitar2.length() -1);
                            quitar = quitar2.toString();

                            Double latapr = Double.parseDouble(quitar);
                            sample.setLatitudaprox(latapr);

                            quitar = tokens[20];
                            quitar2 = new StringBuilder(quitar);
                            quitar2.deleteCharAt(0);
                            quitar2.deleteCharAt(quitar2.length() -1);
                            quitar = quitar2.toString();
                            Double lonapr = Double.parseDouble(quitar);

                            sample.setLongitudaprox(lonapr);
                        }
                        sardanaSamples.add(sample);
                    }
                }
            }
        } catch (IOException e){
            Log.wtf("MyActivity", "Error reading the data file" + line, e);
            e.printStackTrace();
        }

    }
    public void init(){
        AdapterCalendario adapter = new AdapterCalendario(sardanaSamples, this, new AdapterCalendario.OnItemClickListener() {
            @Override
            public void onItemClick(Model item) {
                moveToDescription(item);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.rv_sardanas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void moveToDescription(Model item) {
        Intent intent = new Intent(this, Info2.class);
        intent.putExtra("Model",item);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
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