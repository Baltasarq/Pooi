package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;

/**
 * Empty is?
 * User: baltasarq
 * Date: 10/4/13
 */
public class NativeMethodIs extends NativeMethod {

    public static final String EtqMthIs = "is?";

    public NativeMethodIs(Runtime rt)
    {
        super( rt, EtqMthIs );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        boolean result;
        final Runtime rt = this.getRuntime();

        chkParametersNumber( 1, params );
        final ObjectBag arg = rt.solveToObject( params[ 0 ] );
        final ObjectBag absParent = rt.getAbsoluteParent();
        ObjectBag obj = ref;

        while( obj != absParent )
        {
            if ( obj == arg ) {
                break;
            }

            obj = obj.getParentObject();
        }

        result = ( obj == arg );
        msg.append( Boolean.toString( result ) );
        return rt.createBool( result );
    }

    public int getNumParams() {
        return 1;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "obj" };
    }
}
