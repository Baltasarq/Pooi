package com.devbaltasarq.pooi.core.evaluables.method.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.literals.StrLiteral;
import com.devbaltasarq.pooi.core.evaluables.method.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 * toMays
 * @author baltasarq
 */
public class NativeMethodStrMays extends NativeMethod {
    public static final String EtqMthStrMays = "upper";

    public NativeMethodStrMays()
    {
        super( EtqMthStrMays );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectStr ) ) {
            throw new InterpretError(
                    "Semantic error: expected Str in '"
                    + ref.getName()
                    + '\''
            );
        }

        final String result = ( (ObjectStr) ref ).getValue().toUpperCase();

        msg.append( result );
        return rt.createLiteral( new StrLiteral( result ) );
    }
}
