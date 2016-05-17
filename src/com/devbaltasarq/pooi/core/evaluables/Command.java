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
 * - Args are the arguments involved.
 */
public class Command extends Evaluable {

    public Command()
    {
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

    public boolean isValid()
    {
        return ( this.getReference() != null  );
    }

    public boolean hasMessage()
    {
        return !( getMessage().isEmpty() );
    }

    /**
     * @return the args
     */
    public Evaluable[] getArguments() {
        return args;
    }

    /**
     * @param args the args to set
     */
    public void setArguments(Evaluable[] args)
    {
        this.args = args;
    }

    /**
     * Returns all arguments
     * @return All arguments, as a String.
     */
    public String getArgumentsAsString()
    {
        final Evaluable[] arguments = this.getArguments();
        StringBuilder toret = new StringBuilder();

        for(Evaluable evaluable: arguments) {
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

    public int getNumArguments()
    {
        return this.args.length;
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
        toret.append( this.getArgumentsAsString() );
        toret.append( ')' );

        return toret.toString();
    }

    private String message = "";
    private Evaluable[] args = new Evaluable[ 0 ];
    private Evaluable ref = null;
}
