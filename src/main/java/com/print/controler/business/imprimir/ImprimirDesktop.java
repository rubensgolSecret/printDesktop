package com.print.controler.business.imprimir;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.itextpdf.text.DocumentException;
import com.print.controler.business.TrataArquivo;
import com.print.controler.interfaces.IImprimir;

public class ImprimirDesktop implements IImprimir
{
	private static final Logger logger = Logger.getLogger(ImprimirDesktop.class.getName());

	@Override
	public void imprimir(String path) 
	{
		try
		{
			File docFile;
			PDDocument document;
			URI uri = new URI(path);
			URL url = uri.toURL();

			if (path.endsWith(".zip"))
				docFile = TrataArquivo.extraiArquivo(uri);
			else
			{
				String nameFile = FileUtils.getTempDirectoryPath() + "tmp" + ".pdf";

				docFile = new File(nameFile);
				docFile.deleteOnExit();
				FileUtils.copyURLToFile(url, docFile);
			}
			
			logger.log(Level.INFO, "imprimindo arquivo: {0}", path);

			document = Loader.loadPDF(docFile);
			PrintJob.printFile(document);
		}
		catch (DocumentException | PrinterException | IOException | URISyntaxException e) 
        {
			logger.severe(e.getMessage());
        }
	}
}
