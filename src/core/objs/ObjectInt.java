/*
 * ObjectInteger.java
 *
 * Created on 1 de febrero de 2008, 17:55
 */

package core.objs;

import core.ObjectBag;
import core.exceps.InterpretError;

/**
 * Represents integer objects
 * @author baltasarq
 */
public class ObjectInt extends ValueObject {

    /**
     * Creates a new instance of ObjectInt
     *
     * @param n the name of the new object
     * @param parent the parent of the new object
     * @param container the object this new object will live in.
     */
    public ObjectInt(String n, ObjectBag parent, ObjectBag container) throws InterpretError
    {
        super( n, parent, container );
    }
    
    @Override
    public String toString()
    {
        return Long.toString( getValue() );
    }

    public long getValue() {
        return value;
    }

    public Object getValueObject()
    {
        return this.getValue();
    }

    public void assign(long num)
    {
        value = num;
    }

    @Override
    public ObjectInt copy(String name, ObjectBag container)
            throws InterpretError
    {
        // Copy
        ObjectInt toret = new ObjectInt( name, this.getParentObject(), container );
        toret.assign( getValue() );
        container.set( name, toret );

        return toret;
    }

    /**
     * Creates an empty child of this object in the container's collection,
     * with a new (provided) name
     *
     * @param name The name of the object.
     * @param container The object in which to include the new object
     * @return An ObjectBag reference to the new (copied) object
     * @throws core.exceps.InterpretError
     */
    public ObjectInt createChild(String name, ObjectBag container)
            throws InterpretError
    {
        // Create the new object
        ObjectInt toret = new ObjectInt( name, this, container );
        container.set( name, toret );

        return toret;
    }

    public String assign(String num)
    {
        String toret = "ok: " + num;
        
        try {
            int i = Integer.parseInt( num );
            assign( i );
        }
        catch(NumberFormatException e) {
            toret = "not a number: '" + num + "\'";
        }
        
        return toret;
    }

    private long value;
}
