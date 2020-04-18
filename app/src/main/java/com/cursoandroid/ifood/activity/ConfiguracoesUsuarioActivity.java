package com.cursoandroid.ifood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cursoandroid.ifood.R;
import com.cursoandroid.ifood.helper.ConfiguracaoFirebase;
import com.cursoandroid.ifood.helper.UsuarioFirebase;
import com.cursoandroid.ifood.model.Empresa;
import com.cursoandroid.ifood.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    private EditText editUsuarioNome, editUsuarioEndereco;
    private String idUsuario;
    private DatabaseReference firebaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        //Configurações iniciais
        inicializarComponentes();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuario = UsuarioFirebase.getIdUsuario();

        //Configurações Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*Recuperar dados da empresa*/
        recuperarDadosUsuario();


    }

    private void recuperarDadosUsuario(){

        DatabaseReference empresaRef = firebaseRef
                .child("usuarios")
                .child( idUsuario );
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if( dataSnapshot.getValue() != null ) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    editUsuarioNome.setText(usuario.getNome());
                    editUsuarioEndereco.setText(usuario.getEndereco());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void validarDadosUsuario(View view) {

        //Valida se os campos foram preenchidos
        String nome = editUsuarioNome.getText().toString();
        String endereco = editUsuarioEndereco.getText().toString();


        if (!nome.isEmpty()) {
            if (!endereco.isEmpty()) {


                Usuario usuario = new Usuario();
                usuario.setIdUsuario(idUsuario);
                usuario.setNome( nome );
                usuario.setEndereco( endereco );
                usuario.salvar();
                finish();

            } else {
                exibirMensagem("Digite o nome do usuário");
            }
        } else {
            exibirMensagem("Digite o seu endereço");
        }
    }





    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }



    private void inicializarComponentes(){
        editUsuarioNome = findViewById(R.id.editUsuarioNome);
        editUsuarioEndereco = findViewById(R.id.editUsuarioEndereco);
    }

}
