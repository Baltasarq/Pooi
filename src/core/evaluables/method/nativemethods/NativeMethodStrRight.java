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
 * right
 * @author baltasarq
 */
public class NativeMethodStrRight extends NativeMethod {
    public static final String EtqMthStrRight = "right";

    public NativeMethodStrRight()
    {
        super( EtqMthStrRight );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();
        final ObjectInt arg1;
        final ObjectStr self;
        final String selfValue;
        int arg1Value;
        String result;

        chkParametersNumber( 1, params );

        // Get self
        try {
            self = (ObjectStr) ref;
        }
        catch(ClassCastException exc) {
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
        selfValue = self.getValue();
        arg1Value = (int) arg1.getValue();

        if ( arg1Value > selfValue.length() ) {
            arg1Value = selfValue.length();
        }

        try {
            result = selfValue.substring( selfValue.length() - arg1Value );
        }
        catch (StringIndexOutOfBoundsException exc)
        {
            result = "";
        }

        msg.append( result );
        return rt.createLiteral( new StrLiteral( result ) );
    }
}
