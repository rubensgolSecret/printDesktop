package com.print.controler.interfaces;

import java.util.List;

public interface ITrataArquivo 
{
    public void salvaTxt(List<Integer> nfLidas, String path);

    public List<Integer> carregaArquivo();
}
