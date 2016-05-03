package com.devbaltasarq.pooi.core.evaluables.method.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.literals.StrLiteral;
import com.devbaltasarq.pooi.core.evaluables.method.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectInt;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 * charAt
 * @author baltasarq
 */
public class NativeMethodStrCharAt extends NativeMethod {
    public static final String EtqMthStrCharAt = "at";

    public NativeMethodStrCharAt()
    {
        super( EtqMthStrCharAt );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final long num;
        final Runtime rt = Runtime.getRuntime();
        final String selfValue;
        final String result;

        chkParametersNumber( 1, params );

        try {
            selfValue = ( (ObjectStr) ref ).getValue();
        }
        catch(ClassCastException exc) {
            throw new InterpretError(
                    "Semantic error: expected Str in '"
                    + ref.getName()
                    + '\''
            );
        }

        try {
            num = ( (ObjectInt) rt.solveToObject( params[ 0 ] ) ).getValue();
        }
        catch (ClassCastException exc) {
            throw new InterpretError(
                    "Semantic error: expected Int in '"
                            + ref.getName()
                            + '\''
            );
        }

        if ( num < 0
          || num >= selfValue.length() )
        {
            result = "";
        } else {
            result = Character.toString( selfValue.charAt( (int) num ) );
        }

        msg.append( result );
        return rt.createLiteral( new StrLiteral( result ) );
    }

    public int getNumParams() {
        return 1;
    }
}
