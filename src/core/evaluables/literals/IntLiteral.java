package core.evaluables.literals;

import core.evaluables.Literal;

/**
 * Represents number (integer) literals
 * User: baltasarq
 * Date: 11/19/12
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class IntLiteral extends Literal {
    public IntLiteral(long lit)
    {
        super( new Long( lit ) );
    }

    public Long getValue()
    {
        return (Long) super.getValue();
    }

    public void setValue(long x)
    {
        super.setValue( (Long) x );
    }

    @Override
    public String toString() {
        return Long.toString( this.getValue() );
    }
}
