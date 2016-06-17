package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.objs.ObjectReal;

/**
 * To reside in Int, in order to set two numbers.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodRealSumAssign extends NativeMethod {

    public static final String EtqMthRealSumAssign = "+=";

    public NativeMethodRealSumAssign(Runtime rt)
    {
        super( rt, EtqMthRealSumAssign );
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

        toret = self;
        self.assign( NativeMethodRealSum.doSum( params[ 0 ].toString(), self, arg ) );

        msg.append( selfPath );
        msg.append( " and " );
        msg.append( arg.getPath() );
        msg.append( " added, giving " );
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
