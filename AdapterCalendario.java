package com.example.ushare.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Build;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ushare.Mensajes;
import com.example.ushare.Model;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdapterCalendario extends RecyclerView.Adapter<AdapterCalendario.ViewHolder> {
    private List<Model> mData;
    private LayoutInflater mInflater;
    private Context context;
    final AdapterCalendario.OnItemClickListener listener;
    private List<Model> original_items;

    public interface OnItemClickListener{
        void onItemClick(Model item);
    }

    public AdapterCalendario(List<Model> itemList, Context context, AdapterCalendario.OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
        original_items = new ArrayList<>();
        original_items.addAll(itemList);
    }

    @Override
    public int getItemCount(){return mData.size();}

    @Override
    public AdapterCalendario.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.calendar, null);
        return new AdapterCalendario.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterCalendario.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<Model>items){mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView color;
        TextView municipi, activitat, data, hores, tipus_activitat, suspendido;
        String elegido;

        ViewHolder(View view){
            super(view);
            color = view.findViewById(R.id.color_tipo);
            municipi = view.findViewById(R.id.tv_municipi);
            activitat = view.findViewById(R.id.tv_nomactivitat);
            data = view.findViewById(R.id.tv_data);
            hores = view.findViewById(R.id.tv_hores);

            tipus_activitat = view.findViewById(R.id.tv_tipus_activitat);
            suspendido = view.findViewById(R.id.tv_suspendido);

        }


        void bindData(final Model item){

            activitat.setText(item.getNomactivitat());
            data.setText(item.getDia());
            hores.setText(item.getHores());
            tipus_activitat.setText(item.getTipus());
            suspendido.setText(item.getAnul());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });

            String transformar = item.getMunicipi();
            StringBuilder transformar2 = new StringBuilder(transformar);
            transformar2.deleteCharAt(0);
            transformar2.deleteCharAt(transformar2.length() -1);
            transformar = transformar2.toString();
            municipi.setText(transformar);

            if(item.getTipus().equals("Ballada")){
                elegido = "#1B2BA5";
            }else if(item.getTipus().equals("Aplec")){
                elegido = "#1A9726";
            }else if(item.getTipus().equals("Concert")){
                elegido = "#822F72";
            }else if(item.getTipus().equals("Curset")){
                elegido = "#47FED3";
            }else if(item.getTipus().equals("Concurs")){
                elegido = "#FDAB8A";
            }
            else if(item.getTipus().equals("Diversos (altres actes)")){
                elegido = "#9491BE";
            }
            color.setColorFilter(Color.parseColor(elegido), PorterDuff.Mode.SRC_IN);


        }

    }
    public void filter(String strSearch){
        if(strSearch.length()==0){
            mData.clear();
            mData.addAll(original_items);
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<Model> collect = mData.stream()
                        .filter(i -> i.getNomactivitat().toLowerCase().contains(strSearch))
                        .collect(Collectors.toList());
                mData.clear();
                mData.addAll(collect);
            }
            else{
                mData.clear();
                for (Model i : original_items ){
                    if(i.getNomactivitat().toLowerCase().contains(strSearch)){
                        mData.add(i);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}



