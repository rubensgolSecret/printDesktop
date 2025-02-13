package com.print.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import com.print.controler.business.TrataArquivo;
import com.print.controler.business.comunicaTiny.Comunica;
import com.print.controler.business.imprimir.ImprimirDesktop;
import com.print.controler.interfaces.IImprimir;
import com.print.controler.interfaces.ITrataArquivo;
import com.print.model.LinkEtiqueta;
import com.print.model.enums.EnumRetorno;
import com.print.util.Descricoes;

public class TelaToken extends JFrame 
{
    private JButton bIniciar, bParar;
    private JLabel lToken;
    private JTextField tToken;

   	private final Comunica comunica;
	private final IImprimir imprimir;
	private List<LinkEtiqueta> links;
	private final ITrataArquivo aTrataArquivo;

    private boolean buscando = false;
    private final List<Integer> lidas;
    private Runnable tBuscaRunnable;
    private Thread tBusca;

    private final Erro telaErro;

    public TelaToken(List<Integer> lidas) 
    {
        telaErro = new Erro();
        comunica = new Comunica(lidas);
		imprimir = new ImprimirDesktop();
		aTrataArquivo = new TrataArquivo();
        this.lidas = lidas;

        initComponents();
    }

    private void initComponents() 
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(Descricoes.getDescricao("titulo"));
        setIconImage(getIcone());
        getContentPane().setBackground(new Color(32, 91, 247));
        setResizable(false);
        setLocationRelativeTo(null);

        lToken = new JLabel();
        lToken.setLabelFor(tToken);
        lToken.setText(Descricoes.getDescricao("token"));
        lToken.setToolTipText(Descricoes.getDescricao("tooltipToken"));

        tToken = new JTextField();

        bIniciar = new JButton();
        bIniciar.setText(Descricoes.getDescricao("inicar"));
        bIniciar.addActionListener(_ -> iniciarBusca());

        bParar = new JButton();
        bParar.setText(Descricoes.getDescricao("parar"));
        bParar.addActionListener(_ ->
        {
            buscando = false;
            setBotoes(buscando);
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lToken)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tToken, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bIniciar)
                .addComponent(bParar)
                .addGap(129, 129, 129))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lToken)
                    .addComponent(tToken, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(bIniciar)
                    .addComponent(bParar))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }

    public void display()
    {
        try
        {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) 
            {
                if ("Nimbus".equals(info.getName())) 
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } 
        catch (ClassNotFoundException | InstantiationException | 
               IllegalAccessException | UnsupportedLookAndFeelException ex)
        {
            Logger.getLogger(TelaToken.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> new TelaToken(lidas).setVisible(true));
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

            buscando = retorno == EnumRetorno.SUCESSO || retorno == EnumRetorno.SUCESSO_EM_BRANCO;

            setBotoes(buscando);

            if (buscando)
            {
                EventQueue.invokeLater(() ->
                {
                    try 
                    {
                        buscar(tToken.getText());
                    }
                    catch (InterruptedException | URISyntaxException | IOException ex) 
                    {
                        Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        } 
        catch (Exception e) 
        {
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, e);
        }
	}

    private void buscar(String token) throws InterruptedException, URISyntaxException, IOException 
    {
        tBuscaRunnable = () -> 
        {
            try 
            {
                while (buscando) 
                {
                    setBotoes(buscando);
                    links = comunica.getEtiquetas(token);
                    
                    for (LinkEtiqueta link : links)
                        imprimir.imprimir(link.getLink());
        
                    aTrataArquivo.salvaTxt(comunica.getSeparacoesLidas(), null);
        
                    TimeUnit.MILLISECONDS.sleep(5000);
                }
            } 
            catch (InterruptedException | URISyntaxException | IOException e) 
            {
                Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, e);
            }
        };

        tBusca = new Thread(tBuscaRunnable);
        tBusca.start();
    }

    private boolean isTokenInvalido() 
	{
	    return tToken.getText() == null || tToken.getText().trim().isEmpty();
	}

    private Image getIcone()  
    {
        ImageIcon icon = new ImageIcon(Descricoes.getDescricao("icone"));

        return icon.getImage();
    }

    private void setBotoes(boolean buscando)
	{
		tToken.setEnabled(! buscando);
		bIniciar.setEnabled(! buscando);
		bParar.setEnabled(buscando);
	}
}
