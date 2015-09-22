package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.literals.StrLiteral;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectInt;
import core.objs.ObjectStr;

/**
 * left
 * @author baltasarq
 */
public class NativeMethodStrLeft extends NativeMethod {
    public static final String EtqMthStrLeft = "left";

    public NativeMethodStrLeft()
    {
        super( EtqMthStrLeft );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();
        final ObjectInt arg1;
        final String selfValue;
        int arg1Value;
        String result;

        chkParametersNumber( 1, params );

        // Get self
        if ( !( ref instanceof ObjectStr ) ) {
            throw new InterpretError(
                    "Semantic error: expected string in '"
                    + ref.getName()
                    + '\''
            );
        }

        // Get the parameters
        try {
            arg1 = (ObjectInt) rt.solveToObject( params[ 0 ] );
        }
        catch(ClassCastException exc)
        {
            throw new InterpretError(
                    "Semantic error: expected a number as parameter"
            );
        }

        // Get the substring
        selfValue = ( (ObjectStr) ref ).getValue();
        arg1Value = (int) arg1.getValue();

        if ( arg1Value > selfValue.length() ) {
            arg1Value = selfValue.length();
        }

        try {
            result = ( (ObjectStr) ref ).getValue().substring( 0, arg1Value );
        }
        catch (StringIndexOutOfBoundsException exc)
        {
            result = "";
        }

        msg.append( result );
        return rt.createLiteral( new StrLiteral( result ) );
    }
}
