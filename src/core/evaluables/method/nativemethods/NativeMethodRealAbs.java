package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectReal;

/**
 * Absolute number
 * @author baltasarq
 */
public class NativeMethodRealAbs extends NativeMethod {
    public static final String EtqMthRealAbs = "abs";

    public NativeMethodRealAbs()
    {
        super( EtqMthRealAbs );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectReal) ) {
            throw new InterpretError(
                    "Semantic error: expected Real in '"
                    + ref.getName()
                    + '\''
            );
        }

        final double result = Math.abs( ((ObjectReal) ref).getValue() );

        msg.append( Double.toString( result ) );
        return rt.createReal( result );
    }
}
