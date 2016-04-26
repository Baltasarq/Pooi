package com.devbaltasarq.pooi.core.objs;

import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

/**
 *
 * @author baltasarq
 */
public class ObjectStr extends ValueObject {

    /** Creates a new instance of ObjectStr */
    public ObjectStr(String n, ObjectBag obj, ObjectBag container) throws InterpretError
    {
        super( n, obj, container );
        value = "";
    }

    @Override
    public String toString()
    {
        return '\"' + value + '\"';
    }

    public void assign(String str)
    {
        value = str;
    }

    public String getValue() {
        return value;
    }

    public Object getValueObject()
    {
        return this.getValue();
    }

    @Override
    public ObjectStr copy(String name, ObjectBag container) throws InterpretError
    {
        // Copy
        ObjectStr toret = new ObjectStr(
                name, getParentObject(), container
        );

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
     * @throws com.devbaltasarq.pooi.core.exceps.InterpretError
     */
    public ObjectStr createChild(String name, ObjectBag container)
            throws InterpretError
    {
        // Create the new object
        ObjectStr toret = new ObjectStr( name, this, container );
        container.set( name, toret );

        return toret;
    }

    private String value;
}
