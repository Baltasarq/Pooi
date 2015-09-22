package ui;

import core.AppInfo;
import core.Interpreter;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.Attribute;
import core.exceps.InterpretError;
import core.objs.ObjectRoot;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashSet;

/*
 * Gui.java
 *
 * Created on 1 de febrero de 2008, 13:26
 */

/**
 * Interfaz de la app
 * @author  baltasarq
 */
public class Gui extends JFrame {
    public static final String EtqIconName = "res/pooiIcon.png";
    
    /** Creates new form Gui */
    public Gui()
    {
        this.build();

        // Prepare ui dimensions
        Dimension scrDim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setTitle( AppInfo.Name + " " + AppInfo.Version );
        this.setMinimumSize( new Dimension( 620, 460 ) );
        this.setPreferredSize( new Dimension( 790, 590 ) );

        // Center in screen
        this.setLocation(
            ( scrDim.width  - this.getWidth() ) / 2,
            ( scrDim.height - this.getHeight() ) / 2
        );

        try {
            URL url = this.getClass().getClassLoader().getResource( EtqIconName );
            ImageIcon iconImg = new ImageIcon( url );

            setIconImage( iconImg.getImage() );
        } catch(Exception exc)
        {
            output.append( "[failed to retrieve icon from jar]\n\n" );
        }

        input.requestFocusInWindow();
    }

