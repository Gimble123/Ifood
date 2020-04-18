package com.cursoandroid.ifood.model;

import com.cursoandroid.ifood.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Usuario {

    private String nome;
    private String endereco;
    private String idUsuario;

    public Usuario() {
    }

    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios")
                .child( getIdUsuario() );
        usuariosRef.setValue(this);

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
