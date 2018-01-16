package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.Interpreter;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.objs.ObjectBool;

public class NativeMethodBoolIsTrue extends NativeMethod {
    public static final String EtqMthBoolIsTrue = "true?";

    public NativeMethodBoolIsTrue(com.devbaltasarq.pooi.core.Runtime rt)
    {
        super( rt, EtqMthBoolIsTrue );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws Interpreter.InterpretError
    {
        final com.devbaltasarq.pooi.core.Runtime rt = ref.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectBool) ) {
            throw new Interpreter.InterpretError(
                    "Semantic error: expected Bool in '"
                            + ref.getName()
                            + '\''
            );
        }

        final boolean result = ( ( (ObjectBool) ref ).getValue() );

        msg.append( Boolean.toString( result ) );
        return rt.createBool( result );
    }

    @Override
    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
