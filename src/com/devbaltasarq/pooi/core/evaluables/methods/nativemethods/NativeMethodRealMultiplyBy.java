package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.objs.ObjectInt;
import com.devbaltasarq.pooi.core.objs.ObjectReal;

/**
 * Multiply two numbers.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodRealMultiplyBy extends NativeMethod {

    public static final String EtqMthRealMul = "*";

    public NativeMethodRealMultiplyBy(Runtime rt)
    {
        super( rt, EtqMthRealMul );
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

        toret = rt.createReal( doProduct( params[ 0 ].toString(), self, arg ) );

        msg.append( selfPath );
        msg.append( " multiplied by " );
        msg.append( arg.getPath() );
        msg.append( ", giving " );
        msg.append( toret.toString() );

        return toret;
    }

    public static double doProduct(String paramName, ObjectReal self, ObjectBag arg) throws InterpretError
    {
        double toret;

        if ( arg instanceof ObjectReal ) {
            toret = ( (ObjectReal) arg ).getValue() * self.getValue();
        }
        else
        if ( arg instanceof ObjectInt )  {
            toret = ( (ObjectInt) arg ).getValue() * self.getValue();
        }
        else {
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
