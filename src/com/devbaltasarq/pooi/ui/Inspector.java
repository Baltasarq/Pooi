package com.devbaltasarq.pooi.ui;

import com.devbaltasarq.pooi.core.AppInfo;
import com.devbaltasarq.pooi.core.ObjectBag;
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
        JButton addAttribute = new JButton( "+Attr" );
        JButton addMethod = new JButton( "+Mth" );
        JButton rename = new JButton( "Ren" );
        pnlButtons.add( addAttribute );
        pnlButtons.add( new JSeparator( SwingConstants.HORIZONTAL ) );
        pnlButtons.add( addMethod );
        pnlButtons.add( new JSeparator( SwingConstants.HORIZONTAL ) );
        pnlButtons.add( rename );
        pnlAction.add( pnlButtons );

        addAttribute.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Inspector.this.addAttribute();
            }
        } );

        addMethod.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Inspector.this.addMethod();
            }
        } );

        rename.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Inspector.this.rename();
            }
        } );

        // Add controls for each member
        for(Attribute atr: this.getObj().getAttributes()) {
            ObjectBag objDest = atr.getReference();
            JPanel panel = new JPanel();
            panel.setBorder(new EmptyBorder( 10, 10, 10, 10 ) );
            panel.setLayout( new BoxLayout( panel, BoxLayout.LINE_AXIS ) );
            JLabel contents = new JLabel( objDest.getName() );
            JLabel lblName = new JLabel( atr.getName() );
            panel.add( lblName );
            panel.add( new JSeparator( SwingConstants.HORIZONTAL ) );
            panel.add( contents );
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
            contents.addMouseListener( lblName.getMouseListeners()[ 0 ] );

            if ( objDest instanceof ValueObject ) {
                contents.setText( objDest.toString() );
            }
        }

        for(Method mth: this.getObj().getLocalMethods()) {
            JPanel panel = new JPanel();
            JLabel lblName = new JLabel( mth.getName() + "()" );
            JLabel contents = new JLabel( "{...}" );
            panel.add( lblName, BorderLayout.CENTER );
            panel.add( contents, BorderLayout.EAST );
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
                   // Inspector.this.launchMethodExecution();
                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {

                }
            });
        }
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

    private void rename() {
        String attrName = (String) JOptionPane.showInputDialog(
                this,
                "New object's name:",
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "x" );

        app.simulate( obj.getPath() + " rename \"" + attrName + '\"' );
        this.setVisible( false );
    }

    public ObjectBag getObj() {
        return this.obj;
    }

    private ObjectBag obj;

    private JButton btClose;
    private JPanel pnlAction;
    private Gui app;
}
