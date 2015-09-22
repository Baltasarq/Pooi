package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectReal;

/**
 * isNegative?
 * @author baltasarq
 */
public class NativeMethodRealIsNegative extends NativeMethod {
    public static final String EtqMthRealIsNegative = "neg?";

    public NativeMethodRealIsNegative()
    {
        super( EtqMthRealIsNegative );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectReal ) ) {
            throw new InterpretError(
                    "Semantic error: expected Real in '"
                    + ref.getName()
                    + '\''
            );
        }

        final boolean result = ( ( (ObjectReal) ref ).getValue() < 0 );

        msg.append( Boolean.toString( result ) );
        return rt.createBool( result );
    }
}
