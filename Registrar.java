package com.example.ushare;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.net.Uri;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Switch;
        import android.widget.Toast;

        import com.firebase.ui.auth.AuthUI;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.auth.UserProfileChangeRequest;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Arrays;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;


public class Registrar extends AppCompatActivity {

    EditText usuario, contraseña, confirma_contraseña, fecha, email, apellido;
    Button finalizar;
    Switch coche;
    ImageView foto;

    FirebaseDatabase root_node;
    DatabaseReference reference;
    private FirebaseAuth autentificacion;
    /*private FirebaseAuth.AuthStateListener ListenerAuth;
    public static final int SIGN_IN = 1;

    List<AuthUI.IdpConfig> provider = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build());*/

    String usuario_ddbb, contraseña_ddbb, confirma_contraseña_ddbb, fecha_ddbb, email_ddbb, apellido_ddbb;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        usuario = findViewById(R.id.usuario_id);
        apellido = findViewById(R.id.apellido_id);
        contraseña = findViewById(R.id.contraseña_id);
        confirma_contraseña = findViewById(R.id.confirma_contraseña_id);
        fecha = findViewById(R.id.fecha_id);
        email = findViewById(R.id.email_id);
        finalizar = findViewById(R.id.finalizar_id);



        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //root_node = FirebaseDatabase.getInstance("https://ushare-f98fc-default-rtdb.firebaseio.com/");
                root_node = FirebaseDatabase.getInstance();
                reference = root_node.getReference("users");

                usuario_ddbb = usuario.getText().toString();
                apellido_ddbb = apellido.getText().toString();
                contraseña_ddbb = contraseña.getText().toString();
                confirma_contraseña_ddbb = confirma_contraseña.getText().toString();
                fecha_ddbb = fecha.getText().toString();
                email_ddbb = email.getText().toString();


