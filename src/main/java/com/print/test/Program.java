package com.print.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.update4j.Configuration;

import com.print.controler.interfaces.ITrataArquivo;
import com.print.controler.business.TrataArquivo;
import com.print.view.Tela;

public class Program 
{    
    public static void  main (String[] args)
    {
        List<Integer> nfsLidas;
        ITrataArquivo arqv = new TrataArquivo();

        nfsLidas = arqv.carregaArquivo();

		URL configUrl;
        Configuration config = null;
        Reader in;

        try 
        {
            configUrl = new URI("https://github.com/rubensgolSecret/print/raw/refs/heads/main/src/config/config.xml").toURL();
            in = new InputStreamReader(configUrl.openStream(), StandardCharsets.UTF_8);
            config = Configuration.read(in);
        }
        catch (IOException | URISyntaxException e) 
        {
            System.err.println("Could not load remote config, falling back to local.");

            try 
            {
                in = Files.newBufferedReader(Paths.get("src/config/config.xml"));
                config = Configuration.read(in);
            }
            catch (IOException e1) 
            {
                System.err.println("Error loading local config: " + e1.getMessage());
            }
        }

        Tela tela = new Tela(nfsLidas, config);
        tela.display();
    }
}