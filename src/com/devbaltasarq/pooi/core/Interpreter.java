// Pooi (c) 2014 Baltasar MIT License <jbgarcia@uvigo.es>

package com.devbaltasarq.pooi.core;

import com.devbaltasarq.pooi.core.evaluables.Attribute;
import com.devbaltasarq.pooi.core.evaluables.Command;
import com.devbaltasarq.pooi.core.evaluables.Method;
import com.devbaltasarq.pooi.core.evaluables.Reference;
import com.devbaltasarq.pooi.core.evaluables.methods.InterpretedMethod;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.objs.ObjectBool;

import java.io.*;
import java.util.ArrayList;

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
    public static final String EtqInfoObjMthAbout = "about";
    public static final String EtqComment = "#";

    public static final String PathToScripts = "scripts/";
    public static final String[] InternalScripts = { "math.poi" };

    /**
     * Represents an interpreter error
     * @author baltasarq
     */
    public static class InterpretError extends Exception {

        /** Creates a new instance of InterpretError */
        public InterpretError(String msg) {
            super( msg );
        }

    }

    /**
     * Represents an execution error at bootstrapping
     */
    public static class LoadInternalScriptError extends InterpretError {

        /** Creates a new instance of InterpretError */
        public LoadInternalScriptError(String msg) {
            super( msg );
        }

    }

    public static class AttributeNotFound extends InterpretError {

        /** Creates a new instance of AttributeNotFound */
        public AttributeNotFound(String obj, String atr) {
            super( "Attribute not found: '" + atr + "'" + " in '" + obj + "'" );
        }

    }

    /** Creates a fresh new interpreter, setting certain configuration options */
    public Interpreter(Runtime rt, InterpreterCfg cfg, StringBuffer msgs) throws InterpretError
    {
        this.rt = rt;
        this.cfg = cfg;
        this.error = false;
        this.createInfoObject();

        try {
            this.loadScripts();
        } catch(LoadInternalScriptError exc) {
            msgs.append( exc.getMessage() );
        }
    }

    private void loadScripts() throws LoadInternalScriptError
    {
        for (String path: InternalScripts ) {
            String completePath = PathToScripts + path;
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream( completePath );

            try {
                this.interpretScript( path, stream );
            } catch(InterpretError exc) {
                throw new LoadInternalScriptError( exc.getMessage() );
            }
        }
    }

    public void interpretScript(String fileName, InputStream stream) throws InterpretError
    {
        try {
            BufferedReader reader = new BufferedReader( new InputStreamReader( stream ) );
            String line;

            while ( ( line = reader.readLine() ) != null ) {
                line = line.trim();

                // Eliminate ">", if needed
                if ( line.startsWith( ">" ) ) {
                    line = line.substring( 1 ).trim();
                }

                if ( line.startsWith( EtqComment ) ) {
                    continue;
                }

                this.interpret( line );
            }
            reader.close();
        } catch(IOException exc) {
            throw new InterpretError( "ERROR loading script '" + fileName + "': " + exc.getMessage() );
        }
    }

    public void reset(Runtime rt) throws InterpretError
    {
        this.rt = rt;
        this.error = false;
        this.createInfoObject();
        this.loadScripts();
    }

    private final void createInfoObject() throws InterpretError
    {
        // About method
        Method mthAbout = new InterpretedMethod( this.rt, EtqInfoObjMthAbout,
                "((((Root.info.name + \" v\") + Root.info.version) + \"\n\") + Root.info.license)"
        );

        // Info object
        this.objInfo = this.getRuntime().createObject( EtqInfoObject );
        this.objInfo.set( EtqInfoObjAttrName, this.rt.createString(EtqInfoObjAttrName, AppInfo.Name ) );
        this.objInfo.set( EtqInfoObjAttrVersion, this.rt.createString(EtqInfoObjAttrVersion, AppInfo.Version ) );
        this.objInfo.set( EtqInfoObjAttrEmail, this.rt.createString(EtqInfoObjAttrEmail, AppInfo.Email ) );
        this.objInfo.set( EtqInfoObjAttrAuthor, this.rt.createString(EtqInfoObjAttrAuthor, AppInfo.Author ) );
        this.objInfo.set( EtqInfoObjAttrLicense, this.rt.createString(EtqInfoObjAttrLicense, AppInfo.License ) );
        this.objInfo.set( EtqInfoObjAttrVerbose, this.rt.createBool( this.cfg.isVerbose() ) );
        this.objInfo.set( EtqInfoObjAttrHasGui, this.rt.createBool( this.cfg.hasGui() ) );
        this.objInfo.set( EtqInfoObjMthAbout, mthAbout );
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
                s = s.substring( 0, s.length() - 1 );
            }
        }

        return s;
    }

    public String interpret(String cmds)
    {
        String response = "";
        StringBuilder msg = new StringBuilder();
        StringBuilder result = new StringBuilder();
        InterpretedMethod method = null;
        final Runtime rt = this.getRuntime();
        final ObjectBag objRoot = rt.getRoot();

        // Eliminate spurious literal objects
        rt.getLiteralsContainer().clear( false );

        if ( objRoot == null ) {
            error = true;
            msg.append( "terminated." );
        } else {
            try {
                method = new InterpretedMethod( rt, "REL_TopLevel", cmds );
                this.execute( method, objRoot, method.getRealParams(), msg, result );

                response = removeQuotes( result.toString().trim() );

                // If not verbose...
                if ( !( this.isVerbose() )
                  || msg.toString().trim().equals( response ) )
                {
                    msg.delete( 0, msg.length() );
                }
            } catch(InterpretError e) {
                error = true;
                response =  "Error: " + e.getMessage();
                result.append( response );
            } catch (Exception e) {
                error = true;
                response = "Error: unexpected: " + e.getMessage();
                result.append( response );
            }
        }

        msg.append( response );
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
                final Command command = (Command) evaluable;

                // Substitute __POP's in params
                final int numCmdParams = command.getNumArguments();
                Evaluable[] params = new Evaluable[ numCmdParams ];

                if ( numCmdParams > 0 ) {
                    final Evaluable[] cmdParams = command.getArguments();

                    for(int i = numCmdParams -1; i >= 0; --i) {
                        Evaluable param = cmdParams[ i ];

                        if ( param.isPopTask() ) {
                            param = stack.getTop();
                            stack.pop();
                        }
                        else
                        if ( param instanceof Reference )
                        {
                            param = this.findCorrectReferenceInParam(
                                        rt,
                                        self,
                                        method,
                                        param
                            );
                        }

                        params[ i ] = param;
                    }
                }

                // In reference
                ref = command.getReference();
                if ( ref.isPopTask() ) {
                    ref = stack.getTop();
                    stack.pop();
                }

                ref = this.findCorrectReferenceInParam( rt, self, method, ref );
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
                    if ( toret != null ) {
                        stack.push( toret.toReference() );
                    }
                } else {
                    break;
                }

                // Rebuilds the info object, if needed
                this.chkInfoObject( msg );
            }

            // Gather the results
            if ( toret != null ) {
                final String txtResult = removeQuotes( toret.getNameOrValueAsString() ) + "\n";

                if ( !( result.toString().endsWith( txtResult ) ) ) {
                    result.append( txtResult );
                }

                if ( result.toString().equals( msg.toString() ) ) {
                    msg.delete( 0, msg.length() );
                }
            }
        } catch(InterpretError e) {
            this.error = true;
            final String errorMessage = "Error: " + e.getMessage();
            msg.append( errorMessage );
            result.append( errorMessage );
        }

        return toret;
    }

    /** Finds the correct reference in param val,
      * againts the method, the current object (self), and the runtime.
      * @param rt the runtime the interpreter executes against.
      * @param self the object executing the method.
      * @param method the method that it is being executed.
      * @param val the value to find.
      */
    protected Evaluable findCorrectReferenceInParam(Runtime rt, ObjectBag self, InterpretedMethod method, Evaluable val)
            throws InterpretError
    {
        Evaluable toret = null;

        if ( val instanceof Reference ) {
            final Reference ref = (Reference) val;
            final String[] parts = ref.getAttrs();
            final String firstAttr = parts[ 0 ];
            ObjectBag startPoint = rt.getRoot();
            Evaluable param = method.getRealParameter( firstAttr );

            if ( !( firstAttr.equals( Reserved.RootObject ) ) ) {
                if ( firstAttr.equals( Reserved.SelfRef ) ) {
                    startPoint = self;
                }
                else
                if ( param != null ) {
                    startPoint = rt.solveToObject( param );
                }
            }

            ObjectBag obj = rt.findObjectByPathInObject( startPoint, ref );

            if ( obj != null ) {
                toret = obj.toReference();
            }
        }

        if ( toret == null ) {
            toret = val;
        }

        return toret;
    }

    /** @return whether there was an error or not. */
    public boolean getError()
    {
        return error;
    }

    /** Loads a complete session.
      * @param fileName the file name to load.
      */
    public String loadSession(String fileName)
    {
        final String SESSION_OK = "\n\n*** Session '%s' restored ok. ***\n\n";
        final String SESSION_ERROR = "\n\n*** Session '%s' restored with errors ***.\n\n";
        final String SESSION_FAILED = "\n*** Session '%s' restoration failed ***\n\n";
        final String FILE_NAME = new File( fileName ).getName();
        StringBuilder toret = new StringBuilder();
        String[] source = this.loadSessionFile( fileName, toret );
        
        if ( source.length > 0 ) {
            // Interpret all instructions
            for(String instruction: source) {
                toret.append( ">" + instruction );
                toret.append( '\n' );
                toret.append( interpret( instruction ) );
                toret.append( "\n\n" );
                
                if ( this.getError() ) {
                    break;
                }
            }
            
            if ( this.getError() ) {
                toret.append( String.format( SESSION_ERROR, FILE_NAME ) );
            } else {
                toret.append( String.format( SESSION_OK, FILE_NAME ) );
            }
        } else {
            toret.append( String.format( SESSION_FAILED, FILE_NAME ) );
        }
        
        return toret.toString();
    }
    
     /** Loads a complete session.
       * @param fileName the file name to load.
       * @param console the future messages to show in the console
       */
    private String[] loadSessionFile(String fileName, StringBuilder console)
    {
        final ArrayList<String> toret = new ArrayList<>();
        String lin;
        String instruction = "";
        int openBraces = 0;
        
        try (BufferedReader session = new BufferedReader( new FileReader(
                                             new File( fileName ) ) ) )
        {
            lin = session.readLine();
            while( lin != null ) {
                lin = lin.trim();
              
                // Should this line be considered?
                if ( lin.length() > 0
                  && ( lin.charAt( 0 ) == '>'
                    || openBraces > 0 ) )
                {
                    // Remove the opening '>'
                    if ( openBraces <= 0
                      && lin.charAt( 0 ) == '>' )
                    {
                        lin = lin.substring( 1 );
                    }
                    
                    // Check for braces
                    for(char ch: lin.toCharArray()) {
                        if ( ch == '{' ) {
                            ++openBraces;
                        }
                        else
                            if ( ch == '}' ) {
                            --openBraces;
                        }
                    }
                    
                    // Add it
                    instruction += " " + lin;
                    if ( openBraces <= 0 ) {
                        openBraces = 0;
                        toret.add( instruction );
                        instruction = "";
                    }
                }
                
                lin = session.readLine();
            }
        } catch(Exception e) {
            toret.clear();
            console.append( "\nERROR: " + e.getMessage() + "\n" );
        }
        
        return toret.toArray( new String[ 0 ] );
    }

    /** Checks whether the interpreter is in verbose mode or not.
      * Create the info object if it does not exist.
      * @return true if the interpreter is in verbose mode, or false.
      */
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

    /** Puts the interpreter in verbose mode or not.
      * Create the info object if it does not exist.
      * @param flag true to put the interpreter in verbose mode.
      */
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
    
    /** @return the info object.
      * @see Runtime.
      */
    public ObjectBag getObjInfo()
    {
        return this.objInfo;
    }

    /** @return the main configuration of the app. */
    public InterpreterCfg getConfiguration()
    {
        return this.cfg;
    }

    /** @return true if the app is executing with GUI, false otherwise. */
    public boolean hasGui()
    {
        return this.getConfiguration().hasGui();
    }

    /** @return the runtime for the interpreter.
      * @see Runtime.
      */
    public Runtime getRuntime()
    {
        return this.rt;
    }

    protected boolean error;
    protected Runtime rt;
    private ObjectBag objInfo;
    private final InterpreterCfg cfg;
}
