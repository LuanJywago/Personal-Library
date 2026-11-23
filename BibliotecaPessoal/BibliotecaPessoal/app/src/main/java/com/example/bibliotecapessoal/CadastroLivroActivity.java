package com.example.bibliotecapessoal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bibliotecapessoal.model.Livro;
import com.example.bibliotecapessoal.persistencia.LivroDAO;

import java.util.Calendar;

public class CadastroLivroActivity extends AppCompatActivity {
    TextView tvAcao;
    EditText edtTitulo, edtAutor, edtAno;
    CheckBox chkLido;
    Button btnSalvar;
    private Livro livroSelecionado;
    private LivroDAO livroDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_livro);
        // bottom = 0: o BottomNavigationView já trata a margem inferior
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
        NavegacaoMenu.configurar(this, R.id.nav_novo);

        livroDAO = new LivroDAO(this);

        tvAcao = findViewById(R.id.lblAcao);
        edtTitulo = findViewById(R.id.txtTitulo);
        edtAutor = findViewById(R.id.txtAutor);
        edtAno = findViewById(R.id.txtAno);
        chkLido = findViewById(R.id.chkLido);
        btnSalvar = findViewById(R.id.cmdSalvar);

        // Se veio um livro pela Intent, a tela está em modo de edição
        livroSelecionado = (Livro) getIntent().getSerializableExtra("livroSelecionado");
        if (livroSelecionado != null) {
            tvAcao.setText(getString(R.string.titulo_editar_livro));
            TextView tvSubtitulo = findViewById(R.id.lblSubtituloCadastro);
            tvSubtitulo.setText(getString(R.string.subtitulo_editar));
            edtTitulo.setText(livroSelecionado.getTitulo());
            edtAutor.setText(livroSelecionado.getAutor());
            edtAno.setText(String.valueOf(livroSelecionado.getAno()));
            chkLido.setChecked(livroSelecionado.isLido());
            btnSalvar.setText(getString(R.string.btn_atualizar));
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarLivro();
            }
        });
    }

    private void salvarLivro() {
        String titulo = edtTitulo.getText().toString().trim();
        String autor = edtAutor.getText().toString().trim();
        String textoAno = edtAno.getText().toString().trim();

        if (titulo.isEmpty()) {
            edtTitulo.setError(getString(R.string.erro_titulo_obrigatorio));
            edtTitulo.requestFocus();
            return;
        }
        if (autor.isEmpty()) {
            edtAutor.setError(getString(R.string.erro_autor_obrigatorio));
            edtAutor.requestFocus();
            return;
        }

        // Ano atual, sem escrever o número fixo no código
        int anoAtual = Calendar.getInstance().get(Calendar.YEAR);

        if (textoAno.isEmpty()) { // confira ANTES do parseInt
            edtAno.setError(getString(R.string.erro_ano_obrigatorio));
            edtAno.requestFocus();
            return;
        }
        int ano;
        try {
            ano = Integer.parseInt(textoAno);
        } catch (NumberFormatException e) {
            edtAno.setError(getString(R.string.erro_ano_invalido, anoAtual));
            edtAno.requestFocus();
            return;
        }
        if (ano < 1450 || ano > anoAtual) {
            edtAno.setError(getString(R.string.erro_ano_invalido, anoAtual));
            edtAno.requestFocus();
            return;
        }

        boolean lido = chkLido.isChecked();

        if (livroSelecionado == null) {
            Livro livro = new Livro(titulo, autor, ano, lido);
            if (livroDAO.salvar(livro)) {
                Toast.makeText(this, getString(R.string.msg_salvo_sucesso), Toast.LENGTH_SHORT).show();
                NavegacaoMenu.irPara(this, new Intent(this, ListaLivrosActivity.class));
            } else {
                Toast.makeText(this, getString(R.string.msg_salvo_erro), Toast.LENGTH_SHORT).show();
            }
        } else {
            livroSelecionado.setTitulo(titulo);
            livroSelecionado.setAutor(autor);
            livroSelecionado.setAno(ano);
            livroSelecionado.setLido(lido);
            if (livroDAO.atualizar(livroSelecionado)) {
                Toast.makeText(this, getString(R.string.msg_atualizado_sucesso), Toast.LENGTH_SHORT).show();
                NavegacaoMenu.irPara(this, new Intent(this, ListaLivrosActivity.class));
            } else {
                Toast.makeText(this, getString(R.string.msg_atualizado_erro), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
