package com.example.bibliotecapessoal.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String NOME_DB = "DB_BIBLIOTECA";
    public static final String TABELA_LIVRO = "livros";

    public static final String COLUNA_ID = "id";
    public static final String COLUNA_TITULO = "titulo";
    public static final String COLUNA_AUTOR = "autor";
    public static final String COLUNA_ANO = "ano";
    public static final String COLUNA_LIDO = "lido";
    public static final String COLUNA_NOTA = "nota";

    public DbHelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_LIVRO +
                     " (" + COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     COLUNA_TITULO + " TEXT NOT NULL, " +
                     COLUNA_AUTOR + " TEXT NOT NULL, " +
                     COLUNA_ANO + " INTEGER, " +
                     COLUNA_LIDO + " INTEGER DEFAULT 0);";
        try {
            db.execSQL(sql);
            Log.i("InfoDB", "Tabela criada com sucesso!");
        } catch (Exception e) {
            Log.e("InfoDB", "Erro ao criar a tabela: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Migração para a versão 2: acrescenta a coluna nota sem apagar a tabela
        if (oldVersion < 2) {
            String sql = "ALTER TABLE " + TABELA_LIVRO + " ADD COLUMN " + COLUNA_NOTA + " INTEGER;";
            try {
                db.execSQL(sql);
                Log.i("InfoDB", "Tabela alterada com sucesso!");
            } catch (Exception e) {
                Log.e("InfoDB", "Erro ao alterar a tabela! Erro: " + e.getMessage());
            }
        }
    }
}
