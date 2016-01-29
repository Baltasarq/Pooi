package core;

import core.evaluables.Attribute;
import core.evaluables.Method;
import core.evaluables.ParentAttribute;
import core.evaluables.Reference;
import core.exceps.AttributeNotFound;
import core.exceps.InterpretError;
import core.objs.ObjectRoot;
import core.objs.ValueObject;

import java.util.*;

/**
 * The base class for all objects in the runtime
 * @author baltasarq
 */
public class ObjectBag {

    public static class ContainerAttributePair {
        public ContainerAttributePair(ObjectBag c, Attribute atr)
        {
            this.container = c;
            this.attribute = atr;
        }

        public ObjectBag getContainer()
        {
            return this.container;
        }

        public Attribute getAttribute()
        {
            return this.attribute;
        }

        private ObjectBag container;
        private Attribute attribute;
    };

    public static final boolean Check = true;
    public static final boolean DontCheck = false;

    /**
     * Creates a new instance of ObjectBag
     * When an empty name is passed, an automatic name is created.
     *
     * @param n             The name of the object.
     * @param parent        The parent object in which to register it.
     * @param container     The object this is contained into.
     */
    public ObjectBag(String n, ObjectBag parent, ObjectBag container)
            throws InterpretError
    {
        this( n, parent, container, Check );
    }

    /**
     * Creates a new instance of ObjectBag
     * When an empty name is passed, an automatic name is created.
     *
     * @param n             The name of the object.
     * @param parent        The parent object in which to register it.
     * @param container     The object this is contained into.
     * @param chk           defines whether the objects' name should be checked or not.
     */
    public ObjectBag(String n, ObjectBag parent, ObjectBag container, boolean chk)
            throws InterpretError
    {
        attributes = new Hashtable<>();
        methods = new Hashtable<>();

        // Prepare name
        n = n.trim();
        if ( n.isEmpty() ) {
            n = this.createNewName();
        } else {
            if ( chk ) {
                this.chkIdentifierForObjectName( n );
            }
        }

        // Set attributes
        this.name = n;
        this.container = container;

        // Set the parent attribute
        if ( parent != null ) {
            // Use strictly the low-level method, since init will make recursion
            this.setAttribute( ParentAttribute.ParentAttributeName, new ParentAttribute( parent ) );
        }
    }

    /**
     * Returns the collection of attributes, as a native vector
     *
     * @return The collection of attributes, as a native vector
     */
    public Attribute[] getAttributes()
    {
        return this.attributes.values().toArray( new Attribute[ attributes.size() ] );
    }

    /**
     * Returns the name of the object
     *
     * @return The name of the object, as a String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Change the id of the object
     *
     * @param id A String with the object's id
     */
    public void setName(String id) throws InterpretError
    {
        final ObjectBag containerObj = this.getContainer();

        id = id.trim();

        this.chkIdentifierForObjectName( id );

        if ( containerObj != null ) {
            final Attribute atrInParent = containerObj.localInverseLookUp( this  );

            if ( atrInParent != null
              && atrInParent.getName() != id  )
            {
                if ( containerObj.localLookUpAttribute( id ) != null ) {
                    throw new InterpretError( "'" + id + "' already exists in " + containerObj.getName() );
                }
            }
        }

        this.name = id;
    }

    /**
     * Does an inverse lookup
     * O(n)
     *
     * @param obj The object that is referenced from this container.
     * @return The attribute that points to that object.
     */
    public Attribute localInverseLookUp(ObjectBag obj)
    {
        Attribute toret = null;

        for(Attribute atr: this.attributes.values()) {
            if ( atr.getReference() == obj ) {
                toret = atr;
                break;
            }
        }

        return toret;
    }

    /**
     * Returns a path, built with real attributes
     *
     * @return The object path, as a native vector ObjectBag[]
     */
   protected Attribute[] getObjectPath()
   {
       Vector<Attribute> toret = new Vector<>();

       try {
           final ObjectRoot root = Runtime.getRuntime().getRoot();
           ObjectBag currentObj = this;
           ObjectBag containerObj = currentObj.getContainer();

           while( currentObj != root ) {
               toret.insertElementAt( containerObj.localInverseLookUp( currentObj ), 0 );

               currentObj = containerObj;
               containerObj = containerObj.getContainer();
           }

           toret.insertElementAt( new Attribute( root.getName(), root ), 0 );
       }
       catch (InterpretError ignored)
       {
       }

       return toret.toArray( new Attribute[ toret.size() ] );
   }

