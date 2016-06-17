package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.objs.ObjectInt;

/**
 * Sum two int numbers.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodIntSumAssign extends NativeMethod {

    public static final String EtqMthIntSumAssign = "+=";

    public NativeMethodIntSumAssign(Runtime rt)
    {
        super( rt, EtqMthIntSumAssign );
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

        toret = self;
        self.assign( NativeMethodIntSum.doSum( params[ 0 ].toString(), self, arg ) );

        msg.append( selfPath );
        msg.append( " incremented with " );
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
