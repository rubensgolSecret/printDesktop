package com.print.util;

public class UrlsAPI 
{
    public static String getSeparacao(String token, int numeroPag)
	{
    	StringBuilder urlParaChamada = new StringBuilder()
				.append("https://api.tiny.com.br/api2/separacao.pesquisa.php?");

    	urlParaChamada.append("token")
		  			  .append("=")
				  	  .append(token)
				  	  .append("&")
				  	  .append("formato")
				  	  .append("=")
				  	  .append("JSON")
				  	  .append("&")
				      .append("dataInicial")
				      .append("=")
				      .append(Util.getDataFormatadaMesAnterior())
				      .append("&")
				      .append("dataFinal")
				      .append("=")
				      .append(Util.getDataFormatada())
				      .append("&")
				      .append("pagina")
				      .append("=")
				      .append(numeroPag)
				      .append("&")
				      .append("situacao")
				      .append("=")
				      .append("3");

    	return urlParaChamada.toString();
	}    
}
