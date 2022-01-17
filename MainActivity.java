package com.example.ushare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ushare.tutorial.Tutorial1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private EditText email_et;
    private EditText contra_et;
    private String usuario, contraseña;
    private Button iniciar, tutorial;

    private FirebaseAuth autentificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Intent menu = new Intent(MainActivity.this, menu_principal.class);
        startActivity(menu);
        finish();*/

        email_et = (EditText) findViewById(R.id.Usuario);
        contra_et = (EditText) findViewById(R.id.contraseña_id);
        iniciar = findViewById(R.id.iniciar_boton);
        tutorial = findViewById(R.id.tutorial_boton);


        SharedPreferences usuario_shared = getSharedPreferences("datos", Context.MODE_PRIVATE);
        email_et.setText(usuario_shared.getString("usuario",""));



        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario = email_et.getText().toString();
                contraseña = contra_et.getText().toString();

                if(!usuario.isEmpty() && !contraseña.isEmpty()){
                    iniciar_sesion();
                }else{
                    Toast.makeText(MainActivity.this,"Has de completar els camps",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void Registrar (View view){
        Intent registrar = new Intent(this, Registrar.class);
        startActivity(registrar);
        finish();
    }
    public void Tutorial (View view){
        Intent tutorial = new Intent(this, Tutorial1.class);
        startActivity(tutorial);
        finish();
    }

    //Método para el botón finalizar
    public void Finalizar (View view){
        SharedPreferences nuevo_usuario_shared = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = nuevo_usuario_shared.edit();
        editor.putString("usuario",email_et.getText().toString());
        editor.commit();
        finish();
    }

    private void iniciar_sesion(){
        autentificacion = FirebaseAuth.getInstance();
        autentificacion.signInWithEmailAndPassword(usuario,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()){
                    FirebaseUser user = autentificacion.getCurrentUser();
                    Intent menu = new Intent(MainActivity.this, menu_principal.class);
                    startActivity(menu);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, "Comprovi que els camps siguin correctes", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}