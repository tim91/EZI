package put.poznan.EZI_Search.view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class EziFrame
    extends JFrame
{

    private static final long serialVersionUID = 1L;

    JTextField documentFilePathField;

    JTextField termFilePathField;

    JTextField queryField;

    public EziFrame()
    {
        configure();
        JLabel docLabel = new JLabel( "Plik z dokumentami:" );
        JLabel termLabel = new JLabel( "Plik z termami:" );
        JLabel queryLabel = new JLabel( "Zapytanie:" );

        documentFilePathField = new JTextField();
        documentFilePathField.setEditable( false );
        termFilePathField = new JTextField();
        termFilePathField.setEditable( false );
        JTextField queryField = new JTextField();

        add( docLabel );
        add( documentFilePathField );
        add( buildFileButton( documentFilePathField ) );

        add( termLabel );// second row
        add( termFilePathField );
        add( buildFileButton( termFilePathField ) );

        add( queryLabel );
        add( queryField );
        add( buildRunButton() );
        setVisible( true );
    }

    private Component buildRunButton()
    {
        JButton btn = new JButton( "Szukaj" );
        btn.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                validateFiles();
                try
                {
                    run( new FileInputStream( documentFilePathField.getText() ), new FileInputStream( termFilePathField.getText() ) );
                }
                catch ( FileNotFoundException ex )
                {
                    throw new RuntimeException( ex );
                }
            }

        } );
        return btn;
    }

    private void configure()
    {
        setSize( 700, 150 );
        setTitle( "Ezi" );
        setLocationRelativeTo( null );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        GridLayout layout = new GridLayout( 0, 3 );
        layout.setHgap( 10 );
        layout.setVgap( 10 );
        setLayout( layout );
    }

    private JButton buildFileButton( final JTextField textField )
    {
        final JFrame frame = this;
        JButton btn = new JButton( "Przeglądaj..." );
        btn.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog( frame );
                if ( result == JFileChooser.APPROVE_OPTION )
                {
                    File selectedFile = fileChooser.getSelectedFile();
                    textField.setText( selectedFile.getAbsolutePath() );
                }
            }

        } );
        return btn;
    }

    private void validateFiles()
    {
        checkFileExists( documentFilePathField.getText() );
        checkFileExists( termFilePathField.getText() );
    }

    private void checkFileExists( String path )
    {
        if ( !Files.exists( Paths.get( path ) ) )
            throw new RuntimeException( "File not found: " + path );
    }

    private void run( InputStream docIs, InputStream termIs )
    {
        // TODO
    }
}
