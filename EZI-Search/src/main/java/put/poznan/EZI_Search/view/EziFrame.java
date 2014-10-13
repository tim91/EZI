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

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import put.poznan.EZI_Search.model.Document;

public class EziFrame
    extends JFrame
{

    private static final long serialVersionUID = 1L;

    JTextField documentFilePathField;

    JTextField termFilePathField;

    JTextField queryField;

    DefaultListModel<Document> results = new DefaultListModel<Document>();

    public EziFrame()
    {
        configure();

        JPanel form = buildForm();
        JLabel docLabel = new JLabel( "Plik z dokumentami:" );
        JLabel termLabel = new JLabel( "Plik z termami:" );
        JLabel queryLabel = new JLabel( "Zapytanie:" );

        documentFilePathField = new JTextField();
        documentFilePathField.setEditable( false );
        termFilePathField = new JTextField();
        termFilePathField.setEditable( false );
        JTextField queryField = new JTextField();

        form.add( docLabel );
        form.add( documentFilePathField );
        form.add( buildFileButton( documentFilePathField ) );

        form.add( termLabel );// second row
        form.add( termFilePathField );
        form.add( buildFileButton( termFilePathField ) );

        form.add( queryLabel );
        form.add( queryField );
        form.add( buildRunButton() );
        add( form );
        add( buildList() );
        setVisible( true );
    }

    private Component buildList()
    {
        JList<Document> list = new JList<Document>( results );
        list.setCellRenderer( new DocumentRenderer() );
        return new JScrollPane( list );
    }

    private JPanel buildForm()
    {
        JPanel p = new JPanel();
        GridLayout layout = new GridLayout( 0, 3 );
        layout.setHgap( 10 );
        layout.setVgap( 10 );
        p.setLayout( layout );
        return p;
    }

    private Component buildRunButton()
    {
        JButton btn = new JButton( "Szukaj" );
        btn.addActionListener( new ActionListener()
        {

            @Override
            public void actionPerformed( ActionEvent e )
            {
                try
                {
                    if ( validateFiles() )
                        run( new FileInputStream( documentFilePathField.getText() ), new FileInputStream( termFilePathField.getText() ) );
                    run( null, null );
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
        setSize( 700, 250 );
        setTitle( "Ezi" );
        setLocationRelativeTo( null );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        GridLayout layout = new GridLayout( 0, 1 );
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

    private boolean validateFiles()
    {
        if ( checkFileExists( documentFilePathField.getText() ) )
            return false;
        if ( checkFileExists( termFilePathField.getText() ) )
            return false;
        return true;
    }

    private boolean checkFileExists( String path )
    {
        return Files.exists( Paths.get( path ) );
    }

    private void run( InputStream docIs, InputStream termIs )
    {
        // TODO

        // Dodanie dokumnetu do listy
        // Document d = new Document();
        // d.setTitile( "Title" );
        // d.setContent( "Content" );
        // results.addElement( d );
    }
}
