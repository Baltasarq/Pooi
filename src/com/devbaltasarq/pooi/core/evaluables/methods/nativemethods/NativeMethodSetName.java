package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.evaluables.Attribute;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;

/**
 * This method changes the name of the object
 * @author baltasarq
 */
public class NativeMethodSetName extends NativeMethod {

    public static final String EtqMthSetName = "setName";
    public static final String EtqMthRename = "rename";

    public NativeMethodSetName(com.devbaltasarq.pooi.core.Runtime rt)
    {
        super( rt, EtqMthSetName );
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
        String oldName = ref.getName();

        try {
            final Attribute attr = container.localInverseLookUp( ref );
            oldName = attr.getName();
            container.renameMember( oldName, id );
        }
        catch(InterpretError e)
        {
            msg.append( "Error renaming from '" + oldName + "' to: '" + id +  "': " );
            throw e;
        }

        msg.append( oldName + " renamed to " + ref.getPath() );
        return ref;
    }

    public int getNumParams() {
        return 1;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "objName" };
    }
}
