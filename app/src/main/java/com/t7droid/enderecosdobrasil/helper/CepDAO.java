package com.t7droid.enderecosdobrasil.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.requisicoes.t7droid.cunsultafacilceps.model.CEP;

import java.util.ArrayList;
import java.util.List;

public class CepDAO implements ICEPDAO {

    private SQLiteDatabase escreve;
    private SQLiteDatabase le;

    public CepDAO(Context context) {
        DbHelper db = new DbHelper( context );
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(com.requisicoes.t7droid.cunsultafacilceps.model.CEP cep) {

        ContentValues cv = new ContentValues();
        cv.put("cep", cep.getCep() );
        cv.put("logradouro", cep.getLogradouro() );
        cv.put("complemento", cep.getComplemento() );
        cv.put("bairro", cep.getBairro());
        cv.put("localidade", cep.getLocalidade());
        cv.put("uf", cep.getUf() );
        cv.put("ddd", cep.getDdd());
        cv.put("ibge", cep.getIbge() );
        cv.put("selecionado", cep.getSelecionado());

        try {
            escreve.insert(DbHelper.TABELA_TAREFAS, null, cv );
            Log.i("INFO", "Tarefa salva com sucesso!");
        }catch (Exception e){
            Log.e("INFO", "Erro ao salvar tarefa " + e.getMessage() );
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(com.requisicoes.t7droid.cunsultafacilceps.model.CEP cep) {

        ContentValues cv = new ContentValues();
        cv.put("cep", cep.getCep() );
        cv.put("logradouro", cep.getLogradouro() );
        cv.put("complemento", cep.getComplemento() );
        cv.put("bairro", cep.getBairro());
        cv.put("localidade", cep.getLocalidade());
        cv.put("uf", cep.getUf() );
        cv.put("ddd", cep.getDdd());
        cv.put("ibge", cep.getIbge() );
        cv.put("selecionado", cep.getSelecionado());
        try {
            String[] args = {cep.getId().toString()};
            escreve.update(DbHelper.TABELA_TAREFAS, cv, "id=?", args );
            Log.i("INFO", "Tarefa atualizada com sucesso!");
        }catch (Exception e){
            Log.e("INFO", "Erro ao atualizada tarefa " + e.getMessage() );
            return false;
        }

        return true;
    }

    @Override
    public boolean deletar(com.requisicoes.t7droid.cunsultafacilceps.model.CEP cep) {

        try {
            String[] args = { cep.getId().toString() };
            escreve.delete(DbHelper.TABELA_TAREFAS, "id=?", args );
            Log.i("INFO", "Tarefa removida com sucesso!");
        }catch (Exception e){
            Log.e("INFO", "Erro ao remover tarefa " + e.getMessage() );
            return false;
        }

        return true;
    }

    @Override
    public List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> listar() {

        List<CEP> ceps = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS + " ;";
        Cursor c = le.rawQuery(sql, null);

        while ( c.moveToNext() ){

            com.requisicoes.t7droid.cunsultafacilceps.model.CEP cepo = new CEP();

            Long id = c.getLong( c.getColumnIndex("id") );
            String cep = c.getString( c.getColumnIndex("cep") );
            String logradouro = c.getString( c.getColumnIndex("logradouro") );
            String complemento = c.getString( c.getColumnIndex("complemento") );
            String localidade = c.getString( c.getColumnIndex("localidade") );
            String uf = c.getString( c.getColumnIndex("uf") );
            String ddd = c.getString( c.getColumnIndex("ddd") );
            String ibge = c.getString( c.getColumnIndex("ibge") );
            String selecionado = c.getString(c.getColumnIndex("selecionado") );

            cepo.setId( id );
            cepo.setCep( cep );
            cepo.setLogradouro( logradouro );
            cepo.setComplemento( complemento );
            cepo.setLocalidade( localidade );
            cepo.setUf( uf );
            cepo.setDdd( ddd );
            cepo.setIbge( ibge );
            cepo.setSelecionado(selecionado);

            ceps.add( cepo );
        }

        return ceps;

    }
}
