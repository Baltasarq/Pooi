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
 * charAt
 * @author baltasarq
 */
public class NativeMethodStrCharAt extends NativeMethod {
    public static final String EtqMthStrCharAt = "at";

    public NativeMethodStrCharAt()
    {
        super( EtqMthStrCharAt );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final long num;
        final Runtime rt = Runtime.getRuntime();
        final String selfValue;
        final String result;

        chkParametersNumber( 1, params );

        try {
            selfValue = ( (ObjectStr) ref ).getValue();
        }
        catch(ClassCastException exc) {
            throw new InterpretError(
                    "Semantic error: expected Str in '"
                    + ref.getName()
                    + '\''
            );
        }

        try {
            num = ( (ObjectInt) rt.solveToObject( params[ 0 ] ) ).getValue();
        }
        catch (ClassCastException exc) {
            throw new InterpretError(
                    "Semantic error: expected Int in '"
                            + ref.getName()
                            + '\''
            );
        }

        if ( num < 0
          || num >= selfValue.length() )
        {
            result = "";
        } else {
            result = Character.toString( selfValue.charAt( (int) num ) );
        }

        msg.append( result );
        return rt.createLiteral( new StrLiteral( result ) );
    }
}
