package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 * length
 * @author baltasarq
 */
public class NativeMethodStrLength extends NativeMethod {
    public static final String EtqMthStrLength = "len";

    public NativeMethodStrLength(Runtime rt)
    {
        super( rt, EtqMthStrLength );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = this.getRuntime();

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

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
