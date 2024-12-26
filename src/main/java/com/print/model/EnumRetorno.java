package com.print.model;

public enum EnumRetorno
{
	SUCESSO("Sucesso","Sucesso!"), 
	SUCESSO_EM_BRANCO("Sucesso","Sucesso sem registro!"), 
	ERRO_TOKEN("Token Invalido","Token Invalido!"), 
	ERRO_HEADER("Parametro invalido","Algum parametro foi passado de forma incorreta!"), 
	ERROR_404("Problema de conexão","Problema de conex�o com o servidor!"), 
	ERRO_EM_BRANCO("TOKEN EM BRANCO","TOKEN N�O PODE FICAR EM BRANCO!"),
	APLICATIVO_JA_ATUALIZADO("Aplicativo já atualizado","Aplicativo atualizado");
	
	private EnumRetorno(String descricao, String titulo) 
	{
		this.descricao = descricao;
		this.titulo = titulo;
	}
	
	private final String descricao, titulo;
	
	public String getDescricao()
	{
		return this.descricao;
	}
	
	public String getTitulo()
	{
		return this.titulo;
	}
}
