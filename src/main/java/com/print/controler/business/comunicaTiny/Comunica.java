package com.print.controler.business.comunicaTiny;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.print.model.Expedica;
import com.print.model.LinkEtiqueta;
import com.print.model.RetornoEtiqueta;
import com.print.model.RetornoSeparacao;
import com.print.model.Separacao;
import com.print.model.enums.EnumRetorno;
import com.print.util.UrlsTiny;
import com.print.util.Util;

public class Comunica
{
	private static final Logger logger = Logger.getLogger(Comunica.class.getName());

	private final List<Integer> separacoesLida;

	public Comunica(List<Integer> separacoesLida)
	{
		try 
		{
			FileHandler fh = new FileHandler("resources/log/log-" + Util.getDataFormatadaSemBarra() + "-comunica.log");
			fh.setEncoding("UTF-8");
			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
			fh.setFormatter(new SimpleFormatter());	
		} 
		catch (IOException | SecurityException e) 
		{
			logger.log(Level.SEVERE, "erro{0}", e.getMessage());
		}

		this.separacoesLida = separacoesLida;
	}

	public List<LinkEtiqueta> getEtiquetas(String token) throws IOException, URISyntaxException
	{
		List<LinkEtiqueta> links = new ArrayList<>();
		List<Separacao> separacoes = getSeparacaos(token);
	
		if (separacoes != null) 
		{
			for (Separacao separacao : separacoes) 
				processarSeparacao(token, links, separacao);
		}
	
		return links;
	}
	
	private void processarSeparacao(String token, List<LinkEtiqueta> links, Separacao separacao) throws IOException, URISyntaxException 
	{
		Expedica expedicao = obterExpedicao(token, separacao);
	
		if (expedicao != null && expedicao.getRetorno() != null && expedicao.getRetorno().getExpedicao() != null) 
		{
			RetornoEtiqueta retornoEtiqueta = getEtiqueta(token, expedicao.getRetorno().getExpedicao().getId());
	
			if (retornoEtiqueta != null && retornoEtiqueta.getRetorno() != null)
				adicionarLinksEtiqueta(links, separacao, retornoEtiqueta.getRetorno());
		}
	}
	
	private Expedica obterExpedicao(String token, Separacao separacao) throws IOException, URISyntaxException
	{
		if (separacao.getIdOrigemVinc() > 0) 
			return getExpedicao(token, separacao.getIdOrigemVinc(), separacao.getObjOrigemVinc());

		return null;
	}
	
	private void adicionarLinksEtiqueta(List<LinkEtiqueta> links, Separacao separacao, RetornoEtiqueta retornoEtiqueta)
	{
		if (retornoEtiqueta.getLinks() != null) 
		{
			for (LinkEtiqueta link : retornoEtiqueta.getLinks())
			{
				links.add(link);
				separacoesLida.add(separacao.getId());
			}
		}
	}
	
	private RetornoEtiqueta getEtiqueta(String token, int idExpedi) throws IOException, URISyntaxException
	{
		logger.info("Buscando etiqueta");
		Gson gson = new Gson();
		URI url = new URI(UrlsTiny.getEtiqueta(token, idExpedi));
		HttpURLConnection conexao = (HttpURLConnection) url.toURL().openConnection();

		if (conexao.getResponseCode() != 200)
		{
			logger.log(Level.INFO, "Problema de conexão: {0}", conexao.getResponseMessage());
			return null;
		}

		try (InputStreamReader reader = new InputStreamReader(conexao.getInputStream())) 
		{
			return gson.fromJson(reader, RetornoEtiqueta.class);
		}
	}
	
	private Expedica getExpedicao(String token, int idNota, String tipoObjeto) throws IOException, URISyntaxException
	{
		logger.info("Buscando expedicao");
		URI url = new URI(UrlsTiny.getExpedicao(token, idNota, tipoObjeto));
		HttpURLConnection conexao = (HttpURLConnection) url.toURL().openConnection();

		if (conexao.getResponseCode() != 200)
		{
			logger.severe("problema de conexao");
			return null;
		}

		BufferedReader resposta = new BufferedReader(new InputStreamReader((conexao.getInputStream())));
		String jsonEmString = Util.converteJsonEmString(resposta);

		logger.info("Busca de expedicao concluida");

		Gson gson = new Gson();
		Expedica retorno = gson.fromJson(jsonEmString, Expedica.class);

	   return retorno;
	}
	
