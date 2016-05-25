package com.devbaltasarq.pooi.ui;

import com.devbaltasarq.pooi.core.AppInfo;
import com.devbaltasarq.pooi.core.Interpreter;
import com.devbaltasarq.pooi.core.InterpreterCfg;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

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
        com.devbaltasarq.pooi.core.Runtime rt = null;
        Interpreter interpreter = null;
        InterpreterCfg terpCfg = new InterpreterCfg();
        processArgs( terpCfg, args );

        // Prepare the interpreter (the world of objects)
        try {
            rt = com.devbaltasarq.pooi.core.Runtime.getRuntime();
            interpreter = new Interpreter( rt, terpCfg );
        } catch(InterpretError exc) {
            System.err.println( "PANIC (error bootstrapping): " + exc.getMessage() );
            System.exit( -1 );
        }

        if ( terpCfg.hasGui() ) {
            guiApp( interpreter, terpCfg );
        } else {
            consoleApp( interpreter, terpCfg );
        }
    }

    public static void guiApp(Interpreter interpreter, InterpreterCfg cfg)
    {
        // Prepare look & feel, if possible
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch(Exception ignored) {
        }

        // Prepare VisualEngine
        final VisualEngine g = new VisualEngine( interpreter, cfg );

        try {
            // Run Gui interpreter
            java.awt.EventQueue.invokeLater( new Runnable() {
                @Override
                public void run() {
                    g.setVisible( true );
                }
            } );
        } catch(Exception e)
        {
            g.makeOutput( "\n\nUnexpected error:\n" + e.getLocalizedMessage() + "\n" );
            g.deactivateGui();
        }

    }

    public static void consoleApp(Interpreter interpreter, InterpreterCfg cfg) {
        String input = null;
        Scanner scan = new Scanner( System.in );

        System.out.println( "\n\nWelcome to " + AppInfo.Name + " (\"os exit\" to exit)" );
        do {
            System.out.print( "\n> " );
            input = scan.nextLine();
            System.out.print( interpreter.interpret( input  ) );
        } while( true );
    }
}
