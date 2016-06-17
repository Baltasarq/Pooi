package com.devbaltasarq.pooi.ui;

import com.devbaltasarq.pooi.core.AppInfo;
import com.devbaltasarq.pooi.core.Interpreter;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.InterpreterCfg;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;

/**
 * The main class, triggering the application.
 */
public class App {
    public static final String EtqVersionOption = "--version";
    public static final String EtqNoGuiOption = "--nogui";
    public static final String EtqQuietOption = "--quiet";
    public static final String EtqHelpOption = "--help";

    /** Shows the help of the program, on console */
    public static void showHelp() {
        System.out.println( "\n--version\tShows version information"
                + "\n--nogui\t\tStarts a console interpreter"
                + "\n--quiet\t\tNo verbosity"
                + "\n--help\t\tThis help"
        );
    }

    /** Process the possible arguments of the command line */
    public static void processArgs(InterpreterCfg cfg, String[] args) {
        for(int i = 0; i < args.length; ++i) {
            String arg = args[ i ];

            if ( arg.startsWith( "--" ) ) {
                if ( arg.equals( EtqVersionOption ) ) {
                    System.out.println( AppInfo.getMsgVersion() );
                }
                else
                if ( arg.equals( EtqNoGuiOption ) ) {
                    cfg.setHasGui( false );
                }
                else
                if ( arg.equals( EtqQuietOption ) ) {
                    cfg.setVerbose( false );
                }
                else
                if ( arg.equals( EtqHelpOption ) ) {
                    showHelp();
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

    public static void guiApp(Interpreter interpreter, String msg)
    {
        // Prepare look & feel, if possible
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch(Exception ignored) {
        }

        // Prepare VisualEngine
        final VisualEngine g = new VisualEngine( interpreter );

        try {
            // Run Gui interpreter
            g.setVisible( true );
            g.makeOutput( "\n" + msg + "\n\n" );
        } catch(Exception e)
        {
            g.makeOutput( "\n\nUnexpected error:\n" + e.getLocalizedMessage() + "\n" );
            g.deactivateGui();
        }

    }

    public static void consoleApp(Interpreter terp, String msg) {
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
