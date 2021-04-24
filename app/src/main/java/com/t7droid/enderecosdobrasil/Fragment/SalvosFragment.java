package com.t7droid.enderecosdobrasil.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.t7droid.enderecosdobrasil.R;
import com.t7droid.enderecosdobrasil.activities.DetalhesDosSalvosActivity;
import com.t7droid.enderecosdobrasil.adapters.SalvosAdapter;
import com.t7droid.enderecosdobrasil.helper.RecyclerItemClickListener;
import com.t7droid.enderecosdobrasil.helper.CepDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SalvosFragment extends Fragment {

    public static RecyclerView recyclerView;
    private SalvosAdapter tarefaAdapter;
    List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> listaTarefas = new ArrayList<>();
    private com.requisicoes.t7droid.cunsultafacilceps.model.CEP cepSelecionado;
    LinearLayout linearEmpty, linearRecycler;
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;

    public SalvosFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflando layout para o Fragment
        View view = inflater.inflate(R.layout.fragment_salvos, container, false);
        CepDAO cepDAOc = new CepDAO(getActivity());
        listaTarefas = cepDAOc.listar();
        linearEmpty = view.findViewById(R.id.empty_state);

        swipeContainer = view.findViewById(R.id.swipeContainer);

        // Configurando SwipeToRefresh
        swipeContainer.setOnRefreshListener(() -> {

            listaTarefas.clear();

            //Método para atualizar o adapter após o "refresh"
            listaTarefas.addAll(cepDAOc.listar());
            tarefaAdapter.notifyDataSetChanged();

            if (listaTarefas.size() == 0) {
                linearRecycler.setVisibility(View.GONE);
                linearEmpty.setVisibility(View.VISIBLE);
            } else {
                linearRecycler.setVisibility(View.VISIBLE);
                linearEmpty.setVisibility(View.GONE);
            }
            swipeContainer.setRefreshing(false);
        });

        linearRecycler = view.findViewById(R.id.linearrecycler);
        recyclerView = view.findViewById(R.id.rvsalvos);

        if (listaTarefas.size() >= 1) {
            linearRecycler.setVisibility(View.VISIBLE);
            linearEmpty.setVisibility(View.GONE);
            carregarListaTarefas(listaTarefas);
        } else {
            linearRecycler.setVisibility(View.GONE);
            linearEmpty.setVisibility(View.VISIBLE);
        }

        recyclerView = view.findViewById(R.id.rvsalvos);

        //Adicionando evento de clique ao RecyclerView
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                //Recuperar endereço
                                com.requisicoes.t7droid.cunsultafacilceps.model.CEP cepSelecionada =
                                        listaTarefas.get(position);

                                //Envia endereço para tela de detalhes
                                Intent intent = new Intent(getActivity(), DetalhesDosSalvosActivity.class);
                                intent.putExtra("cepSelecionado", cepSelecionada);
                                startActivity(intent);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                //Recupera endereço para deletar
                                cepSelecionado = listaTarefas.get(position);

                                AlertDialog.Builder dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

                                //Configura título e mensagem
                                dialog.setTitle("Confirmar exclusão");
                                dialog.setMessage("Deseja excluir o endereço: " + cepSelecionado.getCep() + " ?");

                                dialog.setPositiveButton("Sim", (dialog1, which) -> {

                                    if (cepDAOc.deletar(cepSelecionado)) {

                                        listaTarefas.remove(cepSelecionado);
                                        tarefaAdapter.notifyDataSetChanged();
                                        mensagem("Sucesso ao excluir tarefa!");

                                    } else {
                                        mensagem("Erro ao excluir tarefa!");
                                    }

                                    if (listaTarefas.size() >= 1) {
                                        linearRecycler.setVisibility(View.VISIBLE);
                                        linearEmpty.setVisibility(View.GONE);

                                    } else {
                                        linearRecycler.setVisibility(View.GONE);
                                        linearEmpty.setVisibility(View.VISIBLE);
                                    }
                                });

                                dialog.setNegativeButton("Não", null);

                                //Exibir dialog
                                dialog.create();
                                dialog.show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        return view;
    }

    private void mensagem(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    public void carregarListaTarefas(List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> tarefas) {

        //Listar endereços
        tarefaAdapter = new SalvosAdapter(tarefas, getActivity());
        linearEmpty.setVisibility(View.GONE);

        linearRecycler.setVisibility(View.VISIBLE);

        //Configurar Recyclerview
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), LinearLayout.VERTICAL));
        recyclerView.setAdapter(tarefaAdapter);

    }
}
