package com.example.bibliotecapessoal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecapessoal.adapter.AdapterLivro;
import com.example.bibliotecapessoal.model.Livro;
import com.example.bibliotecapessoal.persistencia.LivroDAO;

import java.util.ArrayList;

public class ListaLivrosActivity extends AppCompatActivity {
    RecyclerView recyclerLivro;
    View layoutVazio;
    private AdapterLivro livroAdapter;
    private LivroDAO livroDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_livros);
        // bottom = 0: o BottomNavigationView já trata a margem inferior
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        NavegacaoMenu.configurar(this, R.id.nav_livros);

        livroDAO = new LivroDAO(this);

        layoutVazio = findViewById(R.id.layoutVazio);
        recyclerLivro = findViewById(R.id.rvListaLivro);
        recyclerLivro.setLayoutManager(new LinearLayoutManager(this));
        livroAdapter = new AdapterLivro(new ArrayList<>());
        recyclerLivro.setAdapter(livroAdapter);

        recyclerLivro.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerLivro, new RecyclerItemClickListener.OnItemClickListener() {
            // Clique simples: abre o cadastro para editar o livro
            @Override
            public void onItemClick(View view, int position) {
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }
                Livro livroSelecionado = livroAdapter.getLivro(position);
                Intent intent = new Intent(ListaLivrosActivity.this, CadastroLivroActivity.class);
                intent.putExtra("livroSelecionado", livroSelecionado);
                NavegacaoMenu.irPara(ListaLivrosActivity.this, intent);
            }

            // Clique longo: pede confirmação e exclui o livro
            @Override
            public void onLongItemClick(View view, int position) {
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }
                Livro livroSelecionado = livroAdapter.getLivro(position);
                confirmarExclusao(livroSelecionado);
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLivros();
    }

    private void carregarLivros() {
        livroAdapter.atualizarLista(livroDAO.listarTodos());
        // Mostra a mensagem de lista vazia quando não há livros
        layoutVazio.setVisibility(livroAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void confirmarExclusao(final Livro livroSelecionado) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_excluir_titulo))
                .setMessage(getString(R.string.dialog_excluir_mensagem, livroSelecionado.getTitulo()))
                .setPositiveButton(getString(R.string.dialog_sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (livroDAO.excluir(livroSelecionado)) {
                            Toast.makeText(ListaLivrosActivity.this, getString(R.string.msg_excluido_sucesso), Toast.LENGTH_SHORT).show();
                            carregarLivros();
                        } else {
                            Toast.makeText(ListaLivrosActivity.this, getString(R.string.msg_excluido_erro), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.dialog_nao), null)
                .show();
    }
}
