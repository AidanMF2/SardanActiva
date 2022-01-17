package com.example.ushare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Substitut extends AppCompatActivity {

    Class firstFragment = new menu_principal().getClass();
    Class secondFragment = new menu_principal().getClass();
    Class thirdFragment = new menu_principal().getClass();

    CheckBox flabiol, tible, tenora, trompeta, trombo, fiscorn, contrabaix, ballar;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_estado = database.getReference("Estado").child(user.getUid());
    DatabaseReference ref = database.getReference("users");
    DatabaseReference ref_mas = database.getReference("users").child(user.getUid());
    DatabaseReference ref_mas_mas = database.getReference("users").child(user.getUid()).child("Instrumento");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substitut);

        BottomNavigationView navigation = findViewById(R.id.Navegacion);
        navigation.setSelectedItemId(R.id.thirdFragment);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemSelectedListener);

        flabiol = findViewById(R.id.cb_flabiol);
        tible = findViewById(R.id.cb_tible);
        tenora = findViewById(R.id.cb_tenora);
        trompeta = findViewById(R.id.cb_trompeta);
        trombo = findViewById(R.id.cb_trombo);
        fiscorn = findViewById(R.id.cb_fiscorn);
        contrabaix = findViewById(R.id.cb_contrabaix);
        ballar = findViewById(R.id.cb_ballar);

        ref_mas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Instrumento").child("flabiol").getValue().equals("si")) {
                    flabiol.setChecked(true);
                }
                flabiol.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCheckboxClicked(view);
                    }
                });
                if (snapshot.child("Instrumento").child("tible").getValue().equals("si")) {
                    tible.setChecked(true);
                }
                tible.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCheckboxClicked(view);
                    }
                });
                if (snapshot.child("Instrumento").child("tenora").getValue().equals("si")) {
                    tenora.setChecked(true);
                }
                tenora.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCheckboxClicked(view);
                    }
                });
                if (snapshot.child("Instrumento").child("trompeta").getValue().equals("si")) {
                    trompeta.setChecked(true);
                }
                trompeta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCheckboxClicked(view);
                    }
                });
                if (snapshot.child("Instrumento").child("trombo").getValue().equals("si")) {
                    trombo.setChecked(true);
                }
                trombo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCheckboxClicked(view);
                    }
                });
                if (snapshot.child("Instrumento").child("fiscorn").getValue().equals("si")) {
                    fiscorn.setChecked(true);
                }
                fiscorn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCheckboxClicked(view);
                    }
                });
                if (snapshot.child("Instrumento").child("contrabaix").getValue().equals("si")) {
                    contrabaix.setChecked(true);
                }
                contrabaix.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCheckboxClicked(view);
                    }
                });
                if (snapshot.child("Instrumento").child("ballar").getValue().equals("si")) {
                    ballar.setChecked(true);
                }
                ballar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCheckboxClicked(view);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
            public void Buscar_candidat (View view) {
        Intent substitut = new Intent(this, Substitut.class);
        startActivity(substitut);

    }

    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){

            case R.id.cb_flabiol:
                if(view.getId() == R.id.cb_flabiol){
                    if(checked){
                        ref_mas_mas.child("flabiol").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("flabiol").setValue("si");
                                Toast.makeText(Substitut.this, "Has presentat candidatura per el flabiol", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else {
                        ref_mas_mas.child("flabiol").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("flabiol").setValue("no");
                                Toast.makeText(Substitut.this, "Has borrat la teva candidatura per el flabiol", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            case R.id.cb_tible:
                if(view.getId() == R.id.cb_tible) {
                    if (checked) {
                        ref_mas_mas.child("tible").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("tible").setValue("si");
                                Toast.makeText(Substitut.this, "Has presentat candidatura per el tible", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        ref_mas_mas.child("tible").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("tible").setValue("no");
                                Toast.makeText(Substitut.this, "Has borrat la teva candidatura per el tible", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            case R.id.cb_tenora:
                if(view.getId() == R.id.cb_tenora) {
                    if (checked) {
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("tenora").setValue("si");
                                Toast.makeText(Substitut.this, "Has presentat candidatura per la tenora", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("tenora").setValue("no");
                                Toast.makeText(Substitut.this, "Has borrat la teva candidatura per la tenora", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            case R.id.cb_trompeta:
                if(view.getId() == R.id.cb_trompeta) {
                    if (checked) {
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("trompeta").setValue("si");
                                Toast.makeText(Substitut.this, "Has presentat candidatura per la trompeta", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("trompeta").setValue("no");
                                Toast.makeText(Substitut.this, "Has borrat la teva candidatura per la trompeta", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            case R.id.cb_trombo:
                if(view.getId() == R.id.cb_trombo) {
                    if (checked) {
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("trombo").setValue("si");
                                Toast.makeText(Substitut.this, "Has presentat candidatura per el trombó", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("trombo").setValue("no");
                                Toast.makeText(Substitut.this, "Has borrat la teva candidatura per el trombó", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            case R.id.cb_fiscorn:
                if(view.getId() == R.id.cb_fiscorn) {
                    if (checked) {
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("fiscorn").setValue("si");
                                Toast.makeText(Substitut.this, "Has presentat candidatura per el fiscorn", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("fiscorn").setValue("no");
                                Toast.makeText(Substitut.this, "Has borrat la teva candidatura per el fiscorn", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            case R.id.cb_contrabaix:
                if(view.getId() == R.id.cb_contrabaix) {
                    if (checked) {
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("contrabaix").setValue("si");
                                Toast.makeText(Substitut.this, "Has presentat candidatura per el contrabaix", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("contrabaix").setValue("no");
                                Toast.makeText(Substitut.this, "Has borrat la teva candidatura per el contrabaix", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            case R.id.cb_ballar:
                if(view.getId() == R.id.cb_ballar) {
                    if (checked) {
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("ballar").setValue("si");
                                Toast.makeText(Substitut.this, "Has presentat candidatura per ballar", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        ref_mas.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ref_mas.child("Instrumento").child("ballar").setValue("no");
                                Toast.makeText(Substitut.this, "Has borrat la teva candidatura per ballar", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
        }
    }
    public void Buscar (View view){
        Intent buscar = new Intent(this, Buscar_candidat.class);
        startActivity(buscar);
        finish();
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
                        /*startActivity(new Intent(getApplicationContext(), Substitut.class));
                        finish();
                        overridePendingTransition(0, 0);*/
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