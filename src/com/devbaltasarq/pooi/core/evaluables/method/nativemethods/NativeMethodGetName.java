package com.devbaltasarq.pooi.core.evaluables.method.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.method.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

/**
 * Returns the name of the object
 * @author baltasarq
 */
public class NativeMethodGetName extends NativeMethod {
    public static final String EtqMthGetName = "name";

    public NativeMethodGetName()
    {
        super( EtqMthGetName );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        msg.append( ref.getName() );
        return rt.createString( ref.getName() );
    }
}