    /**
     * The path to follow in order to get to this object from Root
     *
     * @return A String with a dot-separated object identifiers
     */
    public String getPath()
    {
        Attribute[] path = this.getObjectPath();
        StringBuilder toret = new StringBuilder();

        for(int i = 0; i < path.length; ++i) {
            final Attribute objPathPart = path[ i ];

            toret.append( objPathPart.getName() );

            if ( i < ( path.length -1) ) {
                toret.append( '.' );
            }
        }

        return toret.toString();
    }

   private void loadAttributesInList(Attribute[] atrs, LinkedList<Attribute> atrList, HashSet<String> listed)
   {
       ParentAttribute parentAtr = null;
       int loaded = 0;

       // Add all attributes except the parent
       for(Attribute atr: atrs) {
            if ( !listed.contains( atr.getName() ) ) {
                if ( !( atr instanceof ParentAttribute ) ) {
                    atrList.add( atr );
                    ++loaded;
                } else {
                    parentAtr = (ParentAttribute) atr;
                }
            }
       }

       // Add the parent, at the end
       atrList.add( loaded, parentAtr );
   }

    /**
     * Returns all the recursive info in the object, as a single String
     * It is important here to allow attributes to be included in the listing just once,
     * since a member in a derived object overrides the same member in a parent
     * object.
     * No recursive algorithm for inheritance.
     *
     * @return A String with all the info of the object
     */
    @Override
    public String toString()
    {
        final String StrEquals = " = ";
        HashSet<String> listedAttributes = new HashSet<>();
        HashSet<ObjectBag> listedObjects = new HashSet<>();
        LinkedList<Attribute> attributesToList = new LinkedList<>();
        StringBuilder toret = new StringBuilder();

        toret.append( this.getName() );
        toret.append( StrEquals );
        toret.append( "{ " );

        // Load initial attributes
        loadAttributesInList( this.getAttributes(), attributesToList, listedAttributes );
        listedObjects.add( this );

        // Now, show them
        try {
            final ObjectBag absParent = Runtime.getRuntime().getAbsoluteParent();

            while( !attributesToList.isEmpty() ) {
                // Get attribute data
                final Attribute currentAtr = attributesToList.getFirst();
                final ObjectBag currentObj = currentAtr.getReference();
                final String atrName = currentAtr.getName();

                // Remove just processed attribute
                attributesToList.removeFirst();

                // Show it
                if ( currentAtr instanceof ParentAttribute ) {
                    if ( currentObj != absParent ) {
                        loadAttributesInList( currentObj.getAttributes(), attributesToList, listedAttributes );
                    }
                }
                else {
                    toret.append( atrName );
                    toret.append( StrEquals );

                    if ( currentObj instanceof ValueObject ) {
                        toret.append( currentObj.toString() );
                    }
                    else
                    if ( listedObjects.contains( currentObj ) )
                    {
                        toret.append( currentObj.getName() );
                    }
                    else {
                        toret.append( currentObj.toString() );
                    }

                    toret.append( ' ' );
                    listedAttributes.add( atrName );
                    listedObjects.add( currentObj );
                }
            }
        }
        catch(InterpretError | StackOverflowError | OutOfMemoryError exc)
        {
            toret.delete( toret.length() -100, toret.length() -1 );
            toret.append( "... " );
        }

        listedAttributes.clear();
        listedObjects.clear();
        attributesToList.clear();
        return toret.append( "} " ).toString();
    }

    /**
     * Converts object attributes info to Json representation.
     *
     * @return A String holding the Json representation of the object
     */
    public String toJson()
    {
        StringBuilder toret = new StringBuilder();

        // Object
        toret.append( this.getName() );
        toret.append( '=' );
        toret.append( '{' );

        // Append object's name
        toret.append( '"' );
        toret.append( Persistence.ObjectNameMember );
        toret.append( "\":\"" );
        toret.append( this.getName() );
        toret.append( "\"" );

        // Append remaining attributes
        for(Attribute attr : this.getAttributes() ) {
            ObjectBag obj = attr.getReference();

            // Separate with a comma
            toret.append( ',' );

            // Append attribute name
            toret.append( '"');
            toret.append( attr.getName() );
            toret.append( "\":" );

            // Append value
            if ( obj instanceof ValueObject ) {
                // Append value
                toret.append( obj.toString().replace( "\n", "\\n" ) );
            } else {
                // Append target object name in quotes
                toret.append( '"' );
                toret.append( obj.getName() );
                toret.append( '"' );
            }
        }

        toret.append( '}' );

        return toret.toString();
    }

