package com.print.view;

import javax.swing.JOptionPane;

import com.print.model.enums.EnumRetorno;

public  class Erro extends JOptionPane
{
	private static final long serialVersionUID = -8627812068520409197L;
	
	public void display(EnumRetorno retorno)
	{
		showMessageDialog(null, retorno.getDescricao(), 
        		retorno.getTitulo(), JOptionPane.WARNING_MESSAGE);
	}
}
