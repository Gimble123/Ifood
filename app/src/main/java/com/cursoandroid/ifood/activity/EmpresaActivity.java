package com.cursoandroid.ifood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.cursoandroid.ifood.R;
import com.cursoandroid.ifood.adapter.AdapterProduto;
import com.cursoandroid.ifood.helper.ConfiguracaoFirebase;
import com.cursoandroid.ifood.helper.UsuarioFirebase;
import com.cursoandroid.ifood.model.Empresa;
import com.cursoandroid.ifood.model.Produto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmpresaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private Produto produto;
    private DatabaseReference firebaseRef;
    private String idUsuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        //Configurações iniciais
        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();

        //Configuraçõs Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood - empresa");
        setSupportActionBar(toolbar);
        swipe();

        //Configura recyclerview
        recyclerProdutos.setLayoutManager(new LinearLayoutManager(this));
        recyclerProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutos.setAdapter( adapterProduto );

        //Recupera produtos para empresa
        recuperarProdutos();
    }

    public void swipe() {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirProduto( viewHolder );
            }
        };

        new ItemTouchHelper( itemTouch ).attachToRecyclerView( recyclerProdutos );

    }

    public void excluirProduto(final RecyclerView.ViewHolder viewHolder) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura AlertDialog
        alertDialog.setTitle("Excluir Produto da Empresa");
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir esse produto?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                produto = produtos.get( position );

                final DatabaseReference produtosRef = firebaseRef
                        .child("produtos")
                        .child( idUsuarioLogado );

                            produtosRef.child(produto.getIdProduto()).removeValue();
                            adapterProduto.notifyItemChanged(position);

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EmpresaActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                adapterProduto.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();


    }

    private void recuperarProdutos() {

        final DatabaseReference produtosRef = firebaseRef
                .child("produtos")
                .child( idUsuarioLogado );

        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtos.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    produtos.add( ds.getValue(Produto.class) );

                }

                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void inicializarComponentes() {
        recyclerProdutos = findViewById(R.id.recyclerEmpresa);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empresa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuSair :
                deslogarUsuario();
                break;
            case R.id.menuConfiguracoes :
                abrirConfiguracoes();
                break;
            case R.id.menuNovoProduto :
                abrirNovoProduto();
                break;
            case R.id.menuPedidos :
                abrirPedidos();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirNovoProduto() {
        Intent intent = new Intent(EmpresaActivity.this, NovoProdutoEmpresaActivity.class);
        startActivity(intent);
    }

    private void abrirConfiguracoes() {
        Intent intent = new Intent(EmpresaActivity.this, ConfiguracoesEmpresaActivity.class);
        startActivity(intent);
    }

    private void abrirPedidos() {
        Intent intent = new Intent(EmpresaActivity.this, PedidosActivity.class);
        startActivity(intent);
    }
}
