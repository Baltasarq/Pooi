package com.devbaltasarq.pooi.core.evaluables.method.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.evaluables.method.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

/**
 * This method changes the name of the object
 * @author baltasarq
 */
public class NativeMethodSetName extends NativeMethod {

    public static final String EtqMthSetName = "setName";
    public static final String EtqMthRename = "rename";

    public NativeMethodSetName()
    {
        super( EtqMthSetName );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        chkParametersNumber( 1, params );

        return rename(ref, getStringFrom( params[ 0 ] ), msg);
    }

    public static ObjectBag rename(ObjectBag ref, String id, StringBuilder msg)
            throws InterpretError
    {
        final ObjectBag container = ref.getContainer();
        final String oldName = ref.getPath();

        // Changing name comprises two steps:
        // Removing it then adding it again
        // (so its own name and the reference pointing to it are in sync)
        container.remove( ref.getName() );

        try {
            ref.setName( id );
            container.set( ref.getName(), ref );
        }
        catch(InterpretError e)
        {
            ref.setName( oldName );
            container.set( oldName, ref );

            throw e;
        }

        msg.append( oldName + " renamed to " + ref.getPath() );
        return ref;
    }

    public int getNumParams() {
        return 1;
    }
}
