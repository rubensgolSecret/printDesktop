package com.print.test;

import java.io.IOException;

import com.print.geraConfig.CriaConfig;

public class geraConfigExe 
{
    public static void  main (String[] args)
    {
        CriaConfig gConfig = new CriaConfig();
        try
        {
            gConfig.geraConfig();
        }
        catch (IOException e) 
        {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }    
}
