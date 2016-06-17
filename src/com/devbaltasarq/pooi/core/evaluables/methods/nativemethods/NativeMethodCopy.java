package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;

/**
 * Copying objects
 * @author baltasarq
 */
public class NativeMethodCopy extends NativeMethod {
    public static final String EtqMthCopy = "copy";

    public NativeMethodCopy(com.devbaltasarq.pooi.core.Runtime rt)
    {
        super( rt, EtqMthCopy );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final ObjectBag root = ref.getRuntime().getRoot();
        ObjectBag toret;

        chkParametersNumber( 0, params );
        toret = ref.copy( "", root );

        // Compose textual answer
        msg.append( ref.getName() );
        msg.append( " was copied into " );
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