    /**
     * Returns a reference, based on the path of the object
     *
     * @return A reference to this object
     */
    public Reference toReference()
    {
        Attribute[] path = this.getObjectPath();
        ArrayList<String> toret = new ArrayList<>();

        for(int i = 0; i < path.length; ++i) {
            toret.add( path[ i ].getName() );
        }

        return ( new Reference( toret.toArray( new String[ toret.size() ] )  ) );
    }

    /**
     * Returns a list of the attributes in the object
     * @return The list of the attributes, with its short description (kind),
     *         as a String
     */
    public String list()
    {
        StringBuilder toret = new StringBuilder();

        toret.append( getName() );
        toret.append( ": " );
        toret.append( getParentObject().getName() );
        toret.append( " = {\n" );

        for (Method mth : this.methods.values()) {
            toret.append( "\t" );
            toret.append( mth.toString() );
            toret.append( "\n" );
        }

        for (Attribute atr : this.attributes.values()) {
            toret.append( "\t" );
            toret.append( atr.getKind() );
            toret.append( "\n" );
        }

        return toret.append( '}' ).toString();
    }

    /**
     * Returns a short description of the object, involving the "type"
     * (obtained by inheritance)
     * @return A short description of the object, as a String
     */
    public String getKind()
    {
        String toret = this.getName() + ": " + getParentObject().getName() + " = {}";

        if ( getParentObject() == this ) {
            toret = this.getName();
        }

        return toret;
    }

    /**
     * Returns the object which is the parent of this object
     * @return The parent of this object, as an ObjectBag.
     */
    public ObjectBag getParentObject()
    {
        ObjectBag toret = this;
        Attribute atr = this.localLookUpAttribute( ParentAttribute.ParentAttributeName );

        if ( atr!= null
          && atr.getReference() != null )
        {
              toret = atr.getReference();
        }

        return toret;
    }

    /** Checks that there are no cycles in inheritance
      * @param parent The object to check
      */
    private void chkCyclesInParent(ObjectBag parent) throws InterpretError
    {
        final ObjectBag absParent = Runtime.getRuntime().getAbsoluteParent();
        HashSet<ObjectBag> visited = new HashSet<>();

        visited.add( this );
        while( parent != absParent ) {
            // Chk visited
            if ( visited.contains( parent ) ) {
                throw new InterpretError( "setting this parent makes a cycle in: " + parent.getName() );
            } else {
                visited.add( parent );
            }

            // Climb in the hierarchy
            parent = parent.getParentObject();
        }

        return;
    }

    /**
     * Adds an object to the list of attributes of this object
     * @param name The name of this new object in the attribute list
     * @param obj The ObjectBag reference to the object
     * @throws core.exceps.InterpretError
     */
    public void set(String name, ObjectBag obj) throws InterpretError
    {
        final Runtime rt = Runtime.getRuntime();
        Attribute atr;

        // Prepare attribute along with the name
        this.chkIdentifier( name );

        if ( name.equals( ParentAttribute.ParentAttributeName ) ) {
            this.chkCyclesInParent( obj );
            atr = new ParentAttribute( obj );
        } else {
            atr = new Attribute( name, obj );
        }

        // Insert the attribute
        this.setAttribute( name, atr );

        // No longer a temporal object
        if ( obj.getContainer() == rt.getLiteralsContainer() ) {
            obj.setContainer( this );
            obj.setName( name  );
        }

        return;
    }

    /** Adds a new method to the list of methods of this object
      * @param name The name of this new method
      * @param obj The Method object itself
      * @throws core.exceps.InterpretError if name is not valid
     */
    public void set(String name, Method mth) throws InterpretError
    {
        this.chkIdentifier( name );
        this.setMethod( name, mth );
    }

