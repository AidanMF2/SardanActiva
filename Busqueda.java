package com.example.ushare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.ushare.adapters.AdapterCalendario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Busqueda extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private boolean b_aplecs, b_ballades, b_concerts, b_concursos, b_cursets, b_altres;
    private String aplecs, ballades, concerts, concursos, cursets, altres, data_inici, data_final;
    private List<String> buscar_tipus = new ArrayList<>();
    private List<Model> sardanaSamples = new ArrayList<>();
    private String dia_i, mes_i, ano_i, dia_f, mes_f, ano_f;
    int int_dia_i, int_mes_i, int_ano_i, int_dia_f, int_mes_f, int_ano_f;

    AdapterCalendario adapter;
    private SearchView sv_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        BottomNavigationView navigation = findViewById(R.id.Navegacion);
        navigation.setSelectedItemId(R.id.firstFragment);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemSelectedListener);

        sv_search = findViewById(R.id.searchview);

        aplecs = getIntent().getStringExtra("Aplecs");
        ballades = getIntent().getStringExtra("Ballades");
        concerts = getIntent().getStringExtra("Concerts");
        concursos = getIntent().getStringExtra("Concursos");
        cursets = getIntent().getStringExtra("Cursets");
        altres = getIntent().getStringExtra("Altres");

        if(aplecs.equals("Aplec")){
            buscar_tipus.add("Aplec");
        }
        if(ballades.equals("Ballada")){
            buscar_tipus.add("Ballada");
        }
        if(concerts.equals("Concert")){
            buscar_tipus.add("Concert");
        }
        if(concursos.equals("Concursos")){
            buscar_tipus.add("Concurs");
        }
        if(cursets.equals("Curset")){
            buscar_tipus.add("Curset");
        }
        if(altres.equals("Diversos (altres actes)")){
            buscar_tipus.add("Diversos (altres actes)");
        }

        dia_i = getIntent().getStringExtra("dia_inicial");
        int_dia_i = Integer.parseInt(dia_i);
        mes_i = getIntent().getStringExtra("mes_inicial");
        int_mes_i = Integer.parseInt(mes_i);
        ano_i = getIntent().getStringExtra("ano_inicial");
        int_ano_i = Integer.parseInt(ano_i);

        dia_f = getIntent().getStringExtra("dia_final");
        int_dia_f = Integer.parseInt(dia_f);
        mes_f = getIntent().getStringExtra("mes_final");
        int_mes_f = Integer.parseInt(mes_f);
        ano_f = getIntent().getStringExtra("ano_final");
        int_ano_f = Integer.parseInt(ano_f);

        data_inici = (dia_i + "/" + mes_i + "/" + ano_i);
        data_final = (dia_f + "/" + mes_f + "/" + ano_f);

        Log.d("MyActivity", "El dia de busqueda es entre " + data_inici);

        Log.d("MyActivity", "Y  " + data_final);


        initListener();
        LeerData();


        /*if(b_altres) Log.d("MyActivity", "uauauaua");
        else Log.d("MyActivity", "cagaste");*/

        init();

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
                                Log.d("MyActivity", "Ostia ");
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
                String transformar = tokens[1];
                StringBuilder transformar2 = new StringBuilder(transformar);
                transformar2.deleteCharAt(0);
                transformar2.deleteCharAt(transformar2.length() -1);
                transformar = transformar2.toString();

                String transformar_dia = tokens[2];
                String transformar2_dia = transformar_dia.substring(0,2);

                String transformar2_mes = transformar_dia.substring(3,5);

                String transformar2_ano = transformar_dia.substring(6,10);

                for(int i=0;i<buscar_tipus.size();++i){
                    if(transformar.equals(buscar_tipus.get(i))){
                        sample.setId(Integer.parseInt(tokens[0]));
                        sample.setTipus(transformar);
                        sample.setHores(tokens[6]);
                        sample.setMunicipi(tokens[29]);
                        sample.setNomactivitat(tokens[10]);
                        sample.setAnul(tokens[8]);
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
                        sample.setMesdades(tokens[12]);
                        int int_transformar2_dia = Integer.parseInt(transformar2_dia);
                        int int_transformar2_mes = Integer.parseInt(transformar2_mes);
                        int int_transformar2_ano = Integer.parseInt(transformar2_ano);

                        if(int_transformar2_ano <= int_ano_f && int_transformar2_ano >= int_ano_i){
                            if(int_transformar2_ano == int_ano_f){
                                if(int_transformar2_mes <= int_mes_f && int_transformar2_mes >= int_mes_i){
                                    if(int_transformar2_mes == int_mes_f || int_transformar2_mes == int_mes_i){
                                        if(int_transformar2_mes == int_mes_f){
                                            if(int_transformar2_dia <= int_dia_f){
                                                if(int_transformar2_dia >= int_dia_i){
                                                    Log.d("MyActivity", "Entro 1");
                                                    sample.setDia(transformar2_dia + "/" + transformar2_mes + "/" + transformar2_ano);
                                                    sardanaSamples.add(sample);
                                                }
                                            }
                                        }else if(int_transformar2_mes == int_mes_i){
                                            if(int_transformar2_dia>= int_dia_i){
                                                Log.d("MyActivity", "Entro 2");
                                                sample.setDia(transformar2_dia + "/" + transformar2_mes + "/" + transformar2_ano);
                                                sardanaSamples.add(sample);
                                            }
                                        }
                                    }else {
                                        Log.d("MyActivity", "Entro 3");
                                        sample.setDia(transformar2_dia + "/" + transformar2_mes + "/" + transformar2_ano);
                                        sardanaSamples.add(sample);
                                    }
                                }
                            }else if(int_transformar2_ano == int_ano_i){
                                if(int_transformar2_mes >= int_mes_i){
                                    if(int_transformar2_mes == int_mes_i){
                                        if(int_transformar2_dia >= int_dia_i){
                                            Log.d("MyActivity", "Entro 4");
                                            sample.setDia(transformar2_dia + "/" + transformar2_mes + "/" + transformar2_ano);
                                            sardanaSamples.add(sample);
                                        }
                                    }else{
                                        Log.d("MyActivity", "Entro 5");
                                        sample.setDia(transformar2_dia + "/" + transformar2_mes + "/" + transformar2_ano);
                                        sardanaSamples.add(sample);
                                    }
                                }
                            }else{
                                Log.d("MyActivity", "Entro 6");
                                sample.setDia(transformar2_dia + "/" + transformar2_mes + "/" + transformar2_ano);
                                sardanaSamples.add(sample);
                            }
                        }
                    }
                }

                //Log.d("MyActivity", "Justcreated: " + sample);

            }
        } catch (IOException e){
            Log.wtf("MyActivity", "Error reading the data file" + line, e);
            e.printStackTrace();
        }
        for(int i=0;i<sardanaSamples.size();i++){
            Log.d("MyActivity", "encontrado " + sardanaSamples.get(i));
        }
    }

    public void init(){
        adapter = new AdapterCalendario(sardanaSamples, this, new AdapterCalendario.OnItemClickListener() {
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
        Intent intent = new Intent(this,Info.class);
        intent.putExtra("Model",item);
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
}