// (c) 2008 Baltasar MIT License <jbgarcia@uvigo.es>


package com.devbaltasarq.pooi.ui;


import com.devbaltasarq.pooi.core.AppInfo;
import com.devbaltasarq.pooi.core.Interpreter;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.InterpreterCfg;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;


/** The main class, triggering the application. */
public final class App {
    private static final String ETQ_VERSION_OPTION = "--version";
    private static final String ETQ_GUI_OPTION = "--nogui";
    private static final String ETQ_QUITE_OPTION = "--quiet";
    private static final String ETQ_HELP_OPTION = "--help";

    /** Shows the help of the program, on console */
    private static void showHelp()
    {
        System.out.println( "\n--version\tShows version information"
                + "\n--nogui\t\tStarts a console interpreter"
                + "\n--quiet\t\tNo verbosity"
                + "\n--help\t\tThis help"
        );
    }

    /** Process the possible arguments of the command line */
    private static void processArgs(InterpreterCfg cfg, String[] args)
    {
        for(String arg: args) {
            if ( arg.startsWith( "--" ) ) {
                switch ( arg ) {
                    case ETQ_VERSION_OPTION -> 
                            System.out.println( AppInfo.getMsgVersion() );
                    case ETQ_GUI_OPTION -> cfg.setHasGui( false );
                    case ETQ_QUITE_OPTION -> cfg.setVerbose( false );
                    case ETQ_HELP_OPTION -> showHelp();
                    default -> System.err.println( "[ERR] option unknown" );
                }
            }
            else {
                cfg.setScript( new File( arg ) );
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        StringBuffer msg = new StringBuffer();
        com.devbaltasarq.pooi.core.Runtime rt = null;
        Interpreter interpreter = null;
        InterpreterCfg terpCfg = new InterpreterCfg();
        processArgs( terpCfg, args );

        // Prepare the interpreter (the world of objects)
        try {
            rt = com.devbaltasarq.pooi.core.Runtime.getRuntime();
            interpreter = new Interpreter( rt, terpCfg, msg );
        } catch(InterpretError exc) {
            System.err.println( "PANIC (error bootstrapping): " + exc.getMessage() );
            System.exit( -1 );
        }

        if ( terpCfg.hasGui() ) {
            guiApp( interpreter, msg.toString() );
        } else {
            consoleApp( interpreter, msg.toString() );
        }
    }

    private static void guiApp(Interpreter interpreter, String msg)
    {
        // Prepare look & feel, if possible
        try {
            System.setProperty( "swing.aatext", "true" );
            System.setProperty( "awt.useSystemAAFontSettings", "on" );
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch(Exception ignored) {
        }

        // Prepare VisualEngine
        final VisualEngine g = new VisualEngine( interpreter );

        try {
            // Show messages and run interpreter
            new Thread( () -> {
                try {
                    Thread.sleep( 1000 );
                } catch(InterruptedException ignored) {
                }
                finally {
                    g.makeOutput( "\n" + msg + "\n\n" );
                }
            }).start();
            g.setVisible( true );
        } catch(Exception e)
        {
            g.makeOutput( "\n\nUnexpected error:\n" + e.getLocalizedMessage() + "\n" );
            g.deactivateGui();
        }

        return;
    }

    private static void consoleApp(Interpreter terp, String msg) {
        String input = null;
        Scanner scan = new Scanner( System.in );

        try {
            terp.setVerbose( false );
            System.out.println( "\n\nWelcome to " + AppInfo.Name + " (\"os exit\" to exit)" );

            // Load script, if needed
            final File file = terp.getConfiguration().getScript();
            if ( file != null ) {
                System.out.println( "\n" + terp.loadSession( file.getAbsolutePath() ) );
            }

            System.out.print( "\n" + msg + "\n" );

            do {
                System.out.print( "\n> " );
                input = scan.nextLine();
                System.out.print( Interpreter.removeQuotes( terp.interpret( input ) ) );
            } while( true );
        } catch(InterpretError exc) {
            System.err.println( "FATAL interpreter error: " + exc.getMessage() );
        } catch(Exception exc) {
            System.err.println( "FATAL unexpected error: " + exc.getMessage() );
        }
    }
}