    protected final void setMethod(String name, Method mth) throws InterpretError
    {
        if ( this.localLookUpAttribute( name ) != null ) {
            throw new InterpretError( "Already have an attribute named: " + name );
        }

        this.methods.put( name, mth );
    }

    protected final void setAttribute(String name, Attribute atr) throws InterpretError
    {
        if ( this.localLookUpMethod( name ) != null ) {
            throw new InterpretError( "Already have a method named: " + name );
        }

        this.attributes.put( name, atr );
    }

    /**
     * Returns an attribute by its name
     * @param atrName The name of the attribute
     * @return The attribute object, or null if not found
     */
    public Attribute lookUpAttribute(String atrName)
    {
        ObjectBag oldParent = null;
        ObjectBag parent = this.getParentObject();
        Attribute toret = this.localLookUpAttribute( atrName );

        try {
            final ObjectBag absParent = Runtime.getRuntime().getAbsoluteParent();

            while( toret == null
                && oldParent != absParent )
            {
                toret = parent.localLookUpAttribute( atrName );
                oldParent = parent;
                parent = parent.getParentObject();
            }
        } catch(Exception ignored)
        {
            toret = null;
        }

        return toret;
    }

    /**
     * Returns an attribute by its name, in *this* object *only*
     * @param atrName The name of the attribute
     * @return The attribute object
     */
    public Attribute localLookUpAttribute(String atrName)
    {
        return this.attributes.get( atrName );
    }

    /**
     * Returns a method by its name
     * @param mthName The name of the attribute
     * @return The attribute object, or null if not found
     */
    public Method lookUpMethod(String mthName)
    {
        ObjectBag oldParent = null;
        ObjectBag parent = this.getParentObject();
        Method toret = this.localLookUpMethod( mthName );

        try {
            final ObjectBag absParent = Runtime.getRuntime().getAbsoluteParent();

            // Follow the inheritance chain
            while( oldParent != absParent
                && toret == null )
            {
                toret = parent.localLookUpMethod( mthName );
                oldParent = parent;
                parent = parent.getParentObject();
            }
        } catch(Exception ignored) {
            toret = null;
        }

        return toret;
    }

    /**
     * Returns a method by its name, in *this* object only.
     * @param mthName The name of the attribute
     * @return The attribute object, or null if not found
     */
    public Method localLookUpMethod(String mthName)
    {
        return this.methods.get( mthName );
    }

    /**
     * Copies this object in the container's collection,
     * with a new (provided) name
     *
     * @param name The name of the object. Assigned automatically if empty.
     * @param container The object in which to include the new object
     * @return An ObjectBag reference to the new (copied) object
     * @throws core.exceps.InterpretError  if the new name is not valid
     */
    public ObjectBag copy(String name, ObjectBag container) throws InterpretError
    {
        ObjectBag toret = null;
        HashSet<ObjectBag> objectsCopied = new HashSet<>();
        LinkedList<ContainerAttributePair> objectsToCopy = new LinkedList<>();

        objectsToCopy.add(
                new ContainerAttributePair(
                        container,
                        new Attribute( name, this ) ) );
        objectsCopied.add( this );

        while( !objectsToCopy.isEmpty() ) {
            final ContainerAttributePair cap = objectsToCopy.getFirst();
            final ObjectBag objOrg = cap.getAttribute().getReference();
            container = cap.getContainer();
            String destName = cap.getAttribute().getName();

            // Prepare name for the new object
            destName = destName.trim();

            if ( destName.isEmpty() ) {
                destName = this.createNewName();
            }

            // Create the new object
            ObjectBag objDest = new ObjectBag( destName, objOrg.getParentObject(), container );
            objDest.chkIdentifierForObjectName( destName );
            container.set( destName, objDest );

            if ( toret == null ) {
                toret = objDest;
            }

            // Copy all methods
            for (Method mth : objOrg.methods.values()) {
                objDest.set( mth.getName(), mth.copy() );
            }

            // Load all attributes
            for (Attribute atr: objOrg.getAttributes()) {
                final ObjectBag potentialNewObj = atr.getReference();

                if ( !( atr instanceof ParentAttribute ) ) {
                    if ( !objectsCopied.contains( potentialNewObj )
                      && !( potentialNewObj instanceof ValueObject ) )
                    {
                        objectsToCopy.add( new ContainerAttributePair( objDest, atr ) );
                        objectsCopied.add( potentialNewObj );
                    } else {
                        objDest.set( atr.getName(), potentialNewObj );
                    }
                }
            }

            // Get to the next one
            objectsToCopy.removeFirst();
        }

        objectsCopied.clear();
        objectsToCopy.clear();
        return toret;
    }

