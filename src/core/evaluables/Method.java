package core.evaluables;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.exceps.InterpretError;
import core.objs.ObjectStr;

/**
 * Represents methods inside an object.
 * User: baltasarq
 * Date: 11/19/12
 */
public abstract class Method extends Member {

    public Method(String name)
    {
        super( name );
    }

    public void chkParametersNumber(int num, Evaluable[] params)
            throws InterpretError
    {
        if ( params.length != num ) {
            if ( num == 0 ) {
                throw new InterpretError(
                        String.format( "'%s' expects no parameter(s)",
                                       this.getName() )
                );
            } else {
                throw new InterpretError(
                        String.format( "'%s' expects %d parameter(s)",
                                       this.getName(), num )
                );
            }
        }

        return;
    }

    public static String getStringFrom(Evaluable e)
            throws InterpretError
    {
        final core.Runtime rt = Runtime.getRuntime();
        String toret;
        ObjectBag idObj = null;

        try {
            idObj = rt.solveToObject( e );
            toret = ( (ObjectStr) idObj ).getValue();
        }
        catch(Exception exc)
        {
            throw new InterpretError(
                    "expecting string in '"
                    + e.toString() + '\''
            );
        }

        return toret;
    }

    /**
     * Copies the method, creating another exact copy of this one.
     * @return The copied method, as a Method object.
     */
    public abstract Method copy();

}
