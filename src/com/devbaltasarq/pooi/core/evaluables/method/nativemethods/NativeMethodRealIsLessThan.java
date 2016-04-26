package com.devbaltasarq.pooi.core.evaluables.method.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.method.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectInt;
import com.devbaltasarq.pooi.core.objs.ObjectReal;

/**
 * Compare two numbers.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodRealIsLessThan extends NativeMethod {

    public static final String EtqMthRealIsLessThan = "<";

    public NativeMethodRealIsLessThan()
    {
        super( EtqMthRealIsLessThan );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        boolean result;
        final Runtime rt = Runtime.getRuntime();
        final ObjectReal self;

        chkParametersNumber( 1, params );

        try {
            self = (ObjectReal) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( "self object should be a Real" );
        }

        final ObjectBag arg = rt.solveToObject( params[ 0 ] );

        if ( arg instanceof ObjectInt ) {
            result = ( self.getValue() < ( (double) ( (ObjectInt) arg ).getValue() ) );
        }
        else
        if ( arg instanceof ObjectReal) {
            result = ( self.getValue() < ( (ObjectReal) arg ).getValue() );
        } else {
            throw new InterpretError(
                    "expected int as parameter in '"
                    + params[ 0 ].toString()
                    + '\''
            );
        }


        msg.append( Boolean.toString( result ) );
        return rt.createBool( result );
    }
}
