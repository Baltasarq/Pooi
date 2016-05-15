package com.devbaltasarq.pooi.ui;

import com.devbaltasarq.pooi.core.AppInfo;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.Attribute;
import com.devbaltasarq.pooi.core.evaluables.Method;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ValueObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

/**
 * Created by Baltasar on 30/04/2016.
 */
public class Inspector extends JDialog {
    public static final String EtqIconCopy = "com/devbaltasarq/pooi/res/copy.png";
    public static final String EtqIconAddMethod = "com/devbaltasarq/pooi/res/addMethod.png";
    public static final String EtqIconAddAttribute = "com/devbaltasarq/pooi/res/addAttribute.png";

    public Inspector(VisualEngine parent, ObjectBag obj) {
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

        } catch(Exception exc)
        {
            this.visualEngine.makeOutput( "\n[WARNING: failed to retrieve icons from jar]\n\n" );
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

    private void buildActionArea() {
        // Create a box layout in the center
        this.pnlAction = new JPanel();
        JScrollPane scroll = new JScrollPane( this.pnlAction );
        this.pnlAction.setLayout( new BoxLayout( this.pnlAction, BoxLayout.PAGE_AXIS ) );
        this.add( scroll, BorderLayout.CENTER );

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
        edObjectName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Inspector.this.visualEngine.execute(
                            Inspector.this.getObj().getPath() + " rename \"" + edObjectName.getText() + "\"");
                Inspector.this.updateObjectName();
            }
        });

        // Add buttons for management
        JPanel pnlButtons = new JPanel();
        pnlButtons.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
        pnlButtons.setLayout( new BoxLayout( pnlButtons, BoxLayout.LINE_AXIS ) );
        JButton btAddAttribute = new JButton( this.iconAddAttribute );
        btAddAttribute.setToolTipText( "Add attribute" );
        JButton btAddMethod = new JButton( this.iconAddMethod );
        btAddMethod.setToolTipText( "Add method" );
        JButton btRename = new JButton( "Ren" );
        JButton btCopy = new JButton( this.iconCopy );
        btCopy.setToolTipText( "Copy this object" );
        pnlButtons.add( btAddAttribute );
        pnlButtons.add( Box.createHorizontalGlue() );
        pnlButtons.add( btAddMethod );
        pnlButtons.add( Box.createHorizontalGlue() );
        pnlButtons.add( btRename );
        pnlButtons.add( Box.createHorizontalGlue() );
        pnlButtons.add( btCopy );
        this.pnlAction.add( pnlButtons );
        this.pnlAction.add( Box.createVerticalGlue() );

        btAddAttribute.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Inspector.this.addAttribute();
            }
        } );

        btAddMethod.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Inspector.this.addMethod();
            }
        } );

        btRename.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Inspector.this.renameObject();
            }
        } );

        btCopy.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Inspector.this.copyObject();
            }
        } );

        // Add controls for each attribute
        final Attribute[] attrs = this.rt.getRoot().getAttributes();
        final String[] objsNames = new String[ attrs.length ];
        for(int i = 0; i < attrs.length; ++i) {
            objsNames[ i ] = attrs[ i ].getName();
        }

        for(Attribute atr: this.getObj().getAttributes()) {
            ObjectBag objDest = atr.getReference();
            JPanel panel = new JPanel();
            panel.setBorder(new EmptyBorder( 10, 10, 10, 10 ) );
            panel.setLayout( new BoxLayout( panel, BoxLayout.LINE_AXIS ) );
            JComboBox cbContents = new JComboBox( objsNames );
            ( (JLabel)cbContents.getRenderer() ).setHorizontalAlignment( JLabel.RIGHT );
            cbContents.setFont( this.visualEngine.getFont() );
            cbContents.setEditable( true );
            cbContents.getModel().setSelectedItem( objDest.getNameOrValueAsString() );
            JLabel lblName = new JLabel( atr.getName() );

            panel.add( lblName );
            panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
            panel.add( cbContents );
            this.pnlAction.add( panel );
            this.pnlAction.add( Box.createVerticalGlue() );
            lblName.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {

                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {

                }
                @Override
                public void mouseReleased(MouseEvent mouseEvent) {
                    Inspector.this.launchFieldInspection( atr.getName() );
                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {

                }
            });

            cbContents.addActionListener( new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    visualEngine.execute( obj.getPath() + "." + lblName.getText() + " = " + (String) cbContents.getModel().getSelectedItem() );
                }
            } );
        }

        // Add controls for each local method, as well as inherited ones
        ObjectBag obj = this.getObj();
        do {
            for (Method mth : obj.getLocalMethods()) {
                JPanel panel = new JPanel();
                panel.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
                panel.setLayout( new BoxLayout( panel, BoxLayout.LINE_AXIS ) );
                JLabel lblName = new JLabel( mth.getName() + "()" );
                JTextField edContents = new JTextField( mth.getMethodBodyAsString() );
                edContents.setHorizontalAlignment( JTextField.RIGHT );
                panel.add( lblName );
                panel.add( Box.createRigidArea( new Dimension( 10, 0 ) ) );
                panel.add( edContents );
                this.pnlAction.add( panel );
                this.pnlAction.add( Box.createVerticalGlue() );
                lblName.addMouseListener( new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {
                        Inspector.this.launchMethodExecution( lblName.getText() );
                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {

                    }
                } );

                edContents.addMouseListener( new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        Inspector.this.changeMethod( lblName.getText(), edContents.getText() );
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                } );
            }

            obj = obj.getParentObject();
        } while( obj != objRoot
              && obj != null );
    }

    private void build() {
        this.setLayout( new BorderLayout( 5, 5 ) );
        this.setLocationByPlatform( true  );
        this.buildIcons();
        this.buildCloseButton();
        this.buildActionArea();

        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        this.setMinimumSize( new Dimension( 300, 200 ) );
        this.setModal( true );
        this.pack();
        this.updateObjectName();
        this.setVisible( true );
    }

    private void updateObjectName() {
        this.setTitle( this.getObj().getPath() + " - Inspector" );
        this.edObjectName.setText( this.getObj().getName() );
    }

    private void launchFieldInspection(String id) {
        visualEngine.execute( this.obj.getPath() + "." + id );
    }

    private void addAttribute() {
        addAttribute( this, this.visualEngine, this.getObj() );
        this.setVisible( false );
    }

    private void launchMethodExecution(String methodName)
    {
        String arguments = "";
        methodName = methodName.substring( 0, methodName.length() - 2 );
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

    private void addMethod() {
        addMethod( this, this.visualEngine, this.getObj() );
    }

    private void changeMethod(String methodName, String body) {
        changeMethod( this, this.visualEngine, this.getObj(), methodName, body );
        this.setVisible( false );
    }

    private void renameObject() {
        String newName = (String) JOptionPane.showInputDialog(
                this,
                "New object's name:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "x" );

        if ( newName != null ) {
            newName = newName.trim();
            if ( newName.length() > 0 ) {
                visualEngine.execute( obj.getPath() + " rename \"" + newName + '\"' );
                this.setVisible( false );
            }
        }
    }

    private void copyObject() {
        String newName = (String) JOptionPane.showInputDialog(
                this,
                "New object's name:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "x" );

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
                "x" );

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

    private ObjectBag obj;
    private Runtime rt;
    private ObjectBag objRoot;

    private JButton btClose;
    private JTextField edObjectName;
    private JPanel pnlAction;
    private ImageIcon iconCopy;
    private ImageIcon iconAddMethod;
    private ImageIcon iconAddAttribute;
    private VisualEngine visualEngine;
}
