package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.literals.StrLiteral;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectStr;

/**
 * toMays
 * @author baltasarq
 */
public class NativeMethodStrMays extends NativeMethod {
    public static final String EtqMthStrMays = "upper";

    public NativeMethodStrMays()
    {
        super( EtqMthStrMays );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectStr ) ) {
            throw new InterpretError(
                    "Semantic error: expected Str in '"
                    + ref.getName()
                    + '\''
            );
        }

        final String result = ( (ObjectStr) ref ).getValue().toUpperCase();

        msg.append( result );
        return rt.createLiteral( new StrLiteral( result ) );
    }
}
