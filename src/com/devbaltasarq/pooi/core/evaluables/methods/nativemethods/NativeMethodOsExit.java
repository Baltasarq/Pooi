package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;

/**
 * A method that extracts the time and stores it in a DateTime object.
 * Created by Baltasar on 30/01/2016.
 */
public class NativeMethodOsExit extends NativeMethod {
    public static final String EtqMthOsExit = "exit";

    public NativeMethodOsExit(Runtime rt) {
        super( rt, EtqMthOsExit );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        System.exit( 0 );
        return null;
    }

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
