package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 * isNumber?
 * @author baltasarq
 */
public class NativeMethodStrIsNumber extends NativeMethod {
    public static final String EtqMthStrIsNumber = "num?";

    public NativeMethodStrIsNumber(Runtime rt)
    {
        super( rt, EtqMthStrIsNumber );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = this.getRuntime();
        boolean result = true;

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectStr) )
        {
            throw new InterpretError(
                    "Semantic error: expected Str in '"
                    + ref.getName()
                    + '\''
            );
        }

        final String value = ( (ObjectStr) ref ).getValue();

        try {
            Double.parseDouble( value  );
        }
        catch(NumberFormatException exc)
        {
            result = false;
        }

        msg.append( Boolean.toString( result ) );
        return rt.createBool( result );
    }

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
