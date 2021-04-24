package com.t7droid.enderecosdobrasil.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.requisicoes.t7droid.cunsultafacilceps.model.CEP;
import com.t7droid.enderecosdobrasil.R;
import com.t7droid.enderecosdobrasil.helper.CepDAO;

import java.util.List;

public class EnderecosAdapter extends RecyclerView.Adapter<EnderecosAdapter.MyViewHolder>  {
    private List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> listaDeCeps;
    com.requisicoes.t7droid.cunsultafacilceps.model.CEP cep;
    Context context;

    public EnderecosAdapter(List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> lista, Context c) {
        this.listaDeCeps = lista;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.enderecos_retornados_adapter, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        cep = listaDeCeps.get(position);

        if (cep.getSelecionado().equals("true")){
            holder.salvar.setImageDrawable(context.getDrawable(R.drawable.estrela_preenchida));
        } else {
            holder.salvar.setImageDrawable(context.getDrawable(R.drawable.estrela_vazia));
        }

        holder.nome.setText(Html.fromHtml("<font color=#A30F05><b>CEP: " + cep.getCep() + "</b></font>"));

        if (!cep.getLogradouro().equals("")) {
            holder.logradouro.setVisibility(View.VISIBLE);
            holder.logradouro.setText(Html.fromHtml("Logradouro: " + cep.getLogradouro()));
        } else {
            holder.logradouro.setVisibility(View.GONE);
        }

        if (!cep.getComplemento().equals("")){
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
      holder.cidade.setText(Html.fromHtml("Cidade: "+cep.getLocalidade()));
      holder.uf.setText(Html.fromHtml("Estado: "+cep.getUf()));
      holder.ddd.setText(Html.fromHtml("DDD: "+cep.getDdd()));
    }

    @Override
    public int getItemCount() {
        return this.listaDeCeps.size();
    }

    public void add(List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> listaDeCepsNova) {
        listaDeCeps.addAll(listaDeCepsNova);
    }

    public void clear() {
        listaDeCeps.clear();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView logradouro;
        TextView bairro;
        TextView cidade;
        TextView uf;
        TextView ddd;
        TextView complemento;
        ImageView salvar;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.item_cep);
            logradouro = itemView.findViewById(R.id.item_logradouro);
            bairro = itemView.findViewById(R.id.item_bairro);
            cidade = itemView.findViewById(R.id.item_cidade);
            uf = itemView.findViewById(R.id.item_uf);
            ddd = itemView.findViewById(R.id.item_ddd);
            salvar = itemView.findViewById(R.id.salvar);
            complemento = itemView.findViewById(R.id.item_complemento);
        }
    }
}