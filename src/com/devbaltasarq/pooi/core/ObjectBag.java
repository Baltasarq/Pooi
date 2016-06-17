package com.devbaltasarq.pooi.core;

import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.Interpreter.AttributeNotFound;
import com.devbaltasarq.pooi.core.evaluables.*;
import com.devbaltasarq.pooi.core.objs.ObjectParent;
import com.devbaltasarq.pooi.core.objs.ObjectRoot;
import com.devbaltasarq.pooi.core.objs.ValueObject;

import java.util.*;

/**
 * The base class for all objects in the runtime
 * @author baltasarq
 */
public class ObjectBag {

    /** This is needed because of the GUI.
     *  Methods can be native or interpreted, and the object representing it
     *  can be changed for another one at any time, while the user needs to be
     *  able to modify something stable.
     *  Its objective is to wrap a method in the list of available methods in this object.
     */
    public static class Slot {
        public Slot(Method mth) {
            this.mth = mth;
        }

        public Method getMethod() {
            return mth;
        }

        public void setMethod(Method mth) {
            this.mth = mth;
        }

        private Method mth;
    }

    /** Needed in order to make a deep copy */
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
     * @param rt            The runtime in which this object is operating.
     * @param n             The name of the object.
     * @param parent        The parent object in which to register it.
     * @param container     The object this is contained into.
     */
    public ObjectBag(Runtime rt, String n, ObjectBag parent, ObjectBag container)
            throws InterpretError
    {
        this( rt, n, parent, container, Check );
    }

    /**
     * Creates a new instance of ObjectBag
     * When an empty name is passed, an automatic name is created.
     *
     * @param rt            The runtime in which this object is operating.
     * @param n             The name of the object.
     * @param parent        The parent object in which to register it.
     * @param container     The object this is contained into.
     * @param chk           defines whether the objects' name should be checked or not.
     */
    public ObjectBag(Runtime rt, String n, ObjectBag parent, ObjectBag container, boolean chk)
            throws InterpretError
    {
        this.runtime = rt;
        this.attributes = new HashMap<>();
        this.methods = new HashMap<>();

        // Prepare name
        n = n.trim();
        if ( n.isEmpty() ) {
            n = this.createNewName();
        } else {
            if ( chk ) {
                this.chkIdentifierForMemberName( n );
            }
        }

        // Set attributes
        this.name = n;
        this.container = container;

        // Set the parent attribute
        if ( parent != null ) {
            // Use strictly the low-level method, since init will make recursion
            this.setAttribute( ParentAttribute.ParentAttributeName, parent );
        }
    }

