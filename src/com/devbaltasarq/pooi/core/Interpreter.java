package com.devbaltasarq.pooi.core;

import com.devbaltasarq.pooi.core.evaluables.Command;
import com.devbaltasarq.pooi.core.evaluables.Method;
import com.devbaltasarq.pooi.core.evaluables.Reference;
import com.devbaltasarq.pooi.core.evaluables.methods.InterpretedMethod;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectStr;

import java.io.*;

/**
 * The interpreter, making sense of commands sent.
 * @author baltasarq
 */
public class Interpreter {

    /** Creates a new instance of the interpreter */
    public Interpreter() throws InterpretError
    {
        this.error = false;
        this.rt = Runtime.getRuntime();
        this.createAuxiliaryObjects();
    }

    private final void createAuxiliaryObjects() throws InterpretError
    {
        final Runtime rt = this.getRuntime();

        // Author object
        ObjectStr name = rt.createString( "name", AppInfo.Author );
        ObjectStr email =  rt.createString( "email", AppInfo.Email );
        this.objAuthor =  rt.createObject( "author" );
        this.objAuthor.set( name.getName(), name );
        this.objAuthor.set( email.getName(), email );

        // objAbout object
        ObjectStr appName =  rt.createString( "appName", AppInfo.Name );
        ObjectStr version =  rt.createString( "appVersion", AppInfo.Version );
        this.objAbout =  rt.createObject( "about" );
        this.objAbout.set( appName.getName(), appName );
        this.objAbout.set( version.getName(), version );

        // objHelp object
        ObjectStr use = rt.createString( "use",
                                      "copy objects in order to create new ones\n"
                                              + " insert orders in the form ( <object> <msg> <args> )\n\n"
        );

        ObjectStr msgs = rt.createString( "available_messages",
                                                 "\n\nobject rename <string>\n"
                                               + "object name\n"
                                               + "object copy\n"
                                               + "object createChild\n"
                                               + "object erase <string_attribute_name>\n"
                                               + "object set <string_attribute_name> <reference>\n"
                                               + "object clear\n"
                                               + "object list\n"
                                               + "object path\n"
                                               + "object str\n"
                                               + "object is? <reference>\n"
                                               + "\nTry:\n"
                                               + "Str list\n"
                                               + "Int list\n"
                                               + "Real list\n"
                                               + "\n'object.attribute' is the same as 'object.attribute str'"
                                               + "\n'obj.x.y.z = <reference>' is the same as 'obj.x.y set \"z\" <reference>'"
                                               + "\n\n"
        );

        this.objHelp = rt.createObject( "help" );
        objHelp.set( use.getName(), use );
        objHelp.set( msgs.getName(), msgs );
    }

    public String interpret(String cmds)
    {
        StringBuilder msg = new StringBuilder();
        InterpretedMethod method = null;
        final ObjectBag objRoot = this.getRuntime().getRoot();

        // Eliminate spurious literal objects
        this.getRuntime().getLiteralsContainer().clear( false );

        if ( objRoot == null ) {
            error = true;
            msg.append( "terminated." );
        } else {
            try {
                this.saveToTranscript( "> " + cmds.toString() );
                method = new InterpretedMethod( "REL_TopLevel", cmds );
                this.execute( method, objRoot, method.getRealParams(), msg );
                this.saveToTranscript( msg.toString() );
            } catch(InterpretError e) {
                error = true;
                msg.append( "Error: " + e.getMessage() );
            } catch (IOException e) {
                error = true;
                msg.append( "Error: I/O: " + e.getMessage() );
            }
        }

        return msg.toString();
    }

