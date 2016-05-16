package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectReal;

/**
 * isNegative?
 * @author baltasarq
 */
public class NativeMethodRealIsNegative extends NativeMethod {
    public static final String EtqMthRealIsNegative = "neg?";

    public NativeMethodRealIsNegative()
    {
        super( EtqMthRealIsNegative );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectReal ) ) {
            throw new InterpretError(
                    "Semantic error: expected Real in '"
                    + ref.getName()
                    + '\''
            );
        }

        final boolean result = ( ( (ObjectReal) ref ).getValue() < 0 );

        msg.append( Boolean.toString( result ) );
        return rt.createBool( result );
    }

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "x" };
    }
}
