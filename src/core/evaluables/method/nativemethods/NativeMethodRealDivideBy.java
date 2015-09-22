package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectInt;
import core.objs.ObjectReal;

/**
 * Divide two numbers.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodRealDivideBy extends NativeMethod {

    public static final String EtqMthRealDiv = "/";

    public NativeMethodRealDivideBy()
    {
        super( EtqMthRealDiv );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        double result = 0;
        final Runtime rt = Runtime.getRuntime();
        final ObjectReal self;
        final ObjectReal toret;

        chkParametersNumber( 1, params );

        try {
            self = (ObjectReal) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( "self object should be a Real" );
        }

        final ObjectBag arg = rt.solveToObject( params[ 0 ] );

        if ( arg instanceof ObjectInt ) {
            long divider = ( (ObjectInt) arg ).getValue();

            if ( divider != 0 ) {
                result = self.getValue() / divider;
            }
        }
        else
        if ( arg instanceof ObjectReal) {
            long divider = Math.round( ( (ObjectReal) arg ).getValue() );

            if ( divider != 0 ) {
                result = ( self.getValue() / divider );
            }
        } else {
            throw new InterpretError(
                    "expected int as parameter in '"
                    + params[ 0 ].toString()
                    + '\''
            );
        }


        toret = rt.createReal( result );

        msg.append( self.getPath() );
        msg.append( " divided by " );
        msg.append( arg.getPath() );
        msg.append( ", giving " );
        msg.append( toret.toString() );

        return toret;
    }
}
