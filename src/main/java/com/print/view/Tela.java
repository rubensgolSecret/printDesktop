package com.print.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.print.controler.business.TrataArquivo;
import com.print.controler.business.comunicaTiny.Comunica;
import com.print.controler.business.imprimir.ImprimirDesktop;
import com.print.controler.interfaces.IAtualiza;
import com.print.controler.interfaces.IImprimir;
import com.print.controler.interfaces.ITrataArquivo;
import com.print.model.EnumRetorno;
import com.print.model.LinkEtiqueta;
import com.print.util.Atualizador;

public class Tela extends JFrame
{
	private static final long serialVersionUID = 7163765538988263870L;

	private JPanel panel, panelBotao;
	private JTextField tToken;
	private JLabel lToken;
	private JButton bIniciar, bParar, bAtualizar;
	private JProgressBar pBuscando;

	private Container c;
	private Comunica comunica;
	private IImprimir imprimir;
	private List<LinkEtiqueta> links;
	private boolean buscando = false;
	private ITrataArquivo aTrataArquivo;
	private IAtualiza atualiza;

	private TelaErro telaErro;

	public Tela(List<Integer> lidas)
	{
		try 
		{
			c = getContentPane();
			comunica = new Comunica(lidas);
			imprimir = new ImprimirDesktop();
			aTrataArquivo = new TrataArquivo();
			atualiza = new Atualizador();
			telaErro = new TelaErro();
 
            initializeComponents();
		} 
		catch (Exception e) 
		{
		  Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private void initializeComponents()
	{
		panel = new JPanel(new FlowLayout());
		panel.setSize(new Dimension(300,300));

		lToken = new JLabel("Digite o Token:");
		panel.add(lToken);

		panelBotao = new JPanel(new FlowLayout());

	    tToken = new JTextField(20);
	    panel.add(tToken);
	    c.add(panel, BorderLayout.NORTH);

	    bIniciar = new JButton("Iniciar");
	    bIniciar.setVisible(true);
	    bIniciar.addActionListener(e -> iniciarBusca());
	    panelBotao.add(bIniciar);

	    bParar = new JButton("Parar");
	    bParar.addActionListener(e -> {
	        buscando = false;
	        setBotoes(buscando);
	        aTrataArquivo.salvaTxt(comunica.getSeparacoesLidas(), null);
	    });
	    bParar.setEnabled(buscando);
	    panelBotao.add(bParar);

	    bAtualizar = new JButton("Atualizar");
	    bAtualizar.addActionListener(e -> atualizarAplicativo());
	    panelBotao.add(bAtualizar);

	    c.add(panelBotao, BorderLayout.CENTER);

	    pBuscando = new JProgressBar();
	    c.add(pBuscando, BorderLayout.SOUTH);
	}

	private void iniciarBusca()
	{
	    buscando = true;
	    setBotoes(buscando);

	    if (isTokenInvalido())
		{
	        telaErro.display(EnumRetorno.ERRO_EM_BRANCO);
	        buscando = false;
	        setBotoes(buscando);
	        return;
	    }

	    SwingUtilities.invokeLater(() -> 
		{
	        try 
			{
				EnumRetorno retorno = comunica.verificaConexao(tToken.getText());

				switch (retorno)
				{
					case ERROR_404 -> telaErro.display(EnumRetorno.ERROR_404);
					case ERRO_HEADER -> telaErro.display(EnumRetorno.ERRO_HEADER);
					case ERRO_TOKEN -> telaErro.display(EnumRetorno.ERRO_TOKEN);
					default -> {}
				}

	            while (buscando) 
				{
	                if (retorno == EnumRetorno.SUCESSO || retorno == EnumRetorno.SUCESSO_EM_BRANCO)
					{
	                    setBotoes(buscando);
	                    links = comunica.getEtiquetas(tToken.getText());
	                    
						for (LinkEtiqueta link : links)
	                        imprimir.imprimir(link.getLink());

	                    aTrataArquivo.salvaTxt(comunica.getSeparacoesLidas(), null);
	                }

	                TimeUnit.MILLISECONDS.sleep(5000);
	            }
	        } 
			catch (Exception e) 
			{
	            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, e);
	        }
	    });
	}

	private boolean isTokenInvalido() 
	{
	    return tToken.getText() == null || tToken.getText().trim().isEmpty();
	}

	private void atualizarAplicativo() 
	{
	    if (atualiza.verificarAtualizacao())
	        atualiza.atualiza();
		else 
	        telaErro.display(EnumRetorno.APLICATIVO_JA_ATUALIZADO);
	}

	public void display()
	{
		setBackground(new Color(0, 128, 255));
		setTitle("Imprimir etiquetas");
		setIconImage(getIcone());
		setSize(new Dimension(600,600));
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void setBotoes(boolean buscando)
	{
		tToken.setEnabled(! buscando);
		bIniciar.setEnabled(! buscando);
		bParar.setEnabled(buscando);
		pBuscando.setIndeterminate(buscando);
	}

	private Image getIcone()
	{
		ImageIcon icon = new ImageIcon("src/print_ico.png");

		return icon.getImage();
	}
}