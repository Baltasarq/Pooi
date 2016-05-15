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

/**
 * Created by Baltasar on 30/04/2016.
 */
public class Inspector extends JDialog {
    public static final int MaxCharsContents = 25;

    public Inspector(VisualEngine parent, ObjectBag obj) {
        try {
            this.objRoot = Runtime.getRuntime().getAbsoluteParent();
        }
        catch(Exception exc) {
            this.setVisible( false );
            JOptionPane.showMessageDialog( this, "Unexpected ERROR retrieveing root object" );
        }

        this.visualEngine = parent;
        this.obj = obj;
        this.setIconImage( parent.getIconImage() );
        this.build();
    }

    private void buildCloseButton() {
        this.btClose = new JButton( "Close" );
        this.btClose.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Inspector.this.setVisible( false );
            }
        });
        this.add( this.btClose, BorderLayout.PAGE_END );
    }

    private void buildActionArea() {
        // Create a box layout in the center
        this.pnlAction = new JPanel();
        JScrollPane scroll = new JScrollPane( this.pnlAction );
        this.pnlAction.setLayout( new BoxLayout( this.pnlAction, BoxLayout.PAGE_AXIS ) );
        this.add( scroll, BorderLayout.CENTER );

        // Add buttons for management
        JPanel pnlButtons = new JPanel();
        pnlButtons.setBorder( new EmptyBorder( 10, 10, 10, 10 ) );
        pnlButtons.setLayout( new BoxLayout( pnlButtons, BoxLayout.LINE_AXIS ) );
        JButton btAddAttribute = new JButton( "+Attr" );
        JButton btAddMethod = new JButton( "+Mth" );
        JButton btRename = new JButton( "Ren" );
        JButton btCopy = new JButton( "Copy" );
        pnlButtons.add( btAddAttribute );
        pnlButtons.add( new JSeparator( SwingConstants.HORIZONTAL ) );
        pnlButtons.add( btAddMethod );
        pnlButtons.add( new JSeparator( SwingConstants.HORIZONTAL ) );
        pnlButtons.add( btRename );
        pnlButtons.add( new JSeparator( SwingConstants.HORIZONTAL ) );
        pnlButtons.add( btCopy );
        pnlAction.add( pnlButtons );

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
        for(Attribute atr: this.getObj().getAttributes()) {
            ObjectBag objDest = atr.getReference();
            JPanel panel = new JPanel();
            panel.setBorder(new EmptyBorder( 10, 10, 10, 10 ) );
            panel.setLayout( new BoxLayout( panel, BoxLayout.LINE_AXIS ) );
            JLabel lblContents = new JLabel( objDest.getName() );
            JLabel lblName = new JLabel( atr.getName() );

            if ( objDest instanceof ValueObject ) {
                lblContents.setText( prepareToShowInWindow( objDest.toString() ) );
            }

            panel.add( lblName );
            panel.add( new JSeparator( SwingConstants.HORIZONTAL ) );
            panel.add( lblContents );
            this.pnlAction.add( panel );
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

            lblContents.addMouseListener( new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    Inspector.this.changeAttributeValue( lblName.getText(), lblContents.getText()  );
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

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
                JLabel lblContents = new JLabel( "{:}" );
                panel.add( lblName );
                panel.add( new JSeparator( SwingConstants.HORIZONTAL ) );
                panel.add( lblContents );
                this.pnlAction.add( panel );
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

                lblContents.addMouseListener( new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        Inspector.this.changeMethod( lblName.getText(), lblContents.getText() );
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
        this.buildCloseButton();
        this.buildActionArea();

        this.setTitle( this.obj.getPath() );
        this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        this.setMinimumSize( new Dimension( 300, 200 ) );
        this.setModal( true );
        this.pack();
        this.setVisible( true );
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

    private void changeAttributeValue(String attrName, String currentValue) {
        try {
            // Get the names of the available objects
            final Runtime rt = Runtime.getRuntime();
            Attribute[] attrs = rt.getRoot().getAttributes();
            String[] objsNames = new String[ attrs.length + 1 ];
            objsNames[ 0 ] = "0";
            for(int i = 0; i < attrs.length; ++i) {
                objsNames[ i + 1 ] = attrs[ i ].getName();
            }

            // Create the combo box dialog
            JComboBox cbObjectNames = new JComboBox( objsNames );
            cbObjectNames.setEditable( true );
            JOptionPane.showMessageDialog( this, cbObjectNames, attrName + "'s value:", JOptionPane.QUESTION_MESSAGE);

            String newValue = (String) cbObjectNames.getSelectedItem();
            if ( newValue != null ) {
                newValue = newValue.trim();
                if ( newValue.length() > 0 ) {
                    visualEngine.execute( obj.getPath() + "." + attrName + " = " + newValue );
                    this.setVisible( false );
                }
            }
        } catch(InterpretError exc) {
            visualEngine.makeOutput( "Interpreter error: " + exc.getMessage() );
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

    /** Prepares a string to be shown in a window (no CR's...) */
    public static String prepareToShowInWindow(String msg) {
        if ( msg.length() > MaxCharsContents ) {
            msg = msg.substring( 0, MaxCharsContents ) + "...\"";
        }

        msg = msg.replace( '\n', ' ' );
        msg = msg.replace( '\t', ' ' );

        return msg;
    }

    private ObjectBag obj;

    private JButton btClose;
    private JPanel pnlAction;
    private VisualEngine visualEngine;
    private ObjectBag objRoot;
}
