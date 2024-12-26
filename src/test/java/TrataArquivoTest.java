import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import com.print.controler.business.TrataArquivo;

public class TrataArquivoTest
{

    private TrataArquivo trataArquivo;
    private final String testFilePath = "testFile.txt";

    @Before
    public void setUp() 
    {
        trataArquivo = new TrataArquivo();
    }

    @After
    public void clear()
    {
        File file = new File(testFilePath);

        if (file.exists())
            file.delete();
    }

    @Test
    public void testSalvaTxt() throws IOException 
    {
        List<Integer> data = Arrays.asList(100, 200, 351);
        trataArquivo.salvaTxt(data, testFilePath);

        File file = new File(testFilePath);
        assertTrue(file.exists());

        List<Integer> lines = Files.readAllLines(Paths.get(testFilePath))
                                   .stream()
                                   .map(Integer::parseInt)
                                   .toList();

        assertEquals(data, lines);
    }

    @Test
    public void testCarregaArquivo() throws IOException 
    {
        List<Integer> data = Arrays.asList(100, 200, 351);

        Files.write(Paths.get(testFilePath), data.stream().map(String::valueOf).toList());

        List<Integer> lines = trataArquivo.carregaArquivo();

        assertEquals(data, lines);
    }

    // Adicione mais testes para outros métodos de TrataArquivo aqui
}