    /** @return The Runtime this object is living in. */
    public Runtime getRuntime() {
        return this.runtime;
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
     * Returns the collection of methods
     * @return The collection of methods, as a native vector Method[]
     */
    public Method[] getLocalMethods()
    {
        final Slot[] slots = this.getSlots();
        final Method[] toret = new Method[ slots.length ];

        for (int i = 0; i < slots.length; ++i) {
            toret[ i ] = slots[ i ].getMethod();
        }

        return toret;
    }

    /**
     * Returns the collection of slots. Remember Slot's are permanent.
     * @return The collection of slots, as a native vector Slot[]
     */
    public Slot[] getSlots()
    {
        return this.methods.values().toArray( new Slot[ this.methods.size() ] );
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

        this.chkIdentifierForMemberName( id );

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
     * Returns a complete path, built with real objects.
     * The path includes the object itself, at the end.
     *
     * @return The object path, as a native vector ObjectBag[]
     */
   protected ObjectBag[] getObjectPath()
   {
       Vector<ObjectBag> toret = new Vector<>();
       final ObjectRoot root = this.getRuntime().getRoot();
       ObjectBag containerObj = this.getContainer();

       if ( this != root ) {
           toret.add( this );

           while ( containerObj != root ) {
               toret.insertElementAt( containerObj, 0 );
               containerObj = containerObj.getContainer();
           }
       }

       toret.insertElementAt( root, 0  );
       return toret.toArray( new ObjectBag[ toret.size() ] );
   }

    /**
     * The path to follow in order to get to this object from Root
     *
     * @return A String with a dot-separated object identifiers
     */
    public String getPath()
    {
        ObjectBag[] path = this.getObjectPath();
        StringBuilder toret = new StringBuilder();

        for(int i = 0; i < path.length; ++i) {
            final ObjectBag objPathPart = path[ i ];

            toret.append( objPathPart.getName() );

            if ( i < ( path.length - 1 ) ) {
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

       // Add the parent, at the end, the object Object does not have it
       if ( parentAtr != null ) {
           atrList.add( loaded, parentAtr );
       }
   }

    /**
     * Returns the name of the object or its value, if its an int, real,...
     * @return The name of the object or its value as a String.
     */
    public String getNameOrValueAsString() {
        String toret = this.getName();

        if ( this instanceof ValueObject ) {
            toret = this.toString();
        }

        return toret;
    }

    /**
     * Returns the number of attributes in this object
     * @return The number of attributes, as an int.
     */
    public int getNumberOfAttributes() {
        return this.attributes.size();
    }

    /**
     * Returns the number of methods in this object
     * @return the number of methods, as an int
     */
    public int getNumberOfMethods() {
        return this.methods.size();
    }

    /** @return the number of objects between this object and root */
    public int getInheritanceLevel() {
        int toret = 0;
        ObjectBag obj = this.getParentObject();

        while ( !( obj instanceof ObjectParent ) ) {
            ++toret;
            obj = obj.getParentObject();
        }

        return toret;
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
            final ObjectBag absParent = this.getRuntime().getAbsoluteParent();

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
        catch(StackOverflowError | OutOfMemoryError exc)
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
        ObjectBag[] path = this.getObjectPath();
        ArrayList<String> toret = new ArrayList<>( path.length );

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

        for (Slot slot : this.methods.values()) {
            toret.append( "\t" );
            toret.append( slot.getMethod().toString() );
            toret.append( "\n" );
        }

        for (Attribute atr : this.attributes.values()) {
            if ( atr instanceof ParentAttribute ) {
                continue;
            }

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
        final ObjectBag absParent = this.getRuntime().getAbsoluteParent();
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
     * @throws com.devbaltasarq.pooi.core.Interpreter.InterpretError
     */
    public void set(String name, ObjectBag obj) throws InterpretError
    {
        boolean isParent = name.equals( ParentAttribute.ParentAttributeName );

        this.setAttribute( name, obj  );

        // Avoid temporal names such as "lit22"
        if ( obj.getName().startsWith( Runtime.EtqLit ) ) {
            obj.setName( name );
        }

        // Change the container of the pointed object, if apropriated
        if ( !isParent
          && obj instanceof ValueObject )
        {
            obj.setContainer( this );
        }

        return;
    }

    /**
     * Adds an object to the list of attributes of this object,
     * being only accessible from this.
     * @param name The name of this new object in the attribute list
     * @param obj The ObjectBag reference to the object
     * @throws com.devbaltasarq.pooi.core.Interpreter.InterpretError
     */
    public void embed(String name, ObjectBag obj) throws InterpretError
    {
        set( name, obj );
        obj.setContainer( this );
    }

    /** Adds a new method to the list of methods of this object
      * @param name The name of this new method
      * @param mth The Method itself
      * @throws com.devbaltasarq.pooi.core.Interpreter.InterpretError if name is not valid
     */
    public void set(String name, Method mth) throws InterpretError
    {
        this.chkIdentifierForMemberName( name );
        this.setMethod( name, mth );
    }

    /**
     * Returns a member, local to this object, given a name.
     * @param id A String containing the name.
     * @return The member corresponding to that id, or null.
     */
    public Member localLookUpMember(String id) {
        Member toret = localLookUpAttribute( id );

        if ( toret == null ) {
            toret = localLookUpMethod( id );
        }

        return toret;
    }

    protected final void setMethod(String name, Method mth) throws InterpretError
    {
        final Slot slot = this.lookUpSlot( name );

        if ( slot == null ) {
            mth.setName( name );
            this.methods.put( name, new Slot( mth ) );
        } else {
            slot.setMethod( mth );
        }

        return;
    }

    protected final void setAttribute(String name, ObjectBag obj) throws InterpretError
    {
        boolean isParent = name.equals( ParentAttribute.ParentAttributeName );
        final Runtime rt = this.getRuntime();
        Attribute atr = localLookUpAttribute( name );

        // Chk cycles in parent graph
        if ( isParent ) {
            this.chkCyclesInParent( obj );
        }

        if ( atr == null ) {
            this.chkIdentifier( name );

            if ( isParent ) {
                atr = new ParentAttribute( obj );
            } else {
                atr = new Attribute( name, obj );
            }

            this.attributes.put( name, atr );
        } else {
            atr.setReference( obj );
        }

        return;
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
            final ObjectBag absParent = this.getRuntime().getAbsoluteParent();

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

    public void renameMember(String oldName, String newName) throws InterpretError
    {
        final Attribute attr = this.localLookUpAttribute( oldName );
        final Method mth = this.localLookUpMethod( oldName );

        if ( attr != null ) {
            this.renameAttribute( oldName, newName );
        }
        else
        if ( mth != null ) {
            this.renameMethod( oldName, newName );
        }
        else {
            throw new InterpretError( "member '" + oldName + "' was not found" );
        }

        return;
    }

    public void renameMethod(String oldName, String newName) throws InterpretError {
        final Slot slot = this.lookUpSlot( oldName );

        if ( slot != null ) {
            final Method mth = slot.getMethod();

            try {
                this.chkIdentifierForMemberName( newName );
                mth.setName( newName );
                this.methods.remove( oldName );
                this.methods.put( newName, slot );
            } catch(InterpretError exc) {
                if ( mth != null
                  && mth.getName().equals( newName ) )
                {
                    mth.setName( oldName );
                    this.methods.remove( newName );
                    this.methods.put( oldName, slot );
                }

                throw exc;
            }
        } else {
            throw new InterpretError( "method '" + oldName + "' was not found" );
        }

        return;
    }

    public void renameAttribute(String oldName, String newName) throws InterpretError {
        final Attribute attr = this.localLookUpAttribute( oldName );
        ObjectBag ref = null;

        if ( attr != null ) {
            try {
                ref = attr.getReference();
                if ( ref != null ) {
                    ref.setName( newName );
                    attr.setName( newName );
                    this.attributes.remove( oldName );
                    this.attributes.put( newName, attr );
                } else {
                    throw new InterpretError( "INTERNAL ERROR: reference points to void" );
                }
            } catch(InterpretError exc) {
                if ( ref != null
                  && ref.getName().equals( newName ) )
                {
                    ref.setName( oldName );
                }

                if ( attr != null
                  && attr.getName().equals( newName ) )
                {
                    attr.setName( oldName );
                    this.attributes.remove( newName );
                    this.attributes.put( oldName, attr );
                }

                throw exc;
            }
        } else {
            throw new InterpretError( "attribute " + oldName + " was not found" );
        }
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
            final ObjectBag absParent = this.getRuntime().getAbsoluteParent();

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
        final Slot slot = this.lookUpSlot( mthName );

        if ( slot != null ) {
            return slot.getMethod();
        } else {
            return null;
        }
    }

    /** Returns a given slot for a given method, if exists, or null if it does not.
     * @param mthName The name of the method.
     * @return A Slot object pointing to the method, null otherwise.
     */
    public Slot lookUpSlot(String mthName) {
        return this.methods.get( mthName );
    }

    /**
     * Copies this object in the container's collection,
     * with a new (provided) name
     *
     * @param name The name of the object. Assigned automatically if empty.
     * @param container The object in which to include the new object
     * @return An ObjectBag reference to the new (copied) object
     * @throws com.devbaltasarq.pooi.core.Interpreter.InterpretError  if the new name is not valid
     */
    public ObjectBag copy(String name, ObjectBag container) throws InterpretError
    {
        final Runtime rt = this.getRuntime();
        ObjectBag toret = null;
        final HashSet<ObjectBag> objectsCopied = new HashSet<>();
        final LinkedList<ContainerAttributePair> objectsToCopy = new LinkedList<>();

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
            ObjectBag objDest = new ObjectBag( this.getRuntime(), destName, objOrg.getParentObject(), container );
            objDest.chkIdentifierForMemberName( destName );
            container.set( destName, objDest );

            if ( toret == null ) {
                toret = objDest;
            }

            // Copy all methods
            for (Slot slot : objOrg.methods.values()) {
                final Method mth = slot.getMethod();

                objDest.set( mth.getName(), mth.copy() );
            }

            // Load all attributes
            for (Attribute atr: objOrg.getAttributes()) {
                final ObjectBag potentialNewObj = atr.getReference();

                if ( !( atr instanceof ParentAttribute ) ) {
                    if ( !objectsCopied.contains( potentialNewObj ) ) {
                        if ( potentialNewObj instanceof ValueObject ) {
                            objDest.setAttribute(
                                    atr.getName(),
                                    potentialNewObj.copy( atr.getName(), objDest )
                            );
                        } else {
                            objectsToCopy.add( new ContainerAttributePair( objDest, atr ) );
                            objectsCopied.add( potentialNewObj );
                        }
                    } else {
                        objDest.setAttribute( atr.getName(), potentialNewObj );
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
     * @throws com.devbaltasarq.pooi.core.Interpreter.InterpretError
     */
    public ObjectBag createChild(String name, ObjectBag container)
        throws InterpretError
    {
        // Prepare name for the new object
        name = name.trim();

        if ( name.isEmpty() ) {
            name = this.createNewName();
        }

        this.chkIdentifierForMemberName( name );

        // Create the new object
        ObjectBag toret = new ObjectBag( this.getRuntime(), name, this, container );
        container.set( name, toret );

        return toret;
    }

    /**
     *  Removes all attributes.
     *  @param completely States whether or not eliminate all attributes.
     *  If set to false, the parent attribute is preserved.
     */
    public void clear(boolean completely)
    {
        ObjectBag parentObject = this.getParentObject();

        this.attributes.clear();

        if ( !completely ) {
            try {
                this.setAttribute( Reserved.ParentAttribute, parentObject );
            }
            catch (InterpretError ignored)
            {
                // ignored
            }
        }

        return;
    }

    /**
     * Removes an attribute from the attribute list
     * @param name The name of the attribute to removeMember
     * @throws com.devbaltasarq.pooi.core.Interpreter.InterpretError
     */
    public void removeMember(String name) throws InterpretError
    {
        final Attribute attr = this.localLookUpAttribute( name  );
        final Method mth = this.localLookUpMethod( name  );

        if ( attr != null ) {
            if ( attr instanceof ParentAttribute ) {
                throw new InterpretError( "It is not allowed to erase the parent attribute '"
                        + ParentAttribute.ParentAttributeName
                        + "'"  );
            }

            final ObjectBag obj = attr.getReference();

            if ( this.getRuntime().isTypeObject( obj )
              || this.getRuntime().isSpecialObject( obj ) )
            {
                throw new InterpretError( "the removal of '" + obj.getName() + "' is not allowed" );
            }

            this.attributes.remove( name );
        }
        else
        if ( mth != null ) {
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
    public void setContainer(ObjectBag container) throws InterpretError
    {
        ObjectBag oldContainer = this.getContainer();

        if ( container != oldContainer ) {
            this.container = container;

            if ( oldContainer != null ) {
                final Attribute attr = oldContainer.localInverseLookUp( this );

                if ( attr != null ) {
                    oldContainer.removeMember( attr.getName() );
                }
            }
        }

        return;
    }

    /**
     *  Determines if an id is a correct identifier
     */
    public void chkIdentifier(String id) throws InterpretError
    {
        id = id.trim();

        if ( id.isEmpty() ) {
            throw new InterpretError( "an empty string is not a valid identifier" );
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
     * @throws com.devbaltasarq.pooi.core.Interpreter.InterpretError if it is reserved or already exists.
     */
    public void chkIdentifierForMemberName(String id) throws InterpretError
    {
        id = id.trim();

        this.chkIdentifier( id );

        if ( Reserved.isReservedForObjects( id ) ) {
            throw new InterpretError( "'" + id + "' is a reserved id" );
        }

        if ( this.localLookUpAttribute( id ) != null ) {
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
    private Runtime runtime;
    private ObjectBag container;
    private HashMap<String, Attribute> attributes;
    private HashMap<String, Slot> methods;
    private int numUniqueId;

}
