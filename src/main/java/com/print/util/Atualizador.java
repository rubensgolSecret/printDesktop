package com.print.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.print.controler.interfaces.IAtualiza;

public class Atualizador implements IAtualiza
{
    private static final String VERSAO_ATUAL = "1.0.0";
    private static final String URL_VERSAO = "https://raw.githubusercontent.com/rubensgolSecret/printDesktop/refs/heads/main/versao.txt";
    private static final String URL_ARQUIVO = "https://raw.githubusercontent.com/rubensgolSecret/printDesktop/refs/heads/main/print.jar";
    private static final Logger logger = Logger.getLogger(Atualizador.class.getName());

    @Override
    public boolean verificarAtualizacao() 
    {
        try 
        {
            URL url = new URL(URL_VERSAO);

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String versaoRemota = reader.readLine().trim();
            return !VERSAO_ATUAL.equals(versaoRemota);
        }
        catch(IOException e)
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
            URL url = new URL(URL_ARQUIVO);
            InputStream in = url.openStream();

            Files.copy(in, Paths.get("app." + VERSAO_ATUAL + ".jar"), StandardCopyOption.REPLACE_EXISTING);
        
            reiniciarAplicativo();
        }
        catch(IOException e)
        {
            logger.log(Level.SEVERE, "Erro ao baixar atualização", e);
        }
    }

    private void reiniciarAplicativo() throws IOException 
    {
        Runtime.getRuntime().exec("java -jar app." + VERSAO_ATUAL + ".jar");
        System.exit(0);
    }
}