package com.print.controler.business.atualizar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.print.controler.interfaces.IAtualiza;

public class Atualizador implements IAtualiza
{
    private static final String VERSAO_ATUAL = "1.0.5";
    private static final String URL_VERSAO = "https://raw.githubusercontent.com/rubensgolSecret/printDesktop/refs/heads/main/versao.txt";
    private static final String URL_ARQUIVO = "https://raw.githubusercontent.com/rubensgolSecret/printDesktop/refs/heads/main/print.jar";
    private static final Logger logger = Logger.getLogger(Atualizador.class.getName());

    @Override
    public boolean verificarAtualizacao() 
    {
        try 
        {
            URL url = new URI(URL_VERSAO).toURL();

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.toURI().toURL().openStream()));

            String versaoRemota = reader.readLine().trim();
            return !VERSAO_ATUAL.equals(versaoRemota);
        }
        catch(IOException | URISyntaxException e)
        {
            logger.log(Level.SEVERE, "Erro ao verificar atualização", e);
            return false;
        }
    }

    @Override
    public void atualiza() 
    {
        try 
        {
            URI uri = new URI(URL_ARQUIVO);
            URL url = uri.toURL();
            InputStream in = url.openStream();

            Files.copy(in, Paths.get("print." + URL_VERSAO + ".jar"), StandardCopyOption.REPLACE_EXISTING);
        
            reiniciarAplicativo();
        }
        catch(IOException | URISyntaxException e)
        {
            logger.log(Level.SEVERE, "Erro ao baixar atualização", e);
        }
    }

    private void reiniciarAplicativo() throws IOException 
    {
        Runtime.getRuntime().exec(new String[]{"java", "-jar", "print." + URL_VERSAO + ".jar"});
        System.exit(0);
    }
}