package com.devbaltasarq.pooi.ui;

import com.devbaltasarq.pooi.core.AppInfo;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.Attribute;
import com.devbaltasarq.pooi.core.evaluables.Method;
import com.devbaltasarq.pooi.core.evaluables.ParentAttribute;
import com.devbaltasarq.pooi.core.objs.ObjectParent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashSet;

/**
 * Created by Baltasar on 30/04/2016.
 */
public class Inspector extends JDialog {
    public static final String EtqIconCopy = "com/devbaltasarq/pooi/res/copy.png";
    public static final String EtqIconAddMethod = "com/devbaltasarq/pooi/res/addMethod.png";
    public static final String EtqIconAddAttribute = "com/devbaltasarq/pooi/res/addAttribute.png";
    public static final String EtqIconExecute = "com/devbaltasarq/pooi/res/execute.png";
    public static final String EtqIconDelete = "com/devbaltasarq/pooi/res/delete.png";

    public Inspector(VisualEngine parent, ObjectBag obj) {
        this.beingBuilt = true;
        try {
            this.rt = Runtime.getRuntime();
            this.objRoot = rt.getAbsoluteParent();
        }
        catch(Exception exc) {
            this.setVisible( false );
            JOptionPane.showMessageDialog( this, "Unexpected ERROR retrieving root object" );
        }

        this.visualEngine = parent;
        this.obj = obj;
        this.setIconImage( parent.getIconImage() );
        this.setFont( this.visualEngine.getFont() );
        this.build();
        this.beingBuilt = false;
        this.setVisible( true );
    }

    /** Retries icons from jar for future use */
    private void buildIcons()
    {
        URL url;

        try {
            url = this.getClass().getClassLoader().getResource( EtqIconCopy );
            this.iconCopy = new ImageIcon( url );

            url = this.getClass().getClassLoader().getResource( EtqIconAddMethod );
            this.iconAddMethod = new ImageIcon( url );

            url = this.getClass().getClassLoader().getResource( EtqIconAddAttribute );
            this.iconAddAttribute = new ImageIcon( url );

            url = this.getClass().getClassLoader().getResource( EtqIconExecute );
            this.iconExecute = new ImageIcon( url );

            url = this.getClass().getClassLoader().getResource( EtqIconDelete );
            this.iconDelete = new ImageIcon( url );

        } catch(Exception exc)
        {
            this.visualEngine.makeOutput( "Warning: failed to retrieve icons from jar\n\n" );
        }
    }

