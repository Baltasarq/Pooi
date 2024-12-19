package com.devbaltasarq.pooi.core.evaluables.literals;

import com.devbaltasarq.pooi.core.evaluables.Literal;

/**
 * Represents boolean literals
 * User: baltasarq
 * Date: 11/19/12
 */
public class BoolLiteral extends Literal {
    public BoolLiteral(boolean lit)
    {
        super( lit );
    }

    public Boolean getValue()
    {
        return (Boolean) super.getValue();
    }

    public void setValue(boolean x)
    {
        super.setValue( (Boolean) x );
    }

    @Override
    public String toString() {
        return Boolean.toString( this.getValue() );
    }
}
