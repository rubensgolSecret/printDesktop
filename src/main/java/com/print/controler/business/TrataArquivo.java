package com.print.controler.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.print.controler.interfaces.ITrataArquivo;
import com.print.util.Util;

public class TrataArquivo implements ITrataArquivo
{
    private static final Logger logger = Logger.getLogger(TrataArquivo.class.getName());

    public TrataArquivo()
    {
        setupLogger();
    }

    private void setupLogger()
     {
        try 
        {
            FileHandler fh = new FileHandler("log/log-" + Util.getDataFormatadaSemBarra() + "-trata_arquivos.log");
            fh.setEncoding("UTF-8");
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
            fh.setFormatter(new SimpleFormatter());
        } 
        catch (IOException | SecurityException e)
        {
            logger.log(Level.SEVERE, "Error setting up logger: {0}", e.getMessage());
        }
    }

    @Override
    public void salvaTxt(List<Integer> nfLidas, String path)
    {
        if (path == null)
            path = "separacao/" + Util.getDataFormatadaSemBarra() + ".txt";

        try (FileWriter myWriter = new FileWriter(path))
        {
            logger.info("Salvando arquivo com as Separacoes lidas");
            
            for (Integer integer : nfLidas) 
            {
                logger.log(Level.INFO, "Salvando a nf: {0}", integer);
                myWriter.write(integer.toString() + "\n");
            }
        } catch (IOException e)
         {
            logger.log(Level.SEVERE, "Erro ao salvar separacao {0}", e.getMessage());
        }
    }

    @Override
    public List<Integer> carregaArquivo()
    {
        List<Integer> nfs = new ArrayList<>();
        String fileName = Util.getDataFormatadaSemBarra();
        File myObj = new File("separacao/" + fileName + ".txt");

        if (myObj.exists()) 
            readFile(nfs, myObj);
        else 
            createNewFile(myObj);

        return nfs;
    }

    private void readFile(List<Integer> nfs, File myObj) 
    {
        try (Scanner myReader = new Scanner(myObj))
        {
            while (myReader.hasNextLine())
            {
                String data = myReader.nextLine();
                nfs.add(Integer.valueOf(data));
                logger.log(Level.INFO, "Carregado Separacoes: {0}", data);
            }
        } 
        catch (FileNotFoundException e)
        {
            logger.log(Level.SEVERE, "Erro salvar arquivo separacao {0}", e.getMessage());
        }
    }

    private void createNewFile(File myObj) 
    {
        try 
        {
            myObj.createNewFile();
        } 
        catch (IOException e) 
        {
            logger.severe(e.getMessage());
        }
    }

    public static File extraiArquivo(URI uri) throws IOException, DocumentException
    {
        File pastaFile = new File(FileUtils.getTempDirectoryPath() + File.separator + "pastaEtiqueta");
        FileUtils.copyURLToFile(uri.toURL(), pastaFile);

        File arquivoTxt = extractFilesFromZip(pastaFile);

        return arquivoTxt != null ? txtToPdf(arquivoTxt) : null;
    }

    private static File extractFilesFromZip(File pastaFile) throws IOException 
    {
        File arquivoTxt = null;

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(pastaFile.getPath()))) 
        {
            ZipEntry entry;
            byte[] buffer = new byte[1024];

            while ((entry = zis.getNextEntry()) != null) 
            {
                arquivoTxt = new File(FileUtils.getTempDirectoryPath() + File.separator + entry.getName());
                if (entry.isDirectory()) 
                    arquivoTxt.mkdirs();
                else 
                {
                    new File(arquivoTxt.getParent()).mkdirs();
                    
                    try (FileOutputStream fos = new FileOutputStream(arquivoTxt)) 
                    {
                        int length;
                        while ((length = zis.read(buffer)) > 0) 
                            fos.write(buffer, 0, length);
                    }
                }
            }
        }

        return arquivoTxt;
    }


    public static File converteZplToPdf(URI uri) throws IOException, DocumentException
    {
        File zplFile = new File(FileUtils.getTempDirectoryPath() + "tmp.zpl");
        FileUtils.copyURLToFile(uri.toURL(), zplFile);

        return txtToPdf(zplFile);
    }

    public static File txtToPdf(File file) throws IOException, DocumentException
    {
        Document pdfDoc = new Document(PageSize.A4);
        PdfWriter.getInstance(pdfDoc, new FileOutputStream("txt.pdf"))
                 .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        pdfDoc.open();

        Font myfont = new Font();
        myfont.setStyle(Font.NORMAL);
        myfont.setSize(11);
        pdfDoc.add(new Paragraph("\n"));

        try (BufferedReader br = new BufferedReader(new FileReader(file))) 
        {
            String strLine;
            while ((strLine = br.readLine()) != null)
            {
                Paragraph para = new Paragraph(strLine + "\n", myfont);
                para.setAlignment(Element.ALIGN_JUSTIFIED);
                pdfDoc.add(para);
            }
        }

        pdfDoc.close();

        return new File("txt.pdf");
    }
}
