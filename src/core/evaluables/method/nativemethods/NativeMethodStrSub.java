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
 * substring
 * @author baltasarq
 */
public class NativeMethodStrSub extends NativeMethod {
    public static final String EtqMthStrSub = "sub";

    public NativeMethodStrSub()
    {
        super( EtqMthStrSub );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();
        final ObjectInt arg1;
        final ObjectInt arg2;
        String result;

        chkParametersNumber( 2, params );

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
            arg2 = (ObjectInt) rt.solveToObject( params[ 1 ] );
        }
        catch(ClassCastException exc)
        {
            throw new InterpretError(
                    "Semantic error: expected two numbers as parameters"
            );
        }

        // Get the substring
        try {
            result = ( (ObjectStr) ref ).getValue().substring(
                                   (int) arg1.getValue(), (int) arg2.getValue()
            );
        }
        catch (StringIndexOutOfBoundsException exc)
        {
            result = "";
        }

        msg.append( result );
        return rt.createLiteral( new StrLiteral( result ) );
    }
}
