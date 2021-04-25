package com.t7droid.enderecosdobrasil.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.requisicoes.t7droid.cunsultafacilceps.model.CEP;
import com.t7droid.enderecosdobrasil.R;
import com.t7droid.enderecosdobrasil.adapters.EnderecosAdapter;
import com.t7droid.enderecosdobrasil.api.CEPService;
import com.t7droid.enderecosdobrasil.helper.RecyclerItemClickListener;
import com.t7droid.enderecosdobrasil.helper.CepDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.t7droid.enderecosdobrasil.Fragment.PesquisarCEPsFragment.hideKeyboard;
import static com.t7droid.enderecosdobrasil.Fragment.PesquisarCEPsFragment.isConnected;

public class EnderecosFragment extends Fragment {

    private View view;
    private Retrofit retrofitCep;
    private RecyclerView rvEnderecos;
    private CepDAO dao;
    private LinearLayout linearPesquisaEnd, linearResultadoEnd;
    private Button btnEnd;
    private ImageView voltarend;
    private EnderecosAdapter enderecosAdapter;
    private EditText etEnd;
    private String estadoSelecionado, uf;
    private String cidadeSelecionada;
    private Spinner spnEstados, spnCidades;
    private JSONArray m_jArry;
    private JSONObject obj;
    private AlertDialog alerta;
    private ArrayAdapter<String> dataAdapter;
    private List<String> listaDeCidades = new ArrayList<>();
    private List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> listaDeCeps = new ArrayList<>();
    private CEPService cepService;
    private Call<List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP>> call;
    private List<CEP> listaSalvaNoBanco = new ArrayList<>();
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    public EnderecosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_endereco, container, false);

        //Configurando Retrofit
        retrofitCep = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnEnd = view.findViewById(R.id.buttonRecuperarEnd);
        voltarend = view.findViewById(R.id.voltar_end);
        voltarend.setOnClickListener(v -> {
            linearResultadoEnd.setVisibility(View.GONE);
            linearPesquisaEnd.setVisibility(View.VISIBLE);
            voltarend.setVisibility(View.GONE);

        });

        etEnd = view.findViewById(R.id.etendereco);
        spnEstados = view.findViewById(R.id.spnestados);
        spnCidades = view.findViewById(R.id.spncidades);
        rvEnderecos = view.findViewById(R.id.rvenderecos);
        rvEnderecos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvEnderecos.setHasFixedSize(true);
        enderecosAdapter = new EnderecosAdapter(listaDeCeps, getActivity());
        rvEnderecos.setAdapter(enderecosAdapter);

        rvEnderecos.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                rvEnderecos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {

                dao = new CepDAO(getActivity());
                com.requisicoes.t7droid.cunsultafacilceps.model.CEP novoCep = new CEP();
                com.requisicoes.t7droid.cunsultafacilceps.model.CEP cep = listaDeCeps.get(position);

                if (!cep.getSelecionado().equals("true")) {
                    cep.setSelecionado("true");
                    novoCep.setCep(cep.getCep());
                    novoCep.setLocalidade(cep.getLocalidade());

                    if (!cep.getLogradouro().equals("")) {
                        novoCep.setLogradouro(cep.getLogradouro());
                    } else {
                        novoCep.setLogradouro("");
                    }

                    if (!cep.getBairro().equals("")) {
                        novoCep.setBairro(cep.getBairro());
                    } else {
                        novoCep.setBairro("");
                    }

                    novoCep.setDdd(cep.getDdd());
                    novoCep.setIbge(cep.getIbge());
                    novoCep.setUf(cep.getUf());
                    if (!cep.getComplemento().equals("")) {
                        novoCep.setComplemento(cep.getComplemento());
                    } else {
                        novoCep.setComplemento("");
                    }

                    if (dao.salvar(novoCep)) {
                        enderecosAdapter.notifyDataSetChanged();
                        mensagem("Endereço adicionado aos favoritos");
                    } else {
                        mensagem("Falha ao salvar Endereço");
                    }
                } else {
                    mensagem("Esse endereço já está salvo nos favoritos");
                }
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        //Recuperando ultíma posição selecionada do SpinnerEstados
        pref = getActivity().getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE);
        int posicaoDoEstadoSalva = pref.getInt("posicaoEstado", 19);

        spnEstados.setSelection(posicaoDoEstadoSalva);
        listaDeCidades.addAll(carregaListaCidades("35"));

        dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, listaDeCidades);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCidades.setAdapter(dataAdapter);

        //Recuperando última posição selecionada no SpinnerCidades no SharedPreferences
        pref = getActivity().getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE);
        int posicaoDaCidadeSalva = pref.getInt("posicaoCidade", 1);
        spnCidades.setSelection(posicaoDaCidadeSalva);

        spnCidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cidadeSelecionada = String.valueOf(spnCidades.getAdapter().getItem(position));
                etEnd.requestFocus();

                //Salvando posição do SpinnerCidades
                editor = getActivity().getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit();
                editor.putInt("posicaoCidade", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                estadoSelecionado = String.valueOf(spnEstados.getAdapter().getItem(position));

                //Salvando posição do SpinnerCidades no SharedPreferences
                editor = getActivity().getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE).edit();
                editor.putInt("posicaoEstado", position);
                editor.apply();

                switch (estadoSelecionado) {

                    case "RO":
                        estadoSelecionado = "11";
                        break;

                    case "AC":
                        estadoSelecionado = "12";
                        break;

                    case "AM":
                        estadoSelecionado = "13";
                        break;

                    case "RR":
                        estadoSelecionado = "14";
                        break;

                    case "PA":
                        estadoSelecionado = "15";
                        break;

                    case "AP":
                        estadoSelecionado = "16";
                        break;

                    case "TO":
                        estadoSelecionado = "17";
                        break;

                    case "MA":
                        estadoSelecionado = "21";
                        break;

                    case "PI":
                        estadoSelecionado = "22";
                        break;

                    case "CE":
                        estadoSelecionado = "23";
                        break;

                    case "RN":
                        estadoSelecionado = "24";
                        break;

                    case "PB":
                        estadoSelecionado = "25";
                        break;

                    case "PE":
                        estadoSelecionado = "26";
                        break;

                    case "AL":
                        estadoSelecionado = "27";
                        break;

                    case "SE":
                        estadoSelecionado = "28";
                        break;

                    case "BA":
                        estadoSelecionado = "29";
                        break;

                    case "MG":
                        estadoSelecionado = "31";
                        break;

                    case "ES":
                        estadoSelecionado = "32";
                        break;

                    case "RJ":
                        estadoSelecionado = "33";
                        break;

                    case "SP":
                        estadoSelecionado = "35";
                        break;

                    case "PR":
                        estadoSelecionado = "41";
                        break;

                    case "SC":
                        estadoSelecionado = "42";
                        break;

                    case "RS":
                        estadoSelecionado = "43";
                        break;

                    case "MS":
                        estadoSelecionado = "50";
                        break;

                    case "MT":
                        estadoSelecionado = "51";
                        break;

                    case "GO":
                        estadoSelecionado = "52";
                        break;

                    case "DF":
                        estadoSelecionado = "53";
                        break;
                }
                listaDeCidades.clear();
                listaDeCidades.addAll(carregaListaCidades(estadoSelecionado));
                dataAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnEnd.setOnClickListener(v -> {
            if (isConnected(getActivity())) {
                if (etEnd.getText().toString().length() >= 3) {
                    recuperarListaDeCEPs();
                    hideKeyboard(getActivity(), etEnd);
                } else {
                    Toast.makeText(getActivity(), "Insira pelo menos 3 caracteres", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "Conecte-se a internet", Toast.LENGTH_LONG).show();
                LayoutInflater li = getLayoutInflater();
                View view = li.inflate(R.layout.sem_internet, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setView(view);
                alerta = builder.create();
                alerta.show();
            }
        });

        linearPesquisaEnd = view.findViewById(R.id.linearPesquisarEndereco);
        linearResultadoEnd = view.findViewById(R.id.linearResultadoEndereco);


        return view;
    }

    private void mensagem(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getActivity().getAssets().open("municipios.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private List<String> carregaListaCidades(String estadoSelecionado) {

        try {
            obj = new JSONObject(loadJSONFromAsset());
            m_jArry = obj.getJSONArray("municipios");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String nome = jo_inside.getString("nome");
                String codigo_uf = jo_inside.getString("codigo_uf");

                if (codigo_uf.equalsIgnoreCase(estadoSelecionado)) {
                    listaDeCidades.add(nome);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return listaDeCidades;
    }

    private void recuperarListaDeCEPs() {

        switch (estadoSelecionado) {

            case "11":
                uf = "RO";
                break;

            case "12":
                uf = "AC";
                break;

            case "13":
                uf = "AM";
                break;

            case "14":
                uf = "RR";
                break;

            case "15":
                uf = "PA";
                break;

            case "16":
                uf = "AP";
                break;

            case "17":
                uf = "TO";
                break;

            case "21":
                uf = "MA";
                break;

            case "22":
                uf = "PI";
                break;

            case "23":
                uf = "CE";
                break;

            case "24":
                uf = "RN";
                break;

            case "25":
                uf = "PB";
                break;

            case "26":
                uf = "PE";
                break;

            case "27":
                uf = "AL";
                break;

            case "28":
                uf = "SE";
                break;

            case "29":
                uf = "BA";
                break;

            case "31":
                uf = "MG";
                break;

            case "32":
                uf = "ES";
                break;

            case "33":
                uf = "RJ";
                break;

            case "35":
                uf = "SP";
                break;

            case "41":
                uf = "PR";
                break;

            case "SC":
                uf = "42";
                break;

            case "43":
                uf = "RS";
                break;

            case "50":
                uf = "MS";
                break;

            case "51":
                uf = "MT";
                break;

            case "52":
                uf = "GO";
                break;

            case "53":
                uf = "DF";
                break;
        }


        cepService = retrofitCep.create(CEPService.class);
        call = cepService.recuperarListaCEPs(uf + "/" + cidadeSelecionada + "/" + etEnd.getText().toString());

        call.enqueue(new Callback<List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP>>() {
            @Override
            public void onResponse(Call<List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP>> call,
                                   Response<List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP>> response) {

                if (response.isSuccessful()) {
                    enderecosAdapter.clear();
                    enderecosAdapter.notifyDataSetChanged();
                    listaDeCeps = response.body();

                    assert listaDeCeps != null;
                    if (listaDeCeps.size() > 0) {

                        linearPesquisaEnd.setVisibility(View.GONE);
                        linearResultadoEnd.setVisibility(View.VISIBLE);
                        voltarend.setVisibility(View.VISIBLE);

                        mensagem(listaDeCeps.size() + " resultados encontrados");
                        dao = new CepDAO(getActivity());
                        listaSalvaNoBanco = dao.listar();

                        if (!listaSalvaNoBanco.isEmpty()) {
                            for (CEP cepBaixado : listaDeCeps) {
                                for (CEP cep : listaSalvaNoBanco) {
                                    if (cepBaixado.getCep().equals(cep.getCep())) {
                                        cepBaixado.setSelecionado("true");
                                    }
                                }
                            }
                        }

                        enderecosAdapter.add(listaDeCeps);
                        enderecosAdapter.notifyDataSetChanged();

                    } else {
                        mensagem("Nenhum resultado encontrado");
                    }
                } else {
                    mensagem("Falha na resposta" + response.code());
                }

            }

            private void mensagem(String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP>> call, Throwable t) {

            }
        });
    }
}
