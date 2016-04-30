package com.devbaltasarq.pooi.ui;

import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.evaluables.Attribute;
import com.devbaltasarq.pooi.core.evaluables.Method;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Attribute;

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

        // Add controls for each member
        for(Attribute atr: this.getObj().getAttributes()) {
            JPanel panel = new JPanel();
            JLabel lblName = new JLabel( atr.getName() );
            panel.add( lblName, BorderLayout.CENTER );
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
        }

        for(Method mth: this.getObj().getLocalMethods()) {
            JPanel panel = new JPanel();
            JLabel lblName = new JLabel( mth.getName() + "()" );
            panel.add( lblName, BorderLayout.CENTER );
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
                    Inspector.this.launchMethodExecution();
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

    private void launchMethodExecution() {

    }

    public ObjectBag getObj() {
        return this.obj;
    }

    private ObjectBag obj;

    private JButton btClose;
    private JPanel pnlAction;
    private Gui app;
}
