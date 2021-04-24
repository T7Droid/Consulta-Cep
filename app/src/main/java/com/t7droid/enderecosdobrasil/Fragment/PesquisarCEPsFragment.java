package com.t7droid.enderecosdobrasil.Fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.santalu.maskara.widget.MaskEditText;
import com.t7droid.enderecosdobrasil.R;
import com.t7droid.enderecosdobrasil.api.CEPService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PesquisarCEPsFragment extends Fragment {

    private Button botaoRecuperar;
    private TextView textoResultado;
    private Retrofit retrofitCep;
    private MaskEditText etcep;
    private LinearLayout linearPesquisar;
    private ConstraintLayout linearResultado;
    private View view;
    private AlertDialog alerta;
    private ImageView iv;
    private String logradouro, bairro, complemento;

    public PesquisarCEPsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pesquisar_cep, container, false);

        botaoRecuperar = view.findViewById(R.id.buttonRecuperar);
        textoResultado = view.findViewById(R.id.textResultado);
        linearPesquisar = view.findViewById(R.id.linearPesquisar);
        linearResultado = view.findViewById(R.id.linearResultado);

        etcep = view.findViewById(R.id.etcep);
        iv = view.findViewById(R.id.ivvoltarcep);
        iv.setOnClickListener(v -> voltar());

        retrofitCep = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        botaoRecuperar.setOnClickListener(v -> {
            if (isConnected(getActivity())) {
                if (!etcep.getUnMasked().equals("") && etcep.getUnMasked().length() == 8) {
                    recuperarCEPRetrofit();
                    hideKeyboard(getActivity(), etcep);
                } else {
                    mensagem("Digite um CEP válido!");
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

        return view;
    }

    private void mensagem(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private void recuperarCEPRetrofit() {

        CEPService cepService = retrofitCep.create(CEPService.class);
        Call<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> call = cepService.recuperarCEP(etcep.getText().toString());

        call.enqueue(new Callback<com.requisicoes.t7droid.cunsultafacilceps.model.CEP>() {
            @Override
            public void onResponse(Call<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> call, Response<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> response) {
                if (response.isSuccessful()) {
                    com.requisicoes.t7droid.cunsultafacilceps.model.CEP cep = response.body();

                    assert cep != null;
                    if (cep.getCep() != null && cep.getCep() != "") {
                        if (!cep.getLogradouro().equals("")){
                            logradouro = "<br>Logradouro: " + cep.getLogradouro() + "<br>";
                        } else {
                            logradouro = "";
                        }
                        if (!cep.getBairro().equals("")){
                            bairro = "Bairro: " + cep.getBairro() + "<br>";
                        } else {
                            bairro = "";
                        }
                        if (!cep.getComplemento().equals("")){
                            complemento = "Complemento: " + cep.getComplemento() + "<br>";
                        } else {
                            complemento = "";
                        }

                        linearPesquisar.setVisibility(View.GONE);
                        linearResultado.setVisibility(View.VISIBLE);
                        textoResultado.setText(Html.fromHtml(
                                "<font color=#F980238>CEP: " + cep.getCep() + "</font><br><br>" +
                                        logradouro +
                                        bairro +
                                        complemento +
                                        "Localidade: " + cep.getLocalidade() + "<br>" +
                                        "UF: " + cep.getUf() + "<br>" +
                                        "DDD: " + cep.getDdd() + "<br>" +
                                        "Código IBGE: " + cep.getIbge()));
                    } else {
                        mensagem("CEP inexistente");
                    }
                } else {
                    mensagem("CEP inexistente");
                }
            }

            @Override
            public void onFailure(Call<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> call, Throwable t) {

            }
        });
    }

    public void voltar() {
        if (linearResultado.getVisibility() == View.VISIBLE) {
            linearResultado.setVisibility(View.GONE);
            linearPesquisar.setVisibility(View.VISIBLE);
            etcep.requestFocus();
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        }
        return false;
    }

    public static void hideKeyboard(Context context, View editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
