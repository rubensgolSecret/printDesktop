package com.print.util;

public class UrlsAPI 
{
    public static String getUsuario(String usuario)
	{
    	StringBuilder urlParaChamada = new StringBuilder()
				.append("http://192.168.0.114:8080/logar?");

    	urlParaChamada.append("login")
		  			  .append("=")
				  	  .append(usuario);

    	return urlParaChamada.toString();
	}    
}
