package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;

/**
 * Empty is?
 * User: baltasarq
 * Date: 10/4/13
 */
public class NativeMethodEmbed extends NativeMethod {

    public static final String EtqMthEmbed = "embed";

    public NativeMethodEmbed(Runtime rt)
    {
        super( rt, EtqMthEmbed );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        ObjectBag result;
        final Runtime rt = this.getRuntime();

        chkParametersNumber( 1, params );
        final ObjectBag arg = rt.solveToObject( params[ 0 ] );
        ObjectBag obj = ref;

        ref.embed( arg.getName(), arg );

        result = ref;
        msg.append( result  );
        return result;
    }

    public int getNumParams() {
        return 1;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "obj" };
    }
}
