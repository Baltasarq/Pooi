package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectInt;
import com.devbaltasarq.pooi.core.objs.ObjectReal;

/**
 * Multiply two numbers.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodIntMultiplyBy extends NativeMethod {

    public static final String EtqMthIntMul = "*";

    public NativeMethodIntMultiplyBy(Runtime rt)
    {
        super( rt, EtqMthIntMul );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        long result;
        final Runtime rt = this.getRuntime();
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
            result = ( ( (ObjectInt) arg ).getValue() * self.getValue() );
        }
        else
        if ( arg instanceof ObjectReal) {
            result = ( Math.round( ( (ObjectReal) arg ).getValue() ) * self.getValue() );
        } else {
            throw new InterpretError(
                    "expected int as parameter in '"
                    + params[ 0 ].toString()
                    + '\''
            );
        }


        toret = rt.createInt( result );

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
