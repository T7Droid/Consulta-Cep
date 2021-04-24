package com.t7droid.enderecosdobrasil.Fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

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

import com.t7droid.enderecosdobrasil.R;

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
    private EditText etcep;
    private LinearLayout linearPesquisar;
    private ConstraintLayout linearResultado;
    View view;
    ImageView iv;

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
                if (!etcep.getText().toString().equals("") && etcep.getText().toString().length() == 8) {
                    recuperarCEPRetrofit();
                    hideKeyboard(getActivity(), etcep);
                } else {
                    mensagem("Digite um CEP válido!");
                }
            }
        });

        return view;
    }

    private void mensagem(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private void recuperarCEPRetrofit(){

        com.requisicoes.t7droid.cunsultafacilceps.api.CEPService cepService = retrofitCep.create( com.requisicoes.t7droid.cunsultafacilceps.api.CEPService.class );
        Call<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> call = cepService.recuperarCEP(etcep.getText().toString());

        call.enqueue(new Callback<com.requisicoes.t7droid.cunsultafacilceps.model.CEP>() {
            @Override
            public void onResponse(Call<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> call, Response<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> response) {
                if( response.isSuccessful() ){
                    com.requisicoes.t7droid.cunsultafacilceps.model.CEP cep = response.body();

                    if (cep.getCep()!=null) {
                        linearPesquisar.setVisibility(View.GONE);
                        linearResultado.setVisibility(View.VISIBLE);
                        textoResultado.setText(Html.fromHtml(
                                "<font color=#F980238>CEP: " + cep.getCep() + "</font><br><br>" +
                                        "Logradouro: " + cep.getLogradouro() + "<br>" +
                                        "Bairro: " + cep.getBairro() + "<br>" +
                                        "Complemento: " + cep.getComplemento() + "<br>" +
                                        "Localidade: " + cep.getLocalidade() + "<br>" +
                                        "UF: " + cep.getUf()+"<br>"+
                                        "DDD: "+cep.getDdd()+"<br>"+
                                        "Código IBGE: "+cep.getIbge()));
                    } else {
                        mensagem("CEP inexistente");
                    }
                }
            }

            @Override
            public void onFailure(Call<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> call, Throwable t) {

            }
        });
    }
    public void voltar(){
        if (linearResultado.getVisibility()== View.VISIBLE){
            linearResultado.setVisibility(View.GONE);
            linearPesquisar.setVisibility(View.VISIBLE);
            etcep.requestFocus();
        }
    }
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( cm != null ) {
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
