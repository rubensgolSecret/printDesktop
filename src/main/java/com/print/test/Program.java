package com.print.test;

import java.util.List;

import com.print.controler.business.TrataArquivo;
import com.print.controler.interfaces.IAtualiza;
import com.print.controler.interfaces.ITrataArquivo;
import com.print.util.Atualizador;
import com.print.view.Tela;

public class Program 
{    
    public static void  main (String[] args)
    {
        List<Integer> nfsLidas;
        ITrataArquivo arqv = new TrataArquivo();

        nfsLidas = arqv.carregaArquivo();

        IAtualiza atualiza = new Atualizador();

        if (atualiza.verificarAtualizacao())
        {
            atualiza.atualiza();
            System.exit(0);
        }

        Tela tela = new Tela(nfsLidas);
        tela.display();
    }
}