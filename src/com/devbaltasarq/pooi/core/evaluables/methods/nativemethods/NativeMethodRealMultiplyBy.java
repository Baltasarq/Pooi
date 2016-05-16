package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectInt;
import com.devbaltasarq.pooi.core.objs.ObjectReal;

/**
 * Multiply two numbers.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodRealMultiplyBy extends NativeMethod {

    public static final String EtqMthRealMul = "*";

    public NativeMethodRealMultiplyBy()
    {
        super( EtqMthRealMul );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        double result;
        final Runtime rt = Runtime.getRuntime();
        final ObjectReal self;
        final ObjectReal toret;

        chkParametersNumber( 1, params );

        try {
            self = (ObjectReal) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( "self object should be a Real" );
        }

        final ObjectBag arg = rt.solveToObject( params[ 0 ] );

        if ( arg instanceof ObjectReal ) {
            result = ( (ObjectReal) arg ).getValue() * self.getValue();
        }
        else
        if ( arg instanceof ObjectInt )  {
            result = ( (ObjectInt) arg ).getValue() * self.getValue();
        }
        else {
            throw new InterpretError(
                    "expected int as parameter in '"
                    + params[ 0 ].toString()
                    + '\''
            );
        }


        toret = rt.createReal( result );

        msg.append( self.getPath() );
        msg.append( " multiplied by " );
        msg.append( arg.getPath() );
        msg.append( ", giving " );
        msg.append( toret.toString() );

        return toret;
    }

    public int getNumParams() {
        return 1;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "x" };
    }
}
