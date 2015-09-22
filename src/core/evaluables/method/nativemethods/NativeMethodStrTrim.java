package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.literals.StrLiteral;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectStr;

/**
 *
 * @author baltasarq
 */
public class NativeMethodStrTrim extends NativeMethod {
    public static final String EtqMthStrTrim = "trim";

    public NativeMethodStrTrim()
    {
        super( EtqMthStrTrim );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectStr ) ) {
            throw new InterpretError(
                    "Semantic error: expected string in '"
                    + ref.getName()
                    + '\''
            );
        }

        final String result = ( (ObjectStr) ref ).getValue().trim();

        msg.append( result );
        return rt.createLiteral( new StrLiteral( result ) );
    }
}
