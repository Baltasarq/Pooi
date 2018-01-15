package com.devbaltasarq.pooi.ui;

import com.devbaltasarq.pooi.core.AppInfo;

import javax.swing.*;
import java.awt.*;

/**
 * A set of Swing utilities commonly used.
 * Created by Baltasar on 17/05/2016.
 */
public class Util {
    /** @return A JButton in which the text is shown if the icon is null */
    public static JButton createButton(ImageIcon icon, String text) {
        JButton toret;

        if ( icon != null ) {
            toret = new JButton( icon );
        } else {
            toret = new JButton( text );
        }

        toret.setToolTipText( text );
        return toret;
    }

    /** Prepares a suitable dimension for a main window */
    public static void prepareDimensions(JFrame window)
    {
        Dimension realDimension = new Dimension( 640, 480 );

        // Prepare window dimensions
        Dimension scrDim = Toolkit.getDefaultToolkit().getScreenSize();

        if ( scrDim.getWidth() > 1280 ) {
            realDimension.height = 768;
            realDimension.width = 1280;

            if ( scrDim.getHeight() > 800 ) {
                realDimension.height = 1024;
            }
        }
        else
        if ( scrDim.getHeight() > 768 ) {
            realDimension.height = 768;
            realDimension.width = 1024;
        }
        else
        if ( scrDim.getHeight() > 600 ) {
            realDimension.height = 600;
            realDimension.width = 800;
        }

        window.setTitle( AppInfo.Name + " " + AppInfo.Version );
        window.setMinimumSize( new Dimension( 640, 480 ) );
        window.setPreferredSize( realDimension );
    }
}
