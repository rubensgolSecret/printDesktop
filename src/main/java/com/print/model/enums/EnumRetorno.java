package com.print.model.enums;

public enum EnumRetorno
{
	SUCESSO("Sucesso","Sucesso!"), 
	SUCESSO_EM_BRANCO("Sucesso","Sucesso sem registro!"), 
	ERRO_TOKEN("Token Inválido","Token Inválido!"), 
	ERRO_HEADER("Parametro inválido","Algum parametro foi passado de forma incorreta!"), 
	ERROR_404("Problema de conexão","Problema de conex�o com o servidor!"), 
	ERRO_EM_BRANCO("TOKEN EM BRANCO","TOKEN NÃO PODE FICAR EM BRANCO!"),
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
