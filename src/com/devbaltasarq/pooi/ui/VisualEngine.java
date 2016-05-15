package com.devbaltasarq.pooi.ui;

import com.devbaltasarq.pooi.core.AppInfo;
import com.devbaltasarq.pooi.core.Interpreter;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.Attribute;
import com.devbaltasarq.pooi.core.evaluables.Reference;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectRoot;
import com.devbaltasarq.pooi.core.objs.SysObject;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

/*
 * VisualEngine.java
 *
 * Created on 1 de febrero de 2008, 13:26
 */

/**
 * Interfaz de la app
 * @author  baltasarq
 */
public class VisualEngine extends JFrame {
    public static final int MinFontSize = 10;
    public static final int MaxFontSize = 32;
    public static final String EtqIconApp = "com/devbaltasarq/pooi/res/pooiIcon.png";
    public static final String EtqIconReset = "com/devbaltasarq/pooi/res/reset.png";
    public static final String EtqIconNew = "com/devbaltasarq/pooi/res/new.png";
    
    /** Creates new form VisualEngine */
    public VisualEngine()
    {
        this.build();
        this.input.requestFocusInWindow();
    }

    private void buildFontDialog()
    {
        if ( this.dlgFont == null ) {
            final JLabel lblFontSize = new JLabel();
            // Create spinner from MinFontSize to MaxFontSize, in 1.0 steps start value MinFontSize
            final SpinnerNumberModel spinnerModel = new SpinnerNumberModel( MinFontSize, MinFontSize, MaxFontSize, 1.0 );
            this.spFontSize = new JSpinner( spinnerModel );
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
                    int fontSize = Math.toIntExact( Math.round( (Double) spFontSize.getValue() ) );

                    if ( fontSize >= MinFontSize
                      && fontSize <= MaxFontSize )
                    {
                        VisualEngine.this.font = new Font( "monospaced", Font.PLAIN, fontSize );
                        VisualEngine.this.output.setFont( VisualEngine.this.font );
                        VisualEngine.this.updateDiagram();
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

        this.spFontSize.setValue( this.font.getSize() );
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
        this.output = new JTextArea();
        this.output.setBackground( new java.awt.Color( 0, 0, 0 ) );
        this.output.setColumns( 20 );
        this.output.setEditable( false );
        this.output.setFont( new java.awt.Font( "Courier New", 0, 14 ) );
        this.output.setForeground( new java.awt.Color( 255, 255, 255 ) );
        this.output.setRows( 5 );
        this.output.setCursor(new java.awt.Cursor( java.awt.Cursor.DEFAULT_CURSOR ) );
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
                if ( e.getButton() == MouseEvent.BUTTON3 ) {
                    TreePath selPath = VisualEngine.this.trObjectsTree.getPathForLocation( e.getX(), e.getY() );
                    VisualEngine.this.trObjectsTree.setSelectionPath( selPath );
                    VisualEngine.this.popup.show( e.getComponent(), e.getX(), e.getY() );
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

    private void buildToolbar()
    {
        JButton btReset = new JButton( this.iconReset );
        JButton btNewObject = new JButton( this.iconNew);

        btReset.setToolTipText( "reset" );
        btNewObject.setToolTipText( "new object" );

        this.tbIconBar = new JToolBar();
        this.tbIconBar.setFloatable( false );

        this.tbIconBar.add( btReset );
        this.tbIconBar.addSeparator();
        this.tbIconBar.add( btNewObject );

        this.getContentPane().add( this.tbIconBar, BorderLayout.NORTH );

        // Events
        btReset.addActionListener( e -> VisualEngine.this.onReset() );

        btNewObject.addActionListener( e -> VisualEngine.this.onNewObject( Runtime.EtqNameAnObject ) );
    }

    /** Retries icons from jar for future use */
    private void buildIcons()
    {
        URL url;

        try {
            url = this.getClass().getClassLoader().getResource( EtqIconApp );
            this.iconApp = new ImageIcon( url );

            url = this.getClass().getClassLoader().getResource( EtqIconReset );
            this.iconReset = new ImageIcon( url );

            url = this.getClass().getClassLoader().getResource( EtqIconNew );
            this.iconNew = new ImageIcon( url );

        } catch(Exception exc)
        {
            output.append( "[failed to retrieve icons from jar]\n\n" );
        }
    }

    private void buildPopup()
    {
        this.popup = new JPopupMenu();

        // Copy
        JMenuItem miCopy = new JMenuItem( "Copy" );
        miCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                VisualEngine.this.onNewObject( VisualEngine.this.getSelectedObjectPath() );
            }
        });
        this.popup.add( miCopy );

        // List
        JMenuItem miList = new JMenuItem( "List" );
        miList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                VisualEngine.this.execute( VisualEngine.this.getSelectedObjectPath() + " list" );
            }
        });
        this.popup.add( miList );

        // Inspect
        JMenuItem miInspect = new JMenuItem( "Inspect" );
        miInspect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ObjectBag objDest = null;

                try {
                    String objPath = VisualEngine.this.getSelectedObjectPath();
                    String[] objPathParts = objPath.split( "\\." );
                    objDest = VisualEngine.this.interpreter.getRuntime().solveToObject( new Reference( objPathParts ) );
                } catch (InterpretError interpretError) {
                    objDest = VisualEngine.this.interpreter.getRuntime().getAbsoluteParent();
                }
                VisualEngine.this.onInspect( objDest );
            }
        });
        this.popup.add( miInspect );

        // Add attribute
        JMenuItem miAddAttribbute = new JMenuItem( "Add attribute" );
        miAddAttribbute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ObjectBag objDest = null;

                try {
                    String objPath = VisualEngine.this.getSelectedObjectPath();
                    String[] objPathParts = objPath.split( "\\." );
                    objDest = VisualEngine.this.interpreter.getRuntime().solveToObject( new Reference( objPathParts ) );
                } catch (InterpretError interpretError) {
                    objDest = VisualEngine.this.interpreter.getRuntime().getAbsoluteParent();
                }
                Inspector.addAttribute( VisualEngine.this, VisualEngine.this, objDest );
            }
        });
        this.popup.add( miAddAttribbute );

        // Add method
        JMenuItem miAddMethod = new JMenuItem( "Add method" );
        miAddMethod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ObjectBag objDest = null;

                try {
                    String objPath = VisualEngine.this.getSelectedObjectPath();
                    String[] objPathParts = objPath.split( "\\." );
                    objDest = VisualEngine.this.interpreter.getRuntime().solveToObject( new Reference( objPathParts ) );
                } catch (InterpretError interpretError) {
                    objDest = VisualEngine.this.interpreter.getRuntime().getAbsoluteParent();
                }
                Inspector.addMethod( VisualEngine.this, VisualEngine.this, objDest );
            }
        });
        this.popup.add( miAddMethod );

        // Remove
        JMenuItem miErase = new JMenuItem( "Erase" );
        miErase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                VisualEngine.this.eraseObject( VisualEngine.this.getSelectedObjectPath() );
            }
        });
        this.popup.add( miErase );
    }

    private void prepareDimensions()
    {
        Dimension realDimension = new Dimension( 640, 480 );

        // Prepare window dimensions
        Dimension scrDim = Toolkit.getDefaultToolkit().getScreenSize();

        if ( scrDim.getWidth() > 1280 ) {
            realDimension.height = 768;
            realDimension.width = 1280;

            if ( scrDim.getHeight() > 800 ) {
                realDimension.height = 1024;
            }
        }
        else
        if ( scrDim.getHeight() > 768 ) {
            realDimension.height = 768;
            realDimension.width = 1024;
        }
        else
        if ( scrDim.getHeight() > 600 ) {
            realDimension.height = 600;
            realDimension.width = 800;
        }

        this.setTitle( AppInfo.Name + " " + AppInfo.Version );
        this.setMinimumSize( new Dimension( 640, 480 ) );
        this.setPreferredSize( realDimension );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void build()
    {
        // Split view
        this.prepareDimensions();
        this.spPanel = new JSplitPane();
        this.spPanel.setDividerLocation( 150 );
        this.spMain = new JSplitPane();
        this.spMain.setDividerLocation( (int) ( this.getHeight() - ( this.getHeight() * 0.15 ) ) );

        // Build components
        this.buildIcons();
        this.buildMenuBar();
        this.buildPopup();
        this.buildToolbar();
        this.buildInput();
        this.buildOutput();
        this.buildTreeView();

        // The font
        this.font = new Font( "monospaced", Font.PLAIN, 12 );
        this.output.setFont( this.font );

        // Compose it all
        JScrollPane scrlOutput = new JScrollPane();
        scrlOutput.setViewportView( this.output );
        this.spMain.setOrientation( JSplitPane.VERTICAL_SPLIT  );
        this.spMain.setBottomComponent( scrlOutput );
        this.spPanel.setRightComponent( this.spMain );
        this.getContentPane().add( this.spPanel, BorderLayout.CENTER );
        this.getContentPane().add( this.input, BorderLayout.SOUTH );

        // Polish the window
        this.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        this.addWindowListener( new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                VisualEngine.this.close();
            }
        });

        this.pack();
        this.setIconImage( this.iconApp.getImage() );
        this.prepareDimensions();

        // Prepare the diagrammer
        this.pnlCanvas = new Canvas( 2000, 2000 );
        this.scrCanvas = new JScrollPane( this.pnlCanvas );
        this.spMain.setTopComponent( scrCanvas );
        this.pnlCanvas.addMouseListener( new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for(ObjectBox box: VisualEngine.this.diagramBoxes.values()) {
                    Dimension dim = box.getCurrentDimension();
                    int boxX = VisualEngine.this.scrCanvas.getX() + box.getX();
                    int boxY = VisualEngine.this.scrCanvas.getY() + box.getY();

                    if ( e.getX() >= boxX && e.getY() >= boxY
                            && e.getX() < ( boxX + dim.width )
                            && e.getY() < ( boxY + dim.height ) )
                    {
                        if ( e.getButton() == MouseEvent.BUTTON3 ) {
                            VisualEngine.this.setSelectedObject( box.getObj() );
                            VisualEngine.this.popup.show( e.getComponent(), e.getX(), e.getY() );
                        } else {
                            VisualEngine.this.onInspect( box.getObj() );
                        }
                    }
                }
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
        } );

        // Center in screen
        Dimension scrDim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(
                ( scrDim.width  - this.getWidth() ) / 2,
                ( scrDim.height - this.getHeight() ) / 2
        );
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

            this.updateTree();
            this.updateDiagram();
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
        this.execute( "help" );
    }

    private void onAbout()
    {
        this.execute( "about" );
        this.execute( "author" );
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

                // Set it to the order history
                this.input.insertItemAt( msg, 0  );

                if ( this.input.getItemCount() >= 10 ) {
                    this.input.removeItemAt( 9 );
                }
            }
        } catch(StackOverflowError exc)
        {
            this.output.append( "\n\n*** ERROR: stack overflow\n\n" );
        }
        finally {
            this.updateTree();
            this.updateDiagram();
            this.input.setEnabled( true );
            this.input.requestFocus();
        }
    }

    private void setSelectedObject(ObjectBag obj)
    {
        String[] objParts = obj.getPath().split( "\\." );
        int control = 0;
        int numPart = 1;
        DefaultTreeModel model = (DefaultTreeModel) this.trObjectsTree.getModel();
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) model.getRoot();

        while( numPart < objParts.length
            && control < 10000 )
        {
            for (int i = 0; i < currentNode.getChildCount(); ++i) {
                DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) currentNode.getChildAt( i );
                String subObj = (String) subNode.getUserObject();

                if ( subObj.equals( objParts[ numPart ] ) ) {
                    currentNode = subNode;
                    ++numPart;
                    break;
                }
            }

            ++control;  // Safety -- it is not guaranteed to exit from here if the object does not exist
        }

        this.trObjectsTree.setSelectionPath( new TreePath( currentNode.getPath() ) );

    }

    private String getSelectedObjectPath() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.trObjectsTree.getLastSelectedPathComponent();
        StringBuilder toret = new StringBuilder();

        if ( selectedNode != null ) {
            Object[] pathParts = selectedNode.getUserObjectPath();

            // Build the object path joining the path parts
            for (int i = 0; i < pathParts.length; ++i) {
                toret.append(pathParts[i]);

                if ( i < ( pathParts.length - 1 ) ) {
                    toret.append('.');
                }
            }
        }

        return toret.toString();
    }

    private void onSelectedItemChanged(TreePath treePath)
    {
        if ( !rebuildingTree ) {
            rebuildingTree = true;
            String path = this.getSelectedObjectPath();

            if ( !path.isEmpty() ) {
                this.input.setSelectedItem(path.toString());
            }

            this.input.requestFocusInWindow();
            rebuildingTree = false;
        }

        return;
    }

    private void onChangeFont()
    {
        this.buildFontDialog();
        this.dlgFont.setVisible( true );
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
        int n = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure?",
                    AppInfo.Name,
                    JOptionPane.YES_NO_OPTION );

        if ( n == JOptionPane.YES_OPTION ){
            try {
                this.activateGui();
                Runtime.createRuntime();
            } catch (InterpretError e) {
                this.output.append("\n\n*** Error creating runtime:\n" + e.getLocalizedMessage() + "\n");
                this.deactivateGui();
            }

            this.reset();
        }

        return;
    }

    private void onInspect(ObjectBag obj)
    {
        new Inspector( this, obj );
    }

    private void onNewObject(String objPath) {
        String s = (String) JOptionPane.showInputDialog(
                this,
                "New object's name:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                this.iconNew,
                null,
                "obj" );

        if ( s != null ) {
            this.createNewObject( objPath, s );
        }

        return;
    }

    private void createNewObject(String objPath, String name)
    {
        String order = "(" + objPath + " copy) rename \"";
        name = name.trim();

        if ( !name.isEmpty() ) {
            this.execute( order + name + '"' );
        }
    }

    private void eraseObject(String objPath)
    {
        if ( objPath != null
          && !objPath.isEmpty() )
        {
            int lastPointPos = objPath.lastIndexOf( '.' );
            String objParentPath = objPath.substring( 0, lastPointPos );
            String objName = objPath.substring( lastPointPos + 1 );
            this.execute( objParentPath + " erase \"" + objName + "\"" );
        }
    }

    public void reset()
    {
        try {
            this.interpreter = new Interpreter();
            this.output.setText( "" );
            this.updateTree();
            this.updateDiagram();

            this.output.append( "Pooi [Prototype-based, object-oriented interpreter]\n"
                    + "\ntype in your message\n"
                    + "try \"Root list\", \"help\" or \"about\" to start\n\n\n"
            );
        } catch(InterpretError e) {
            this.output.append( "\n\n*** Error in bootstrap:\n" + e.getLocalizedMessage() + "\n" );
            this.deactivateGui();
        }
    }

    public void updateDiagram()
    {
        final int HorizontalSeparation = 25;
        final int VerticalSeparation = 50;
        final Runtime rt = this.interpreter.getRuntime();

        this.pnlCanvas.setFont( this.font );

        // Created unwanted objects to be shown
        HashSet<ObjectBag> doNotAdd = new HashSet<>();
        doNotAdd.add( this.interpreter.getAboutObject() );
        doNotAdd.add( this.interpreter.getAuthorObject() );
        doNotAdd.add( this.interpreter.getHelpObject() );
        doNotAdd.add( rt.getAbsoluteParent() );
        doNotAdd.add( rt.getAnObject() );

        // Registers
        this.diagramBoxes = new HashMap<>();

        // Create boxes
        int xUpLevel = 20;
        int xDownLevel = 20;
        final int UpRowLevel = 150;
        final int DownRowLevel = 300;

        // Inheritance root
        ObjectBox rootBox = new ObjectBox( rt.getAbsoluteParent(), 20, 20 );
        rootBox.prepareDrawing( pnlCanvas );
        diagramBoxes.put( rt.getAbsoluteParent().getPath(), rootBox );

        // anObject
        ObjectBox anObjectBox = new ObjectBox( rt.getAnObject(), xUpLevel, UpRowLevel );
        xUpLevel += anObjectBox.prepareDrawing( pnlCanvas ).width + HorizontalSeparation;
        diagramBoxes.put( rt.getAnObject().getPath(), anObjectBox );

        for(Attribute atr: rt.getRoot().getAttributes()) {
            ObjectBag obj = atr.getReference();

            if ( !( obj instanceof SysObject )
                && !doNotAdd.contains( obj ) )
            {
                int pos = xUpLevel;
                int level = UpRowLevel;

                if ( obj.getParentObject() != rt.getAbsoluteParent() ) {
                    level = DownRowLevel;
                    pos = xDownLevel;
                }

                ObjectBox box = new ObjectBox( obj, pos, level );
                diagramBoxes.put( obj.getPath(), box );

                Dimension dimension = box.prepareDrawing( pnlCanvas );
                if ( obj.getParentObject() == rt.getAbsoluteParent() ) {
                    xUpLevel += dimension.width + HorizontalSeparation;
                } else {
                    xDownLevel += dimension.width + HorizontalSeparation;
                }
            }
        }

        // Calculate max height for first level
        int maxHeight = 0;
        for (ObjectBox box: diagramBoxes.values()) {
            if ( box.getY() == UpRowLevel ) {
                maxHeight = Math.max( maxHeight, box.getMeasuredDimension().height );
            }
        }

        // Recalculate vertical position, if needed
        if ( ( UpRowLevel + maxHeight ) >= DownRowLevel ) {
            final int newLevel = UpRowLevel + maxHeight + VerticalSeparation;
            for (ObjectBox box: diagramBoxes.values()) {
                if ( box.getY() == DownRowLevel ) {
                    box.setY( newLevel );
                }
            }
        }

        // Draw inheritance vertexes
        this.pnlCanvas.setBackgroundColor( Color.lightGray );
        this.pnlCanvas.setColor( Color.black );
        for(ObjectBox box: diagramBoxes.values()) {
            ObjectBox parentBox = diagramBoxes.get( box.getObj().getParentObject().getPath() );
            Dimension dimChild = box.getMeasuredDimension();
            Dimension dimParent = parentBox.getMeasuredDimension();

            int childPosX = box.getX() + ( (int) ( dimChild.width / 2 ) );
            int parentPosX = parentBox.getX() + ( (int) ( dimParent.width / 2 ) );
            this.pnlCanvas.drawLine( childPosX, box.getY(), parentPosX, parentBox.getY() + dimParent.height  );
        }

        // Draw boxes
        for(ObjectBox box: diagramBoxes.values()) {
            box.draw( this.pnlCanvas );
        }
    }

    public void activateGui()
    {
        this.setGuiEnabled( true );
    }

    public void deactivateGui()
    {
        this.setGuiEnabled( true );
    }

    private void setGuiEnabled(boolean status)
    {
        this.input.setEnabled( status );
        this.menuPpal.setEnabled( status );
    }

    void execute(String msg) {
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

    public String makeInput(String msg)
    {
        String toret = (String) JOptionPane.showInputDialog(
                this,
                msg,
                AppInfo.Name,
                JOptionPane.QUESTION_MESSAGE );

        if ( toret == null ) {
            toret = "";
        }

        return toret.trim();
    }

    public void makeOutput(String msg)
    {
        this.output.append( msg );
    }
    
    private JDialog dlgFont;
    private JComboBox<String> input;
    private JMenuBar menuPpal;
    private JTextArea output;
    private JTree trObjectsTree;
    private JSpinner spFontSize;
    private JSplitPane spPanel;
    private JSplitPane spMain;
    private JScrollPane scrCanvas;
    private Canvas pnlCanvas;
    private JToolBar tbIconBar;
    private JPopupMenu popup;
    private Font font;

    private ImageIcon iconReset;
    private ImageIcon iconNew;
    private ImageIcon iconApp;

    private HashMap<String, ObjectBox> diagramBoxes;

    private Interpreter interpreter = null;

    private static boolean rebuildingTree = false;
}