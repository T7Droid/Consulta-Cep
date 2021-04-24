package com.t7droid.enderecosdobrasil.helper;

import java.util.List;

public interface ICEPDAO {

    public boolean salvar(com.requisicoes.t7droid.cunsultafacilceps.model.CEP tarefa);
    public boolean atualizar(com.requisicoes.t7droid.cunsultafacilceps.model.CEP tarefa);
    public boolean deletar(com.requisicoes.t7droid.cunsultafacilceps.model.CEP tarefa);
    public List<com.requisicoes.t7droid.cunsultafacilceps.model.CEP> listar();

}
