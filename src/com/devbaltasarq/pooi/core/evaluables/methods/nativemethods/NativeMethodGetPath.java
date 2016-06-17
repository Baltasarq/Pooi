package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;

/**
 * Returns the path of the object
 * @author baltasarq
 */
public class NativeMethodGetPath extends NativeMethod {
    public static final String EtqMthPath = "path";

    public NativeMethodGetPath(Runtime rt)
    {
        super( rt, EtqMthPath );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final String refPath = ref.getPath();
        final Runtime rt = this.getRuntime();

        chkParametersNumber( 0, params );

        msg.append( refPath );
        return rt.createString( refPath );
    }

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
