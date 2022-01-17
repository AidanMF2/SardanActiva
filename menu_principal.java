package com.example.ushare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.example.ushare.tutorial.Splash;
import com.example.ushare.tutorial.Tutorial1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class menu_principal extends AppCompatActivity {
    private String url;
    SharedPreferences sharedPreferences;
    Boolean firstTimex, encontrado;
    Integer entero;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_apuntados = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        firstTimex = sharedPreferences.getBoolean("firstTimex",true);

        //ShowPopupWindow();
        encontrado = false;
        if(firstTimex){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            firstTimex = false;
            editor.putBoolean("firstTimex",firstTimex);
            editor.apply();
            ShowPopupWindow();
        }


        DatabaseReference ref = ref_apuntados.child("users").child(user.getUid()).child("Apuntados").child("contador");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                entero = Integer.parseInt(snapshot.getValue().toString());
                encontrado = true;
                Log.d("dentro", "porque peta  " + entero);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void ShowPopupWindow (){
        final Dialog dialog = new Dialog(menu_principal.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.popup);
        Button ir = dialog.findViewById(R.id.bt_ir);
        ir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(menu_principal.this, Perfil.class);
                startActivity(i);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void Sardanistes (View view){
        Log.d("dentro", "pooorno  ");
        if(encontrado){
            if(!entero.equals(null)){
                if(entero >= 1){
                    Log.d("dentro", "asieres  ");
                    Intent sardanista = new Intent(this, Sardanistas.class);
                    startActivity(sardanista);
                }else{
                    Toast.makeText(getApplicationContext(),"Has d'apuntarte a algún acte abans de veure qui va! ", Toast.LENGTH_LONG).show();
                }
            }else Toast.makeText(getApplicationContext(),"Torna a probar!", Toast.LENGTH_SHORT).show();

        }

    }
    public void Chat (View view){
        Intent chat = new Intent(this, Chat.class);
        startActivity(chat);
    }
    public void Filtrar (View view){
        Intent filtrar = new Intent(this, Filtrar.class);
        startActivity(filtrar);
    }
    public void Perfil (View view){
        Intent perfil = new Intent(this, Perfil.class);
        startActivity(perfil);
    }
    public void Substitut (View view){
        Intent substitut = new Intent(this, Substitut.class);
        startActivity(substitut);
    }
    public void onBackPressed(){
        finish();
        super.onBackPressed();
    }
}