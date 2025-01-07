package com.print.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.print.view.Token;

public class Descricoes
{
    private static final InputStream FINAL_PATH = ClassLoader.getSystemResourceAsStream("textos.properties");

    private static Properties appProps;

    public static String getDescricao(String descricao)
    {
        if (appProps == null)
        {
            appProps = new Properties();

            try 
            {
                appProps.load(FINAL_PATH);
            }
            catch (IOException e) 
            {
                Logger.getLogger(Token.class.getName()).log(Level.SEVERE, "Erro ao carregar arquivo de propriedades", e);
            }
        }

        return appProps.getProperty(descricao);
    }
}
