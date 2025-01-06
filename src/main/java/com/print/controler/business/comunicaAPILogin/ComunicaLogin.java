package com.print.controler.business.comunicaAPILogin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.print.model.Usuario;
import com.print.util.UrlsAPI;
import com.print.util.Util;

public class ComunicaLogin 
{
	private static final Logger logger = Logger.getLogger(ComunicaLogin.class.getName());
	
    public ComunicaLogin()
	{
		try 
		{
			FileHandler fh = new FileHandler("resources/log/log-" + Util.getDataFormatadaSemBarra() + "-login.log");
			fh.setEncoding("UTF-8");
			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
			fh.setFormatter(new SimpleFormatter());	
		} 
		catch (IOException | SecurityException e) 
		{
			logger.log(Level.SEVERE, "erro{0}", e.getMessage());
		}
	}

    public Usuario login(String login, String senha)
    {
        Usuario usuario = buscarUsuario(login);

        if (usuario == null)
            return null;
        else if (!usuario.getSenha().equals(senha))
            return new Usuario();

        return usuario;
    }

    private Usuario buscarUsuario(String login)
    {
        Gson gson = new Gson();

        try
        {
            logger.info("Buscando Usuario");
            URI url = new URI(UrlsAPI.getUsuario(login));
            HttpURLConnection conexao = (HttpURLConnection) url.toURL().openConnection();
    
            if (conexao.getResponseCode() != 200)
            {
                logger.severe("problema de conexao");
                return null;
            }
    
            BufferedReader resposta = new BufferedReader(new InputStreamReader((conexao.getInputStream())));
            String jsonEmString = Util.converteJsonEmString(resposta);
    
            logger.info("Busca do usuario concluida");
    
            return  gson.fromJson(jsonEmString, Usuario.class);
        }
        catch (JsonSyntaxException | IOException | URISyntaxException e)
        {
            logger.severe("Erro ao buscar usuario");
        }

	   return null;
    }
}
