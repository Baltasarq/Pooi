package com.devbaltasarq.pooi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by baltasarq on 27/04/16.
 */
public class Canvas extends JPanel {
    /** Creates a new canvas with the specified dimensions
     * @param width The width of the drawing area.
     * @param height The height of the drawing area.
     */
    public Canvas(int width, int height)
    {
        this.setMinimumSize( new Dimension( width, height ) );

        canvas  = new BufferedImage( width, height,  BufferedImage.TYPE_INT_RGB);
        JLabel canvasFrame = new JLabel( new ImageIcon( canvas ) );
        canvasFrame.setPreferredSize( new Dimension( width, height ) );
        canvasFrame.setMinimumSize( new Dimension( width, height ) );
        this.add( canvasFrame );
        this.setColor( Color.black );
        this.setBackgroundColor( Color.white );
        this.setFontSize( 10  );
    }

    /** Gets the foreground color
     * @return The foreground color, as a Color object
     */
    public Color getColor() {
        return this.color;
    }

    /** Modifies the foreground color
     * @param c The new foreground color, as a Color object.
     */
    public void setColor(Color c) {
        this.color = c;
    }

    /** Gets the background color.
     * @return The background color, as a Color object.
     */
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    /** Modifies the background color
     * @param c The new background color, as a Color object.
     */

    public void setBackgroundColor(Color c) {
        this.backgroundColor = c;
        this.cls();
    }

    /** Clears the drawing area */
    public void cls() {
        final Graphics grfs = this.canvas.getGraphics();

        grfs.setColor( this.getBackgroundColor() );
        grfs.fillRect( 0, 0, this.getWidth(), this.getHeight()  );
    }

    /** Draws a new line
     * @param x1 x coordinate first point
     * @param y1 y coordinate first point
     * @param x2 x coordinate second point
     * @param y2 y coordinate second point
     */
    public void drawLine(int x1, int y1, int x2, int y2)
    {
        final Graphics grfs = this.canvas.getGraphics();

        grfs.setColor( this.getColor() );
        grfs.drawLine( x1, y1, x2, y2  );
    }

    /**
     * Draws a rectangle in the graphics window, given its
     * starting and ending point.
     * @param x1 the x coordinate of the starting point
     * @param y1 the y coordinate of the starting point
     * @param x2 the x coordinate of the ending point
     * @param y2 the y coordinate of the ending point
     * @param filled whether to fill the rectangle or not
     */
    public void rectangle(int x1, int y1, int x2, int y2, boolean filled) {
        final Graphics grfs = this.canvas.getGraphics();

        if ( x1 > x2 ) {
            int t = x1;
            x1 = x2;
            x2 = t;
        }

        if ( y1 > y2 ) {
            int t = y1;
            y1 = y2;
            y2 = t;
        }

        final int rectWidth = Math.abs( x2 - x1 );
        final int rectHeight = Math.abs( y2 - y1 );

        grfs.setColor( this.getColor() );

        if ( filled ) {
            grfs.fillRect( x1, y1, rectWidth, rectHeight );
        } else {
            grfs.drawRect( x1, y1, rectWidth, rectHeight );
        }
    }

    /**
     * Draws a circle in the graphics window,
     * given its starting and ending point.
     * @param x the x coordinate of the starting point
     * @param y the y coordinate of the starting point
     * @param r the radius
     * @param filled whether to fill the circle or not
     */
    public void circle(int x, int y, int r, boolean filled)
    {
        Graphics grfs = this.canvas.getGraphics();

        if ( filled ) {
            grfs.drawOval( x, y, r, r );
        } else {
            grfs.fillOval( x, y, r, r  );
        }
    }

    /**
     * Draws a string in the graphic window
     * @param x the x coordinate of the starting point
     * @param y the y coordinate of the starting point
     * @param str the vector of chars containing the string.
     */
    public void print(int x, int y, String str)
    {
        if ( str != null ) {
            final Graphics grfs = canvas.getGraphics();

            grfs.setColor( this.getColor() );
            grfs.drawChars( str.toCharArray(), 0, str.length(), x, y );
        }
    }

    /** Changes the font size
     * @param newSize The font size, as an int
     */
    public void setFontSize(int newSize)
    {
        final Graphics grfs = this.canvas.getGraphics();

        grfs.setFont( new Font( "mono", Font.PLAIN, newSize ) );
        this.fontSize = newSize;
    }

    public int getTextHeight() {
        final Graphics grfs = this.canvas.getGraphics();
        FontMetrics metrics = grfs.getFontMetrics( grfs.getFont() );

        return metrics.getHeight();
    }

    public int getTextWidth(String text) {
        final Graphics grfs = this.canvas.getGraphics();
        FontMetrics metrics = grfs.getFontMetrics( grfs.getFont() );

        return metrics.stringWidth( text );
    }

    private BufferedImage canvas;
    private Color color;
    private Color backgroundColor;
    private int fontSize;
}
