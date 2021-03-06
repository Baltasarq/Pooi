package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 *  toReal
 * @author baltasarq
 */
public class NativeMethodStrToReal extends NativeMethod {
    public static final String EtqMthStrToReal = "real";

    public NativeMethodStrToReal(Runtime rt)
    {
        super( rt, EtqMthStrToReal );
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

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
