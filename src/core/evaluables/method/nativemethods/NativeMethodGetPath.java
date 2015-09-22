package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;

/**
 * Returns the path of the object
 * @author baltasarq
 */
public class NativeMethodGetPath extends NativeMethod {
    public static final String EtqMthPath = "path";

    public NativeMethodGetPath()
    {
        super( EtqMthPath );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        msg.append( ref.getPath() );
        return rt.createString( ref.getPath() );
    }
}
