package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;

/**
 * Empty is?
 * User: baltasarq
 * Date: 10/4/13
 */
public class NativeMethodIs extends NativeMethod {

    public static final String EtqMthIs = "is?";

    public NativeMethodIs()
    {
        super( EtqMthIs );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        boolean result;
        final Runtime rt = Runtime.getRuntime();

        chkParametersNumber( 1, params );
        final ObjectBag arg = rt.solveToObject( params[ 0 ] );
        final ObjectBag absParent = Runtime.getRuntime().getAbsoluteParent();
        ObjectBag obj = ref;

        while( obj != absParent )
        {
            if ( obj == arg ) {
                break;
            }

            obj = obj.getParentObject();
        }

        result = ( obj == arg );
        msg.append( Boolean.toString( result ) );
        return rt.createBool( result );
    }
}
