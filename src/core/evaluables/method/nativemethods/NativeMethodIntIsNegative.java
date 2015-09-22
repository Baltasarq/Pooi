package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectInt;

/**
 * isNegative?
 * @author baltasarq
 */
public class NativeMethodIntIsNegative extends NativeMethod {
    public static final String EtqMthIntIsNegative = "neg?";

    public NativeMethodIntIsNegative()
    {
        super( EtqMthIntIsNegative );
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

        final boolean result = ( ( (ObjectInt) ref ).getValue() < 0 );

        msg.append( Boolean.toString( result ) );
        return rt.createBool( result  );
    }
}
