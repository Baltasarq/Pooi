/*
 * Command.java
 * Usado para devolver una entrada descompuesta en atributos, message
 * y parámetros.
 */

package com.devbaltasarq.pooi.core.evaluables;

import com.devbaltasarq.pooi.core.Evaluable;

/**
 * The command built from absParent.mth( arg0, arg1, ...argN)
 * El comando formado por absParent.mth( arg0, arg1, ...argN)
 * @author baltasarq
 * - objNames are the attributes of the path to get to the object
 * - Message is the method.
 * - Params are the parameters involved.
 */
public class Command extends Evaluable {

    public Command()
    {
    }

    public Command(String id)
    {
        this.setReference( new Reference( new String[] { id } ) );
    }

    public Command(String[] atrs)
    {
        this.setReference( new Reference( atrs ) );
    }

    /**
     * @return the message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message)
    {
        this.message = "";
        message = message.trim();
        
        if ( message != null
          && !( message.isEmpty() ) )
        {
            this.message = message;
        }
    }

    /**
     * @return the params
     */
    public Evaluable[] getParams() {
        return params;
    }

    public boolean hasMessage()
    {
        return !( getMessage().isEmpty() );
    }

    /**
     * @param params the params to set
     */
    public void setParams(Evaluable[] params)
    {
        this.params = params;
    }

    public boolean isValid()
    {
        return ( this.getReference() != null  );
    }

    /**
     * Returns all parameters
     * @return All parameters, as a String.
     */
    public String getParamsAsString()
    {
        final Evaluable[] params = this.getParams();
        StringBuilder toret = new StringBuilder();

        for(Evaluable evaluable: params) {
            toret.append( evaluable.toString() );
            toret.append( ' ' );
        }

        return toret.toString();
    }

    public Evaluable getReference()
    {
        return this.ref;
    }

    public void setReference(Evaluable ref)
    {
        this.ref = ref;
    }

    public int getNumParams()
    {
        return this.params.length;
    }

    public String toString()
    {
        StringBuilder toret = new StringBuilder();

        toret.append( '(' );
        toret.append( ' ' );
        toret.append( this.getReference().toString() );
        toret.append( ' ' );
        toret.append( this.getMessage() );
        toret.append( ' ' );
        toret.append( this.getParamsAsString() );
        toret.append( ')' );

        return toret.toString();
    }

    private String message = "";
    private Evaluable[] params = new Evaluable[ 0 ];
    private Evaluable ref = null;
}
