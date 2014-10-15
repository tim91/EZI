package put.poznan.EZI_Search.view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
import put.poznan.EZI_Search.model.Query;
import put.poznan.EZI_Search.model.SearchReport;
import put.poznan.EZI_Search.tfidf.TFIDFSol;

public class EziFrame
    extends JFrame
{

    private static final long serialVersionUID = 1L;

    JTextField documentFilePathField;

    JTextField termFilePathField;

    JTextField queryField;

    DefaultListModel<String> results = new DefaultListModel<String>();

    
    public static void main(String[] args) {
    	new EziFrame();
	}
    
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
        queryField = new JTextField();

        form.add( docLabel );
        form.add( documentFilePathField );
        form.add( buildFileButton( documentFilePathField ) );

        form.add( termLabel );// second row
        form.add( termFilePathField );
        form.add( buildFileButton( termFilePathField ) );

        form.add( queryLabel );
        form.add( queryField );
        form.add( buildRunButton() );
        form.add( buildKeysButton() );
        form.add( buildDocsButton() );
        add( form );
        add( buildList() );
        setVisible( true );
    }

    private Component buildKeysButton()
    {
        JButton btn = new JButton( "Pokaż klucze" );
        btn.addActionListener( new ActionListener()
        {

            public void actionPerformed( ActionEvent e )
            {
                results.clear();
                for(String key:TFIDFSol.getInstance().getKeywords()){
                    results.addElement( key );
                }
                
            }

        } );
        return btn;
    }
    private Component buildDocsButton()
    {
        JButton btn = new JButton( "Pokaż dokumenty" );
        btn.addActionListener( new ActionListener()
        {

            public void actionPerformed( ActionEvent e )
            {
                results.clear();
                for(Document d:TFIDFSol.getInstance().getDb().values()){
                    results.addElement( d.getTitile()+" "+d.getContent() );
                }
                
            }

        } );
        return btn;
    }

    private Component buildList()
    {
        JList<String> list = new JList<String>( results );
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

            public void actionPerformed( ActionEvent e )
            {
//				    run( queryField.getText(), documentFilePathField.getText(),
//				         termFilePathField.getText() );
                	run( queryField.getText(), "./documents/docs2.txt",
                			"./keywords/keyw2.txt" );
            }

        } );
        return btn;
    }

    private void configure()
    {
        setSize( 700, 350 );
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


    private void run( String query, String docIs, String termIs )
    {
    	TFIDFSol sol = TFIDFSol.getInstance();
    	sol.setDocumentsFile(docIs);
    	sol.setKeywordsFile(termIs);
    	sol.setQuery(new Query(query));
    	
    	SearchReport report = sol.search();
    	report.printReport();
    	
    	results.clear();
    	for(String doc:report.getReport().split( "\n" )){
            results.addElement( doc );   
    	}
    }
}
