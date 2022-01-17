package com.example.ushare.adapters;

import android.app.Person;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ushare.Model;
import com.example.ushare.Personas_DDBB;
import com.example.ushare.R;
import com.example.ushare.Usuarios_DDBB;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdapterPersona extends RecyclerView.Adapter<AdapterPersona.ViewHolder> {
    private List<Personas_DDBB> mData;
    private LayoutInflater mInflater;
    private Context context;


    final AdapterPersona.OnItemClickListener listener;
    private List<Personas_DDBB> original_items;


    public interface OnItemClickListener{
        void onItemClick(Personas_DDBB item);
    }

    public AdapterPersona(List<Personas_DDBB> itemList, Context context, AdapterPersona.OnItemClickListener listener){
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
    public AdapterPersona.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.lista_substitutos, null);
        return new AdapterPersona.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterPersona.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<Personas_DDBB>items){mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView nombre, apellido, municipio, edat;
        String aa;

        ViewHolder(View view){
            super(view);
            icon = view.findViewById(R.id.IconImageView);
            nombre = view.findViewById(R.id.tv_nombre);
            apellido = view.findViewById(R.id.tv_apellido);
            municipio = view.findViewById(R.id.tv_municipi);
            edat = view.findViewById(R.id.tv_edat);
        }


        void bindData(final Personas_DDBB item){
            aa = item.getUri();
            //Toast.makeText("as", "entonces el valor es" + aa, Toast.LENGTH_SHORT).show();
            //icon.setImageResource(item.getFoto());
            //icon.setImageURI(Uri.parse(aa));
            //Log.d("que coño","joodeeer" + aa);
            if(!aa.isEmpty() && !aa.equals("")){
                    Picasso.get()
                            .load(aa)
                            .resize(70,70)
                            .into(icon);
            }
            else{
                icon.setImageResource(R.drawable.usuarioperfil);
            }
            
            //icon.setText(item.getUri());
            nombre.setText(item.getNombre());
            apellido.setText(item.getApellido());
            municipio.setText(item.getMunicipio());
            edat.setText(String.valueOf(item.getEdat()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });

        }

    }
    public void filter(String strSearch){
        if(strSearch.length()==0){
            mData.clear();
            mData.addAll(original_items);
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<Personas_DDBB> collect = mData.stream()
                        .filter(i -> i.getNombre().toLowerCase().contains(strSearch))
                        .collect(Collectors.toList());
                mData.clear();
                mData.addAll(collect);
            }
            else{
                mData.clear();
                for (Personas_DDBB i : original_items ){
                    if(i.getNombre().toLowerCase().contains(strSearch)){
                        mData.add(i);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}
