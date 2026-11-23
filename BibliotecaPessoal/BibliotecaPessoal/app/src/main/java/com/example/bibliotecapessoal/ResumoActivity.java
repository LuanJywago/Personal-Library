package com.example.bibliotecapessoal;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bibliotecapessoal.persistencia.LivroDAO;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class ResumoActivity extends AppCompatActivity {
    TextView tvTotal, tvLidos, tvNaoLidos;
    private LivroDAO livroDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resumo);
        // bottom = 0: o BottomNavigationView já trata a margem inferior
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        NavegacaoMenu.configurar(this, R.id.nav_resumo);

        livroDAO = new LivroDAO(this);

        tvTotal = findViewById(R.id.lblTotal);
        tvLidos = findViewById(R.id.lblLidos);
        tvNaoLidos = findViewById(R.id.lblNaoLidos);

        // Contadores obtidos direto do banco com SELECT COUNT(*)
        int total = livroDAO.contar(null);
        int lidos = livroDAO.contar(true);
        int naoLidos = livroDAO.contar(false);

        tvTotal.setText(getString(R.string.resumo_numero, total));
        tvLidos.setText(getString(R.string.resumo_numero, lidos));
        tvNaoLidos.setText(getString(R.string.resumo_numero, naoLidos));

        // Percentual de livros lidos (evita divisão por zero quando não há livros)
        int percentual = total == 0 ? 0 : (lidos * 100) / total;
        LinearProgressIndicator progresso = findViewById(R.id.progressoLeitura);
        progresso.setProgress(percentual);
        TextView tvProgresso = findViewById(R.id.lblProgresso);
        tvProgresso.setText(getString(R.string.resumo_progresso, percentual));
    }
}
