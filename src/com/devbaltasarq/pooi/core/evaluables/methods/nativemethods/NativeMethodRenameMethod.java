package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.Method;
import com.devbaltasarq.pooi.core.evaluables.methods.InterpretedMethod;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

/**
 * Native method for adding a new attribute or method to the object.
 * persona.set( "edad", 0 )
 * @author baltasarq
 */
public class NativeMethodRenameMethod extends NativeMethod {

    public static final String EtqMthRenameMethod = "renameMethod";

    public NativeMethodRenameMethod()
    {
        super( EtqMthRenameMethod );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();
        this.chkParametersNumber( 2, params );

        final String id1 = getStringFrom( params[ 0 ] );
        final String id2 = getStringFrom( params[ 1 ] );
        ref.renameMember( id1, id2 );

        msg.append( "method '" );
        msg.append( id1 );
        msg.append( "' renamed as '" );
        msg.append( id2 );
        msg.append( '\'' );

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