                if(!usuario_ddbb.isEmpty() && !contraseña_ddbb.isEmpty() && !confirma_contraseña_ddbb.isEmpty() && !fecha_ddbb.isEmpty()
                        && !email_ddbb.isEmpty() && !apellido_ddbb.isEmpty()){
                    boolean compr = comprobar_fecha(fecha_ddbb);
                    if(contraseña_ddbb.length() < 6){
                        Toast.makeText(Registrar.this,"La contrasenya ha de tenir 6 o més caràcters",Toast.LENGTH_LONG).show();
                    }else if(!contraseña_ddbb.equals(confirma_contraseña_ddbb)){
                        Toast.makeText(Registrar.this,"Les contrasenyes han de ser iguals",Toast.LENGTH_LONG).show();
                    }else if(!email_ddbb.contains("@") && !email_ddbb.contains(".")){
                        Toast.makeText(Registrar.this,"El correu ha de ser correcte",Toast.LENGTH_LONG).show();
                    }else if(!compr){
                        Toast.makeText(Registrar.this, "La data ha de ser dd/MM/aaaa", Toast.LENGTH_LONG).show();
                    }else{
                        registrar_usuario();
                    }
                }else {
                    Toast.makeText(Registrar.this, "Has de completar els camps", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void registrar_usuario(){
        autentificacion = FirebaseAuth.getInstance();
        autentificacion.createUserWithEmailAndPassword(email_ddbb,contraseña_ddbb).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                if(task.isSuccessful()){

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref_user = database.getReference("Users").child(user.getUid());
                    String id = user.getUid();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(usuario_ddbb)
                            .setPhotoUri(Uri.parse("https://pbs.twimg.com/media/Bryrze3IAAEj7oU.png"))
                            .build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Registrar.this, user.getDisplayName(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    //Toast.makeText(Registrar.this, id, Toast.LENGTH_LONG).show();

                    String miedad = calcular_edad(fecha_ddbb);

                    Usuarios_DDBB apoyo_DDBB = new Usuarios_DDBB(id,usuario_ddbb, contraseña_ddbb, confirma_contraseña_ddbb,
                            fecha_ddbb, email_ddbb,"desconectado","17:09",0,0,"",miedad,apellido_ddbb,"","","","","");
                    reference.child(id).setValue(apoyo_DDBB);
                    Toast.makeText(Registrar.this, "S'ha registrat correctament", Toast.LENGTH_LONG).show();
                    int zero = 0;
                    root_node.getReference().child("users").child(user.getUid()).child("Apuntados").child("contador").setValue(zero);
                    root_node.getReference().child("users").child(user.getUid()).child("coche").setValue("no");
                    root_node.getReference().child("users").child(user.getUid()).child("Instrumento").child("flabiol").setValue("no");
                    root_node.getReference().child("users").child(user.getUid()).child("Instrumento").child("tible").setValue("no");
                    root_node.getReference().child("users").child(user.getUid()).child("Instrumento").child("tenora").setValue("no");
                    root_node.getReference().child("users").child(user.getUid()).child("Instrumento").child("trompeta").setValue("no");
                    root_node.getReference().child("users").child(user.getUid()).child("Instrumento").child("trombo").setValue("no");
                    root_node.getReference().child("users").child(user.getUid()).child("Instrumento").child("fiscorn").setValue("no");
                    root_node.getReference().child("users").child(user.getUid()).child("Instrumento").child("contrabaix").setValue("no");
                    root_node.getReference().child("users").child(user.getUid()).child("Instrumento").child("ballar").setValue("no");
                    //root_node.getReference().child("users").child(user.getUid()).child("Candidato").setValue("no");
                    root_node.getReference().child("users").child(user.getUid()).child("texto").setValue("");
                    root_node.getReference().child("users").child(user.getUid()).child("imagen").setValue("");
                    Intent iniciar = new Intent(Registrar.this, MainActivity.class);
                    startActivity(iniciar);
                    finish();
                }else{
                    Toast.makeText(Registrar.this, "Aquest correu ja està registrat", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String calcular_edad(String fecha){
        Calendar c1 = Calendar.getInstance();
        int dia_actual = c1.get(Calendar.DAY_OF_MONTH);
        int mes_actual = c1.get(Calendar.MONTH) + 1;
        int ano_actual = c1.get(Calendar.YEAR);

        String data_string = fecha;
        String dia = data_string.substring(0,2);
        int dia_i = Integer.parseInt(dia);
        String mes = data_string.substring(3,5);
        int mes_i = Integer.parseInt(mes);
        String ano = data_string.substring(6,10);
        int ano_i = Integer.parseInt(ano);

        int ano_final = ano_actual - ano_i;
        if(mes_actual < mes_i)ano_final -= 1;
        else if(mes_actual==mes_i){
            if(dia_actual<dia_i)ano_final-=1;
        }
        String mi_edad = String.valueOf(ano_final);
        return mi_edad;
    }

    private boolean comprobar_fecha(String fecha_comprobar) {
        if(fecha_comprobar.length()!=10){
            //Log.d("sa","salgo aquí 1");
            return false;
        }
        String dia = fecha_comprobar.substring(0, 2);
        int dia_c = Integer.valueOf(dia);
        String barra1 = fecha_comprobar.substring(2,3);
        String mes = fecha_comprobar.substring(3, 5);
        int mes_c = Integer.valueOf(mes);
        String barra2 = fecha_comprobar.substring(5,6);
        String ano = fecha_comprobar.substring(6, 10);
        int ano_c = Integer.valueOf(ano);

        if (dia_c > 00 && dia_c < 32) {
            //Log.d("sa","entro aquí 1");
            if (barra1.equals("/")) {
                //Log.d("sa","entro aquí 2");
                if (mes_c > 00 && mes_c < 13) {
                    //Log.d("sa","entro aquí 3");
                    if (barra2.equals("/")) {
                        //Log.d("sa","entro aquí 4");
                        if (ano_c > 1900 && ano_c < 2100) {
                            //Log.d("sa","entro aquí 5");
                            SimpleDateFormat formato_fecha = new SimpleDateFormat("dd/MM/yyyy");
                            formato_fecha.setLenient(false);
                            try {
                                //Log.d("sa","entro aquí 6");
                                formato_fecha.parse(fecha_comprobar);
                                return true;
                            } catch (Exception e) {
                                //Log.d("sa","pos al final aqui");
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
        /*SimpleDateFormat formato_fecha = new SimpleDateFormat("dd/MM/yyyy");
        formato_fecha.setLenient(false);
        try{
            formato_fecha.parse(fecha_comprobar);
            return true;
        }catch (Exception e){
            return false;
        }*/

    public void Iniciar (View view){
        Intent iniciar = new Intent(this, MainActivity.class);
        startActivity(iniciar);
        finish();
    }
}