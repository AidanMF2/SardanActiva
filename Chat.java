package com.example.ushare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.ushare.adapters.AdapterPersona;
import com.example.ushare.adapters.AdapterUsuarios;
import com.example.ushare.adapters.PaginasAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.List;

public class Chat extends AppCompatActivity/* implements SearchView.OnQueryTextListener*/{

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_solicitud_count = database.getReference("Contador").child(user.getUid());
    DatabaseReference ref_estado = database.getReference("Estado").child(user.getUid());
    DatabaseReference ref_users = database.getReference("users");

    SearchView sv_search;
    AdapterUsuarios adapter;
    ArrayList<Usuarios_DDBB> list;
    RecyclerView rv;
    LinearLayoutManager lm;


    String busqueda="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        BottomNavigationView navigation = findViewById(R.id.Navegacion);
        navigation.setSelectedItemId(R.id.fourthFragment);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemSelectedListener);


        rv = findViewById(R.id.rv_sardanas);
        sv_search = findViewById(R.id.searchview);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        list = new ArrayList<>();
        adapter = new AdapterUsuarios(list);
        rv.setAdapter(adapter);

        ref_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot datasnapshot : snapshot.getChildren()){
                        Usuarios_DDBB ma = datasnapshot.getValue(Usuarios_DDBB.class);
                        list.add(ma);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscar(newText);
                return true;
            }
        });

        //initListener();

        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new PaginasAdapter(this));


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0 : {
                        tab.setText("Usuaris");
                        tab.setIcon(R.drawable.usuarios);
                        break;
                    }
                    case 1 : {
                        tab.setText("Xats");
                        tab.setIcon(R.drawable.chats);
                        break;
                    }
                    case 2 : {
                        tab.setText("Solicituts");
                        tab.setIcon(R.drawable.solicitudes);
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        //badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
                        badgeDrawable.setVisible(true);

                        ref_solicitud_count.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    Integer val = snapshot.getValue(Integer.class);
                                    badgeDrawable.setVisible(true);
                                    if(val.equals("0")){
                                        badgeDrawable.setVisible(false);
                                    }else badgeDrawable.setNumber(val);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;
                    }
                    case 3 : {
                        tab.setText("Meves solicituts");
                        tab.setIcon(R.drawable.missolicitudes);
                        break;
                    }

                }
            }
        });
        tabLayoutMediator.attach();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BadgeDrawable badgeDrawable = tabLayout.getTabAt(position).getOrCreateBadge();
                //badgeDrawable.setVisible(false);

                if(position==1 || position==2 || position==3){
                    rv.setVisibility(View.GONE);
                    sv_search.setVisibility(View.GONE);
                    viewPager2.setVisibility(View.VISIBLE);
                    //viewPager2 = ViewPager2.LayoutParams.WRAP_CONTENT;
                    //viewPager2.setLayoutParams(1) ;
                }else{
                    viewPager2.setVisibility(View.GONE);
                    sv_search.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.VISIBLE);
                }
                if(position==2){
                    countacero();
                }


            }
        });

    }

    private void buscar(String s) {
        ArrayList<Usuarios_DDBB>milista = new ArrayList<>();

        for(Usuarios_DDBB obj : list){
            if(obj.getUsuario().toLowerCase().contains(s.toLowerCase()) || obj.getApellido().toLowerCase().contains(s.toLowerCase())){
                Log.d("·sss","entonces loq ue busca es " + obj.getUsuario().toLowerCase());
                milista.add(obj);
            }
        }
        AdapterUsuarios adapterUsuarios = new AdapterUsuarios(milista);
        rv.setAdapter(adapterUsuarios);
    }


    /*@Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("ss","en el desto sale " + newText);
        busqueda = newText;
        //listAdapter = new AdapterUsuarios(newText);
        //listAdapter.filter(newText);
        return false;
    }
    private void initListener(){
        sv_search.setOnQueryTextListener(this);
    }*/

    private void estadousuario(String estado){
        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Estado est = new Estado(estado,"","","");
                ref_estado.setValue(est);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        estadousuario("conectado");
    }

    @Override
    protected void onPause() {
        super.onPause();
        estadousuario("desconectado");
        dameultimafecha();
    }

    private void dameultimafecha() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 2);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ref_estado.child("fecha").setValue(dateFormat.format(c.getTime()));
                ref_estado.child("hora").setValue(timeFormat.format(c.getTime()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void countacero() {
        ref_solicitud_count.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ref_solicitud_count.setValue(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onBackPressed(){
        Intent iniciar = new Intent(this, menu_principal.class);
        iniciar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(iniciar);
        finish();
        super.onBackPressed();
    }

    private final BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.firstFragment:
                    //Log.d("esto","enserio que le estoy dando porque no cambia=?");
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
                        /*startActivity(new Intent(getApplicationContext(), Chat.class));
                        finish();
                        overridePendingTransition(0, 0);*/
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