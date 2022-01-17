package com.example.ushare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PerfilPublico extends AppCompatActivity {

    private TextView nombre, apellido, edad, municipio, texto, actividad, cobles;

    private CheckBox coche;

    private ImageView foto;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_apuntados = database.getReference().child("users");
    DatabaseReference ref = ref_apuntados.child("users").child(user.getUid()).child("Apuntados").child("contador");
    DatabaseReference ref_check = ref_apuntados.child("users").child(user.getUid()).child("Apuntados");

    String downloadUrl;
    private FirebaseStorage storage;
    private StorageReference storageReference;/* = storage.getReference().child(user.getUid());*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_publico);

        Personas_DDBB model = (Personas_DDBB) getIntent().getSerializableExtra("Model");

        nombre = findViewById(R.id.tv_nombre);
        apellido = findViewById(R.id.tv_apellido);
        edad = findViewById(R.id.tv_edat);
        municipio = findViewById(R.id.tv_municipi);
        texto = findViewById(R.id.tv_descripcion);
        actividad = findViewById(R.id.tv_activitat);
        cobles = findViewById(R.id.tv_cobles);
        foto = findViewById(R.id.foto_usuario);
        coche = findViewById(R.id.cb_coche);
        coche.setEnabled(false);

        nombre.setText(model.getNombre());
        //nombre.setTextSize(13);
        apellido.setText(model.getApellido());
        edad.setText(model.getEdat());
        municipio.setText(model.getMunicipio());
        texto.setText(model.getTexto());
        actividad.setText(model.getActividad());
        cobles.setText(model.getCobles());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        StorageReference referencia_imagen = storageReference.child(model.id);

        try {
            referencia_imagen.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        if(!task.getResult().toString().isEmpty()){
                            downloadUrl = task.getResult().toString();
                            //Log.d("safga", "saco entonces: " + downloadUrl);
                            mostrar_imagen();
                        }

                    }
                }
            });
        } catch (Exception e) {
            //e.printStackTrace();
            foto.setImageResource(R.drawable.usuarioperfil);
        }

        if(model.getCoche().equals("si"))coche.setChecked(true);
        else coche.setChecked(false);


        /*DatabaseReference referencia_buena = database.getReference().child("users").child(model.id);

        referencia_buena.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot: snapshot.getChildren()){
                    String actividad = datasnapshot.getKey().toString();
                    Log.d("MyActivity", "se mantiene o " + actividad);
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
        });*/
    }

    public void Contacte (View view){

    }

    private void mostrar_imagen() {
        Glide.with(this)
                .load(downloadUrl)
                .override(150,150)
                .into(foto);
    }
}