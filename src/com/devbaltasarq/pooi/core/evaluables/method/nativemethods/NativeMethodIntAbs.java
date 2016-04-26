package com.devbaltasarq.pooi.core.evaluables.method.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.method.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectInt;

/**
 * Absolute number
 * @author baltasarq
 */
public class NativeMethodIntAbs extends NativeMethod {
    public static final String EtqMthIntAbs = "abs";

    public NativeMethodIntAbs()
    {
        super( EtqMthIntAbs );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectInt) ) {
            throw new InterpretError(
                    "Semantic error: expected Int in '"
                    + ref.getName()
                    + '\''
            );
        }

        final long result = Math.abs( ( (ObjectInt) ref ).getValue() );

        msg.append( Long.toString( result ) );
        return rt.createInt( result );
    }
}
