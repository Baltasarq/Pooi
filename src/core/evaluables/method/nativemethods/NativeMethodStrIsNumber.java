package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectStr;

/**
 * isNumber?
 * @author baltasarq
 */
public class NativeMethodStrIsNumber extends NativeMethod {
    public static final String EtqMthStrIsNumber = "num?";

    public NativeMethodStrIsNumber()
    {
        super( EtqMthStrIsNumber );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();
        boolean result = true;

        chkParametersNumber( 0, params );

        if ( !( ref instanceof ObjectStr) )
        {
            throw new InterpretError(
                    "Semantic error: expected Str in '"
                    + ref.getName()
                    + '\''
            );
        }

        final String value = ( (ObjectStr) ref ).getValue();

        try {
            Double.parseDouble( value  );
        }
        catch(NumberFormatException exc)
        {
            result = false;
        }

        msg.append( Boolean.toString( result ) );
        return rt.createBool( result );
    }
}
