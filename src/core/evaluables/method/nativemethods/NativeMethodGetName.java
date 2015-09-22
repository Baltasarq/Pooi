package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;

/**
 * Returns the name of the object
 * @author baltasarq
 */
public class NativeMethodGetName extends NativeMethod {
    public static final String EtqMthGetName = "name";

    public NativeMethodGetName()
    {
        super( EtqMthGetName );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        msg.append( ref.getName() );
        return rt.createString( ref.getName() );
    }
}