    /**
     * Creates an empty child of this object in the container's collection,
     * with a new (provided) name
     *
     * @param name The name of the object. Assigned automatically if empty.
     * @param container The object in which to include the new object
     * @return An ObjectBag reference to the new (copied) object
     * @throws core.exceps.InterpretError
     */
    public ObjectBag createChild(String name, ObjectBag container)
        throws InterpretError
    {
        // Prepare name for the new object
        name = name.trim();

        if ( name.isEmpty() ) {
            name = this.createNewName();
        }

        this.chkIdentifierForObjectName( name );

        // Create the new object
        ObjectBag toret = new ObjectBag( name, this, container );
        container.set( name, toret );

        return toret;
    }

    /**
     *  Removes all attributes.
     *  @param completely Stes whether or not eliminate all attributes.
     *  If set to false, the parent attribute is preserved.
     */
    public void clear(boolean completely)
    {
        ObjectBag parentObject = this.getParentObject();

        attributes.clear();

        if ( !completely ) {

            try {
                Attribute parentAttribute = new ParentAttribute( parentObject  );
                this.setAttribute( Reserved.ParentAttribute, parentAttribute );
            }
            catch (InterpretError ignored)
            {
            }
        }

        return;
    }

    /**
     * Removes an attribute from the attribute list
     * @param name The name of the attribute to remove
     * @throws core.exceps.InterpretError
     */
    public void remove(String name) throws InterpretError
    {
        if ( this.attributes.containsKey(name) ) {

            if ( name.equals( ParentAttribute.ParentAttributeName ) ) {
                throw new InterpretError( "It is not allowed to erase the parent attribute '"
                        + ParentAttribute.ParentAttributeName
                        + "'"  );
            }

            this.attributes.remove( name );
        }
        else
        if ( this.methods.containsKey(name) ) {
            this.methods.remove( name );
        }
        else {
            throw new AttributeNotFound( this.getName(), name );
        }

        return;
    }

    /**
     * @return the container
     */
    public ObjectBag getContainer()
    {
        return container;
    }

    /**
     * @param container the container to set
     */
    public void setContainer(ObjectBag container)
    {
        this.container = container;
    }

    /**
     *  Determines if an id is a correct identifier
     */
    public void chkIdentifier(String id) throws InterpretError
    {
        char ch;

        id = id.trim();
        if ( id.isEmpty() ) {
            throw new InterpretError( "an empty string is not a valid identifier" );
        }

        ch = id.charAt( 0 );

        if ( !Character.isLetter( ch )
          && ch != '_' )
        {
            throw new InterpretError( "'" + id + "' is not a valid id (digits at the beginning are not allowed)" );
        }

        if ( id.startsWith( "__" ) ) {
            throw new InterpretError( "'__' is not allowed for beginning a valid id" );
        }

        if ( !Lexer.isIdentifier( id ) ) {
            throw new InterpretError( "'" + id + "' is not a valid id" );
        }

        return;
    }

    /**
     * Determines if this is a good identifier name for an object name.
     * @param id The identifier to check
     * @throws core.exceps.InterpretError if it is reserved or already exists.
     */
    public void chkIdentifierForObjectName(String id) throws InterpretError
    {
        id = id.trim();

        this.chkIdentifier( id );

        if ( Reserved.isReservedForObjects( id ) ) {
            throw new InterpretError( "'" + id + "' is a reserved id" );
        }

        if ( this.localLookUpAttribute(id) != null ) {
            throw new InterpretError( "'" + id + "' already exists in " + getName() );
        }

        return;
    }

    /** @return a new, valid name */
    private String createNewName()
    {
        ++numUniqueId;
        return ( getName() + Integer.toString( numUniqueId ) );
    }

    private String name;
    private ObjectBag container;
    private Hashtable<String, Attribute> attributes;
    private Hashtable<String, Method> methods;
    private int numUniqueId;

}
