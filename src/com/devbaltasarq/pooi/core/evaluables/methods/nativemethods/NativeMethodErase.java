package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;

/**
 * This method eliminates an attribute in an object
 * @author baltasarq
 */
public class NativeMethodErase extends NativeMethod {
    public static final String EtqMthErase = "erase";

    public NativeMethodErase(com.devbaltasarq.pooi.core.Runtime rt)
    {
        super( rt, EtqMthErase );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        chkParametersNumber( 1, params  );

        final String id1 = getStringFrom( params[ 0 ] );

        ref.removeMember( id1 );

        msg.append( "'" );
        msg.append( id1 );
        msg.append( "' erased from '" );
        msg.append( ref.getName() );
        msg.append( "'." );

        return ref;
    }

    public int getNumParams() {
        return 1;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "attrName" };
    }
}
