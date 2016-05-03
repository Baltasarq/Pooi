package com.devbaltasarq.pooi.core.evaluables.method.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.method.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectInt;
import com.devbaltasarq.pooi.core.objs.ObjectReal;

/**
 * Divide two numbers.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodIntDivideBy extends NativeMethod {

    public static final String EtqMthIntDiv = "/";

    public NativeMethodIntDivideBy()
    {
        super( EtqMthIntDiv );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        long result = 0;
        final Runtime rt = Runtime.getRuntime();
        final ObjectInt self;
        final ObjectInt toret;

        chkParametersNumber( 1, params );

        try {
            self = (ObjectInt) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( "self object should be an Int" );
        }

        final ObjectBag arg = rt.solveToObject( params[ 0 ] );

        if ( arg instanceof ObjectInt ) {
            long divider = ( (ObjectInt) arg ).getValue();

            if ( divider != 0 ) {
                result = self.getValue() / divider;
            }
        }
        else
        if ( arg instanceof ObjectReal) {
            long divider = Math.round( ( (ObjectReal) arg ).getValue() );

            if ( divider != 0 ) {
                result = self.getValue() / divider;
            }
        } else {
            throw new InterpretError(
                    "expected int as parameter in '"
                    + params[ 0 ].toString()
                    + '\''
            );
        }


        toret = rt.createInt( result );

        msg.append( self.getPath() );
        msg.append( " divided by " );
        msg.append( arg.getPath() );
        msg.append( ", giving " );
        msg.append( toret.toString() );

        return toret;
    }

    public int getNumParams() {
        return 1;
    }
}
