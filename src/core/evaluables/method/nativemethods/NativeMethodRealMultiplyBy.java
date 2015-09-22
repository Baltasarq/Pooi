package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectInt;
import core.objs.ObjectReal;

/**
 * Multiply two numbers.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodRealMultiplyBy extends NativeMethod {

    public static final String EtqMthRealMul = "*";

    public NativeMethodRealMultiplyBy()
    {
        super( EtqMthRealMul );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        double result;
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

        if ( arg instanceof ObjectReal ) {
            result = ( (ObjectReal) arg ).getValue() * self.getValue();
        }
        else
        if ( arg instanceof ObjectInt )  {
            result = ( (ObjectInt) arg ).getValue() * self.getValue();
        }
        else {
            throw new InterpretError(
                    "expected int as parameter in '"
                    + params[ 0 ].toString()
                    + '\''
            );
        }


        toret = rt.createReal( result );

        msg.append( self.getPath() );
        msg.append( " multiplied by " );
        msg.append( arg.getPath() );
        msg.append( ", giving " );
        msg.append( toret.toString() );

        return toret;
    }
}
