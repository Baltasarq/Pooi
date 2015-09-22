// ObjectFloat.java

package core.objs;

import core.ObjectBag;
import core.exceps.InterpretError;

/**
 * Represents floating point objects
 * User: baltasarq
 * Date: 11/15/12
  */
public class ObjectReal extends ValueObject {
    /** Creates a new instance of ObjectReal */
    public ObjectReal(String n, ObjectBag obj, ObjectBag container) throws InterpretError
    {
        super( n, obj, container );
    }

    @Override
    public String toString()
    {
        return Double.toString( this.getValue() );
    }

    public double getValue() {
        return value;
    }

    public Object getValueObject()
    {
        return this.getValue();
    }

    public void assign(double num)
    {
        value = num;
    }

    @Override
    public ObjectReal copy(String name, ObjectBag container)
            throws InterpretError
    {
        // Copy
        ObjectReal toret =
                new ObjectReal( name,
                        this.getParentObject(),
                        container )
                ;

        toret.assign( getValue() );
        container.set( name, toret );

        return toret;
    }

    /**
     * Creates an empty child of this object in the container's collection,
     * with a new (provided) name
     *
     * @param name The name of the object. Assigned automatically if empty.
     * @param container The object in which to include the new object
     * @return An ObjectBag reference to the new (copied) object
     * @throws core.exceps.InterpretError
     */
    public ObjectReal createChild(String name, ObjectBag container)
            throws InterpretError
    {
        // Create the new object
        ObjectReal toret = new ObjectReal( name, this, container );
        container.set( name, toret );

        return toret;
    }

    public String assign(String num) {
        String toret = "ok: " + num;

        try {
            double i = Double.parseDouble( num );
            assign( i );
        }
        catch(NumberFormatException e) {
            toret = "not a number: '" + num + "\'";
        }

        return toret;
    }

    private double value;
}
