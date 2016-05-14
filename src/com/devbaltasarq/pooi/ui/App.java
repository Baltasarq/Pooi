package com.devbaltasarq.pooi.ui;

import javax.swing.*;

/**
 * The main class, triggering the application.
 */
public class App {
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

        // Prepare VisualEngine
        final VisualEngine g = new VisualEngine();

        try {
            // Prepare interpreter & run
            java.awt.EventQueue.invokeLater( new Runnable() {
                @Override
                public void run() {
                    g.setVisible( true );  g.reset();
                }
            } );
        } catch(Exception e)
        {
            g.makeOutput( "\n\nUnexpected error:\n" + e.getLocalizedMessage() + "\n" );
            g.deactivateGui();
        }
    }
}
