package com.print.model.enums;

import com.print.util.Descricoes;

public enum EnumRetorno
{
	SUCESSO(Descricoes.getDescricao("sucesso"), Descricoes.getDescricao("sucesso_descricao")), 
	SUCESSO_EM_BRANCO(Descricoes.getDescricao("sucesso"), Descricoes.getDescricao("sucesso_descricao")), 
	ERRO_TOKEN(Descricoes.getDescricao("erro_token"), Descricoes.getDescricao("erro_token_descricao")), 
	ERRO_HEADER(Descricoes.getDescricao("erro_header"), Descricoes.getDescricao("erro_header_descricao")), 
	ERROR_404(Descricoes.getDescricao("erro_404"), Descricoes.getDescricao("erro_404_descricao")), 
	ERRO_EM_BRANCO(Descricoes.getDescricao("erro_em_branco"), Descricoes.getDescricao("erro_em_branco_descricao")),
	APLICATIVO_JA_ATUALIZADO(Descricoes.getDescricao("aplica_ja_atualizado"), Descricoes.getDescricao("aplica_ja_atualizado_descricao")),
	ERRO_ATUALIZACAO(Descricoes.getDescricao("erro_atualizacao"), Descricoes.getDescricao("erro_atualizacao_descricao")),
	Usuario_Nao_Encontrado(Descricoes.getDescricao("usuario_nao_encontrado"), Descricoes.getDescricao("usuario_nao_encontrado_descricao")),
	Usuario_Invalido(Descricoes.getDescricao("usuario_invalido"), Descricoes.getDescricao("usuario_invalido_descricao")),;
	
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