    private void buildFontDialog(int fontSize)
    {
        if ( this.dlgFont == null ) {
            final JLabel lblFontSize = new JLabel();
            this.spFontSize = new JSpinner();
            final JButton btCloseDlgFont = new JButton();

            this.dlgFont = new JDialog();
            this.dlgFont.setTitle( "Pooi" );
            this.dlgFont.setAlwaysOnTop( true );
            this.dlgFont.setModal( true );
            this.dlgFont.setName( "dlgFont" );
            lblFontSize.setText( "Font size" );

            btCloseDlgFont.setMnemonic( 'o' );
            btCloseDlgFont.setText( "Ok" );
            btCloseDlgFont.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    dlgFont.setVisible( false );
                    int fontSize = (Integer) spFontSize.getValue();

                    if ( fontSize > 4 ) {
                        output.setFont( new Font( "Courier", Font.PLAIN, fontSize ) );
                    }
                }
            });

            // Prepare layout
            JPanel pnlEd = new JPanel();
            pnlEd.setLayout(new BorderLayout(5, 5));
            pnlEd.add(this.spFontSize, BorderLayout.CENTER);
            pnlEd.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.spFontSize.getHeight()));

            JPanel pnlBt = new JPanel();
            pnlBt.setLayout( new BorderLayout( 5, 5 ) );
            pnlBt.add( btCloseDlgFont, BorderLayout.EAST );
            pnlBt.setMaximumSize(new Dimension(Integer.MAX_VALUE, btCloseDlgFont.getHeight()));

            this.dlgFont.getContentPane().setLayout( new BoxLayout( this.dlgFont.getContentPane(), BoxLayout.PAGE_AXIS ));
            this.dlgFont.getContentPane().add( Box.createVerticalGlue() );
            this.dlgFont.getContentPane().add( pnlEd );
            this.dlgFont.getContentPane().add( Box.createVerticalGlue() );
            this.dlgFont.getContentPane().add( pnlBt );
            this.dlgFont.getContentPane().add( Box.createVerticalGlue() );
        }

        // Prepare dialog
        Dimension d = new Dimension( 150, 100 );
        this.dlgFont.setTitle( AppInfo.Name );
        this.dlgFont.setIconImage( this.getIconImage() );
        this.dlgFont.setMinimumSize( d );
        this.dlgFont.setPreferredSize( d );
        this.dlgFont.setSize( d );

        // Center the dialog
        Point p = new Point( this.getLocation() );
        p.x += ( this.getWidth() - this.dlgFont.getWidth() ) / 2;
        p.y += ( this.getHeight() - this.dlgFont.getHeight() ) / 2;
        this.dlgFont.setLocation( p );

        this.spFontSize.setValue( fontSize );
        return;
    }

    private void buildMenuBar()
    {
        JMenu menuFile = new JMenu();
        this.menuPpal = new JMenuBar();
        JMenuItem opLoadSession = new JMenuItem();
        JMenuItem opSaveTranscript = new JMenuItem();
        JMenuItem opExport = new JMenuItem();
        JMenuItem opReset = new JMenuItem();
        JMenuItem opExit = new JMenuItem();
        JMenu menuView = new JMenu();
        JMenuItem opFont = new JMenuItem();
        JMenu menuHelp = new JMenu();
        JMenuItem opHelp = new JMenuItem();
        JMenuItem opAbout = new JMenuItem();

        menuFile.setMnemonic('f');
        menuFile.setText("File");

        opLoadSession.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        opLoadSession.setMnemonic('l');
        opLoadSession.setText("Load session transcript");
        opLoadSession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onLoadSession();
            }
        });
        menuFile.add(opLoadSession);

        opSaveTranscript.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        opSaveTranscript.setMnemonic('s');
        opSaveTranscript.setText("Save session transcript");
        opSaveTranscript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSaveTranscript();
            }
        });
        menuFile.add(opSaveTranscript);

        opExport.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        opExport.setMnemonic('x');
        opExport.setText("Export objects");
        opExport.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onExport();
            }
        } );
        menuFile.add(opExport);

        opReset.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        opReset.setMnemonic('r');
        opReset.setText("Reset");
        opReset.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onReset();
            }
        } );
        menuFile.add( opReset );

        opExit.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        opExit.setMnemonic('q');
        opExit.setText("Quit");
        opExit.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                close();
            }
        } );
        menuFile.add(opExit);

        this.menuPpal.add(menuFile);

        menuView.setMnemonic('v');
        menuView.setText("View");

        opFont.setMnemonic('f');
        opFont.setText("Font");
        opFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onChangeFont();
            }
        });
        menuView.add(opFont);

        this.menuPpal.add(menuView);

        menuHelp.setMnemonic('h');
        menuHelp.setText("Help");

        opHelp.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        opHelp.setMnemonic('h');
        opHelp.setText("Help content");
        opHelp.setActionCommand("Help content ...");
        opHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onHelp();
            }
        });
        menuHelp.add(opHelp);

        opAbout.setMnemonic('a');
        opAbout.setText("About ...");
        opAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAbout();
            }
        });
        menuHelp.add(opAbout);

        this.menuPpal.add(menuHelp);

        setJMenuBar( this.menuPpal );

        return;
    }

    private void buildOutput()
    {
        // Output
        JScrollPane scrlOutput = new JScrollPane();

        this.output = new JTextArea();
        this.output.setBackground( new java.awt.Color( 0, 0, 0 ) );
        this.output.setColumns( 20 );
        this.output.setEditable( false );
        this.output.setFont( new java.awt.Font( "Courier New", 0, 14 ) );
        this.output.setForeground( new java.awt.Color( 255, 255, 255 ) );
        this.output.setRows( 5 );
        this.output.setCursor(new java.awt.Cursor( java.awt.Cursor.DEFAULT_CURSOR ) );

        scrlOutput.setViewportView( output );
        this.spPanel.setRightComponent( scrlOutput );
    }

    private void buildInput()
    {
        // Input
        this.input = new JComboBox<String>();
        this.input.setEditable( true );
        this.input.setFont( new java.awt.Font( "Courier New", 0, 18 ) );
        this.input.getEditor().getEditorComponent().addKeyListener(
                new java.awt.event.KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent keyEvent) {
                    }

                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                    }

                    @Override
                    public void keyReleased(KeyEvent keyEvent) {
                        if ( keyEvent.getKeyCode() == KeyEvent.VK_ENTER ) {
                            onInputEntered();
                        }

                        return;
                    }
                }
        );

        return;
    }

    private void buildTreeView()
    {
        // Tree view
        trObjectsTree = new JTree();
        trObjectsTree.addTreeSelectionListener( new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
                onSelectedItemChanged( evt.getNewLeadSelectionPath() );
            }
        } );
        trObjectsTree.addMouseListener( new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if ( e.getClickCount() == 2 ) {
                    Gui.this.onSelectedItemChanged( trObjectsTree.getSelectionPath() );
                }

                return;
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        JScrollPane scrlPaneTree = new JScrollPane();
        scrlPaneTree.setViewportView( trObjectsTree );
        this.spPanel.setLeftComponent( scrlPaneTree );
    }

    private void buildTabbedPane()
    {
        this.tpMain = new JTabbedPane();
        this.tpMain.setTabPlacement( JTabbedPane.BOTTOM );
        this.tpMain.addTab( "Console", this.spPanel );
     //   this.tpMain.addTab( "Diagram", this.pnlCanvas );
    }

    private void buildCanvas()
    {
        final int width = 800;
        final int height = 600;

        this.pnlCanvas = new JPanel();
        this.biCanvas = new BufferedImage( width, height,  BufferedImage.TYPE_INT_RGB);
        this.pnlCanvas.setAutoscrolls( true );
        this.lblCanvasFrame = new JLabel( new ImageIcon( this.biCanvas ) );
        this.pnlCanvas.add( this.lblCanvasFrame );

        final Graphics grfs = this.biCanvas.getGraphics();
        grfs.setColor( Color.WHITE );
        grfs.fillRect( 0, 0, width, height );
        pnlCanvas.repaint();
        this.lblCanvasFrame.setPreferredSize( new Dimension( width, height ) );
        this.lblCanvasFrame.setMinimumSize( new Dimension( width, height ) );
        this.biCanvas.getGraphics().drawRect( 10, 10, 100, 100 );

    }

    private void buildToolbar()
    {
        JButton btReset = new JButton( "Reset" );
        JButton btNewObject = new JButton( "New object" );

        this.tbIconBar = new JToolBar();
        this.tbIconBar.setFloatable( false );

        this.tbIconBar.add( btReset );
        this.tbIconBar.addSeparator();
        this.tbIconBar.add( btNewObject );

        this.getContentPane().add( this.tbIconBar, BorderLayout.NORTH );

        // Events
        btReset.addActionListener( e -> Gui.this.onReset() );

        btNewObject.addActionListener( e -> {
            Gui.this.simulate( "(anObject copy)" );
        });
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void build()
    {
        // Split view
        this.spPanel = new JSplitPane();
        this.spPanel.setDividerLocation( 150 );

        // Build components
        this.buildMenuBar();
        this.buildToolbar();
        this.buildInput();
        this.buildOutput();
        this.buildTreeView();
        this.buildCanvas();
        this.buildTabbedPane();

        // Compose it all
        this.getContentPane().add( this.input, java.awt.BorderLayout.SOUTH );
        this.getContentPane().add( this.tpMain, java.awt.BorderLayout.CENTER );

        // Polish the window
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new java.awt.Dimension(500, 400));
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                Gui.this.close();
            }
        });
        this.pack();
    }

    private void onLoadSession()
    {
        JFileChooser fileSelector = new JFileChooser();        

        // Prepare
        fileSelector.setDialogTitle( "Choose a transcript text file" );
        fileSelector.setDialogType( JFileChooser.OPEN_DIALOG );
        
        FileNameExtensionFilter filter =
                new FileNameExtensionFilter( 
                    "Transcript text files", "txt"
        );
        fileSelector.setFileFilter( filter );

        // Show
        if ( fileSelector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION )
        {
            // Load
            output.append( "\n" +
                    interpreter.loadSession( fileSelector.getSelectedFile().getAbsolutePath() )
                    + "\n\n"
            );
            output.setCaretPosition( output.getText().length() );

            updateTree();
        }
    }

    private void onSaveTranscript()
    {
        JFileChooser fileSelector = new JFileChooser();        

        // Prepare
        fileSelector.setDialogTitle( "Choose a transcript text file" );
        fileSelector.setDialogType( JFileChooser.SAVE_DIALOG );
        
        FileNameExtensionFilter filter =
                new FileNameExtensionFilter( 
                    "Transcript text files", "txt"
        );
        fileSelector.setFileFilter( filter );                

        // Show
        if ( fileSelector.showSaveDialog( this ) == JFileChooser.APPROVE_OPTION ) {
            interpreter.activateTranscript(
                    fileSelector.getSelectedFile().getAbsolutePath()
            );
        }
    }

    private void onHelp()
    {
        this.simulate( "help" );
    }

    private void onAbout()
    {
        this.simulate( "about" );
        this.simulate( "author" );
    }

    private void onInputEntered()
    {
        String msg = (String) this.input.getSelectedItem();
        msg = msg.trim();

        try {
            if ( this.input.isEnabled()
              && !msg.isEmpty() )
            {
                this.input.setEnabled( false );
                this.input.setSelectedItem( "" );

                // Show the order in the output panel
                this.output.append( "> " + msg + "\n" ) ;

                // Interpret the order
                this.output.append( interpreter.interpret( msg ) );
                this.output.append( "\n\n" );
                this.output.setCaretPosition( output.getText().length() );

                // Check for the root existing -- if not, end
                if ( this.interpreter.getRuntime().getRoot() == null ) {
                    this.close();
                }

                // If it was not an error, set it to the order history
                if ( !( this.interpreter.getError() ) ) {
                    this.input.addItem( msg );

                    if ( this.input.getItemCount() > 10 ) {
                        this.input.removeItemAt( 0 );
                    }
                }
            }
        } catch(StackOverflowError exc)
        {
            this.output.append( "\n\n*** ERROR: stack overflow\n\n" );
        }
        finally {
            this.updateTree();
            this.input.setEnabled( true );
            this.input.requestFocus();
        }
    }

    private void onSelectedItemChanged(TreePath treePath)
    {
        if ( !rebuildingTree) {
            rebuildingTree = true;

            if ( treePath != null ) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                Object[] pathParts = node.getUserObjectPath();
                StringBuilder path = new StringBuilder();

                // Build the object path joining the path parts
                for(int i = 0; i < pathParts.length; ++i) {
                    path.append( pathParts[ i ] );

                    if ( i < ( pathParts.length -1 ) ) {
                        path.append( '.' );
                    }
                }

                input.setSelectedItem( path.toString() );
                input.requestFocusInWindow();
            }

            rebuildingTree = false;
        }

        return;
    }

    private void onChangeFont()
    {
        this.buildFontDialog(output.getFont().getSize());
        this.dlgFont.setVisible(true);
    }

    private void onExport()
    {
        new DlgExport( this.interpreter.getRuntime(), this, true ).setVisible( true );
    }

    private void close()
    {
        interpreter.endTranscript();
        System.exit( 0 );
    }

    private void onReset()
    {
        try {
            this.activateGui();
            Runtime.createRuntime();
        } catch(InterpretError e)
        {
            this.output.append( "\n\n*** Error creating runtime:\n" + e.getLocalizedMessage() + "\n" );
            this.deactivateGui();
        }

        this.reset();
    }

    private void reset()
    {
        try {
            this.interpreter = new Interpreter();
            this.output.setText("");
            this.updateTree();

            this.output.append( "Pooi [Prototype-based, object-oriented interpreter]\n"
                    + "\ntype in your message\n"
                    + "try \"Root list\", \"help\" or \"about\" to start\n\n\n"
            );
        } catch(InterpretError e) {
            this.output.append( "\n\n*** Error in bootstrap:\n" + e.getLocalizedMessage() + "\n" );
            this.deactivateGui();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {       

        try {
             UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
             );
        } catch(Exception ignored) {
        }

        // Prepare Gui
        final Gui g = new Gui();

        try {
            // Prepare interpreter
            g.reset();

            // run
            java.awt.EventQueue.invokeLater( new Runnable() {
                @Override
                public void run() {
                   g.setVisible( true );
                }
            } );
        } catch(Exception e)
        {
            g.output.append( "\n\nUnexpected error:\n" + e.getLocalizedMessage() + "\n" );
            g.deactivateGui();
        }
    }

    private void activateGui()
    {
        this.setGuiEnabled( true );
    }

    private void deactivateGui()
    {
        this.setGuiEnabled( true );
    }

    private void setGuiEnabled(boolean status)
    {
        this.input.setEnabled( status );
        this.menuPpal.setEnabled( status );
    }

    private void simulate(String msg) {
        this.input.setSelectedItem( msg );
        this.onInputEntered();
    }

    private void updateTree()
    {
        final ObjectRoot root = this.interpreter.getRuntime().getRoot();
        trObjectsTree.setVisible( false );
        HashSet<ObjectBag> shownObjects = new HashSet<ObjectBag>();

        // Get root
        DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode( root.getName() );
        trObjectsTree.setModel( new DefaultTreeModel( treeRoot ) );
        treeRoot.setUserObject( root.getName() );
        shownObjects.add( root );

        // Run all over objects in Root
        addNodes( treeRoot, root, shownObjects  );

        trObjectsTree.expandRow( 0 );
        trObjectsTree.setVisible( true );
    }

    private void addNodes(DefaultMutableTreeNode node, ObjectBag obj, HashSet<ObjectBag> shown)
    {
        DefaultMutableTreeNode newNode;
        final Runtime rt = this.interpreter.getRuntime();
        
        for( Attribute attr: obj.getAttributes() ) {
            if ( attr.getReference() == rt.getAnObject()
             || ( !rt.isSpecialObject( attr.getReference() )
               && !rt.isTypeObject(  attr.getReference() ) ) )
            {
                 newNode = new DefaultMutableTreeNode( attr.getName() );
                 newNode.setUserObject( attr.getName() );
                 node.add( newNode );
             
                 ObjectBag subObj = attr.getReference();

                 if ( subObj != null
                   && !shown.contains( subObj ) )
                 {
                     shown.add( obj );
                     addNodes( newNode, subObj, shown );
                 }
            }
        }
    }
    
    private JDialog dlgFont;
    private JComboBox<String> input;
    private JMenuBar menuPpal;
    private JTextArea output;
    private JTree trObjectsTree;
    private JSpinner spFontSize;
    private JSplitPane spPanel;
    private JTabbedPane tpMain;
    private JPanel pnlCanvas;
    private BufferedImage biCanvas;
    private JLabel lblCanvasFrame;
    private JToolBar tbIconBar;

    private Interpreter interpreter = null;

    private static boolean rebuildingTree = false;
}
