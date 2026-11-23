package com.example.bibliotecapessoal.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotecapessoal.R;
import com.example.bibliotecapessoal.model.Livro;

import java.util.List;

public class AdapterLivro extends RecyclerView.Adapter<AdapterLivro.MyViewHolder> {
    private List<Livro> listaLivros;

    public AdapterLivro(List<Livro> listaLivros) {
        this.listaLivros = listaLivros;
    }

    // Substitui a lista interna e redesenha o RecyclerView
    public void atualizarLista(List<Livro> novaLista) {
        this.listaLivros = novaLista;
        notifyDataSetChanged();
    }

    // Devolve o livro da posição clicada
    public Livro getLivro(int position) {
        return listaLivros.get(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_livro, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Livro livro = listaLivros.get(position);
        Context context = holder.itemView.getContext();

        holder.tituloLivro.setText(livro.getTitulo());
        holder.autorAnoLivro.setText(context.getString(R.string.item_autor_ano, livro.getAutor(), livro.getAno()));

        // Indicador visual: etiqueta e ícone verdes para lido, cinza/rosa para não lido
        if (livro.isLido()) {
            holder.statusLivro.setText(R.string.status_lido);
            holder.statusLivro.setBackgroundResource(R.drawable.bg_status_lido);
            holder.iconeLivro.setBackgroundResource(R.drawable.bg_icone_lido);
            ImageViewCompat.setImageTintList(holder.iconeLivro,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.verde_lido)));
        } else {
            holder.statusLivro.setText(R.string.status_nao_lido);
            holder.statusLivro.setBackgroundResource(R.drawable.bg_status_nao_lido);
            holder.iconeLivro.setBackgroundResource(R.drawable.bg_icone_nao_lido);
            ImageViewCompat.setImageTintList(holder.iconeLivro,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.rosa)));
        }
    }

    @Override
    public int getItemCount() {
        return listaLivros.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tituloLivro, autorAnoLivro, statusLivro;
        ImageView iconeLivro;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iconeLivro = itemView.findViewById(R.id.imgLivro);
            tituloLivro = itemView.findViewById(R.id.textViewTitulo);
            autorAnoLivro = itemView.findViewById(R.id.textViewAutorAno);
            statusLivro = itemView.findViewById(R.id.textViewStatus);
        }
    }
}
