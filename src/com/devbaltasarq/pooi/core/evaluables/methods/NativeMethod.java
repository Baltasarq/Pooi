package com.devbaltasarq.pooi.core.evaluables.methods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.evaluables.Method;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Representing available methods in the interpreter
 * @author baltasarq
 */
public abstract class NativeMethod extends Method {


    public NativeMethod(com.devbaltasarq.pooi.core.Runtime rt, String name)
    {
        super( rt, name );
    }

    /**
     * Execute the method
     */
    public abstract ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError;

    /**
     * Copy the needed NativeMethod by means of reflection
     * @return The copied method, as a Method object.
     */
    @Override
    public NativeMethod copy()
    {
        NativeMethod toret = null;
        Constructor[] ctors = this.getClass().getDeclaredConstructors();
        Constructor ctor = ctors[ 0 ];

        try {
            toret = (NativeMethod) ctor.newInstance( this.getName() );
        } catch (InstantiationException
               | IllegalAccessException
               | InvocationTargetException ignored)
        {
        }

        return toret;
    }

    @Override
    public String getMethodBodyAsString() {
        return "{: }";
    }
}
