package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectInt;

/**
 * Absolute number
 * @author baltasarq
 */
public class NativeMethodIntAbs extends NativeMethod {
    public static final String EtqMthIntAbs = "abs";

    public NativeMethodIntAbs()
    {
        super( EtqMthIntAbs );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectInt) ) {
            throw new InterpretError(
                    "Semantic error: expected Int in '"
                    + ref.getName()
                    + '\''
            );
        }

        final long result = Math.abs( ( (ObjectInt) ref ).getValue() );

        msg.append( Long.toString( result ) );
        return rt.createInt( result );
    }
}
