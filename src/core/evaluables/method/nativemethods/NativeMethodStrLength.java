package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectStr;

/**
 * length
 * @author baltasarq
 */
public class NativeMethodStrLength extends NativeMethod {
    public static final String EtqMthStrLength = "len";

    public NativeMethodStrLength()
    {
        super( EtqMthStrLength );
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

        long result = ( (ObjectStr) ref ).getValue().length();
        msg.append( Long.toString( result ) );
        return rt.createInt( result );
    }
}
