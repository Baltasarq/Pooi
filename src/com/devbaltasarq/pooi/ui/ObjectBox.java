package com.devbaltasarq.pooi.ui;

import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.evaluables.Attribute;
import com.devbaltasarq.pooi.core.evaluables.Method;
import com.devbaltasarq.pooi.core.evaluables.ParentAttribute;
import com.devbaltasarq.pooi.core.objs.ValueObject;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by baltasarq on 27/04/16.
 */
public class ObjectBox {
    public static final int MaxVisibleLength = 25;
    public static final int HorizontalPadding = 20;
    public static final int VerticalPadding = 20;
    public static final int VerticalSeparation = 2;

    public ObjectBox(ObjectBag obj)
    {
        this.obj = obj;
        this.x = 0;
        this.y = 0;
        this.infoLines = new String[0];
    }

    public ObjectBox(ObjectBag obj, int x, int y)
    {
        this( obj );
        this.x = x;
        this.y = y;
    }

    public ObjectBag getObj() {
        return this.obj;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Dimension getMeasuredDimension() {
        return this.measuredDimension;
    }

    public Dimension getCurrentDimension() {
        return this.currentDimension;
    }

    public void draw(Canvas canvas)
    {
        this.draw( canvas, this.x, this.y  );
    }

    public Dimension prepareDrawing(Canvas canvas)
    {
        ArrayList<String> lines = new ArrayList<>();
        int maxLength = 0;
        this.measuredDimension = new Dimension();

        if ( this.getObj() != null ) {
            // Name
            lines.add( this.getObj().getName() );
            maxLength = Math.max( this.getObj().getName().length(), maxLength );

            // If it is the inheritance root, don't get into detail
            if ( this.getObj().getParentObject() != this.getObj() ) {
                // If it is not root
                if ( this.getObj().getParentObject() != this.getObj() ) {
                    for (Attribute atr : this.getObj().getAttributes()) {
                        if ( atr instanceof ParentAttribute ) {
                            continue;
                        }

                        ObjectBag destObj = atr.getReference();
                        String value = destObj.getName();

                        if ( destObj instanceof ValueObject ) {
                            value = destObj.toString();
                        }

                        // Prepare info string, truncate if needed
                        String line = atr.getName() + ": " + value;
                        if ( line.length() > MaxVisibleLength ) {
                            line = line.substring( 0, MaxVisibleLength ) + "...";
                        }

                        lines.add( line );
                        maxLength = Math.max( line.length(), maxLength );
                    }

                    for (Method mth : this.getObj().getLocalMethods()) {
                        lines.add( mth.getName() + "()" );
                        maxLength = Math.max( lines.get( lines.size() - 1 ).length(), maxLength );
                    }
                }
            }
        }

        // Measure string sizes
        FontMetrics metrics = canvas.getGraphics().getFontMetrics( canvas.getFont() );
        this.measuredDimension.height = ( VerticalPadding * 2 ) + ( ( metrics.getHeight() + VerticalSeparation ) * lines.size() );
        this.measuredDimension.width = ( HorizontalPadding * 2 ) + metrics.stringWidth( new String( new char[ maxLength ]).replace( "\0", "M" ) );

        this.infoLines = new String[ lines.size() ];
        lines.toArray( this.infoLines );
        return this.measuredDimension;
    }

    public void draw(Canvas canvas, int x, int y)
    {
        final int cr = canvas.getGraphics().getFontMetrics( canvas.getFont() ).getHeight() + VerticalSeparation;
        int posX = x + HorizontalPadding;
        int posY = y + VerticalPadding;

        if ( this.measuredDimension == null ) {
            this.prepareDrawing( canvas );
        }

        // Draw box
        canvas.setColor( Color.white );
        canvas.rectangle( x, y, x + this.measuredDimension.width, y  + this.measuredDimension.height, true  );
        canvas.setColor( Color.blue );

        // Name
        canvas.print( posX, posY, this.infoLines[ 0 ] );
        posX += 10;
        posY += cr;

        // Members
        for(int i = 1; i < this.infoLines.length; ++i) {
            canvas.print(posX, posY, this.infoLines[ i ] );
            posY += cr;
        }

        this.currentDimension = this.measuredDimension;
        this.measuredDimension = null;
        canvas.repaint();
    }

    private ObjectBag obj;
    private int x;
    private int y;
    private String[] infoLines;
    private Dimension measuredDimension;
    private Dimension currentDimension;
}
