package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

/**
 *
 * @author baltasarq
 */
public class NativeMethodCreateChild extends NativeMethod {
    public static final String EtqMthCreateChild = "createChild";

    public NativeMethodCreateChild()
    {
        super( EtqMthCreateChild );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        ObjectBag toret;

        chkParametersNumber( 0, params );
        toret = ref.createChild( "", ref.getContainer() );

        msg.append( '\'' );
        msg.append( toret.getPath() );
        msg.append( "' was created as a child of '" );
        msg.append( ref.getPath() );
        msg.append( '\'' );
        return toret;
    }

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
