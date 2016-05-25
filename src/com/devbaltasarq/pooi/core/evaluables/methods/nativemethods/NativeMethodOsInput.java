package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.AppInfo;
import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.literals.StrLiteral;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

import javax.swing.*;

/**
 * Asks for a value, and returns it as a literal string.
 * @author baltasarq
 */
public class NativeMethodOsInput extends NativeMethod {
    public static final String EtqMthOsInput = "input";

    public NativeMethodOsInput(Runtime rt)
    {
        super( rt, EtqMthOsInput );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = this.getRuntime();
        chkParametersNumber( 1, params  );
        final String inputMessage = getStringFrom( params[ 0 ] );

        msg.append( inputMessage );
        String strValue = (String) JOptionPane.showInputDialog(
                null,
                inputMessage,
                AppInfo.Name,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");

        if ( strValue == null ) {
            strValue = "";
        }

        msg.append( strValue );
        return rt.createLiteral( new StrLiteral( strValue ) );
    }

    public int getNumParams() {
        return 1;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "msg" };
    }
}
