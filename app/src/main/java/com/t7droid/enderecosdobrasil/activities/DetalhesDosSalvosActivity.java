package com.t7droid.enderecosdobrasil.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.requisicoes.t7droid.cunsultafacilceps.model.CEP;
import com.t7droid.enderecosdobrasil.R;

public class DetalhesDosSalvosActivity extends AppCompatActivity {

    com.requisicoes.t7droid.cunsultafacilceps.model.CEP cep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_dos_salvos);

       cep = (CEP) getIntent().getSerializableExtra("cepSelecionado");

        Toast.makeText(this, "CEP: " + cep.getCep(), Toast.LENGTH_SHORT).show();

    }
}