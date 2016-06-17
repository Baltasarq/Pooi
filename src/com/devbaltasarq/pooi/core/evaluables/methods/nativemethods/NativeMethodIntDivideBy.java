package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.objs.ObjectInt;
import com.devbaltasarq.pooi.core.objs.ObjectReal;

/**
 * Divide two numbers.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodIntDivideBy extends NativeMethod {

    public static final String EtqMthIntDiv = "/";

    public NativeMethodIntDivideBy(Runtime rt)
    {
        super( rt, EtqMthIntDiv );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final String selfPath = ref.getPath();
        final Runtime rt = this.getRuntime();
        final ObjectInt self;
        final ObjectInt toret;

        chkParametersNumber( 1, params );

        try {
            self = (ObjectInt) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( selfPath + " object should be an Int" );
        }

        final ObjectBag arg = rt.solveToObject( params[ 0 ] );

        toret = rt.createInt( doDivision( params[ 0 ].toString(), self, arg ) );

        msg.append( selfPath );
        msg.append( " divided by " );
        msg.append( arg.getPath() );
        msg.append( ", giving " );
        msg.append( toret.toString() );

        return toret;
    }

    public static long doDivision(String paramName, ObjectInt self, ObjectBag arg) throws InterpretError
    {
        long toret = 0;

        if ( arg instanceof ObjectInt ) {
            long divider = ( (ObjectInt) arg ).getValue();

            if ( divider != 0 ) {
                toret = self.getValue() / divider;
            }
        }
        else
        if ( arg instanceof ObjectReal ) {
            long divider = Math.round( ( (ObjectReal) arg ).getValue() );

            if ( divider != 0 ) {
                toret = self.getValue() / divider;
            }
        } else {
            throw new InterpretError(
                    "expected int as parameter in '"
                    + paramName
                    + '\''
            );
        }
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
