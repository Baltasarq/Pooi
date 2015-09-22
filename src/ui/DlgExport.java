package ui;

import core.AppInfo;
import core.ObjectBag;
import core.evaluables.ParentAttribute;
import core.Runtime;
import core.evaluables.Attribute;
import core.persistence.JsonPersistence;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Creates a dialog that allows to choose which objects to save as JSON.
 * @author baltasarq
 */
public class DlgExport extends javax.swing.JDialog {

    public static String fileName = "";
    public static final String JsonExt = JsonPersistence.JsonExt;

    /** Creates new dialog DlgExport */
    public DlgExport(Runtime rt, Frame parent, boolean modal)
    {
        super( parent, modal );
        this.rt = rt;
        
        this.build();
        this.setLocationRelativeTo( parent );

        this.loadList();

        if ( fileName.isEmpty() ) {
            DlgExport.fileName =  System.getProperty( "user.home" ) + File.separator + "pooi.json";
        }

        this.edFileName.setText( DlgExport.fileName );
        this.onSelectAll();
    }

    protected String prepareFileName(String fileName)
    {
        int pos = fileName.lastIndexOf( '.' );

        if ( pos > -1 ) {
            if ( fileName.substring( pos +1 ).compareTo( DlgExport.JsonExt ) != 0 )
            {
                fileName = fileName.substring( 0, pos +1 ) + DlgExport.JsonExt;
            }
        }
        else {
            fileName += '.' + DlgExport.JsonExt;
        }

        return fileName;
    }

    /** Selects all objects on the list */
    protected void onSelectAll()
    {
        this.lstObjects.setSelectionInterval(0, this.lstObjects.getModel().getSize());
    }

    /** Loads the list of objects */
    protected final void loadList()
    {
        final Attribute[] objects = rt.getRoot().getAttributes();
        DefaultListModel<String> listModel = new DefaultListModel<String>();

        for(Attribute attr : objects)
        {
            final ObjectBag obj = attr.getReference();

            if ( !( attr instanceof ParentAttribute ) ) {
                if ( !rt.isTypeObject( obj )
                  && !rt.isSpecialObject( obj ) )
                {
                    listModel.addElement( attr.getName() );
                }
            }
        }

        this.lstObjects.setModel( listModel );
    }

    /** Returns all selected names in the list
     * @return A native String[] vector of object names
     */
    public String[] getSelectedObjectNames()
    {
        ArrayList<String> toret = new ArrayList<String>();
        int[] selected = this.lstObjects.getSelectedIndices();

        for(int selectedPos : selected) {
            if ( selectedPos < this.lstObjects.getModel().getSize() ) {
                toret.add(
                        this.lstObjects.getModel().getElementAt( selectedPos ));
            }
        }

        return toret.toArray( new String[ toret.size() ] );
    }

    /** Returns all selected objects in the list
     * @return The objects selected as a native ObjectBag[] vector.
     */
    public ObjectBag[] getSelectedObjects()
    {
        ArrayList<ObjectBag> toret = new ArrayList<ObjectBag>();
        String[] names = this.getSelectedObjectNames();
        ObjectBag root = rt.getRoot();

        for(String name : names) {
            toret.add( root.lookUpAttribute( name ).getReference() );
        }

        return toret.toArray( new ObjectBag[ toret.size() ] );
    }

