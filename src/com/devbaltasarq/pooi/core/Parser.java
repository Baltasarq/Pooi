// Parser.java

package com.devbaltasarq.pooi.core;

import com.devbaltasarq.pooi.core.evaluables.Command;
import com.devbaltasarq.pooi.core.evaluables.Reference;
import com.devbaltasarq.pooi.core.evaluables.literals.BoolLiteral;
import com.devbaltasarq.pooi.core.evaluables.literals.IntLiteral;
import com.devbaltasarq.pooi.core.evaluables.literals.RealLiteral;
import com.devbaltasarq.pooi.core.evaluables.literals.StrLiteral;
import com.devbaltasarq.pooi.core.evaluables.methods.InterpretedMethod;
import com.devbaltasarq.pooi.core.evaluables.methods.nativemethods.NativeMethodSet;
import com.devbaltasarq.pooi.core.evaluables.methods.nativemethods.NativeMethodStr;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

import java.util.ArrayList;

/**
 * Parse a text command
 * User: baltasarq
 * Date: 11/16/12
 */
public class Parser {
    public static final String PopTask = "__POP";

    private static void clean(ArrayList<Command> toret)
    {
        for(int i = 0; i < toret.size(); ++i)
        {
            Evaluable evaluable = toret.get( i );

            if ( evaluable instanceof Command ) {
                Command cmd = (Command) evaluable;

                if ( cmd.getReference().toString().equals( PopTask )
                        && cmd.getMessage().isEmpty() )
                {
                    toret.remove( i );
                    --i;
                }
            }
        }

        return;
    }

    /**
     * Parses the orders given by the user, in the form:
     * ref mth param1 ...paramN; ref mth param1 ...paramN; ...
     * @param order The input given by the user
     * @return A vector of Command object representing user's intentions
     * @throws com.devbaltasarq.pooi.core.exceps.InterpretError
     */
    public static Command[] parseOrder(String order) throws InterpretError {
        ArrayList<Command> toret = new ArrayList<Command>();
        order = order.trim();

        // empty
        if  ( !order.isEmpty() ) {

            // remove last ';'
            if ( order.charAt( order.length() -1 ) == ';') {
                order = order.substring( 0, order.length() -1 ).trim();
            }

            // parse it
            Lexer lex = new Lexer( order );
            lex.skipSpaces();
            Command cmd = parseCommand( lex, toret );

            while ( cmd != null ) {
                toret.add( cmd );

                // Traverse spaces to get to next command
                lex.skipSpaces();
                if ( lex.getCurrentChar() == ';' ) {
                    lex.advance();
                    lex.skipSpaces();
                }

                cmd = parseCommand( lex, toret );
            }
        }

        clean( toret );
        return toret.toArray( new Command[ toret.size() ] );
    }

    /**
     * Parses the order given by the user, in the form:
     * ref mth param1 ...paramN
     *
     * @param lex the lexer with the order given by the user
     * @param cmds the orders stack so far
     * @return A command object that represents user's intentions
     * @throws com.devbaltasarq.pooi.core.exceps.InterpretError when parsing is not possible
     */
    public static Command parseCommand(Lexer lex, ArrayList<Command> cmds) throws InterpretError
    {
        Command toret = null;

        lex.skipSpaces();
        if ( !lex.isEOL() ) {
            toret = new Command();

            // Set the attributes of the reference
            if ( lex.getCurrentChar() == '(' ) {
                toret.setReference( new Reference( new String[]{ PopTask } ) );
                lex.advance();
                cmds.add( parseCommand( lex, cmds ) );
            } else {
                toret.setReference( parseParam( lex ) );
            }

            parsePartialCommand( toret, lex, cmds );
        }

        if ( toret != null
          && !toret.isValid() )
        {
            toret = null;
        }

        return toret;
    }

    private static boolean isCommandEnd(Lexer lex)
    {
        boolean toret = false;

        lex.skipSpaces();
        final char ch = lex.getCurrentChar();

        if ( ch == ';'
          || ch == ')' )
        {
            toret = true;
            lex.advance();
        }

        return toret;
    }

    private static String getMessageId(Lexer lex)
    {
        char ch;
        String toret = "";
        Lexer.TokenType tokenType;

        // Decide which kind of message, native: *,+,-... or average
        lex.skipSpaces();

        if( lex.isOperator() ) {
            do {
                toret += Character.toString( lex.getCurrentChar() );
                lex.advance();
            } while( lex.isOperator() );
        } else {
            toret = lex.getToken();
            ch = lex.getCurrentChar();

            if ( lex.isOperator() ) {
                toret += Character.toString( ch );
                lex.advance();
            }
        }

        lex.skipSpaces();
        return toret.trim();
    }

