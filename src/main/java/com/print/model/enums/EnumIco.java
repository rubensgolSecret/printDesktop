package com.print.model.enums;

public enum EnumIco 
{
    HOME("home.png"),
    COMECA("comecar.png");
    
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
