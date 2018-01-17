// Pooi (c) Baltasar 2013 - 2018 MIT License <jbgarcia@uvigo.es>
package com.devbaltasarq.pooi.core.evaluables;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.Reserved;
import com.devbaltasarq.pooi.core.evaluables.literals.StrLiteral;

/**
 * The command built from absParent.mth( arg0, arg1, ...argN)
 * El comando formado por absParent.mth( arg0, arg1, ...argN)
 * @author baltasarq
 * - objNames are the attributes of the path to get to the object
 * - Message is the method.
 * - Args are the arguments involved.
 */
public class Command extends Evaluable {
    /** @return the message */
    public String getMessage()
    {
        return message;
    }

    /** Sets a new message for the command (the method to call), so "+" in x + 1..
     *  @param message the message to set
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

    @Override
    public boolean isPopTask()
    {
        return ( this.getNumArguments() == 0 )
            && ( this.getReference().toString().equals( Reserved.PopTask ) );
    }

    public boolean isValid()
    {
        return ( this.getReference() != null  );
    }

    /** @return the args of the command (as a Evaluable[]), so "1" in x + 1. */
    public Evaluable[] getArguments() {
        return args;
    }

    /** Changes the arguments of the command.
     *  @param args the args to set
     */
    public void setArguments(Evaluable[] args)
    {
        this.args = args;
    }

    /** Returns all arguments
     *  @return All arguments, as a String.
     */
    public String getArgumentsAsString()
    {
        final Evaluable[] arguments = this.getArguments();
        StringBuilder toret = new StringBuilder();

        for(Evaluable evaluable: arguments) {
            boolean isStringLiteral = evaluable instanceof StrLiteral;

            if ( isStringLiteral ) {
                toret.append( '"' );
            }

            toret.append( evaluable.toString() );

            if ( isStringLiteral ) {
                toret.append( '"' );
            }

            toret.append( ' ' );
        }

        return toret.toString();
    }

    /** Gets the reference of the command, so "x" in x + 1.
     *  @return The reference, as an Evaluable.
     *  @see Evaluable
     *  @see Reference
     */
    public Evaluable getReference()
    {
        return this.ref;
    }

    /** Changes the reference of the command, , so "x" in x + 1.
     *  @param ref The new reference, as an Evaluable object (probably a Reference).
     *  @see Evaluable
     *  @see Reference
     */
    public void setReference(Evaluable ref)
    {
        this.ref = ref;
    }

    /** The number of arguments in the command, so "1" in X + 1.
     *  @return the number of arguments.
     */
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
