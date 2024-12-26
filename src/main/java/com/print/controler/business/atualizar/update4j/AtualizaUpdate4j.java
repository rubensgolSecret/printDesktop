package com.print.controler.business.atualizar.update4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.update4j.Archive;
import org.update4j.Configuration;
import org.update4j.UpdateOptions;
import org.update4j.UpdateOptions.ArchiveUpdateOptions;
import org.update4j.service.UpdateHandler;

import com.print.controler.interfaces.IAtualiza;

public class AtualizaUpdate4j implements IAtualiza, UpdateHandler
{
    private final Configuration config;

    public AtualizaUpdate4j(Configuration config)
    {
        this.config = config;
    }

    @Override
    public void atualiza() 
    {
        Path zip = Paths.get("print-update.zip");
        ArchiveUpdateOptions arqv;

        try 
        {
            arqv = UpdateOptions.archive(zip).updateHandler(AtualizaUpdate4j.this);

            if(config.update(arqv).getException() == null)
                Archive.read(zip).install();

            config.launch();
        }
        catch(IOException e)
        {
            Logger.getLogger(AtualizaUpdate4j.class.getName()).log(Level.SEVERE, null, e);
		}
    }

    @Override
    public boolean temAtualizacao()  
    {
        try 
        {
            return config.requiresUpdate();
        }
        catch (IOException e) 
        {
            Logger.getLogger(AtualizaUpdate4j.class.getName()).log(Level.SEVERE, null, e);
        }

        return false;
    }
}
