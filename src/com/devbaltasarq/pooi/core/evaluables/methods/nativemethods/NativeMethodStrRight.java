package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.literals.StrLiteral;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectInt;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 * right
 * @author baltasarq
 */
public class NativeMethodStrRight extends NativeMethod {
    public static final String EtqMthStrRight = "right";

    public NativeMethodStrRight()
    {
        super( EtqMthStrRight );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();
        final ObjectInt arg1;
        final ObjectStr self;
        final String selfValue;
        int arg1Value;
        String result;

        chkParametersNumber( 1, params );

        // Get self
        try {
            self = (ObjectStr) ref;
        }
        catch(ClassCastException exc) {
            throw new InterpretError(
                    "Semantic error: expected string in '"
                    + ref.getName()
                    + '\''
            );
        }

        // Get the parameters
        try {
            arg1 = (ObjectInt) rt.solveToObject( params[ 0 ] );
        }
        catch(ClassCastException exc)
        {
            throw new InterpretError(
                    "Semantic error: expected a number as parameter"
            );
        }

        // Get the substring
        selfValue = self.getValue();
        arg1Value = (int) arg1.getValue();

        if ( arg1Value > selfValue.length() ) {
            arg1Value = selfValue.length();
        }

        try {
            result = selfValue.substring( selfValue.length() - arg1Value );
        }
        catch (StringIndexOutOfBoundsException exc)
        {
            result = "";
        }

        msg.append( result );
        return rt.createLiteral( new StrLiteral( result ) );
    }

    public int getNumParams() {
        return 1;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "pos" };
    }
}
