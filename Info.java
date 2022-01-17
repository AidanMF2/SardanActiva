package com.example.ushare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.UiSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Info extends AppCompatActivity{

    private TextView nomactivitat, municipi, data2, tipus_activitat, descripcion, suspendido;
    private String lat, lon;
    private Double lat_d, lon_d;
    private CheckBox asistir;
    private int contador_hijos;
    /*private MapView mapa;
    private GoogleMap mMap;
    private UiSettings mUiSettings;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";*/

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_apuntados = database.getReference();
    DatabaseReference ref = ref_apuntados.child("users").child(user.getUid()).child("Apuntados").child("contador");
    DatabaseReference ref_check = ref_apuntados.child("users").child(user.getUid()).child("Apuntados");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Model model = (Model) getIntent().getSerializableExtra("Model");

        nomactivitat = findViewById(R.id.tv_nomactivitat);
        municipi = findViewById(R.id.tv_municipi);
        data2 = findViewById(R.id.tv_data2);
        tipus_activitat = findViewById(R.id.tv_tipus_activitat);
        descripcion = findViewById(R.id.tv_descripcion);
        suspendido = findViewById(R.id.tv_suspendido);
        asistir = findViewById(R.id.cb_asisto);

        ref_check.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot: snapshot.getChildren()){
                    String actividad = datasnapshot.getKey().toString();
                    if(model.getId().toString().equals(actividad)){
                        asistir.setChecked(true);
                        Log.d("MyActivity", "se mantiene o " + actividad);
                        asistir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onCheckboxClicked(view, model);
                            }
                        });


                        nomactivitat.setText(model.getNomactivitat());
                        municipi.setText(model.getMunicipi());
                        data2.setText(model.getDia());
                        tipus_activitat.setText(model.getTipus());
                        descripcion.setText(model.getMesdades());
                        suspendido.setText(model.getAnul());

                        if(model.getLatitud() != null){
                            lat_d = model.getLatitud();
                            lon_d = model.getLongitud();
                        }else{
                            lat_d = model.getLatitudaprox();
                            lon_d = model.getLongitudaprox();
                        }
                    }
                    else {
                        asistir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onCheckboxClicked(view, model);
                            }
                        });


                        nomactivitat.setText(model.getNomactivitat());
                        municipi.setText(model.getMunicipi());
                        data2.setText(model.getDia());
                        tipus_activitat.setText(model.getTipus());
                        descripcion.setText(model.getMesdades());
                        suspendido.setText(model.getAnul());

                        if(model.getLatitud() != null){
                            lat_d = model.getLatitud();
                            lon_d = model.getLongitud();
                        }else{
                            lat_d = model.getLatitudaprox();
                            lon_d = model.getLongitudaprox();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        if(mapFragment !=null)mapFragment.getMapAsync(this);*/
        //mapa.getMapAsync(this);

        /*mapa = findViewById(R.id.mapView);

        initGoogleMap(savedInstanceState);*/

    }

    public void moveToDescription(Model item) {
        Intent intent = new Intent(this,Info.class);
        intent.putExtra("Model",item);
        startActivity(intent);
    }
    public void Mapa (View view){
        Intent map = new Intent(this, MapsActivity.class);
        lat = Double.toString(lat_d);
        map.putExtra("lat",lat);
        lon = Double.toString(lon_d);
        map.putExtra("lon",lon);
        startActivity(map);
    }

    public void onCheckboxClicked(View view,Model model){
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()){
            case R.id.cb_asisto:
                if(checked){
                    String borrar = model.getId().toString();
                    Log.d("MyActivity", "Marcado");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int contador = snapshot.getValue(Integer.class);
                            contador +=1;
                            ref.setValue(contador);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    ref_apuntados.child("users").child(user.getUid()).child("Apuntados").child(borrar).setValue("asiste");

                }else{
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int contador = snapshot.getValue(Integer.class);
                            contador -=1;
                            ref.setValue(contador);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    String borrar = model.getId().toString();
                    ref_apuntados.child("users").child(user.getUid()).child("Apuntados").child(borrar).removeValue();
                }
        }
    }

}