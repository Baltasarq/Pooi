package com.devbaltasarq.pooi.core.evaluables.method.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.method.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

/**
 * Empty is?
 * User: baltasarq
 * Date: 10/4/13
 */
public class NativeMethodIs extends NativeMethod {

    public static final String EtqMthIs = "is?";

    public NativeMethodIs()
    {
        super( EtqMthIs );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        boolean result;
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 1, params );
        final ObjectBag arg = rt.solveToObject( params[ 0 ] );
        final ObjectBag absParent = Runtime.getRuntime().getAbsoluteParent();
        ObjectBag obj = ref;

        while( obj != absParent )
        {
            if ( obj == arg ) {
                break;
            }

            obj = obj.getParentObject();
        }

        result = ( obj == arg );
        msg.append( Boolean.toString( result ) );
        return rt.createBool( result );
    }

    public int getNumParams() {
        return 1;
    }
}
