package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.objs.ObjectInt;
import com.devbaltasarq.pooi.core.objs.ObjectReal;

/**
 * To reside in Real, in order to add two numbers.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodRealSum extends NativeMethod {

    public static final String EtqMthRealSum = "+";

    public NativeMethodRealSum(Runtime rt)
    {
        super( rt, EtqMthRealSum );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final String selfPath = ref.getPath();
        final Runtime rt = this.getRuntime();
        final ObjectReal self;
        final ObjectReal toret;

        chkParametersNumber( 1, params );

        try {
            self = (ObjectReal) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( selfPath + " object should be a Real" );
        }

        final ObjectBag arg = rt.solveToObject( params[ 0 ] );

        toret = rt.createReal( doSum( params[ 0 ].toString(), self, arg ) );

        msg.append( selfPath );
        msg.append( " and " );
        msg.append( arg.getPath() );
        msg.append( " added, giving " );
        msg.append( toret.toString() );

        return toret;
    }

    public static double doSum(String paramName, ObjectReal self, ObjectBag arg) throws InterpretError {
        double result;
        if ( arg instanceof ObjectReal ) {
            result = ( ( (ObjectReal) arg ).getValue() + self.getValue() );
        }
        else
        if ( arg instanceof ObjectInt ) {
            result = ( ( (ObjectInt) arg ).getValue() + self.getValue() );
        } else {
            throw new InterpretError(
                    "expected int as parameter in '"
                    + paramName
                    + '\''
            );
        }
        return result;
    }

    public int getNumParams() {
        return 1;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "x" };
    }
}
