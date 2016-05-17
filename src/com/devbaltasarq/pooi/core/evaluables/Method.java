package com.devbaltasarq.pooi.core.evaluables;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

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

    public abstract int getNumParams();

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
        final Runtime rt = Runtime.getRuntime();
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
     * Returns a list with the formal parameters of the method
     * @return a primitive vector of String, containing the formal parameters
     */
    public abstract String[] getFormalParameters();

    /**
     * Returns a string with the formal parameters of the method
     * @return a String, with the formal parameters
     */
    public String getFormalParametersAsString() {
        StringBuilder toret = new StringBuilder();

        for(String param: this.getFormalParameters()) {
            toret.append( toret );
            toret.append( ' ' );
        }

        return toret.toString().trim();
    }

    /** @return Gets the body of the method as a representation in text */
    public abstract String getMethodBodyAsString();

    /**
     * Copies the method, creating another exact copy of this one.
     * @return The copied method, as a Method object.
     */
    public abstract Method copy();

}
