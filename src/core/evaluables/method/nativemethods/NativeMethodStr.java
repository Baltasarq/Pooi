package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.literals.StrLiteral;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;

/**
 *
 * @author baltasarq
 */
public class NativeMethodStr extends NativeMethod {
    public static final String EtqMthToString = "str";

    public NativeMethodStr()
    {
        super( EtqMthToString );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 0, params );

        final String toret = ref.toString();
        msg.append( toret );
        return rt.createLiteral( new StrLiteral( toret ) );
    }
}
