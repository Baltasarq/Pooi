package com.devbaltasarq.pooi.core.evaluables.method.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.method.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 * toInt
 * @author baltasarq
 */
public class NativeMethodStrToInt extends NativeMethod {
    public static final String EtqMthStrToInt = "int";

    public NativeMethodStrToInt()
    {
        super( EtqMthStrToInt );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectStr) ) {
            throw new InterpretError(
                    "Semantic error: expected string in '"
                    + ref.getName()
                    + '\''
            );
        }

        long result;

        try {
            result = Integer.parseInt( ( (ObjectStr) ref ).getValue() );
        }
        catch(NumberFormatException exc) {
            try {
                result = Math.round( Double.parseDouble( ( (ObjectStr) ref ).getValue() ) );
            }
            catch(NumberFormatException exc2) {
                result = -1;
            }
        }

        msg.append( Long.toString( result ) );
        return rt.createInt( result );
    }
}