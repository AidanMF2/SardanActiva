package com.example.ushare.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ushare.Chat;
import com.example.ushare.Mensajes;
import com.example.ushare.Model;
import com.example.ushare.Personas_DDBB;
import com.example.ushare.R;
import com.example.ushare.Solicitudes;
import com.example.ushare.Usuarios_DDBB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdapterUsuarios extends RecyclerView.Adapter<AdapterUsuarios.viewHolderAdapter> {

    List<Usuarios_DDBB> userList;
    Context context;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private List<Usuarios_DDBB> original_items;


    SharedPreferences mPref;

    AdapterUsuarios listAdapter;

    ImageView icon;



    public AdapterUsuarios(List<Usuarios_DDBB> userList, Context context) {
        this.userList = userList;
        this.context = context;

        /*original_items = new ArrayList<>();
        original_items.addAll(userList);*/
    }
    public AdapterUsuarios(List<Usuarios_DDBB> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_usuarios,parent,false);
        viewHolderAdapter holder = new viewHolderAdapter(v);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolderAdapter holder, int position) {
        Usuarios_DDBB userss = userList.get(position);
        //final Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

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


        if(userss.getId().equals(user.getUid())){
            holder.cardView.setVisibility(View.GONE);
        }else holder.cardView.setVisibility(View.VISIBLE);


        DatabaseReference ref_mis_botones = database.getReference("Solicitudes").child(user.getUid());
        ref_mis_botones.child(userss.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String estado = snapshot.child("estado").getValue(String.class);
                if(snapshot.exists()){
                    if(estado.equals("enviado")){
                        holder.send.setVisibility(View.VISIBLE);
                        holder.add.setVisibility(View.GONE);
                        holder.amigos.setVisibility(View.GONE);
                        holder.tengosolicitud.setVisibility(View.GONE);
                    }
                    else if(estado.equals("amigos")){
                        holder.send.setVisibility(View.GONE);
                        holder.add.setVisibility(View.GONE);
                        holder.amigos.setVisibility(View.VISIBLE);
                        holder.tengosolicitud.setVisibility(View.GONE);
                    }
                    else if(estado.equals("solicitud")){
                        holder.send.setVisibility(View.GONE);
                        holder.add.setVisibility(View.GONE);
                        holder.amigos.setVisibility(View.GONE);
                        holder.tengosolicitud.setVisibility(View.VISIBLE);
                    }
                    else {
                        holder.send.setVisibility(View.GONE);
                        holder.add.setVisibility(View.VISIBLE);
                        holder.amigos.setVisibility(View.GONE);
                        holder.tengosolicitud.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference A = database.getReference("Solicitudes").child(user.getUid());
                A.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Solicitudes sol = new Solicitudes("enviado","");
                        A.child(userss.getId()).setValue(sol);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference B = database.getReference("Solicitudes").child(userss.getId());
                B.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Solicitudes sol = new Solicitudes("solicitud","");
                        B.child(user.getUid()).setValue(sol);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                DatabaseReference count = database.getReference("Contador").child(userss.getId());
                count.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Integer val = snapshot.getValue(Integer.class);
                            if(val==0){
                                count.setValue(1);
                            }else count.setValue(val+1);
                        }else{
                            count.setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //vibrator.vibrate(300);

            }
        });

        holder.tengosolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String idchat = ref_mis_botones.push().getKey();

                DatabaseReference A = database.getReference("Solicitudes").child(userss.getId()).child(user.getUid());
                A.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Solicitudes sol = new Solicitudes("amigos",idchat);
                        A.setValue(sol);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                DatabaseReference B = database.getReference("Solicitudes").child(user.getUid()).child(userss.getId());
                B.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Solicitudes sol = new Solicitudes("amigos",idchat);
                        B.setValue(sol);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //vibrator.vibrate(300);
            }
        });
        holder.amigos.setOnClickListener(new View.OnClickListener() {
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


   /* public void filter(String strSearch){

        if(strSearch.length()==0){
            userList.clear();
            userList.addAll(original_items);
        }
        else{
            Log.d("ss","me sale por pantalla " + strSearch);


            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<Usuarios_DDBB> collect = userList.stream()
                        .filter(i -> i.getUsuario().toLowerCase().contains(strSearch))
                        .collect(Collectors.toList());
                userList.clear();
                userList.addAll(collect);
            }
            else{
                userList.clear();
                for (Usuarios_DDBB i : original_items ){
                    if(i.getUsuario().toLowerCase().contains(strSearch)){
                        userList.add(i);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }*/

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {
        TextView nombre, apellido, edat, municipio;
        ImageView icon;
        CardView cardView;
        Button add,send,amigos,tengosolicitud;
        public viewHolderAdapter(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tv_nombre);
            apellido = itemView.findViewById(R.id.tv_apellido);
            edat = itemView.findViewById(R.id.tv_edat);
            municipio = itemView.findViewById(R.id.tv_municipi);
            icon = itemView.findViewById(R.id.IconImageView);
            cardView = itemView.findViewById(R.id.cv);
            add = itemView.findViewById(R.id.btn_add);
            send = itemView.findViewById(R.id.btn_send);
            amigos = itemView.findViewById(R.id.btn_amigos);
            tengosolicitud = itemView.findViewById(R.id.btn_tengosolicitud);
        }
    }

}
