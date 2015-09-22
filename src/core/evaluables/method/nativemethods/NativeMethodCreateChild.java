package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;

/**
 *
 * @author baltasarq
 */
public class NativeMethodCreateChild extends NativeMethod {
    public static final String EtqMthCreateChild = "createChild";

    public NativeMethodCreateChild()
    {
        super( EtqMthCreateChild );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        ObjectBag toret;

        chkParametersNumber( 0, params );
        toret = ref.createChild( "", ref.getContainer() );

        msg.append( '\'' );
        msg.append( toret.getPath() );
        msg.append( "' was created as a child of '" );
        msg.append( ref.getPath() );
        msg.append( '\'' );
        return toret;
    }
}
