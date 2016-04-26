/*
 * ObjectBool.java
 *
 * Created on 1 de febrero de 2008, 17:55
 */

package com.devbaltasarq.pooi.core.objs;

import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

/**
 * Represents boolean objects
 * @author baltasarq
 */
public class ObjectBool extends ValueObject {

    /** Creates a new instance of ObjectBool */
    public ObjectBool(String n, ObjectBag obj, ObjectBag container) throws InterpretError
    {
        super( n, obj, container );
    }
    
    @Override
    public String toString()
    {
        return Boolean.toString( getValue() );
    }

    public boolean getValue() {
        return value;
    }

    public Object getValueObject()
    {
        return this.getValue();
    }
    
    public void assign(boolean value)
    {
        this.value = value;
    }

    @Override
    public ObjectBool copy(String name, ObjectBag litsContainer) throws InterpretError
    {
        // Copy
        ObjectBool toret = new ObjectBool( name, this.getParentObject(), litsContainer  );
        toret.assign( getValue() );
        litsContainer.set( name, toret );

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
    public ObjectBool createChild(String name, ObjectBag container)
            throws InterpretError
    {
        // Create the new object
        ObjectBool toret = new ObjectBool( name, this, container );
        container.set( name, toret );

        return toret;
    }

    public String assign(String value) {
        String toret = "ok: " + value;
        
        try {
            boolean i = Boolean.parseBoolean(value);
            assign( i );
        }
        catch(NumberFormatException e) {
            toret = "not a boolean: '" + value + "\'";
        }
        
        return toret;
    }

    private boolean value;
}
