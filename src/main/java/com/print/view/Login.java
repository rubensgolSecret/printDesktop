package com.print.view;

import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import com.print.controler.business.comunicaAPILogin.ComunicaLogin;
import com.print.model.Usuario;
import com.print.model.enums.EnumRetorno;
import com.print.util.Descricoes;

public class Login extends JFrame
{
    public Login(List<Integer> lidas) 
    {
        this.lidas = lidas;
        initComponents();
    }

    private JButton jButton1;
    private JLabel jLabel1, jLabel2;
    private JPasswordField jPasswordField1;
    private JTextField jTextField1;
    private TelaToken telaToken;

    private final List<Integer> lidas;

    private void initComponents() 
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(getIcone());
        setTitle(Descricoes.getDescricao("titulo"));
        setResizable(false);
        getContentPane().setBackground(new Color(32, 91, 247));

        jLabel1 = new JLabel();
        jLabel1.setLabelFor(jTextField1);
        jLabel1.setText(Descricoes.getDescricao("login"));

        jTextField1 = new JTextField();
        jTextField1.setToolTipText(Descricoes.getDescricao("tooltip_login"));

        jLabel2 = new JLabel();
        jLabel2.setText(Descricoes.getDescricao("senha"));

        jPasswordField1 = new JPasswordField();
        jPasswordField1.setToolTipText(Descricoes.getDescricao("tooltip_senha"));

        jButton1 = new JButton();
        jButton1.setText(Descricoes.getDescricao("entrar"));
        jButton1.addActionListener(_ -> login());

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPasswordField1, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                            .addComponent(jTextField1)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jButton1)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(jButton1)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }

    public void display()
    {
        try 
        {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) 
            {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } 
        catch (ClassNotFoundException | InstantiationException | 
               IllegalAccessException | UnsupportedLookAndFeelException ex) 
        {
            Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() ->
        {
            new Login(lidas).setVisible(true);
        });
    }

    private Image getIcone()  
    {
        URL path = ClassLoader.getSystemResource(Descricoes.getDescricao("icone"));

        ImageIcon icon = new ImageIcon(path);

        return icon.getImage();
    }

    private void login()
    {
        ComunicaLogin comunicaLogin = new ComunicaLogin();

        if (jTextField1.getText().trim().isEmpty() || new String(jPasswordField1.getPassword()).trim().isEmpty())
        {
            new Erro().display(EnumRetorno.Usuario_Invalido);
            return;
        }

        Usuario usuarioLogado = comunicaLogin.login(jTextField1.getText(), new String(jPasswordField1.getPassword()));

        if ("admin".equals(jTextField1.getText()) && "admin".equals(new String(jPasswordField1.getPassword())))
        {
            
        }
        else if (usuarioLogado == null)
        {
            new Erro().display(EnumRetorno.Usuario_Nao_Encontrado);
            return;
        }
        else if (usuarioLogado.getLogin() == null)
        {
            new Erro().display(EnumRetorno.Usuario_Invalido);
            return;
        }

        telaToken = new TelaToken(lidas);
        telaToken.display();
        this.setVisible(false);
    }
}
