package com.example.ushare.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ushare.Mensajes;
import com.example.ushare.R;
import com.example.ushare.Usuarios_DDBB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterChatLista extends RecyclerView.Adapter<AdapterChatLista.viewHolderAdapterchatlist> {

    List<Usuarios_DDBB> userList;
    Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    SharedPreferences mPref;

    public AdapterChatLista(List<Usuarios_DDBB> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderAdapterchatlist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chatlist,parent,false);
        viewHolderAdapterchatlist holder = new viewHolderAdapterchatlist(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderAdapterchatlist holder, int position) {
        Usuarios_DDBB userss = userList.get(position);
        final Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        holder.nombre.setText(userss.getUsuario());
        holder.apellido.setText(userss.getApellido());
        holder.edat.setText(userss.getEdat());
        holder.municipio.setText(userss.getMunicipio());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("users").child(userss.getId()).child("imagen");

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String aa = snapshot.getValue().toString();
                Log.d("Sale", "Https: " + aa);
                if(!aa.isEmpty() && !aa.equals("")){
                    Picasso.get()
                            .load(aa)
                            .resize(70,70)
                            .into(holder.icon);
                }
                else{
                    holder.icon.setImageResource(R.drawable.usuarioperfil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        DatabaseReference ref_mis_solicitudes = database.getReference("Solicitudes").child(user.getUid());
        ref_mis_solicitudes.child(userss.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                if(snapshot.exists()){
                    if(estado.equals("amigos")){
                        holder.cardView.setVisibility(View.VISIBLE);
                    }else holder.cardView.setVisibility(View.GONE);
                }else holder.cardView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Calendar c = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        DatabaseReference ref_Estado = database.getReference("Estado").child(userss.getId());
        ref_Estado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                String fecha = snapshot.child("fecha").getValue(String.class);
                String hora = snapshot.child("hora").getValue(String.class);
                if(snapshot.exists()){
                    if(estado.equals("conectado")){
                        holder.icon_conectado.setVisibility(View.VISIBLE);
                        holder.icon_desconectado.setVisibility(View.GONE);
                    }else{
                        holder.icon_conectado.setVisibility(View.GONE);
                        holder.icon_desconectado.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPref = view.getContext().getSharedPreferences("usuario_sp",Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = mPref.edit();

                DatabaseReference ref = database.getReference("Solicitudes").child(user.getUid()).child(userss.getId()).child("idchat");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String id = snapshot.getValue(String.class);
                        if(snapshot.exists()){
                            Intent intent = new Intent(view.getContext(), Mensajes.class);
                            intent.putExtra("nombre",userss.getUsuario());
                            //intent.putExtra("img_user",userss.get());
                            intent.putExtra("id_user",userss.getId());
                            intent.putExtra("id_unico",id);

                            editor.putString("usuario_sp",userss.getId());
                            editor.apply();

                            view.getContext().startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }




    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class viewHolderAdapterchatlist extends RecyclerView.ViewHolder {
        TextView nombre, apellido,municipio,edat;
        ImageView icon;
        CardView cardView;
        TextView tv_conectado, tv_desconectado;
        ImageView icon_conectado, icon_desconectado;

        public viewHolderAdapterchatlist(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tv_nombre);
            apellido = itemView.findViewById(R.id.tv_apellido);
            municipio = itemView.findViewById(R.id.tv_municipi);
            edat = itemView.findViewById(R.id.tv_edat);
            cardView = itemView.findViewById(R.id.cv);
            icon = itemView.findViewById(R.id.IconImageView);
            icon_conectado = itemView.findViewById(R.id.icon_conectado);
            icon_desconectado = itemView.findViewById(R.id.icon_desconectado);

        }
    }
}
