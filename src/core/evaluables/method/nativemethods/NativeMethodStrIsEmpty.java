package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectStr;

/**
 * Empty string?.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodStrIsEmpty extends NativeMethod {

    public static final String EtqMthStrIsEmpty = "empty?";

    public NativeMethodStrIsEmpty()
    {
        super( EtqMthStrIsEmpty );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        boolean result;
        final Runtime rt = Runtime.getRuntime();
        final ObjectStr self;

        chkParametersNumber( 0, params );

        try {
            self = (ObjectStr) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( "self object should be a Str" );
        }


        result = self.getValue().isEmpty();
        msg.append( Boolean.toString( result ) );
        return rt.createBool( result );
    }
}
