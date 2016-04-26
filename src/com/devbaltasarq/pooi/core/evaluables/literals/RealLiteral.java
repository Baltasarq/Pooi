package com.devbaltasarq.pooi.core.evaluables.literals;

import com.devbaltasarq.pooi.core.evaluables.Literal;

/**
 * Represents floating point literals
 * User: baltasarq
 * Date: 11/19/12
 */
public class RealLiteral extends Literal {
    public RealLiteral(double lit)
    {
        super( new Double( lit ) );
    }

    public Double getValue()
    {
        return (Double) super.getValue();
    }

    public void setValue(double x)
    {
        super.setValue( (Double) x );
    }

    @Override
    public String toString() {
        return Double.toString( this.getValue() );
    }
}
