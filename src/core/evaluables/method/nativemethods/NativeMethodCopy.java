package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;

/**
 * Copying objects
 * @author baltasarq
 */
public class NativeMethodCopy extends NativeMethod {
    public static final String EtqMthCopy = "copy";

    public NativeMethodCopy()
    {
        super( EtqMthCopy );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final ObjectBag container = ref.getContainer();
        ObjectBag toret;

        chkParametersNumber( 0, params );
        toret = ref.copy( "", container );

        // Compose textual answer
        msg.append( ref.getName() );
        msg.append( " was copied into " );
        msg.append( container.getPath() );
        msg.append( " as '" );
        msg.append( toret.getPath() );
        msg.append( '\'' );

        return toret;
    }
}