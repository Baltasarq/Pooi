package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectInt;
import core.objs.ObjectReal;

/**
 * Substract two numbers.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodIntSubstract extends NativeMethod {

    public static final String EtqMthIntSub = "-";

    public NativeMethodIntSubstract()
    {
        super( EtqMthIntSub );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        long result;
        final Runtime rt = Runtime.getRuntime();
        final ObjectInt self;
        final ObjectInt toret;

        chkParametersNumber( 1, params );

        try {
            self = (ObjectInt) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( "self object should be an Int" );
        }

        final ObjectBag arg = rt.solveToObject( params[ 0 ] );

        if ( arg instanceof ObjectInt ) {
            result = self.getValue() - ( (ObjectInt) arg ).getValue();
        }
        else
        if ( arg instanceof ObjectReal) {
            result = ( self.getValue() - Math.round( ( (ObjectReal) arg ).getValue() ) );
        } else {
            throw new InterpretError(
                    "expected int as parameter in '"
                    + params[ 0 ].toString()
                    + '\''
            );
        }


        toret = rt.createInt( result );

        msg.append( self.getPath() );
        msg.append( " substracted from " );
        msg.append( arg.getPath() );
        msg.append( ", giving " );
        msg.append( toret.toString() );

        return toret;
    }
}
