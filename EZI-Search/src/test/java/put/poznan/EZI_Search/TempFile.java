package put.poznan.EZI_Search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class TempFile
{

    private File file;

    public TempFile()
    {
        String tempDirPath = System.getProperty( "java.io.tmpdir" );
        File tempDir = new File( tempDirPath );
        if ( !tempDir.exists() )
            throw new RuntimeException( "Katalog tymczasowy nie istnieje" );
        try
        {
            file = File.createTempFile( "tmp", "tmp", tempDir );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Błąd podczas tworzenia pliku tymczasowego", e );
        }
    }

    public File getFile()
    {
        return file;
    }

    public InputStream getInputStream()
    {
        InputStream fis;
        try
        {
            fis = new FileInputStream( file );
        }
        catch ( FileNotFoundException e )
        {
            throw new RuntimeException( "Nie można utworzyć strumienia z pliku: " + file.getAbsolutePath(), e );
        }
        return fis;
    }

    public void delete()
    {
        file.delete();
    }

}