	private List<Separacao> getSeparacaos(String token) throws IOException, URISyntaxException
	{
		RetornoSeparacao retornoSepara = getSeparacao(token,1);
		List<Separacao> separacaos = null;
		int numeroPag;

		if (retornoSepara != null && retornoSepara.getRetorno() != null)
		{
			retornoSepara = retornoSepara.getRetorno();
			numeroPag = retornoSepara.getNumeroPaginas();

			if (retornoSepara.getStatusProcessamento() == 3 && retornoSepara.getCodigoErro() == 0)
			{
				separacaos = new ArrayList<>();

				if (numeroPag > 1)
				{
					for (Separacao separacao : retornoSepara.getSeparacoes()) 
					{
						if (! Util.comaparaData(separacao.getDataCheckOut()) )
							continue;

						if (separacoesLida.contains(separacao.getId()))
							continue;

						separacaos.add(separacao);
					}

					for (int i = 2; i <= numeroPag; i++) 
					{
						retornoSepara =	getSeparacao(token, i);
						
						if (retornoSepara != null && retornoSepara.getRetorno() != null)
						{
							retornoSepara = retornoSepara.getRetorno();

							if (retornoSepara.getStatusProcessamento() == 3 && retornoSepara.getCodigoErro() == 0)
							{
								for (Separacao separacao : retornoSepara.getSeparacoes()) 
								{
									if (! Util.comaparaData(separacao.getDataCheckOut()) )
										continue;
			
									if (separacoesLida.contains(separacao.getId()))
										continue;
			
									separacaos.add(separacao);
								}
							}
						}
						
					}
				}
				else if (retornoSepara.getNumeroPaginas() == 1)
				{
					for (Separacao separacao : retornoSepara.getSeparacoes()) 
					{
						if (! Util.comaparaData(separacao.getDataCheckOut()) )
							continue;

						if (separacoesLida.contains(separacao.getId()))
							continue;

						separacaos.add(separacao);
					}
				}
			}
		}

		return separacaos;
	}

	private RetornoSeparacao getSeparacao(String token, int nummeroPag) throws IOException, URISyntaxException
	{
		logger.info("Buscando separacao");
        URI url = new URI(UrlsTiny.getSeparacao(token, nummeroPag));

        HttpURLConnection conexao = (HttpURLConnection) url.toURL().openConnection();

        if (conexao.getResponseCode() != 200)
		{
			logger.severe("problema de conexao");
			return null;
		}

        BufferedReader resposta = new BufferedReader(new InputStreamReader((conexao.getInputStream())));
        String jsonEmString = Util.converteJsonEmString(resposta);

		logger.info("busca de separacao concluida");

        Gson gson = new Gson();
        RetornoSeparacao retorno = gson.fromJson(jsonEmString, RetornoSeparacao.class);

       return retorno;
	}
	
    public EnumRetorno verificaConexao(String token) throws Exception
    {
		logger.info("verificando conexao");
    	RetornoSeparacao retorno = getSeparacao(token, 1);
    	
    	if (retorno == null || retorno.getRetorno() == null)
    		return EnumRetorno.ERROR_404;

		logger.log(Level.INFO, "Retorno da busca{0}", retorno.getStatus());
	
    	retorno = retorno.getRetorno();
    	
    	if (retorno.getStatusProcessamento() == 2 && retorno.getCodigoErro() == 31)
    		return EnumRetorno.ERRO_TOKEN;
    	else if (retorno.getStatusProcessamento() == 3)
    		return EnumRetorno.SUCESSO;
		else if (retorno.getStatusProcessamento() == 1 && retorno.getCodigoErro() == 32)
			return EnumRetorno.SUCESSO_EM_BRANCO;
    	else
    		return EnumRetorno.ERRO_HEADER;
    }

	public List<Integer> getSeparacoesLidas()
	{
		return separacoesLida;
	}
}
