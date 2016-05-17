package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.*;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.Method;
import com.devbaltasarq.pooi.core.evaluables.literals.StrLiteral;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;
import com.devbaltasarq.pooi.core.objs.ValueObject;

/**
 * This method prints a text on the standard output
 * @author baltasarq
 */
public class NativeMethodOsPrint extends NativeMethod {
    public static final String EtqMthOsPrint = "print";

    public NativeMethodOsPrint()
    {
        super( EtqMthOsPrint );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        chkParametersNumber( 1, params  );
        msg.append( getStringFrom( params[ 0 ] ) );
        return ref;
    }

    public int getNumParams() {
        return 1;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "text" };
    }
}
