package com.devbaltasarq.pooi.core;

import com.devbaltasarq.pooi.core.evaluables.Attribute;
import com.devbaltasarq.pooi.core.evaluables.Command;
import com.devbaltasarq.pooi.core.evaluables.Method;
import com.devbaltasarq.pooi.core.evaluables.Reference;
import com.devbaltasarq.pooi.core.evaluables.methods.InterpretedMethod;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectBool;

import java.io.*;

/**
 * The interpreter, making sense of commands sent.
 * @author baltasarq
 */
public class Interpreter {
    public static final String EtqInfoObject = "info";
    public static final String EtqInfoObjAttrName = "name";
    public static final String EtqInfoObjAttrVersion = "version";
    public static final String EtqInfoObjAttrEmail = "email";
    public static final String EtqInfoObjAttrAuthor = "author";
    public static final String EtqInfoObjAttrHelp = "help";
    public static final String EtqInfoObjAttrLicense = "license";
    public static final String EtqInfoObjAttrVerbose = "verbose";
    public static final String EtqInfoObjAttrHasGui = "gui";

    /** Creates a fresh new interpreter, setting certain configuration options */
    public Interpreter(Runtime rt, InterpreterCfg cfg) throws InterpretError
    {
        this.rt = rt;
        this.cfg = cfg;
        this.error = false;
        this.createInfoObject();
    }

    private final void createInfoObject() throws InterpretError
    {
        // Info object
        this.objInfo = this.getRuntime().createObject( EtqInfoObject );
        this.objInfo.set( EtqInfoObjAttrName, this.rt.createString(EtqInfoObjAttrName, AppInfo.Name ) );
        this.objInfo.set( EtqInfoObjAttrVersion, this.rt.createString(EtqInfoObjAttrVersion, AppInfo.Version ) );
        this.objInfo.set( EtqInfoObjAttrEmail, this.rt.createString(EtqInfoObjAttrEmail, AppInfo.Email ) );
        this.objInfo.set( EtqInfoObjAttrAuthor, this.rt.createString(EtqInfoObjAttrAuthor, AppInfo.Author ) );
        this.objInfo.set( EtqInfoObjAttrLicense, this.rt.createString(EtqInfoObjAttrLicense, AppInfo.License ) );
        this.objInfo.set( EtqInfoObjAttrVerbose, this.rt.createBool( this.cfg.isVerbose() ) );
        this.objInfo.set( EtqInfoObjAttrHasGui, this.rt.createBool( this.cfg.hasGui() ) );
        this.objInfo.set( EtqInfoObjAttrHelp, this.rt.createString(EtqInfoObjAttrHelp,
                                      "copy objects in order to create new ones\n"
                                               + " insert orders in the form ( <object> <msg> <args> )\n\n"
                                               +   "\n\nobject rename <string>\n"
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
        ) );

        return;
    }

    public static String removeQuotes(String s) {
        if ( s.startsWith( "\"" ) ) {
            s = s.substring( 1 );
            if ( s.endsWith( "\""  ) ) {
                s = s.substring( 0, s.length() - 2 );
            }
        }

        return s;
    }

    public String interpret(String cmds)
    {
        StringBuilder msg = new StringBuilder();
        StringBuilder result = new StringBuilder();
        InterpretedMethod method = null;
        final Runtime rt = this.getRuntime();
        final ObjectBag objRoot = rt.getRoot();

        // Eliminate spurious literal objects
        this.getRuntime().getLiteralsContainer().clear( false );

        if ( objRoot == null ) {
            error = true;
            msg.append( "terminated." );
        } else {
            try {
                this.saveToTranscript( "> " + cmds );
                method = new InterpretedMethod( rt, "REL_TopLevel", cmds );
                this.execute( method, objRoot, method.getRealParams(), msg, result );

                // Si no es verboso...
                if ( !( this.isVerbose() ) ) {
                    msg.delete( 0, msg.length() );
                }

                this.saveToTranscript( msg.toString() );
                this.saveToTranscript( removeQuotes( result.toString() ) );
            } catch(InterpretError e) {
                error = true;
                result.append( "Error: " + e.getMessage() );
            } catch (IOException e) {
                error = true;
                result.append( "Error: I/O: " + e.getMessage() );
            }
        }

        msg.append( removeQuotes( result.toString() ) );
        return msg.toString();
    }

