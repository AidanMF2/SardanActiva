package com.example.ushare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Filtrar extends AppCompatActivity {

    final private Calendar myCalendar = Calendar.getInstance();
    final private Calendar myCalendar2 = Calendar.getInstance();


    private EditText et_inicio, et_fin;
    private TextView tv_en, tv_i;
    private Button boton_inicio, boton_fin, boton_cerca;
    private int dia1,mes1,ano1, dia_actual,mes_actual, ano_actual, dia_mañana, mes_mañana, ano_mañana;
    private int dia_inicio, mes_inicio, ano_inicio, dia_fin, mes_fin, ano_fin;
    private boolean fecha_anterior;
    private CheckBox cb_aplecs, cb_ballades, cb_concerts, cb_cursets, cb_altres, cb_concursos;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_apuntados = database.getReference();
    DatabaseReference ref = ref_apuntados.child("users").child(user.getUid()).child("Apuntados").child("contador");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar);

        BottomNavigationView navigation = findViewById(R.id.Navegacion);
        navigation.setSelectedItemId(R.id.firstFragment);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemSelectedListener);

        et_inicio = findViewById(R.id.calendar_inicio);
        et_fin = findViewById(R.id.calendar_final);
        tv_en = findViewById(R.id.tv_entre);
        tv_i = findViewById(R.id.tv_i);
        boton_inicio = findViewById(R.id.boton_ini);
        boton_fin = findViewById(R.id.boton_fi);
        boton_cerca = findViewById(R.id.cerca_boton);
        cb_aplecs = findViewById(R.id.aplecs);
        cb_altres = findViewById(R.id.altres);
        cb_ballades = findViewById(R.id.ballades);
        cb_concerts = findViewById(R.id.concerts);
        cb_concursos = findViewById(R.id.concursos);
        cb_cursets = findViewById(R.id.cursets);

        final Calendar c1 = Calendar.getInstance();
        dia_actual = c1.get(Calendar.DAY_OF_MONTH);
        mes_actual = c1.get(Calendar.MONTH) + 1;
        ano_actual = c1.get(Calendar.YEAR);

        et_inicio.setText(dia_actual+"/"+mes_actual+"/"+ano_actual);


        dia_inicio = dia_actual;
        mes_inicio = mes_actual;
        ano_inicio = ano_actual;

        final Calendar c2 = Calendar.getInstance();
        c2.roll(Calendar.DATE,1);
        dia_mañana = c2.get(Calendar.DAY_OF_MONTH);
        mes_mañana = c2.get(Calendar.MONTH) + 1;
        ano_mañana = c2.get(Calendar.YEAR);

        dia_fin = dia_mañana;
        mes_fin = mes_mañana;
        ano_fin = ano_mañana;
        et_fin.setText(dia_fin+"/"+mes_fin+"/"+ano_fin);

        fecha_anterior = false;

        boton_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fecha(et_inicio,0);
            }
        });
        boton_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fecha(et_fin,1);
            }
        });

        boton_cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aa = String.valueOf(dia_inicio);
                Log.d("MyActivity", aa);
                String bb = String.valueOf(dia_fin);
                Log.d("MyActivity", bb);

                if(ano_fin < ano_inicio){
                    fecha_anterior = true;
                }else if(ano_fin == ano_inicio){
                    if(mes_fin < mes_inicio){
                        fecha_anterior = true;
                    }
                    else if(mes_fin == mes_inicio){
                        if(dia_fin < dia_inicio){
                            fecha_anterior = true;
                        }
                    }
                }

                if(fecha_anterior){
                    Toast.makeText(Filtrar.this, "Introdueix una data correcte", Toast.LENGTH_LONG).show();
                    fecha_anterior = false;
                }
                else{
                    Busqueda(view);
                }

            }

        });
    }
    public void Busqueda (View view){
        Intent iniciar = new Intent(this, Busqueda.class);
        if (cb_aplecs.isChecked()) iniciar.putExtra("Aplecs","Aplec");
        else iniciar.putExtra("Aplecs","No");
        if (cb_ballades.isChecked()) iniciar.putExtra("Ballades","Ballada");
        else iniciar.putExtra("Ballades","No");
        if (cb_concerts.isChecked()) iniciar.putExtra("Concerts","Concert");
        else iniciar.putExtra("Concerts","No");
        if (cb_concursos.isChecked()) iniciar.putExtra("Concursos","Concurs");
        else iniciar.putExtra("Concursos","No");
        if (cb_cursets.isChecked()) iniciar.putExtra("Cursets","Curset");
        else iniciar.putExtra("Cursets","No");
        if (cb_altres.isChecked()) iniciar.putExtra("Altres","Diversos (altres actes)");
        else iniciar.putExtra("Altres","No");
        if(!cb_aplecs.isChecked() && !cb_ballades.isChecked() && !cb_concerts.isChecked()
                && !cb_concursos.isChecked() && !cb_cursets.isChecked() && !cb_altres.isChecked()){
            iniciar.putExtra("Aplecs","Aplec");
            iniciar.putExtra("Ballades","Ballada");
            iniciar.putExtra("Concerts","Concert");
            iniciar.putExtra("Concursos","Concurs");
            iniciar.putExtra("Cursets","Curset");
            iniciar.putExtra("Altres","Diversos (altres actes)");
        }
        String dia_inicio1 = String.valueOf(dia_inicio);
        String mes_inicio1 = String.valueOf(mes_inicio);
        String ano_inicio1 = String.valueOf(ano_inicio);
        iniciar.putExtra("dia_inicial",dia_inicio1);
        iniciar.putExtra("mes_inicial",mes_inicio1);
        iniciar.putExtra("ano_inicial",ano_inicio1);

        String dia_fin1 = String.valueOf(dia_fin);
        String mes_fin1 = String.valueOf(mes_fin);
        String ano_fin1 = String.valueOf(ano_fin);
        iniciar.putExtra("dia_final",dia_fin1);
        iniciar.putExtra("mes_final",mes_fin1);
        iniciar.putExtra("ano_final",ano_fin1);
        startActivity(iniciar);
    }

    private void fecha(EditText et, int num){
        if(num==0){
            final Calendar c = Calendar.getInstance();
            dia1 = c.get(Calendar.DAY_OF_MONTH);
            mes1 = c.get(Calendar.MONTH);
            ano1 = c.get(Calendar.YEAR);
        }else{
            final Calendar c = Calendar.getInstance();
            c.roll(Calendar.DATE,1);
            dia1 = c.get(Calendar.DAY_OF_MONTH);
            mes1 = c.get(Calendar.MONTH);
            ano1 = c.get(Calendar.YEAR);
        }


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                if(num==0){
                    dia_inicio = i2;
                    mes_inicio = i1 + 1;
                    ano_inicio = i;
                }else{
                    dia_fin = i2;
                    mes_fin = i1 + 1;
                    ano_fin = i;
                }
                et.setText(i2 + "/" + (i1 + 1) + "/" + i);
            }

        },ano1,mes1,dia1);
        datePickerDialog.show();
    }


    public void Actes_apuntats (View view){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int contador = snapshot.getValue(Integer.class);
                if(contador == 0){
                    Toast.makeText(Filtrar.this, "No té actes apuntats", Toast.LENGTH_LONG).show();
                }else{
                    Intent actes = new Intent(Filtrar.this, Calendario.class);
                    startActivity(actes);
                    finish();
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
                    if(item.getItemId() == R.id.firstFragment){
                        /*startActivity(new Intent(getApplicationContext(),Filtrar.class));
                        finish();
                        overridePendingTransition(0,0);*/
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