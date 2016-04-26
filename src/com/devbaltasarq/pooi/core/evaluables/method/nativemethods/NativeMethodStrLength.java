package com.devbaltasarq.pooi.core.evaluables.method.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.method.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 * length
 * @author baltasarq
 */
public class NativeMethodStrLength extends NativeMethod {
    public static final String EtqMthStrLength = "len";

    public NativeMethodStrLength()
    {
        super( EtqMthStrLength );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectStr) ) {
            throw new InterpretError(
                    "Semantic error: expected string in '"
                    + ref.getName()
                    + '\''
            );
        }

        long result = ( (ObjectStr) ref ).getValue().length();
        msg.append( Long.toString( result ) );
        return rt.createInt( result );
    }
}
