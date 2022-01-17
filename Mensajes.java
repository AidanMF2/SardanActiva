package com.example.ushare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ushare.adapters.AdapterChats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Mensajes extends AppCompatActivity {
    //CircleImageView img_user;
    TextView username;
    ImageView ic_conectado, ic_desconectado;
    SharedPreferences mPref;
    EditText et_mensaje_txt;
    ImageButton btn_enviar_msj;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_estado = database.getReference("Estado").child(user.getUid());
    DatabaseReference ref_chat = database.getReference("Chats");

    //ID CHAT GLOBAL
    String id_chat_global;
    Boolean amigo_online = false;

    //RV
    RecyclerView rv_chats;
    AdapterChats adapter;
    ArrayList<Chats_DDBB> chatlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPref = getApplication().getSharedPreferences("usuario_sp",MODE_PRIVATE);

        //img_user = findViewById(R.id.img_user);
        username = findViewById(R.id.tv_user);
        ic_conectado = findViewById(R.id.icon_conectado);
        ic_desconectado = findViewById(R.id.icon_desconectado);

        String usuario = getIntent().getExtras().getString("nombre");
        //String foto = getIntent().getExtras().getString("img_user");
        String id_user = getIntent().getExtras().getString("id_user");
        id_chat_global = getIntent().getExtras().getString("id_unico");
        et_mensaje_txt = findViewById(R.id.et_txt_mensaje);
        btn_enviar_msj = findViewById(R.id.btn_enviar_msj);

        colocarenvisto();

        btn_enviar_msj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msj = et_mensaje_txt.getText().toString();

                if(!msj.isEmpty()){
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.HOUR, 1);
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    String idpush = ref_chat.push().getKey();

                    if(amigo_online){
                        Chats_DDBB chatmsj = new Chats_DDBB(idpush,user.getUid(), id_user, msj,"si",dateFormat.format(c.getTime()),timeFormat.format(c.getTime()));
                        ref_chat.child(id_chat_global).child(idpush).setValue(chatmsj);
                        Toast.makeText(Mensajes.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                        et_mensaje_txt.setText("");
                    }else{
                        Chats_DDBB chatmsj = new Chats_DDBB(idpush,user.getUid(), id_user, msj,"no",dateFormat.format(c.getTime()),timeFormat.format(c.getTime()));
                        ref_chat.child(id_chat_global).child(idpush).setValue(chatmsj);
                        Toast.makeText(Mensajes.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                        et_mensaje_txt.setText("");
                    }
                }else{
                    Toast.makeText(Mensajes.this, "El missatge es buit", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final String id_user_sp = mPref.getString("usuario_sp","");


        username.setText(usuario);
        //Glide.with(this).load(foto).into(img_user);

        final DatabaseReference ref = database.getReference("Estado").child(id_user_sp).child("chatcon");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String chatcon = snapshot.getValue(String.class);
                if(snapshot.exists()){
                    if(chatcon.equals(user.getUid())){
                        amigo_online = true;
                        ic_conectado.setVisibility(View.VISIBLE);
                        ic_desconectado.setVisibility(View.GONE);

                    }else{
                        amigo_online = false;
                        ic_conectado.setVisibility(View.GONE);
                        ic_desconectado.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //RV
        rv_chats = findViewById(R.id.rv);
        rv_chats.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rv_chats.setLayoutManager(linearLayoutManager);

        chatlist = new ArrayList<>();
        adapter = new AdapterChats(chatlist,this);
        rv_chats.setAdapter(adapter);

        Leermensajes();


    }

    private void colocarenvisto() {
        ref_chat.child(id_chat_global).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chats_DDBB chats = dataSnapshot.getValue(Chats_DDBB.class);
                    if(chats.getRecibe().equals(user.getUid())){
                        ref_chat.child(id_chat_global).child(chats.getId()).child("visto").setValue("si");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Leermensajes() {
        ref_chat.child(id_chat_global).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatlist.removeAll(chatlist);
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Chats_DDBB chat = dataSnapshot.getValue(Chats_DDBB.class);
                        chatlist.add(chat);
                        setScroll();
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setScroll() {
        rv_chats.scrollToPosition(adapter.getItemCount()-1);
    }


    private void estadousuario(String estado){
        ref_estado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String id_user_sp = mPref.getString("usuario_sp","");

                Estado est = new Estado(estado,"","",id_user_sp);
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
        c.add(Calendar.HOUR, 1);
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
}