package com.devbaltasarq.pooi.core.evaluables.method.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.method.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 *  toReal
 * @author baltasarq
 */
public class NativeMethodStrToReal extends NativeMethod {
    public static final String EtqMthStrToReal = "real";

    public NativeMethodStrToReal()
    {
        super( EtqMthStrToReal );
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

        double result;

        try {
            result = Double.parseDouble( ( (ObjectStr) ref ).getValue() );
        }
        catch(NumberFormatException exc) {
            result = -1;
        }

        msg.append( Double.toString( result ) );
        return rt.createReal( result );
    }
}
