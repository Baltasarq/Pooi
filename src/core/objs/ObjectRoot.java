// ObjectRoot.java

package core.objs;

import core.ObjectBag;
import core.exceps.InterpretError;

/**
 * Represents the Root object
 * @author baltasarq
 */
public final class ObjectRoot extends SysObject {

    public ObjectRoot(String EtqNameRoot, ObjectBag parent)
            throws InterpretError
    {
        super( EtqNameRoot, parent, null );
    }

    @Override
    public ObjectBag copy(String name, ObjectBag container) throws InterpretError
    {
        throw new InterpretError( "It is not possible to copy the root object" );
    }
}
