package core.objs;

import core.ObjectBag;
import core.exceps.InterpretError;

/**
 * The base class for all objects that hold values (Int, Bool, Real, Str)
 * @author baltasarq
 */
public abstract class ValueObject extends ObjectBag {
    public ValueObject(String n, ObjectBag parent, ObjectBag container) throws InterpretError
    {
        super( n, parent, container );
    }

    @Override
    public String getKind() {
        return getName() + ": " + getParentObject().getName() + " = " + this.toString();
    }

    @Override
    public String list()
    {
        StringBuilder toret = new StringBuilder();

        toret.append( super.list() );
        toret.append( " = " );
        toret.append( toString() );

        return toret.toString();
    }

    public abstract Object getValueObject();
}

