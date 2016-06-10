package com.devbaltasarq.pooi.core.objs;

import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.ObjectBag;

/**
 * The base class for all objects that hold values (Int, Bool, Real, Str)
 * @author baltasarq
 */
public abstract class ValueObject extends ObjectBag {
    public ValueObject(com.devbaltasarq.pooi.core.Runtime rt, String n, ObjectBag parent, ObjectBag container)
            throws InterpretError
    {
        super( rt, n, parent, container );
    }

    @Override
    public String getKind() {
        return getName() + ": " + getParentObject().getName() + " = " + this.toString();
    }

    @Override
    public String list()
    {
        StringBuilder toret = new StringBuilder();

        toret.append( super.list() );
        toret.append( " = " );
        toret.append( toString() );

        return toret.toString();
    }

    public abstract Object getValueObject();
}


