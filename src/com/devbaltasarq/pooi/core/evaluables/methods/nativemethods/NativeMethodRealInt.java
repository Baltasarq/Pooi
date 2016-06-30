package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.objs.ObjectInt;
import com.devbaltasarq.pooi.core.objs.ObjectReal;

/**
 * Copying objects
 * @author baltasarq
 */
public class NativeMethodRealInt extends NativeMethod {
    public static final String EtqMthInt = "int";

    public NativeMethodRealInt(Runtime rt)
    {
        super( rt, EtqMthInt );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = this.getRuntime();
        final ObjectBag root = rt.getRoot();
        final String selfPath = ref.getPath();
        final ObjectReal self;
        final ObjectInt toret;

        chkParametersNumber( 0, params );

        try {
            self = (ObjectReal) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( selfPath + " should be a Real" );
        }

        toret = rt.createInt( (int) self.getValue() );

        // Compose textual answer
        msg.append( ref.getName() );
        msg.append( " was truncated into " );
        msg.append( root.getPath() );
        msg.append( " as '" );
        msg.append( toret.getPath() );
        msg.append( '\'' );

        return toret;
    }

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
