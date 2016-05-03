package com.devbaltasarq.pooi.ui;

import com.devbaltasarq.pooi.core.AppInfo;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.Attribute;
import com.devbaltasarq.pooi.core.evaluables.Method;
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
    public Inspector(Gui parent, ObjectBag obj) {
        try {
            this.objRoot = Runtime.getRuntime().getAbsoluteParent();
        }
        catch(Exception exc) {
            this.setVisible( false );
            JOptionPane.showMessageDialog( this, "Unexpected ERROR retrieveing root object" );
        }

        this.app = parent;
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

        // Add controls for each member
        for(Attribute atr: this.getObj().getAttributes()) {
            ObjectBag objDest = atr.getReference();
            JPanel panel = new JPanel();
            panel.setBorder(new EmptyBorder( 10, 10, 10, 10 ) );
            panel.setLayout( new BoxLayout( panel, BoxLayout.LINE_AXIS ) );
            JLabel lblContents = new JLabel( objDest.getName() );
            JLabel lblName = new JLabel( atr.getName() );
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

            if ( objDest instanceof ValueObject ) {
                lblContents.setText( objDest.toString() );
            }

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
        app.simulate( this.obj.getPath() + "." + id );
    }

    private void addAttribute() {
        String attrName = (String) JOptionPane.showInputDialog(
                this,
                "New attribute's name:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "x" );

        app.simulate( obj.getPath() + "." + attrName + " = 0" );
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
        }

        app.simulate( obj.getPath() + " " + methodName + " " + arguments );
        this.setVisible( false );
    }

    private void addMethod() {
        String mthName = (String) JOptionPane.showInputDialog(
                this,
                "New method's name:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "x" );

        String mthBody = (String) JOptionPane.showInputDialog(
                this,
                mthName + "'s body:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "{: }" );

        app.simulate( this.getObj().getPath() + "." + mthName + " = " + mthBody );
        this.setVisible( false );
    }

    private void changeMethod(String methodName, String body) {
        String mthBody = (String) JOptionPane.showInputDialog(
                this,
                methodName + "'s body:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                body );

        app.simulate( this.getObj().getPath() + "." + methodName + " = " + mthBody );
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

        app.simulate( obj.getPath() + " rename \"" + newName + '\"' );
        this.setVisible( false );
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

        app.simulate( "(" + obj.getPath() + " copy) rename \"" + newName + '\"' );
        this.setVisible( false );
    }

    private void changeAttributeValue(String attrName, String currentValue) {
        String newValue = (String) JOptionPane.showInputDialog(
                this,
                attrName + "'s value:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                currentValue );

        app.simulate( obj.getPath() + "." + attrName + " = " + newValue );
        this.setVisible( false );
    }

    public ObjectBag getObj() {
        return this.obj;
    }

    private ObjectBag obj;

    private JButton btClose;
    private JPanel pnlAction;
    private Gui app;
    private ObjectBag objRoot;
}
