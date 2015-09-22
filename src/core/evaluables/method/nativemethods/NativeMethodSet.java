package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.InterpretedMethod;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;

/**
 * Native method for adding a new attribute or method to the object.
 * persona.set( "edad", 0 )
 * @author baltasarq
 */
public class NativeMethodSet extends NativeMethod {

    public static final String EtqMthSet = "set";

    public NativeMethodSet()
    {
        super( EtqMthSet );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();

        this.chkParametersNumber( 2, params );

        final String id1 = getStringFrom( params[ 0 ] );
        final Evaluable paramToAdd = params[ 1 ];
        ObjectBag obj2 = null;


        if ( paramToAdd instanceof InterpretedMethod ) {
            InterpretedMethod mth = (InterpretedMethod) paramToAdd;

            mth.setName( id1 );
            ref.set( id1, mth );
        } else {
            obj2 = rt.solveToObject( params[ 1 ] );
            final boolean wasALit = ( obj2.getContainer() == rt.getLiteralsContainer() );

            ref.set( id1, obj2 );

            if ( wasALit ) {
                obj2.setName( id1 );
            }
        }

        msg.append( '\'' );
        msg.append( id1 );
        msg.append( "' set in " );
        msg.append( ref.getPath()  );
        return ref;
    }
}
