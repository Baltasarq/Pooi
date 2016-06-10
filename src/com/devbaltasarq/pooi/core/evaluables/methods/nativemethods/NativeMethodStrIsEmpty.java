package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 * Empty string?.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodStrIsEmpty extends NativeMethod {

    public static final String EtqMthStrIsEmpty = "empty?";

    public NativeMethodStrIsEmpty(Runtime rt)
    {
        super( rt, EtqMthStrIsEmpty );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        boolean result;
        final Runtime rt = this.getRuntime();
        final ObjectStr self;

        chkParametersNumber( 0, params );

        try {
            self = (ObjectStr) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( "self object should be a Str" );
        }


        result = self.getValue().isEmpty();
        msg.append( Boolean.toString( result ) );
        return rt.createBool( result );
    }

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