    protected void chkInfoObject(StringBuilder msg) throws InterpretError
    {
        final Runtime rt = this.getRuntime();

        // Chk the object is missing
        if ( rt.getRoot().localLookUpAttribute( EtqInfoObject ) == null ) {
            this.createInfoObject();
            msg.append( '\n' );
            msg.append( EtqInfoObject );
            msg.append( " object re-created (cannot be removed)." );
        }

        final ObjectBag objInfo = this.getObjInfo();

        // Chk the verbose option
        if ( objInfo.localLookUpAttribute( EtqInfoObjAttrVerbose ) == null ) {
            objInfo.set( EtqInfoObjAttrVerbose, rt.createBool( this.getConfiguration().hasGui() ) );
            msg.append( '\n' );
            msg.append( EtqInfoObjAttrVerbose );
            msg.append( " re-created in " );
            msg.append( EtqInfoObject );
            msg.append( " (cannot be removed)." );
        }

        // Chk the gui option
        if ( objInfo.localLookUpAttribute(EtqInfoObjAttrHasGui) == null ) {
            objInfo.set(EtqInfoObjAttrHasGui, rt.createBool( this.getConfiguration().hasGui() ) );
            msg.append( '\n' );
            msg.append(EtqInfoObjAttrHasGui);
            msg.append( "  re-created in " );
            msg.append( EtqInfoObject );
            msg.append( " (cannot be removed)." );
        }
    }

    protected ObjectBag execute(InterpretedMethod method, ObjectBag self, Evaluable[] args,
                                StringBuilder msg, StringBuilder result)
    {
        Evaluable ref;
        ObjectBag toret = null;
        this.error = false;
        ExecutionStack stack = new ExecutionStack();

        try {
            // Set self & params
            method.setRealParams( self, args );

            // Execute command stack
            for(Evaluable evaluable: method.getCmds()) {
                Command command = (Command) evaluable;

                // Substitute __POP's in params
                final int numCmdParams = command.getNumArguments();
                Evaluable[] params = new Evaluable[ numCmdParams ];

                if ( numCmdParams > 0 ) {
                    final Evaluable[] cmdParams = command.getArguments();

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
                        toret = this.execute( (InterpretedMethod) mth, obj, params, msg, result );
                    }
                }

                if ( !error ) {
                    msg.append( '\n' );
                    stack.push( toret.toReference() );
                } else {
                    break;
                }

                // Rebuilds the info object, if needed
                this.chkInfoObject( msg );
            }
        } catch(InterpretError e) {
            error = true;
            msg.append( "Error: " + e.getMessage() );
        }

        // Gather the results
        result.delete( 0, result.length() );

        if ( toret != null ) {
            result.append( toret.getNameOrValueAsString() );
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

    public boolean isVerbose() throws InterpretError
    {
        final InterpreterCfg cfg = this.getConfiguration();
        Attribute attrVerbose = null;
        ObjectBag info = this.getObjInfo();

        // Create the object, if it does not exist
        if ( info == null ) {
            this.createInfoObject();
            info = this.getObjInfo();
        }

        // Create the attribute about verbose, if needed
        attrVerbose = info.localLookUpAttribute( EtqInfoObjAttrVerbose );

        if ( attrVerbose == null ) {
            this.getObjInfo().set( EtqInfoObjAttrVerbose, this.getRuntime().createBool( cfg.isVerbose() ) );
        }

        cfg.setVerbose( ( (ObjectBool) attrVerbose.getReference() ).getValue() );
        return cfg.isVerbose();
    }

    public void setVerbose(boolean flag) throws InterpretError {
        Attribute attrVerbose = null;
        ObjectBag info = this.getObjInfo();

        // Create the object, if it does not exist
        if ( info == null ) {
            this.createInfoObject();
            info = this.getObjInfo();
        }

        // Create the attribute about verbose, if needed
        this.getObjInfo().set( EtqInfoObjAttrVerbose, this.getRuntime().createBool( flag ) );
        this.getConfiguration().setVerbose( flag );
    }

    public InterpreterCfg getConfiguration() {
        return this.cfg;
    }

    public ObjectBag getObjInfo() {
        return this.objInfo;
    }

    public boolean hasGui() {
        return this.getConfiguration().hasGui();
    }

    public Runtime getRuntime()
    {
        return this.rt;
    }

    protected boolean error;
    protected final Runtime rt;
    private ObjectBag objInfo;
    protected BufferedWriter transcript = null;
    private InterpreterCfg cfg;
}
