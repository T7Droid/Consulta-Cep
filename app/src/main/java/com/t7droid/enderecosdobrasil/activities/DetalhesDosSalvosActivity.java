package com.t7droid.enderecosdobrasil.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.requisicoes.t7droid.cunsultafacilceps.model.CEP;
import com.t7droid.enderecosdobrasil.R;

public class DetalhesDosSalvosActivity extends AppCompatActivity {

    private TextView tvdetalhes;
    com.requisicoes.t7droid.cunsultafacilceps.model.CEP cep;
    private FloatingActionButton fabPesquisar, fabEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_dos_salvos);

        cep = (CEP) getIntent().getSerializableExtra("cepSelecionado");

        inicializarViews();

        tvdetalhes.setText(Html.fromHtml(
                "\t\t<font color=#A30F05><b>CEP: " + cep.getCep() + "</b></font><br><br>" +
                        "Logradouro: " + cep.getLogradouro() + "<br>" +
                        "Complemento: " + cep.getComplemento() + "<br>" +
                        "Bairro: " + cep.getBairro() + "<br>" +
                        "Localidade: " + cep.getLocalidade() + "<br>" +
                        "UF: " + cep.getUf() + "<br>" +
                        "DDD: " + cep.getDdd() + "<br>" +
                        "Ibge: " + cep.getIbge()));

    }

    private void inicializarViews() {
        tvdetalhes = findViewById(R.id.tvdetalhe);
        fabPesquisar = findViewById(R.id.fab_pesquisar);
        fabPesquisar.setOnClickListener(v -> pesquisar());
        fabEnviar = findViewById(R.id.fab_enviar);
        fabEnviar.setOnClickListener(v -> enviar());
    }

    private void pesquisar() {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, cep.getCep());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.compartilhar:
                indicar();
                break;

            case R.id.avaliar:
                avaliar();
                break;

            case R.id.sugestoes:
                enviarEmail();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void enviarEmail() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"thyagoneves.sa@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Sugestões");
        email.putExtra(Intent.EXTRA_TEXT, "Olá, gostaria de enviar uma sugestão...");
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Enviar email:"));
    }

    private void avaliar() {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/app/details?id=" + getPackageName())));
        }
    }

    private void indicar() {

        String text = "Olá! Gostaria de compartilhar o App\n*CONSULTA CEP*" +
                " com você! Este é o link para download na Play Store:\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, "Falha ao compartilhar", Toast.LENGTH_SHORT).show();
        }
    }

    private void enviar() {

        String text = "*CEP: " + cep.getCep() + "*\n" +
                "Logradouro: " + cep.getLogradouro() + "\n" +
                "Complemento: " + cep.getComplemento() + "\n" +
                "Bairro: " + cep.getBairro() + "\n" +
                "Localidade: " + cep.getLocalidade() + "\n" +
                "UF: " + cep.getUf() + "\n" +
                "DDD: " + cep.getDdd() + "\n" +
                "Ibge: " + cep.getIbge();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, "Falha ao compartilhar", Toast.LENGTH_SHORT).show();
        }
    }
}