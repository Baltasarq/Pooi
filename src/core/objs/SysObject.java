package core.objs;

import core.ObjectBag;
import core.evaluables.Attribute;
import core.evaluables.Method;
import core.exceps.InterpretError;

/**
 * Represents objects created by the runtime
 * Their main characteristic is that attributes and methods are not
 * verified (names, cycles) on creation.
 * User: baltasarq
 * Date: 30/11/12
 */
public class SysObject extends ObjectBag {

    /**
     * Creates a new instance of ObjectBag
     * When an empty name is passed, an automatic name is created.
     *
     * @param n         The name of the object.
     * @param parent    The parent object in which to register it.
     * @param container The object this is contained into.
     */
    public SysObject(String n, ObjectBag parent, ObjectBag container)
            throws InterpretError
    {
        super( n, parent, container, ObjectBag.DontCheck );
    }

    public void set(String name, Method mth) throws InterpretError
    {
        this.setMethod( name, mth );
    }

    public void set(String name, ObjectBag obj) throws InterpretError
    {
        final Attribute atr = new Attribute( name, obj );

        this.setAttribute( name, atr );
        atr.getReference().setContainer( this );
        this.setAttribute( name, atr );
    }
}
