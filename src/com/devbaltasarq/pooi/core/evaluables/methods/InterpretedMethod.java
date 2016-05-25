package com.devbaltasarq.pooi.core.evaluables.methods;

import com.devbaltasarq.pooi.core.*;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.Command;
import com.devbaltasarq.pooi.core.evaluables.Method;
import com.devbaltasarq.pooi.core.evaluables.Reference;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

/**
 * Method holding interpreted code.
 * User: baltasarq
 * Date: 11/19/12
 */
public class InterpretedMethod extends Method {
    public static final String DefaultMethodId = "__mth_";

    public InterpretedMethod(Runtime rt, String name)
    {
        super( rt, name );
        this.stackCmds = new ArrayList<>();
        this.params = new HashMap<>();
        this.formalParams = new String[ 0 ];
    }

    public InterpretedMethod(Runtime rt, String name, String cmds) throws InterpretError
    {
        this( rt, name );
        this.setCmds( cmds );
    }

    public void setCmds(String cmds) throws InterpretError
    {
        if ( !cmds.startsWith( "{" ) ) {
            cmds = "{:" + cmds + "}";
        }

        cmds = this.extractParams( cmds );

        this.stackCmds.clear();
        this.stackCmds.addAll( Arrays.asList( Parser.parseOrder( this.getRuntime(), cmds ) ) );
    }

    public void setRealParams(ObjectBag self, Evaluable[] args) throws InterpretError
    {
        String[] formalParams = this.getFormalParameters();

        if ( args.length != formalParams.length ) {
            throw new InterpretError( "formal and real arguments do not match" );
        }

        this.params.clear();
        this.params.put(
                Reserved.SelfRef,
                new Reference( Parser.parseReference( new Lexer( self.getPath() ) ) )
        );

        for(int i = 0; i < formalParams.length; ++i) {
            this.params.put( formalParams[ i ], args[ i ] );
        }

        return;
    }

    protected String extractParams(String cmds) throws InterpretError
    {
        ArrayList<String> formalParamsList = new ArrayList<>();
        cmds = cmds.trim();

        // Remove method marks
        if ( !cmds.startsWith( "{" )
          || !cmds.endsWith( "}" ) )
        {
            throw new InterpretError( "missing expected method marks: {}" );
        }

        cmds = cmds.substring( 1 );
        cmds = cmds.substring( 0, cmds.length() -1 );

        // Parse params
        cmds = cmds.trim();
        Lexer lex = new Lexer( cmds );

        while( lex.getNextTokenType() != Lexer.TokenType.SpecialCharacter
            && !lex.isEOL() )
        {
            formalParamsList.add( lex.getToken() );

            // Skip spaces or commas
            lex.skipSpaces();
            if ( lex.getCurrentChar() == ',' ) {
                lex.advance();
                lex.skipSpaces();
            }
        }

        // Check ':' and removeMember it
        if ( lex.getCurrentChar() != ':' ) {
            throw new InterpretError( "missing expected parameter separator: ':'" );
        }

        this.formalParams = formalParamsList.toArray( new String[ formalParamsList.size() ] );
        return ( cmds.substring( lex.getPos() +1 ).trim() );
    }

    public void addCmds(String cmd) throws InterpretError
    {
        this.stackCmds.addAll( Arrays.asList( Parser.parseOrder( this.getRuntime(), cmd ) ) );
    }

    public InterpretedMethod copy()
    {
        InterpretedMethod toret = new InterpretedMethod( this.getRuntime(), this.getName() );

        // Commands
        toret.stackCmds.addAll( Arrays.asList( this.getCmds() ) );

        // Formal paramenters
        toret.formalParams = this.formalParams.clone();

        // Slots for real parameters
        for(String paramName: this.params.keySet()) {
            toret.params.put( paramName, null );
        }

        return toret;
    }

    public Command[] getCmds()
    {
        return this.stackCmds.toArray( new Command[ this.stackCmds.size() ] );
    }

    public Evaluable[] getRealParams()
    {
        return ( this.params.values().toArray( new Evaluable[ this.params.size() ] ) );
    }

    public Evaluable getRealParameter(String id)
    {
        return ( this.params.get( id ) );
    }

    @Override
    public String[] getFormalParameters()
    {
        return this.formalParams;
    }

    @Override
    public String getMethodBodyAsString() {
        Stack<String> stack = new Stack<>();
        StringBuilder toret = new StringBuilder();

        toret.append( "{" );

        // Add params
        for(String param: this.getFormalParameters()) {
            toret.append( param );
            toret.append( ' ' );
        }

        toret.append( ": " );

        // Add commands
        for(Command cmd: this.getCmds()) {
            String replaceCmd = "";
            String cmdAsStr = cmd.toString();
            int pos = cmdAsStr.indexOf( Parser.PopTask );

            if ( pos >= 0 ) {
                // Process the reference
                if ( cmd.getReference().toString().equals( Parser.PopTask ) ) {
                    replaceCmd = stack.pop();
                    cmdAsStr = cmdAsStr.substring( 0, pos )
                            + replaceCmd
                            + cmdAsStr.substring( pos + Parser.PopTask.length(), cmdAsStr.length() );
                }

                // Process possible targets
                pos += replaceCmd.length();
                pos = cmdAsStr.indexOf( Parser.PopTask, pos );
                while( pos >= 0 ) {
                    replaceCmd = stack.pop();
                    cmdAsStr = cmdAsStr.substring( 0, pos )
                            + replaceCmd
                            + cmdAsStr.substring( pos + Parser.PopTask.length(), cmdAsStr.length() );

                    // Next?
                    pos += replaceCmd.length();
                    pos = cmdAsStr.indexOf( Parser.PopTask, pos );
                }
            }

            // Push command
            stack.push( cmdAsStr );
        }

        // Dump the remaining
        for(String strCmd: stack) {
            toret.append( strCmd );
            toret.append( "; " );
        }

        toret.append( "}" );
        return toret.toString();
    }

    public static String createNewMethodId()
    {
        String toret = DefaultMethodId;

        toret += Integer.toString( numMths );

        ++numMths;
        return toret;
    }

    public int getNumParams() {
        return this.formalParams.length;
    }

    private ArrayList<Command> stackCmds;
    private HashMap<String, Evaluable> params;
    private String[] formalParams;

    private static int numMths = 0;
}
