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

public class Atualizador {
    private static final String VERSAO_ATUAL = "1.0.0";
    private static final String URL_VERSAO = "https://example.com/versao.txt";
    private static final String URL_ARQUIVO = "https://example.com/app.jar";
    private static final Logger logger = Logger.getLogger(Atualizador.class.getName());

    public static void main(String[] args) 
    {
        try 
        {
            if (verificarAtualizacao()) 
            {
                baixarAtualizacao();
                reiniciarAplicativo();
            }
            else 
                iniciarAplicativo();
        } 
        catch (IOException e) 
        {
            logger.log(Level.SEVERE, "Erro ao verificar ou aplicar atualização", e);
        }
    }

    private static boolean verificarAtualizacao() throws IOException
    {
        URL url = new URL(URL_VERSAO);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) 
        {
            String versaoRemota = reader.readLine().trim();
            return !VERSAO_ATUAL.equals(versaoRemota);
        }
    }

    private static void baixarAtualizacao() throws IOException 
    {
        URL url = new URL(URL_ARQUIVO);

        try (InputStream in = url.openStream()) 
        {
            Files.copy(in, Paths.get("app-atualizado.jar"), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void reiniciarAplicativo() throws IOException 
    {
        // Código para reiniciar o aplicativo, por exemplo, executando o novo JAR
        Runtime.getRuntime().exec("java -jar app-atualizado.jar");
        System.exit(0);
    }

    private static void iniciarAplicativo() 
    {
        // Código para iniciar o aplicativo normalmente
        System.out.println("Iniciando aplicativo versão " + VERSAO_ATUAL);
    }
}