    private void buildCloseButton() {
        JPanel pnlClose = new JPanel();
        FlowLayout flow = new FlowLayout();
        flow.setAlignment( FlowLayout.RIGHT );
        pnlClose.setLayout( flow );
        this.btClose = new JButton( "Close" );
        this.btClose.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Inspector.this.setVisible( false );
            }
        });
        pnlClose.add( this.btClose );
        this.add( pnlClose, BorderLayout.PAGE_END );
    }

    private void buildBasicAttrsPanel() {
        // Add panel for object's name
        JPanel pnlName = new JPanel();
        pnlName.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
        pnlName.setLayout( new BoxLayout( pnlName, BoxLayout.LINE_AXIS ) );
        JLabel lblObjectName = new JLabel( "Name" );
        this.edObjectName = new JTextField();
        edObjectName.setHorizontalAlignment( JTextField.RIGHT );
        pnlName.add( lblObjectName );
        pnlName.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        pnlName.add( edObjectName );
        this.pnlAction.add( pnlName );
        this.pnlAction.add( Box.createVerticalGlue() );
        this.edObjectName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if ( !Inspector.this.beingBuilt ) {
                    Inspector.this.visualEngine.execute(
                        Inspector.this.getObj().getPath() + " rename \"" + edObjectName.getText() + "\"" );
                    Inspector.this.updateObjectBasicAttributes();
                }
            }
        });

        // Add panel for parent attribute
        if ( !( this.getObj() instanceof ObjectParent ) ) {
            JPanel pnlAttrParent = new JPanel();
            pnlAttrParent.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
            pnlAttrParent.setLayout( new BoxLayout( pnlAttrParent, BoxLayout.LINE_AXIS ) );
            JLabel lblParentAttributeName = new JLabel( "Parent" );
            this.cbParent = new JComboBox( this.availableObjectsNames );
            ( (JLabel) this.cbParent.getRenderer() ).setHorizontalAlignment( JLabel.RIGHT );
            ( (JTextField) this.cbParent.getEditor().getEditorComponent() ).setHorizontalAlignment( JTextField.RIGHT  );
            this.cbParent.setFont( this.visualEngine.getFont() );
            this.cbParent.setEditable( true );
            this.cbParent.getModel().setSelectedItem( this.getObj().getParentObject().getNameOrValueAsString() );
            pnlAttrParent.add( lblParentAttributeName );
            pnlAttrParent.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
            pnlAttrParent.add( this.cbParent );
            this.pnlAction.add( pnlAttrParent );
            this.pnlAction.add( Box.createVerticalGlue() );
            this.cbParent.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if ( !Inspector.this.beingBuilt ) {
                        Inspector.this.visualEngine.execute(
                            Inspector.this.getObj().getPath() + ".parent = " + cbParent.getSelectedItem() );
                        Inspector.this.updateObjectBasicAttributes();
                    }
                }
            });
        }
    }

    private void buildManagementButtonsPanel() {
        JPanel pnlButtons = new JPanel();
        pnlButtons.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
        pnlButtons.setLayout( new BoxLayout( pnlButtons, BoxLayout.LINE_AXIS ) );
        JButton btAddAttribute = Util.createButton( this.iconAddAttribute,  "Add attribute" );
        JButton btAddMethod = Util.createButton( this.iconAddMethod, "Add method" );
        JButton btCopy = Util.createButton( this.iconCopy, "Copy" );
        pnlButtons.add( btAddAttribute );
        pnlButtons.add( Box.createHorizontalGlue() );
        pnlButtons.add( btAddMethod );
        pnlButtons.add( Box.createHorizontalGlue() );
        pnlButtons.add( btCopy );
        this.pnlAction.add( pnlButtons );
        this.pnlAction.add( Box.createVerticalGlue() );

        btAddAttribute.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String attrName = "attr" + ( Inspector.this.getObj().getNumberOfAttributes()  + 1 );
                Inspector.this.visualEngine.execute( getObj().getPath() + "." + attrName + " = 0" );
                Inspector.this.addAttributePanel( getObj().lookUpAttribute( attrName ) );
                Inspector.this.pack();
            }
        } );

        btAddMethod.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mthName = "mth" + ( Inspector.this.getObj().getNumberOfMethods() + 1 );
                Inspector.this.visualEngine.execute( getObj().getPath() + "." + mthName + " = {:}" );
                Inspector.this.addMethodPanel( getObj().localLookUpMethod( mthName ) );
                Inspector.this.pack();
            }
        } );

        btCopy.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Inspector.this.copyObject();
            }
        } );
    }

    private void addAttributePanel(Attribute attr) {
        ObjectBag objDest = attr.getReference();
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder( 10, 10, 10, 10 ) );
        panel.setLayout( new BoxLayout( panel, BoxLayout.LINE_AXIS ) );

        JButton btDelete = Util.createButton( this.iconDelete, "Delete" );
        JComboBox cbContents = new JComboBox( this.availableObjectsNames );
        ( (JLabel) cbContents.getRenderer() ).setHorizontalAlignment( JLabel.RIGHT );
        ( (JTextField) cbContents.getEditor().getEditorComponent() ).setHorizontalAlignment( JTextField.RIGHT  );
        cbContents.setFont( this.visualEngine.getFont() );
        cbContents.setEditable( true );
        cbContents.getModel().setSelectedItem( objDest.getNameOrValueAsString() );
        JTextField edName = new JTextField( attr.getName() );

        panel.add( btDelete );
        panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        panel.add( edName );
        panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        panel.add( cbContents );
        this.pnlAttributes.add( panel );
        this.pnlAttributes.add( Box.createVerticalGlue() );

        edName.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( !Inspector.this.beingBuilt ) {
                    Inspector.this.visualEngine.execute( getObj().getPath()
                                    + "." + attr.getName() + " rename \"" + edName.getText() + "\"" );
                    edName.setText( attr.getName() );
                }
            }
        } );

        cbContents.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if ( !Inspector.this.beingBuilt ) {
                    visualEngine.execute( obj.getPath() + "." + edName.getText() + " = " + cbContents.getModel().getSelectedItem() );
                }
            }
        } );

        btDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if ( !Inspector.this.beingBuilt ) {
                    visualEngine.execute( obj.getPath() + " erase \"" + attr.getName() + "\"" );
                    pnlAttributes.remove( panel );
                    Inspector.this.pack();
                }
            }
        });
    }

    private void buildAttributesManagementPanel() {
        HashSet<String> attrIds = new HashSet<>( this.getObj().getNumberOfMethods() );

        this.pnlAttributes = new JPanel();
        JScrollPane scroll = new JScrollPane( this.pnlAttributes );
        this.pnlAttributes.setLayout( new BoxLayout( this.pnlAttributes, BoxLayout.PAGE_AXIS ) );
        this.pnlAction.add( scroll );
        this.pnlAction.add( Box.createVerticalGlue() );

        for(Attribute attr: this.getObj().getAttributes()) {
            if ( attr instanceof ParentAttribute ) {
                continue;
            }

            String attrName = attr.getName();

            if ( attrIds.contains( attrName ) ) {
                continue;
            } else {
                attrIds.add( attrName );
            }

           this.addAttributePanel( attr );
        }
    }

    private void addMethodPanel(Method mth) {
        String mthName = mth.getName();
        JPanel panel = new JPanel();
        panel.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
        panel.setLayout( new BoxLayout( panel, BoxLayout.LINE_AXIS ) );

        JButton btDelete = Util.createButton( this.iconDelete, "Delete" );
        JButton btExecute = Util.createButton( this.iconExecute, "Execute" );
        JTextField edName = new JTextField( mthName );
        JTextField edContents = new JTextField( mth.getMethodBodyAsString() );
        edContents.setHorizontalAlignment( JTextField.RIGHT );
        panel.add( btExecute );
        panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        panel.add( btDelete );
        panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        panel.add( edName );
        panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
        panel.add( edContents );
        this.pnlMethods.add( panel );
        this.pnlMethods.add( Box.createVerticalGlue() );

        edName.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( !Inspector.this.beingBuilt ) {
                    Inspector.this.visualEngine.execute(
                            getObj().getPath()
                            + " renameMethod \"" + mth.getName()
                            + "\" \"" + edName.getText() + "\""  );
                    edName.setText( mth.getName() );
                }
            }
        } );

        edContents.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( !Inspector.this.beingBuilt ) {
                    visualEngine.execute( getObj().getPath() + "." + edName.getText() + " = " + edContents.getText() );
                }
            }
        } );

        btExecute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if ( !Inspector.this.beingBuilt ) {
                    Inspector.this.launchMethodExecution( mth.getName() );
                }
            }
        });

        btDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if ( !Inspector.this.beingBuilt ) {
                    visualEngine.execute( obj.getPath() + " erase \"" + mth.getName() + "\"" );
                    pnlMethods.remove( panel );
                    Inspector.this.pack();
                }
            }
        });
    }

    private void buildMethodsManagementPanel() {
        this.pnlMethods = new JPanel();
        JScrollPane scroll = new JScrollPane( this.pnlMethods );
        this.pnlMethods.setLayout( new BoxLayout( this.pnlMethods, BoxLayout.PAGE_AXIS ) );
        this.pnlAction.add( scroll );
        this.pnlAction.add( Box.createVerticalGlue() );

        HashSet<String> mthIds = new HashSet<>( this.getObj().getNumberOfMethods() );
        ObjectBag obj = this.getObj();
        do {
            for (Method mth : obj.getLocalMethods()) {
                String mthName = mth.getName();

                if ( mthIds.contains( mthName ) ) {
                    continue;
                } else {
                    mthIds.add( mthName );
                }

                this.addMethodPanel( mth );
            }

            obj = obj.getParentObject();
        } while( obj != objRoot
              && obj != null );
    }

    private void buildActionArea() {
        // Create a box layout in the center
        this.pnlAction = new JPanel();
        JScrollPane scroll = new JScrollPane( this.pnlAction );
        this.pnlAction.setLayout( new BoxLayout( this.pnlAction, BoxLayout.PAGE_AXIS ) );
        this.add( scroll, BorderLayout.CENTER );

        this.buildBasicAttrsPanel();
        this.buildManagementButtonsPanel();
        this.buildAttributesManagementPanel();
        this.buildMethodsManagementPanel();
    }

    private void build() {
        this.prepareAvailableObjectsNames();

        // Build
        this.setLayout( new BorderLayout( 5, 5 ) );
        this.buildIcons();
        this.buildCloseButton();
        this.buildActionArea();

        // Configure
        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        this.setModal( true );
        this.pack();
        this.updateObjectBasicAttributes();
        this.setMinimumSize( new Dimension( 300, 200 ) );

        // Resize and put it in its location
        Point p = new Point( this.visualEngine.getLocation() );
        p.x += ( this.visualEngine.getWidth() - this.getWidth() ) / 2;
        p.y += ( this.visualEngine.getHeight() - this.getHeight() ) / 2;
        this.setLocation( p );
    }

    private void updateObjectBasicAttributes() {
        ObjectBag obj = this.getObj();
        ObjectBag objParent = this.getObj().getParentObject();

        if ( obj != null ) {
            this.setTitle( this.getObj().getPath() + " - Inspector" );
            this.edObjectName.setText( this.getObj().getName() );
        } else {
            this.setTitle( "Inspector" );
        }

        if ( objParent != null
          && !( obj instanceof ObjectParent ) )
        {
            this.cbParent.setSelectedItem( objParent.getName() );
        }
    }

    private void launchMethodExecution(String methodName)
    {
        String arguments = "";
        Method mth = this.getObj().lookUpMethod( methodName );

        if ( mth.getNumParams() > 0 ) {
            arguments = (String) JOptionPane.showInputDialog(
                    this,
                    "Method's arguments:",
                    AppInfo.Name,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "0" );

            if ( arguments != null ) {
                arguments = arguments.trim();
                if ( arguments.length() > 0 ) {
                    visualEngine.execute( obj.getPath() + " " + methodName + " " + arguments );
                    this.setVisible( false );
                }
            }
        }
    }

    private void copyObject()
    {
        String newName = (String) JOptionPane.showInputDialog(
                this,
                "New object's name:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "obj" + ( visualEngine.getInterpreter().getRuntime().getRoot().getNumberOfAttributes() + 1 ) );

        if ( newName != null ) {
            newName = newName.trim();
            if ( newName.length() > 0 ) {
                visualEngine.execute( "(" + obj.getPath() + " copy) rename \"" + newName + '\"' );
                this.setVisible( false );
            }
        }
    }

    public ObjectBag getObj() {
        return this.obj;
    }

    public static void addAttribute(Window frame, VisualEngine visualEngine, ObjectBag obj) {
        String attrName = (String) JOptionPane.showInputDialog(
                frame,
                "New attribute's name:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "x" );

        if ( attrName != null ) {
            attrName = attrName.trim();

            if ( attrName.length() > 0 ) {
                visualEngine.execute( obj.getPath() + "." + attrName + " = 0" );
            }
        }
    }

    public static void addMethod(Window frame, VisualEngine visualEngine, ObjectBag obj) {
        String mthName = (String) JOptionPane.showInputDialog(
                frame,
                "New method's name:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "f" );

        if ( mthName != null ) {
            mthName = mthName.trim();
            if ( mthName.length() > 0 ) {
                changeMethod( frame, visualEngine, obj, mthName, "{:}" );
            }
        }

        return;
    }

    public static void changeMethod(Window frame, VisualEngine visualEngine, ObjectBag obj, String name, String body)
    {
        String mthBody = (String) JOptionPane.showInputDialog(
                frame,
                name + "'s body:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                body );

        if ( mthBody != null ) {
            mthBody = mthBody.trim();
            if ( mthBody.length() > 0 ) {
                visualEngine.execute( obj.getPath() + "." + name + " = " + mthBody );
            }

        }

        return;
    }

    public String[] prepareAvailableObjectsNames() {
        final Attribute[] attrs = this.rt.getRoot().getAttributes();
        this.availableObjectsNames = new String[ attrs.length ];
        for(int i = 0; i < attrs.length; ++i) {
            this.availableObjectsNames[ i ] = attrs[ i ].getName();
        }

        return this.availableObjectsNames;
    }

    private ObjectBag obj;
    private Runtime rt;
    private ObjectBag objRoot;
    private String[] availableObjectsNames;
    private Boolean beingBuilt;

    private JButton btClose;
    private JTextField edObjectName;
    private JComboBox cbParent;
    private JPanel pnlAction;
    private JPanel pnlAttributes;
    private JPanel pnlMethods;
    private ImageIcon iconCopy;
    private ImageIcon iconAddMethod;
    private ImageIcon iconAddAttribute;
    private ImageIcon iconDelete;
    private ImageIcon iconExecute;
    private VisualEngine visualEngine;
}
