package com.print.controler.interfaces;

import java.util.List;

public interface ITrataArquivo 
{
    public void salvaTxt(List<Integer> nfLidas);

    public List<Integer> carregaArquivo();
}
