package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;

/**
 * This method changes the name of the object
 * @author baltasarq
 */
public class NativeMethodRename extends NativeMethod {

    public static final String EtqMthRename = "rename";

    public NativeMethodRename()
    {
        super(EtqMthRename);
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        chkParametersNumber( 1, params );
        return NativeMethodSetName.rename( ref, getStringFrom( params[ 0 ] ), msg );
    }
}
