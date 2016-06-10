package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

/**
 * To reside in Str, in order to concat two strings.
 * User: baltasarq
 * Date: 11/30/12
 */
public class NativeMethodStrConcat extends NativeMethod {

    public static final String EtqMthStrConcat = "+";

    public NativeMethodStrConcat(Runtime rt)
    {
        super( rt, EtqMthStrConcat );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        String result;
        final Runtime rt = this.getRuntime();
        final ObjectStr self;
        final ObjectStr toret;

        chkParametersNumber( 1, params );

        try {
            self = (ObjectStr) ref;
        }
        catch(Exception exc)
        {
            throw new InterpretError( "self object should be an Str" );
        }

        final ObjectBag arg = rt.solveToObject( params[ 0 ] );

        if ( arg instanceof ObjectStr ) {
            result = self.getValue() + ( (ObjectStr) arg ).getValue();
        } else {
            throw new InterpretError(
                    "expected Str as parameter in '"
                    + params[ 0 ].toString()
                    + '\''
            );
        }

        toret = rt.createString( result );

        msg.append( self.getPath() );
        msg.append( " and " );
        msg.append( arg.getPath() );
        msg.append( " added, giving '" );
        msg.append( toret.toString() );
        msg.append( '\'' );

        return toret;
    }

    public int getNumParams() {
        return 1;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[]{ "text" };
    }
}
