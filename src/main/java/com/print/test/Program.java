package com.print.test;

import java.util.List;

import com.print.controler.business.TrataArquivo;
import com.print.controler.business.atualizar.Atualizador;
import com.print.controler.interfaces.IAtualiza;
import com.print.controler.interfaces.ITrataArquivo;
import com.print.view.Login;

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

        Login tela = new Login(nfsLidas);

        tela.display();
    }
}