    /**
     * This method is needed in order to deal with the case in which
     * you have x.a = y, meaning x set "a" y
     * @param toret the resulting command, as a Command object.
     * @param lex the lexer being used to interpret the command, as a Lexer object.
     */
    public static void extractMethod(Command toret, Lexer lex) throws InterpretError {
        String messageId = getMessageId( lex );

        if ( messageId.equals( Reserved.AssignmentOperator  ) ) {
            // Transform the method id
            messageId = NativeMethodSet.EtqMthSet;

            // Is it a reference?
            Evaluable evl = toret.getReference();

            if ( evl instanceof Reference ) {
                final Reference ref = (Reference) evl;

                // If no context provided, assume it is in root
                if ( ref.getAttrs().length == 1 ) {
                    ref.setAttrs( new String[]{ "Root", ref.getAttrs()[ 0 ] } );
                }

                String atr = ref.top();

                ref.pop();
                lex.insertAtCurrentPos( '"' + atr + '"' );
            } else {
                throw new InterpretError( "reference expected at left of: "  + Reserved.AssignmentOperator );
            }
        }

        toret.setMessage( messageId  );
    }

    private  static Command parsePartialCommand(Command toret, Lexer lex, ArrayList<Command> cmds)
            throws InterpretError
    {
        lex.skipSpaces();

        // Set message
        if ( !lex.isEOL()
          && !isCommandEnd( lex ) )
        {

            extractMethod( toret, lex );

            lex.skipSpaces();
            if ( !lex.isEOL()
              && !isCommandEnd( lex ) )
            {
                // Set params
                ArrayList<Evaluable> params = new ArrayList<Evaluable>();
                int numParams = 0;

                while( !lex.isEOL()
                    && !isCommandEnd( lex ) )
                {
                    if ( lex.getCurrentChar() == '(' ) {
                        lex.advance();
                        params.add( new Reference( PopTask ) );
                        cmds.add( parseCommand( lex, cmds ) );
                    }
                    else
                    if ( lex.getCurrentChar() == '{' ) {
                        params.add( parseMethod( lex ) );
                    }
                    else {
                        params.add( parseParam( lex ) );
                    }

                    ++numParams;
                }

                if ( numParams > 0 ) {
                    toret.setParams( params.toArray( new Evaluable[ params.size() ] ) );
                }
            }
        }

        if ( toret.isValid()
          && toret.getMessage().trim().isEmpty()
          && !toret.getReference().toString().equals( PopTask  ) )
        {
            toret.setMessage( NativeMethodStr.EtqMthToString );
        }

        return toret;
    }


    private static Evaluable parseParam(Lexer lex) throws InterpretError
    {
        Evaluable toret = null;

        lex.skipSpaces();

        if ( lex.getNextTokenType() == Lexer.TokenType.Id ) {
            toret = new Reference( parseReference( lex ) );
        } else {
            toret = parseLiteral( lex );
        }

        return toret;
    }

    private static Evaluable parseLiteral(Lexer lex) throws InterpretError
    {
        Evaluable toret = null;
        Lexer.TokenType nextTokenType = lex.getNextTokenType();

        lex.skipSpaces();

        if ( nextTokenType == Lexer.TokenType.Bool ) {
            toret = new BoolLiteral( Boolean.parseBoolean( lex.getToken() ) );
        }
        else
        if ( nextTokenType == Lexer.TokenType.Str ) {
            toret = new StrLiteral( lex.getStringLiteral() );
        }
        else
        if ( nextTokenType == Lexer.TokenType.Number ) {
            String strNum = lex.getNumberLiteral();

            try {
                if ( strNum.contains( "." ) ) {
                    toret = new RealLiteral( Double.parseDouble( strNum ) );
                } else {
                    toret = new IntLiteral( Long.parseLong( strNum ) );
                }
            }
            catch(NumberFormatException exc) {
                toret = new IntLiteral( -1  );
            }
        } else {
            throw new InterpretError( "literal expected" );
        }

        return toret;
    }

    public static String[] parseReference(Lexer lex) throws InterpretError
    {
        ArrayList<String> toret = new ArrayList<String>();
        String token;

        do {
            // Token
            lex.skipSpaces();
            token = lex.getToken();

            if ( token.isEmpty() ) {
                throw new InterpretError( "error parsing reference: " + toret.toString() );
            }

            toret.add( token );

            // Separator
            lex.skipSpaces();
            if ( lex.getCurrentChar() == '.' ) {
                lex.advance();
            } else {
               break;
            }
        } while ( !lex.isEOL() );

        return toret.toArray( new String[ toret.size() ] );
    }

    public static InterpretedMethod parseMethod(Lexer lex) throws InterpretError
    {
        int braceCount = 0;
        InterpretedMethod toret = null;
        StringBuilder strMth = new StringBuilder();

        lex.skipSpaces();
        if ( lex.getCurrentChar() != '{' ) {
            throw new InterpretError( "expected method" );
        }

        // Read all method body
        braceCount = 1;
        while( !lex.isEOL()
            && braceCount > 0 )
        {
            final char ch = lex.getCurrentChar();

            if ( ch == '}' ) {
                --braceCount;
            }
            else
            if ( ch == '{' ) {
                ++braceCount;
            }

            strMth.append( ch );

            lex.advance();
        }

        // Store '}'
        if ( !lex.isEOL() ) {
            strMth.append( '}' );
            lex.advance();
        }

        // Create method
        toret = new InterpretedMethod(
                InterpretedMethod.createNewMethodId(),
                strMth.toString()
        );

        return toret;
    }
}
