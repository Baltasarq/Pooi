// Literal.java

package core.evaluables;

import core.Evaluable;

/**
 * Literals base class.
 * User: baltasarq
 * Date: 11/19/12
 */
public abstract class Literal extends Evaluable {

    public Literal(Object value)
    {
        this.setValue( value );
    }

    public Object getValue()
    {
        return this.value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public abstract String toString();

    private Object value;
}
