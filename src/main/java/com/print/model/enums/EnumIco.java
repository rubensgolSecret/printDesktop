package com.print.model.enums;

import com.print.util.Descricoes;

public enum EnumIco 
{
    HOME(Descricoes.getDescricao("ico"));

    private EnumIco(String descricao) 
    {
        this.descricao = descricao;
    }
    
    private final String descricao;
    
    public String getDescricao()
    {
        return this.descricao;
    }    
}
