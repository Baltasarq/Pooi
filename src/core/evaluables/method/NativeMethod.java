package core.evaluables.method;

import core.Evaluable;
import core.ObjectBag;
import core.evaluables.Method;
import core.exceps.InterpretError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Representing available methods in the interpreter
 * @author baltasarq
 */
public abstract class NativeMethod extends Method {


    public NativeMethod(String name)
    {
        super( name );
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

    public String toString()
    {
        return ( this.getName() + " = {: }" );
    }
}
