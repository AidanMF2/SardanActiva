package com.example.ushare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ushare.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;


public class Perfil extends AppCompatActivity {

    TextView nombre, data_naix, edat, tv_activitat, tv_cobles, tv_municipi, apellido;
    EditText descripcion, municipi, activitat, cobles;
    ImageView foto;
    CheckBox coche;
    Button guardar;
    public Uri imageurl;
    String downloadUrl;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_estado = database.getReference("Estado").child(user.getUid());
    DatabaseReference ref = database.getReference("users");
    DatabaseReference ref_mas = database.getReference("users").child(user.getUid());
    DatabaseReference ref_mas_mas = database.getReference("users").child(user.getUid()).child("Instrumento");
    private FirebaseStorage storage;
    private StorageReference storageReference;/* = storage.getReference().child(user.getUid());*/


    ActivityMainBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        BottomNavigationView navigation = findViewById(R.id.Navegacion);
        navigation.setSelectedItemId(R.id.fifthFragment);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemSelectedListener);

        nombre = findViewById(R.id.tv_nombre);
        apellido = findViewById(R.id.tv_apellido);
        data_naix = findViewById(R.id.tv_data_naix);
        edat = findViewById(R.id.tv_edad);
        tv_municipi = findViewById(R.id.tv_municipi);
        tv_activitat = findViewById(R.id.tv_activitat);
        tv_cobles = findViewById(R.id.tv_cobles);

        municipi = findViewById(R.id.et_municipi);
        descripcion = findViewById(R.id.et_descripcion);
        activitat = findViewById(R.id.et_activitat);
        cobles = findViewById(R.id.et_cobles);

        foto = findViewById(R.id.foto_usuario);

        coche = findViewById(R.id.cb_coche);

        guardar = findViewById(R.id.bt_guardar);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        StorageReference referencia_imagen = storageReference.child(user.getUid());

        try {
            referencia_imagen.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        if(!task.getResult().toString().isEmpty()){
                            downloadUrl = task.getResult().toString();
                            Log.d("safga", "saco entonces: " + downloadUrl);
                            mostrar_imagen();

                        }

                    }
                }
            });
        } catch (Exception e) {
            //e.printStackTrace();
            foto.setImageResource(R.drawable.usuarioperfil);
        }

        /*Glide.with(this)
                .load(a_ver)
                .override(70,70)
                .into(foto);*/

        ref_mas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nombre.setText(snapshot.child("usuario").getValue().toString());
                apellido.setText(snapshot.child("apellido").getValue().toString());
                data_naix.setText(snapshot.child("fecha").getValue().toString());
                descripcion.setText(snapshot.child("texto").getValue().toString());
                municipi.setText(snapshot.child("municipio").getValue().toString());
                activitat.setText(snapshot.child("activitats").getValue().toString());
                cobles.setText(snapshot.child("cobles").getValue().toString());

                if(snapshot.child("coche").getValue().equals("si")){
                    coche.setChecked(true);
                }

                coche.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!snapshot.child("texto").getValue().toString().equals(descripcion.getText().toString()) || !snapshot.child("municipio").getValue().toString().equals(municipi.getText().toString()) ||
                                        !snapshot.child("activitats").getValue().toString().equals(activitat.getText().toString()) || !snapshot.child("cobles").getValue().toString().equals(cobles.getText().toString())){
                            Toast.makeText(Perfil.this, "Guarda abans", Toast.LENGTH_SHORT).show();
                            boolean prueba = !coche.isChecked();
                            coche.setChecked(prueba);
                        }else onCheckboxClicked(view);
                    }
                });

                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mensaje = descripcion.getText().toString();
                        ref_mas.child("texto").setValue(mensaje);
                        String mensaje_municipio = municipi.getText().toString();
                        ref_mas.child("municipio").setValue(mensaje_municipio);
                        String mensaje_activitats = activitat.getText().toString();
                        ref_mas.child("activitats").setValue(mensaje_activitats);
                        String mensaje_cobles = cobles.getText().toString();
                        ref_mas.child("cobles").setValue(mensaje_cobles);
                        Toast.makeText(Perfil.this, "Guardat Correctament", Toast.LENGTH_LONG).show();

                    }
                });



                foto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        seleccionar_foto();
                    }
                });
                Calendar c1 = Calendar.getInstance();
                int dia_actual = c1.get(Calendar.DAY_OF_MONTH);
                int mes_actual = c1.get(Calendar.MONTH) + 1;
                int ano_actual = c1.get(Calendar.YEAR);

                String data_string = snapshot.child("fecha").getValue().toString();
                String dia = data_string.substring(0,2);
                int dia_i = Integer.parseInt(dia);
                String mes = data_string.substring(3,5);
                int mes_i = Integer.parseInt(mes);
                String ano = data_string.substring(6,10);
                int ano_i = Integer.parseInt(ano);

                /*Log.d("MyActivity", "dia: " + dia_i);
                Log.d("MyActivity", "mes: " + mes_i);
                Log.d("MyActivity", "ano: " + ano_i);

                Log.d("MyActivity", "hoy_dia: " + dia_actual);
                Log.d("MyActivity", "hoy_mes: " + mes_actual);
                Log.d("MyActivity", "hoy_ano: " + ano_actual);*/

                int ano_final = ano_actual - ano_i;
                if(mes_actual < mes_i)ano_final -= 1;
                else if(mes_actual==mes_i){
                    if(dia_actual<dia_i)ano_final-=1;
                }
                String mi_edad = String.valueOf(ano_final);
                edat.setText(mi_edad);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void mostrar_imagen() {
        Glide.with(this)
                .load(downloadUrl)
                .override(70,70)
                .into(foto);
    }

    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.cb_coche:
                if(view.getId() == R.id.cb_coche){
                    if(checked){
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("coche").setValue("si");
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }else{
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("coche").setValue("no");
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
        }
    }

    public void onBackPressed(){
        Intent iniciar = new Intent(this, menu_principal.class);
        iniciar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(iniciar);
        finish();
        super.onBackPressed();
    }

    private void seleccionar_foto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageurl = data.getData();

            foto.setImageURI(imageurl);
            cargar_imagen();

            StorageReference referencia_imagen = storageReference.child(user.getUid());

            try {
                referencia_imagen.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            if(!task.getResult().toString().isEmpty()){
                                downloadUrl = task.getResult().toString();
                                database.getReference().child("users").child(user.getUid()).child("imagen").setValue(downloadUrl);
                                Log.d("safga", "saco entonces: " + downloadUrl);
                                mostrar_imagen();
                            }

                        }
                    }
                });
            } catch (Exception e) {
                //e.printStackTrace();
                foto.setImageResource(R.drawable.usuarioperfil);
            }
        }

    }

    private void cargar_imagen() {
        StorageReference referencia_imagen = storageReference.child(user.getUid());

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Carregant imatge...");
        pd.show();

        referencia_imagen.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Snackbar.make(findViewById(android.R.id.content), "Imatge Carregada.", Snackbar.LENGTH_LONG).show();
                try {
                    referencia_imagen.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                if(!task.getResult().toString().isEmpty()){
                                    downloadUrl = task.getResult().toString();
                                    Log.d("safga", "saco entonces: " + downloadUrl);
                                    mostrar_imagen();

                                }

                            }
                        }
                    });
                } catch (Exception e) {
                    //e.printStackTrace();
                    foto.setImageResource(R.drawable.usuarioperfil);
                }
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progés: " + (int) progressPercent + "%");
                        if (progressPercent == 100.00) pd.dismiss();
                    }
                });

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
                        /*startActivity(new Intent(getApplicationContext(), Perfil.class));
                        finish();
                        overridePendingTransition(0, 0);*/
                    }
            }
        }
    };
}
