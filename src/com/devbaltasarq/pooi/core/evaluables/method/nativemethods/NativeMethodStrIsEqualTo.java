package com.devbaltasarq.pooi.core.evaluables.method.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.method.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 * Compare two strings.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodStrIsEqualTo extends NativeMethod {

    public static final String EtqMthStrIsEqualTo = "==";

    public NativeMethodStrIsEqualTo()
    {
        super( EtqMthStrIsEqualTo );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        boolean result;
        final Runtime rt = Runtime.getRuntime();
        final ObjectStr self;

        chkParametersNumber( 1, params );

        try {
            self = (ObjectStr) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( "self object should be a Str" );
        }

        final ObjectBag arg = rt.solveToObject( params[ 0 ] );

        if ( arg instanceof ObjectStr ) {
            result = ( self.getValue().compareTo( ( (ObjectStr) arg ).getValue() ) == 0 );
        } else {
            throw new InterpretError(
                    "expected Str as parameter in '"
                    + params[ 0 ].toString()
                    + '\''
            );
        }


        msg.append( Boolean.toString( result ) );
        return rt.createBool( result );
    }
}
