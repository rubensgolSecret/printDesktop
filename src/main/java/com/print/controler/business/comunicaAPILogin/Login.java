package com.print.controler.business.comunicaAPILogin;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.print.util.Util;

public class Login 
{
	private static final Logger logger = Logger.getLogger(Login.class.getName());
	
    public Login()
	{
		try 
		{
			FileHandler fh = new FileHandler("log/log-" + Util.getDataFormatadaSemBarra() + "-login.log");
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

    public boolean login(String login, String senha)
    {
        return true;
    }
}
