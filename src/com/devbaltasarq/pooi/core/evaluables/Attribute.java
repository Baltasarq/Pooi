/*
 * Attribute.java
 *
 * Created on 1 de febrero de 2008, 13:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.devbaltasarq.pooi.core.evaluables;

import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.objs.ValueObject;

/**
 * Represents attributes inside objects
 * @author baltasarq
 */
public class Attribute extends Member {
    
    /** Creates a new instance of Attribute */
    public Attribute(String n, ObjectBag ref) {
        super( n );
        this.setReference( ref );
    }

    /** Changes the object this attributes points to
     * @param obj The object this attribute will point to
     */
    public void setReference(ObjectBag obj) {

        reference = obj;
    }

    /**
     * Get the object this attributes points to
     * @return The ObjectBag this attribute points to
     */
    public ObjectBag getReference()
    {
        return reference;
    }

    /**
     * Returns the kind (short description of the object pointed) of the reference
     * @return A String describing the kind of the object pointed
     * @see ObjectBag
     */
    public String getKind()
    {
        String toret = this.getReference().getKind();

        if ( this.reference instanceof ValueObject ) {
            toret = this.getReference().getKind();
        }

        return toret;
    }

    /**
     * Converts the attribute info to a String
     * @return A String with the attribute info
     */
    @Override
    public String toString()
    {
        return ( this.getName() + " = " + reference.toString() );
    }

    private ObjectBag reference;
}
