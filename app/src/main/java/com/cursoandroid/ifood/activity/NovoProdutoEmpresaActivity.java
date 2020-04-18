package com.cursoandroid.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cursoandroid.ifood.R;
import com.cursoandroid.ifood.helper.UsuarioFirebase;
import com.cursoandroid.ifood.model.Produto;

public class NovoProdutoEmpresaActivity extends AppCompatActivity {

    private EditText editProdutoNome, editProdutoDescricao,
            editProdutoPreco;
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);

        /*Configurações iniciais*/
        inicializarComponentes();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //Configuraçõs Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo produto");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void validarDadosProdutos(View view){

        //Valida se os campos foram preenchidos
        String nome = editProdutoNome.getText().toString();
        String descricao = editProdutoDescricao.getText().toString();
        String preco = editProdutoPreco.getText().toString();


        if( !nome.isEmpty()){
            if( !descricao.isEmpty()){
                if( !preco.isEmpty()){
                        Produto produto = new Produto();
                        produto.setIdUsuario( idUsuarioLogado );
                        produto.setNomeProduto( nome );
                        produto.setDescricaoProduto( descricao );
                        produto.setPrecoProduto( Double.parseDouble(preco) );
                        produto.salvar();

                        finish();
                        exibirMensagem("Produto salvo com sucesso! ");

                    }else{
                        exibirMensagem("Digite o preço do produto");
                    }
                }else{
                    exibirMensagem("Digite uma descrição");
                }
            }else{
                exibirMensagem("Digite uma nome");
            }
    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }

    private void inicializarComponentes(){
        editProdutoNome = findViewById(R.id.editUsuarioNome);
        editProdutoDescricao = findViewById(R.id.editUsuarioEndereco);
        editProdutoPreco = findViewById(R.id.editProdutoPreco);

    }
}
