package core;

import core.evaluables.Reference;
import core.exceps.InterpretError;

import java.util.ArrayList;

/**
 * The execution stack for a method.
 * User: baltasarq
 * Date: 11/26/12
 */
public class ExecutionStack {

    public ExecutionStack()
    {
        this.stack = new ArrayList<>();
    }

    public Reference getTop() throws InterpretError
    {
        if ( isEmpty() ) {
            throw this.createStackEmptyException();
        }

        return this.stack.get( this.stack.size() -1 );
    }

    public void pop() throws InterpretError
    {
        if ( isEmpty() ) {
            throw this.createStackEmptyException();
        }

        this.stack.remove( this.stack.size() -1 );
    }

    private InterpretError createStackEmptyException()
    {
        return new InterpretError( "nonsense instruction: empty stack" );
    }

    public void push(Reference ref)
    {
        this.stack.add( ref );
    }

    public boolean isEmpty()
    {
        return ( this.stack.size() == 0 );
    }

    private ArrayList<Reference> stack;
}
