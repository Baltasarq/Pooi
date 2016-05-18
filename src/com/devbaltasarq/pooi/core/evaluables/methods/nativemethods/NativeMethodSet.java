package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.InterpretedMethod;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

/**
 * Native method for adding a new attribute or method to the object.
 * persona.set( "edad", 0 )
 * @author baltasarq
 */
public class NativeMethodSet extends NativeMethod {

    public static final String EtqMthSet = "set";

    public NativeMethodSet()
    {
        super( EtqMthSet );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        this.chkParametersNumber( 2, params );

        final String id1 = getStringFrom( params[ 0 ] );
        final Evaluable paramToAdd = params[ 1 ];
        ObjectBag obj2 = null;


        if ( paramToAdd instanceof InterpretedMethod ) {
            InterpretedMethod mth = (InterpretedMethod) paramToAdd;

            mth.setName( id1 );
            ref.set( id1, mth );
        } else {
            obj2 = rt.solveToObject( params[ 1 ] );
            ref.set( id1, obj2 );
        }

        msg.append( '\'' );
        msg.append( id1 );
        msg.append( "' set in " );
        msg.append( ref.getPath()  );
        return ref;
    }

    public int getNumParams() {
        return 2;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "attrName", "obj" };
    }
}
