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
public class NativeMethodIntReal extends NativeMethod {
    public static final String EtqMthReal = "real";

    public NativeMethodIntReal(com.devbaltasarq.pooi.core.Runtime rt)
    {
        super( rt, EtqMthReal );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = this.getRuntime();
        final ObjectBag root = rt.getRoot();
        final String selfPath = ref.getPath();
        final ObjectInt self;
        final ObjectReal toret;

        chkParametersNumber( 0, params );

        try {
            self = (ObjectInt) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( selfPath + " should be an Int" );
        }

        toret = rt.createReal( (double) self.getValue() );

        // Compose textual answer
        msg.append( ref.getName() );
        msg.append( " was converted into " );
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
