package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.literals.StrLiteral;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

/**
 *
 * @author baltasarq
 */
public class NativeMethodStr extends NativeMethod {
    public static final String EtqMthToString = "str";

    public NativeMethodStr()
    {
        super( EtqMthToString );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        final String toret = ref.toString();
        msg.append( toret );
        return rt.createLiteral( new StrLiteral( toret ) );
    }

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
