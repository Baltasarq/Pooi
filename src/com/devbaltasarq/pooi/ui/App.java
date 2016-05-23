package com.devbaltasarq.pooi.ui;

import com.devbaltasarq.pooi.core.Interpreter;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

import javax.swing.*;

/**
 * The main class, triggering the application.
 */
public class App {
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Interpreter interpreter = null;

        // Prepare the interpreter (the world of objects)
        try {
            interpreter = new Interpreter( false, false );
        } catch(InterpretError exc) {
            System.err.println( "PANIC (error bootstrapping): " + exc.getMessage() );
            System.exit( -1 );
        }

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch(Exception ignored) {
        }

        // Prepare VisualEngine
        final VisualEngine g = new VisualEngine( interpreter );

        try {
            // Prepare interpreter & run
            java.awt.EventQueue.invokeLater( new Runnable() {
                @Override
                public void run() {
                    g.setVisible( true );
                    g.reset();
                }
            } );
        } catch(Exception e)
        {
            g.makeOutput( "\n\nUnexpected error:\n" + e.getLocalizedMessage() + "\n" );
            g.deactivateGui();
        }
    }
}
