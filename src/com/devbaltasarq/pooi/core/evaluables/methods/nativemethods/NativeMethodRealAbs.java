package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectReal;

/**
 * Absolute number
 * @author baltasarq
 */
public class NativeMethodRealAbs extends NativeMethod {
    public static final String EtqMthRealAbs = "abs";

    public NativeMethodRealAbs(Runtime rt)
    {
        super( rt, EtqMthRealAbs );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = this.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectReal) ) {
            throw new InterpretError(
                    "Semantic error: expected Real in '"
                    + ref.getName()
                    + '\''
            );
        }

        final double result = Math.abs( ((ObjectReal) ref).getValue() );

        msg.append( Double.toString( result ) );
        return rt.createReal( result );
    }

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
