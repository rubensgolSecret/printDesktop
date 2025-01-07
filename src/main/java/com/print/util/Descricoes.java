package com.print.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.print.view.Token;

public class Descricoes
{
    private static final String finalPath = "resources/textos.properties";

    public static String getDescricao(String descricao)
    {
        Properties appProps = new Properties();

        try 
        {
            appProps.load(new FileInputStream(finalPath));
        }
        catch (IOException e) 
        {
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, "Erro ao carregar arquivo de propriedades", e);
        }

        return appProps.getProperty(descricao);
    }
}