    /** This method is called from within the constructor to initialize the form. */
    private void build()
    {
        // Object list
        JScrollPane scrlObjectList = new JScrollPane();
        lstObjects = new JList<String>();
        scrlObjectList.setViewportView( lstObjects );

        // File name input
        edFileName = new JTextField();
        edFileName.setEditable( false );

        // Unselect all button
        JButton btUnselectAll = new JButton();
        btUnselectAll.setMnemonic( 'u' );
        btUnselectAll.setText("Unselect All");
        btUnselectAll.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onUnselectAll();
            }
        });

        // Select all button
        JButton btSelectAll = new JButton();
        btSelectAll.setMnemonic( 'a' );
        btSelectAll.setText("Select All");
        btSelectAll.addActionListener( new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSelectAll();
            }
        });

        // Export button
        JButton btExport = new JButton();
        btExport.setMnemonic( 'x' );
        btExport.setText("Export");
        btExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onExport();
            }
        });

        // Close button
        JButton btClose = new JButton();
        btClose.setMnemonic( 'c' );
        btClose.setText("Close");
        btClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onClose();
            }
        });

        // Save button
        JButton btSave = new JButton();
        btSave.setMnemonic( '.' );
        btSave.setText( "..." );
        btSave.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSetSaveName();
            }
        }
        );

        // Panel for dialog buttons
        JPanel pnlDlgButtons = new JPanel();
        pnlDlgButtons.setLayout( new BoxLayout( pnlDlgButtons, BoxLayout.LINE_AXIS ) );
        pnlDlgButtons.add( Box.createHorizontalGlue() );
        pnlDlgButtons.add( btClose );
        pnlDlgButtons.add( Box.createRigidArea( new Dimension( 5, 0 ) ) );
        pnlDlgButtons.add( btExport );

        // Panel for buttons managing list
        JPanel pnlListButtons = new JPanel();
        pnlListButtons.setLayout( new BoxLayout( pnlListButtons, BoxLayout.PAGE_AXIS ) );
        pnlListButtons.add( btSelectAll );
        pnlListButtons.add( Box.createRigidArea( new Dimension( 0, 5 ) ) );
        pnlListButtons.add( btUnselectAll );
        pnlListButtons.add( Box.createVerticalGlue() );

        // List panel
        JPanel pnlList = new JPanel();
        pnlList.setLayout( new BorderLayout( 5, 5 ) );
        pnlList.add( this.lstObjects, BorderLayout.CENTER );
        pnlList.add( pnlListButtons, BorderLayout.EAST );
        pnlList.setBorder( BorderFactory.createTitledBorder( "Objects to export" ) );

        // File panel
        JPanel pnlFileName = new JPanel();
        pnlFileName.setLayout( new BorderLayout( 5, 5 ) );
        pnlFileName.add( edFileName, BorderLayout.CENTER );
        pnlFileName.add( btSave, BorderLayout.EAST );
        pnlFileName.setMaximumSize( new Dimension( Integer.MAX_VALUE, this.edFileName.getHeight() ) );
        pnlFileName.setBorder( BorderFactory.createTitledBorder( "Output file name" ) );

        // Add final layouts
        this.getContentPane().setLayout( new BoxLayout( this.getContentPane(), BoxLayout.PAGE_AXIS ) );
        this.getContentPane().add( pnlList );
        this.getContentPane().add( pnlFileName );
        this.getContentPane().add( Box.createRigidArea( new Dimension( 0, 5 ) ) );
        this.getContentPane().add( pnlDlgButtons );
        this.getContentPane().add( Box.createRigidArea( new Dimension( 0, 5 ) ) );

        // Dialog
        this.setMinimumSize( new Dimension( 350, 350 ) );
        this.setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        this.setTitle( AppInfo.Name + " export" );
        this.pack();

        // Fix some sizes
        btSelectAll.setMaximumSize( new Dimension( btUnselectAll.getWidth(), btSelectAll.getHeight() ) );
    }

    private void onSetSaveName()
    {
        JFileChooser fileSelector = new JFileChooser();

        // Prepare
        fileSelector.setDialogTitle( "Choose a JSON destination file" );
        fileSelector.setDialogType( JFileChooser.SAVE_DIALOG );

        FileNameExtensionFilter filter =
                new FileNameExtensionFilter(
                    "JSON files", DlgExport.JsonExt
        );
        fileSelector.setFileFilter( filter );

        // Show
        if ( fileSelector.showSaveDialog( this ) == JFileChooser.APPROVE_OPTION ) {
            String fileNameStr = "";

            try {
                fileNameStr = fileSelector.getSelectedFile().getCanonicalPath();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        this, "Error opening...", fileNameStr, JOptionPane.ERROR_MESSAGE
                );
                
                this.edFileName.setText( DlgExport.fileName );
            }

            this.edFileName.setText( this.prepareFileName( fileNameStr ) );
        }
    }

    private void onUnselectAll()
    {
        this.lstObjects.setSelectedIndices( new int[ 0 ] );
    }

    private void onClose()
    {
        this.setVisible( false );
    }

    private void onExport()
    {
        ObjectBag[] objs = this.getSelectedObjects();
        JsonPersistence persist = new JsonPersistence( objs );
        File path = new File( this.edFileName.getText() );


        if ( !persist.save( path ) ) {
            JOptionPane.showMessageDialog( this, path.getAbsolutePath(),
                    "Error writing", JOptionPane.ERROR_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog( this, path.getAbsolutePath(),
                    "File generated", JOptionPane.INFORMATION_MESSAGE
            );
        }

    }

    private JTextField edFileName;
    private JList<String> lstObjects;

    private Runtime rt = null;
}
