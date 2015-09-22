package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;

/**
 * This method eliminates an attribute in an object
 * @author baltasarq
 */
public class NativeMethodErase extends NativeMethod {
    public static final String EtqMthErase = "erase";

    public NativeMethodErase()
    {
        super( EtqMthErase );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        chkParametersNumber( 1, params  );

        final String id1 = getStringFrom( params[ 0 ] );

        ref.remove( id1 );

        msg.append( "'" );
        msg.append( id1 );
        msg.append( "' erased from '" );
        msg.append( ref.getName() );
        msg.append( "'." );

        return ref;
    }
}
