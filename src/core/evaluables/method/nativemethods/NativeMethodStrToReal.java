package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectStr;

/**
 *  toReal
 * @author baltasarq
 */
public class NativeMethodStrToReal extends NativeMethod {
    public static final String EtqMthStrToReal = "real";

    public NativeMethodStrToReal()
    {
        super( EtqMthStrToReal );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectStr) ) {
            throw new InterpretError(
                    "Semantic error: expected string in '"
                    + ref.getName()
                    + '\''
            );
        }

        double result;

        try {
            result = Double.parseDouble( ( (ObjectStr) ref ).getValue() );
        }
        catch(NumberFormatException exc) {
            result = -1;
        }

        msg.append( Double.toString( result ) );
        return rt.createReal( result );
    }
}