    protected ObjectBag execute(InterpretedMethod method, ObjectBag self, Evaluable[] args, StringBuilder msg)
    {
        Evaluable ref;
        ObjectBag toret = null;
        error = false;
        final Runtime rt = this.getRuntime();
        ExecutionStack stack = new ExecutionStack();

        try {
            // Set self & params
            method.setRealParams( self, args );

            // Execute command stack
            for(Evaluable evaluable: method.getCmds()) {
                Command command = (Command) evaluable;

                // Substitute __POP's in params
                final int numCmdParams = command.getNumParams();
                Evaluable[] params = new Evaluable[ numCmdParams ];

                if ( numCmdParams > 0 ) {
                    final Evaluable[] cmdParams = command.getParams();

                    for(int i = numCmdParams -1; i >= 0; --i) {
                        Evaluable param = cmdParams[ i ];

                        if ( param.toString().equals( Parser.PopTask ))
                        {
                            param = stack.getTop();
                            stack.pop();
                        }
                        else
                        if ( param instanceof Reference )
                        {
                            param = this.findCorrectReference(
                                        rt,
                                        self,
                                        method,
                                        (Reference) param
                            );
                        }

                        params[ i ] = param;
                    }
                }

                // In reference
                ref = command.getReference();
                if ( ref.toString().equals( Parser.PopTask ) ) {
                    ref = stack.getTop();
                    stack.pop();
                }

                ref = this.findCorrectReference( rt, self, method, ref );
                ObjectBag obj = rt.solveToObject( ref );

                // Is it a valid order?
                if ( !command.isValid() ) {
                    throw new InterpretError( "syntax error: missing reference" );
                }

                Method mth = obj.lookUpMethod( command.getMessage() );

                if ( mth == null ) {
                    throw new InterpretError( "syntax error: message '"
                                          + command.getMessage()
                                          + "' not found in '"
                                          + obj.getName()
                                          + '\''
                    );
                } else {
                    if ( mth instanceof NativeMethod ) {
                        NativeMethod nativeMth = (NativeMethod) mth;
                        toret = nativeMth.doIt( obj, params, msg );
                    } else {
                        toret = this.execute( (InterpretedMethod) mth, obj, params, msg );
                    }
                }

                if ( !error ) {
                    msg.append( '\n' );
                    stack.push( toret.toReference() );
                } else {
                    break;
                }
            }
        } catch(InterpretError e) {
            error = true;
            msg.append( "Error: " + e.getMessage() );
        }

        return toret;
    }

    protected Evaluable findCorrectReference(Runtime rt, ObjectBag self, InterpretedMethod method, Evaluable ref)
            throws InterpretError
    {
        Evaluable toret = null;

        if ( ref instanceof Reference ) {
            toret = method.getRealParameter( ref.toString() );

            if ( toret == null ) {
                ObjectBag obj = rt.findObjectByPathInObject( self, (Reference) ref );

                if ( obj != null ) {
                    toret = obj.toReference();
                }
            }
        }

        if ( toret == null ) {
            toret = ref;
        }

        return toret;
    }

    protected String saveToTranscript(String txt) throws IOException
    {
        if ( transcript != null ) {
            transcript.write( txt + "\n" );
        }
        
        return txt;
    }

    public void activateTranscript(String fileName)
    {
        try {
            transcript = new BufferedWriter( new FileWriter( new File( fileName ) ) );
        } catch(Exception e) {
            transcript = null;
        }
    }
    
    public void endTranscript()
    {
        if ( transcript != null ) {
            try {
                transcript.close();
            } catch (IOException ignored) {
                
            }
        }
    }

    public boolean getError()
    {
        return error;
    }

    public String loadSession(String fileName) {
        StringBuilder toret = new StringBuilder();
        String lin;
        
        try {
            BufferedReader session = new BufferedReader( new FileReader( new File( fileName ) ) );
            
            lin = session.readLine();
            while( lin != null ) {
                lin = lin.trim();
                
                if ( lin.length() > 1 ) {
                    if ( lin.charAt( 0 ) == '>' ) {
                        toret.append( lin );
                        toret.append( '\n' );
                        toret.append(
                                interpret( lin.substring( 1, lin.length() ) ) 
                        );
                        toret.append( "\n\n" );
                    }
                }
                
                lin = session.readLine();
            }
            
            session.close();
            toret.append( "\n\nsession restored ok.\n\n");
        } catch(Exception e) {
            toret.append( "\nsession failed to restore\n\n" );
        }  
        
        return toret.toString();
    }

    public ObjectBag getAuthorObject() {
        return this.objAuthor;
    }

    public ObjectBag getAboutObject() {
        return this.objAbout;
    }

    public ObjectBag getHelpObject() {
        return this.objHelp;
    }

    public Runtime getRuntime()
    {
        return this.rt;
    }


    protected boolean error;
    protected final Runtime rt;
    private ObjectBag objAuthor;
    private ObjectBag objAbout;
    private ObjectBag objHelp;
    protected BufferedWriter transcript = null;
}
