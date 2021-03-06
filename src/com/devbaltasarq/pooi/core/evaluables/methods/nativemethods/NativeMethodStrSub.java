package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.literals.StrLiteral;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectInt;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 * substring
 * @author baltasarq
 */
public class NativeMethodStrSub extends NativeMethod {
    public static final String EtqMthStrSub = "sub";

    public NativeMethodStrSub(Runtime rt)
    {
        super( rt, EtqMthStrSub );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = this.getRuntime();
        final ObjectInt arg1;
        final ObjectInt arg2;
        String result;

        chkParametersNumber( 2, params );

        // Get self
        if ( !( ref instanceof ObjectStr ) ) {
            throw new InterpretError(
                    "Semantic error: expected string in '"
                    + ref.getName()
                    + '\''
            );
        }

        // Get the parameters
        try {
            arg1 = (ObjectInt) rt.solveToObject( params[ 0 ] );
            arg2 = (ObjectInt) rt.solveToObject( params[ 1 ] );
        }
        catch(ClassCastException exc)
        {
            throw new InterpretError(
                    "Semantic error: expected two numbers as parameters"
            );
        }

        // Get the substring
        try {
            result = ( (ObjectStr) ref ).getValue().substring(
                                   (int) arg1.getValue(), (int) arg2.getValue()
            );
        }
        catch (StringIndexOutOfBoundsException exc)
        {
            result = "";
        }

        msg.append( result );
        return rt.createLiteral( new StrLiteral( result ) );
    }

    public int getNumParams() {
        return 2;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "pos1", "pos2" };
    }
}
