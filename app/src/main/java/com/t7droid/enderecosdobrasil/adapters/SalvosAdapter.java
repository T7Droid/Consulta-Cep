package com.t7droid.enderecosdobrasil.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.requisicoes.t7droid.cunsultafacilceps.model.CEP;
import com.t7droid.enderecosdobrasil.R;

import java.util.List;


public class SalvosAdapter extends RecyclerView.Adapter<SalvosAdapter.MyViewHolder> {

    private List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> listaTarefas;
    private Context context;

    public SalvosAdapter(List<CEP> lista, Context c) {
        this.listaTarefas = lista;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_enderecos_salvos_adapter, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        com.requisicoes.t7droid.cunsultafacilceps.model.CEP cep = listaTarefas.get(position);

        holder.nome.setText(Html.fromHtml("<font color=#A30F05><b>CEP: " + cep.getCep() + "</b></font>"));

        if (!cep.getLogradouro().equals("")) {
            holder.logradouro.setVisibility(View.VISIBLE);
            holder.logradouro.setText(Html.fromHtml("Logradouro: " + cep.getLogradouro()));
        } else {
            holder.logradouro.setVisibility(View.GONE);
        }

        if (!cep.getComplemento().equals("")) {
            holder.complemento.setVisibility(View.VISIBLE);
            holder.complemento.setText(Html.fromHtml("Complemento: " + cep.getComplemento()));
        } else {
            holder.complemento.setVisibility(View.GONE);
        }
        if (!cep.getBairro().equals("")) {
            holder.bairro.setVisibility(View.VISIBLE);
            holder.bairro.setText(Html.fromHtml("Bairro: " + cep.getBairro()));
        } else {
            holder.bairro.setVisibility(View.GONE);
        }
        holder.cidade.setText(Html.fromHtml("Cidade: " + cep.getLocalidade()));
        holder.uf.setText(Html.fromHtml("Estado: " + cep.getUf()));
        holder.ddd.setText(Html.fromHtml("DDD: " + cep.getDdd()));
    }

    @Override
    public int getItemCount() {
        return this.listaTarefas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView logradouro;
        TextView bairro;
        TextView cidade;
        TextView uf;
        TextView ddd;
        TextView complemento;
        ImageView salvo;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.tvcep);
            logradouro = itemView.findViewById(R.id.tvlogradouro);
            bairro = itemView.findViewById(R.id.tvbairro);
            cidade = itemView.findViewById(R.id.tvcidade);
            uf = itemView.findViewById(R.id.tvuf);
            ddd = itemView.findViewById(R.id.tvddd);
            complemento = itemView.findViewById(R.id.tvcomplemento);
            salvo = itemView.findViewById(R.id.tvsalvo);

        }
    }
}
