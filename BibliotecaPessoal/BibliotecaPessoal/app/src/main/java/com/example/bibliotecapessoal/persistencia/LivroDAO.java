package com.example.bibliotecapessoal.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bibliotecapessoal.model.Livro;

import java.util.ArrayList;
import java.util.List;

public class LivroDAO implements ILivroDAO {
    private SQLiteDatabase escreve;
    private SQLiteDatabase le;

    public LivroDAO(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Livro livro) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUNA_TITULO, livro.getTitulo());
        cv.put(DbHelper.COLUNA_AUTOR, livro.getAutor());
        cv.put(DbHelper.COLUNA_ANO, livro.getAno());
        cv.put(DbHelper.COLUNA_LIDO, livro.isLido() ? 1 : 0);
        try {
            long resultado = escreve.insert(DbHelper.TABELA_LIVRO, null, cv);
            if (resultado != -1) {
                Log.i("InfoDB", "Sucesso ao salvar o registro");
                return true;
            }
            Log.e("InfoDB", "Erro ao salvar o registro: insert retornou -1");
        } catch (Exception e) {
            Log.e("InfoDB", "Erro ao salvar o registro: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean atualizar(Livro livro) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUNA_TITULO, livro.getTitulo());
        cv.put(DbHelper.COLUNA_AUTOR, livro.getAutor());
        cv.put(DbHelper.COLUNA_ANO, livro.getAno());
        cv.put(DbHelper.COLUNA_LIDO, livro.isLido() ? 1 : 0);
        try {
            String[] args = {livro.getId().toString()};
            int linhas = escreve.update(DbHelper.TABELA_LIVRO, cv, DbHelper.COLUNA_ID + "=?", args);
            if (linhas > 0) {
                Log.i("InfoDB", "Sucesso ao alterar o registro");
                return true;
            }
            Log.e("InfoDB", "Erro ao alterar o registro: nenhuma linha afetada");
        } catch (Exception e) {
            Log.e("InfoDB", "Erro ao alterar o registro: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean excluir(Livro livro) {
        try {
            String[] args = {livro.getId().toString()};
            int linhas = escreve.delete(DbHelper.TABELA_LIVRO, DbHelper.COLUNA_ID + "=?", args);
            if (linhas > 0) {
                Log.i("InfoDB", "Sucesso ao excluir o registro");
                return true;
            }
            Log.e("InfoDB", "Erro ao excluir o registro: nenhuma linha afetada");
        } catch (Exception e) {
            Log.e("InfoDB", "Erro ao excluir o registro: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Livro> listarTodos() {
        List<Livro> lstLivro = new ArrayList<>();
        String sql = "SELECT * FROM " + DbHelper.TABELA_LIVRO +
                     " ORDER BY " + DbHelper.COLUNA_TITULO + " COLLATE NOCASE ASC;";
        try (Cursor cursor = le.rawQuery(sql, null)) {
            int indexId = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_ID);
            int indexTitulo = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_TITULO);
            int indexAutor = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_AUTOR);
            int indexAno = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_ANO);
            int indexLido = cursor.getColumnIndexOrThrow(DbHelper.COLUNA_LIDO);

            while (cursor.moveToNext()) {
                Livro livro = new Livro();
                livro.setId(cursor.getLong(indexId));
                livro.setTitulo(cursor.getString(indexTitulo));
                livro.setAutor(cursor.getString(indexAutor));
                livro.setAno(cursor.getInt(indexAno));
                livro.setLido(cursor.getInt(indexLido) == 1);
                lstLivro.add(livro);
            }
            Log.i("InfoDB", "Sucesso ao pesquisar os registros");
        } catch (Exception e) {
            Log.e("InfoDB", "Erro ao pesquisar os registros", e);
        }
        return lstLivro;
    }

    @Override
    public int contar(Boolean lido) {
        // null = total geral, true = lidos, false = não lidos
        String sql = "SELECT COUNT(*) FROM " + DbHelper.TABELA_LIVRO;
        String[] args = null;
        if (lido != null) {
            sql += " WHERE " + DbHelper.COLUNA_LIDO + " = ?";
            args = new String[]{lido ? "1" : "0"};
        }
        int total = 0;
        try (Cursor cursor = le.rawQuery(sql, args)) {
            if (cursor.moveToFirst()) {
                total = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("InfoDB", "Erro ao contar os registros", e);
        }
        return total;
    }
}
