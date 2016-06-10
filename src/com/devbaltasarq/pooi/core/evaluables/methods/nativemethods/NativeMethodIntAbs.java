package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectInt;

/**
 * Absolute number
 * @author baltasarq
 */
public class NativeMethodIntAbs extends NativeMethod {
    public static final String EtqMthIntAbs = "abs";

    public NativeMethodIntAbs(Runtime rt)
    {
        super( rt, EtqMthIntAbs );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = this.getRuntime();

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

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
