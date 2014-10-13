package put.poznan.EZI_Search.view;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import put.poznan.EZI_Search.model.Document;

public class DocumentRenderer
    extends JPanel
    implements ListCellRenderer<Document>
{

    private static final long serialVersionUID = 1L;

    public Component getListCellRendererComponent( JList<? extends Document> list, Document value, int index, boolean isSelected, boolean cellHasFocus )
    {
        this.removeAll();
        setLayout( new GridLayout( 2, 1 ) );

        JLabel title = new JLabel( value.getTitile() );
        Font titleFont = new Font( "Verdana", Font.BOLD, 15 );
        title.setFont( titleFont );
        JLabel content = new JLabel( value.getContent() );
        add( title );
        add( content );
        return this;
    }

}
