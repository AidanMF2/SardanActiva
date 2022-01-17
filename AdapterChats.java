package com.example.ushare.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ushare.Chats_DDBB;
import com.example.ushare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterChats extends RecyclerView.Adapter<AdapterChats.viewHolderAdapter> {

    List<Chats_DDBB> chatList;
    Context context;
    public static final int mensaje_left = 0;
    public static final int mensaje_right = 1;
    Boolean soloright = false;
    FirebaseUser fuser;


    public AdapterChats(List<Chats_DDBB> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {
        TextView tv_mensaje, tv_fecha;
        ImageView img_entregado, img_visto;


        public viewHolderAdapter(@NonNull View itemView) {
            super(itemView);
            tv_mensaje = itemView.findViewById(R.id.tv_mensaje);
            tv_fecha = itemView.findViewById(R.id.tv_fecha);
            img_entregado = itemView.findViewById(R.id.img_entregado);
            img_visto = itemView.findViewById(R.id.img_visto);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if(chatList.get(position).getEnvia().equals(fuser.getUid())){
            soloright = true;
            return mensaje_right;
        } else{
            soloright = false;
            return mensaje_left;
        }
    }

    @NonNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == mensaje_right){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new AdapterChats.viewHolderAdapter(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new AdapterChats.viewHolderAdapter(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderAdapter holder, int position) {
        Chats_DDBB chats = chatList.get(position);

        holder.tv_mensaje.setText(chats.getMensaje());


        if(soloright){

            if(chats.getVisto().equals("si")){
                holder.img_entregado.setVisibility(View.GONE);
                holder.img_visto.setVisibility(View.VISIBLE);
            }else{
                holder.img_entregado.setVisibility(View.VISIBLE);
                holder.img_visto.setVisibility(View.GONE);
            }
            final Calendar c = Calendar.getInstance();
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            if(chats.getFecha().equals(dateFormat.format(c.getTime()))){
                holder.tv_fecha.setText("Avui " + chats.getHora());
            }else{
                holder.tv_fecha.setText(chats.getFecha() + " " + chats.getHora());
            }

        }
    }


}
