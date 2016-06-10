// ObjectRoot.java

package com.devbaltasarq.pooi.core.objs;

import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.Interpreter.InterpretError;

/**
 * Represents the Root object
 * @author baltasarq
 */
public final class ObjectRoot extends SysObject {

    public ObjectRoot(Runtime rt, String EtqNameRoot, ObjectBag parent)
            throws InterpretError
    {
        super( rt, EtqNameRoot, parent, null );
    }

    @Override
    public ObjectBag copy(String name, ObjectBag container) throws InterpretError
    {
        throw new InterpretError( "It is not possible to copy the root object" );
    }

    @Override
    /** @return the number of objects between this object and root, plus itself
     *  This is the root, so... 0.
     */
    public int getInheritanceLevel() {
        return 0;
    }
}
