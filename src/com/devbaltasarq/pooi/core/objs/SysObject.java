package com.devbaltasarq.pooi.core.objs;

import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.ObjectBag;

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
     * @param rt        The runtime for this object
     * @param n         The name of the object.
     * @param parent    The parent object in which to register it.
     * @param container The object this is contained into.
     */
    public SysObject(com.devbaltasarq.pooi.core.Runtime rt, String n, ObjectBag parent, ObjectBag container)
            throws InterpretError
    {
        super( rt, n, parent, container, ObjectBag.DontCheck );
    }
}
