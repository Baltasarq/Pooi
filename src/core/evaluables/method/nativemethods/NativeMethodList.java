package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;

/**
 *
 * @author baltasarq
 */
public class NativeMethodList extends NativeMethod {
    public static final String EtqMthList = "list";

    public NativeMethodList()
    {
        super( EtqMthList );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        msg.append( ref.list() );
        return rt.createString( ref.list() );
    }
}
