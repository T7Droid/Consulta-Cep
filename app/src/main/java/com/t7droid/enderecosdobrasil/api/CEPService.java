package com.t7droid.enderecosdobrasil.api;

import androidx.annotation.Keep;

import com.requisicoes.t7droid.cunsultafacilceps.model.CEP;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CEPService {

    @GET("{cep}/json/")
    Call<CEP> recuperarCEP(@Path("cep") String cep);

    @GET("{cep}/json/")
    Call<List<CEP>> recuperarListaCEPs(@Path("cep") String cep);

}
