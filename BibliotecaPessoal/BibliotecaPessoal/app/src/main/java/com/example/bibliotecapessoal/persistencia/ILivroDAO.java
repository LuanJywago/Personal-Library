package com.example.bibliotecapessoal.persistencia;

import com.example.bibliotecapessoal.model.Livro;

import java.util.List;

public interface ILivroDAO {
    public boolean salvar(Livro livro);
    public boolean atualizar(Livro livro);
    public boolean excluir(Livro livro);
    public List<Livro> listarTodos();
    public int contar(Boolean lido);
}
