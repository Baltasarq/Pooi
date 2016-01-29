// Runtime.java

package core;

import core.evaluables.Attribute;
import core.evaluables.Literal;
import core.evaluables.Reference;
import core.evaluables.literals.BoolLiteral;
import core.evaluables.literals.IntLiteral;
import core.evaluables.literals.RealLiteral;
import core.evaluables.literals.StrLiteral;
import core.evaluables.method.InterpretedMethod;
import core.evaluables.method.nativemethods.*;
import core.exceps.AttributeNotFound;
import core.exceps.InterpretError;
import core.objs.*;

import java.util.Calendar;

/**
 * The runtime, i.e. preexisting objects, root, etc.
 * User: baltasarq
 * Date: 11/16/12
 */
public final class Runtime {

    public static final String EtqNameRoot = Reserved.RootObject;
    public static final String EtqTopParentObject = Reserved.TopParentObject;
    public static final String EtqNameInt = Reserved.IntObject;
    public static final String EtqNameBool = Reserved.BoolObject;
    public static final String EtqNameReal = Reserved.RealObject;
    public static final String EtqNameStr = Reserved.StrObject;
    public static final String EtqNameDateTime = "DateTime";
    public static final String EtqNameAnObject = "anObject";
    public static final String EtqNameLiterals = "bin";
    public static final String EtqLit = "lit";

    private Runtime() throws InterpretError
    {
        // The inheritance root and the main container
        this.absParent = new ObjectParent( EtqTopParentObject );
        this.root = new ObjectRoot( EtqNameRoot, absParent );
        this.root.set( EtqTopParentObject, this.absParent );

        // Main "type" objects
        this.str = new SysObject( EtqNameStr, absParent, root );
        this.root.set( EtqNameStr, this.str );

        this.integer = new SysObject( EtqNameInt, absParent, root );
        this.root.set( EtqNameInt, this.integer );

        this.real = new SysObject( EtqNameReal, absParent, root );
        this.root.set( EtqNameReal, this.real );

        this.bool = new SysObject( EtqNameBool, absParent, root );
        this.root.set( EtqNameBool, this.bool );

        this.dateTime = new SysObject( EtqNameDateTime, absParent, root );
        this.root.set( EtqNameDateTime, this.dateTime );

        // The first prototype
        this.anObject = new ObjectBag( EtqNameAnObject, absParent, root );
        this.root.set( EtqNameAnObject, this.anObject );

        // An object to hold literals
        this.literals = new SysObject( EtqNameLiterals, absParent, root );
        this.root.set( EtqNameLiterals, this.literals );

        // The operating system rep
        this.os = new ObjectOs( absParent, root );
        this.root.set( ObjectOs.Name, this.os );

        this.addMethodsToRuntimeObjects();
        this.getLiteralsContainer().clear( false );
    }

    public void addMethodsToRuntimeObjects() throws InterpretError
    {
        // Int
        this.integer.set( NativeMethodIntSum.EtqMthIntSum, new NativeMethodIntSum() );
        this.integer.set( NativeMethodIntSubstract.EtqMthIntSub, new NativeMethodIntSubstract() );
        this.integer.set( NativeMethodIntMultiplyBy.EtqMthIntMul, new NativeMethodIntMultiplyBy() );
        this.integer.set( NativeMethodIntDivideBy.EtqMthIntDiv, new NativeMethodIntDivideBy() );
        this.integer.set( NativeMethodIntIsNegative.EtqMthIntIsNegative, new NativeMethodIntIsNegative() );
        this.integer.set( NativeMethodIntAbs.EtqMthIntAbs, new NativeMethodIntAbs() );
        this.integer.set( NativeMethodIntIsEqualTo.EtqMthIntIsEqualTo, new NativeMethodIntIsEqualTo() );
        this.integer.set( NativeMethodIntIsGreaterThan.EtqMthIntIsGreaterThan, new NativeMethodIntIsGreaterThan() );
        this.integer.set( NativeMethodIntIsLessThan.EtqMthIntIsLessThan, new NativeMethodIntIsLessThan() );

        // Real
        this.real.set( NativeMethodRealSum.EtqMthRealSum, new NativeMethodRealSum() );
        this.real.set( NativeMethodRealSubstract.EtqMthRealSub, new NativeMethodRealSubstract() );
        this.real.set( NativeMethodRealMultiplyBy.EtqMthRealMul, new NativeMethodRealMultiplyBy() );
        this.real.set( NativeMethodRealDivideBy.EtqMthRealDiv, new NativeMethodRealDivideBy() );
        this.real.set( NativeMethodRealIsNegative.EtqMthRealIsNegative, new NativeMethodRealIsNegative() );
        this.real.set( NativeMethodRealAbs.EtqMthRealAbs, new NativeMethodRealAbs() );
        this.real.set( NativeMethodRealIsEqualTo.EtqMthRealIsEqualTo, new NativeMethodRealIsEqualTo() );
        this.real.set( NativeMethodRealIsGreaterThan.EtqMthRealIsGreaterThan, new NativeMethodRealIsGreaterThan() );
        this.real.set( NativeMethodRealIsLessThan.EtqMthRealIsLessThan, new NativeMethodRealIsLessThan() );

        // Str
        this.str.set( NativeMethodStrConcat.EtqMthStrConcat, new NativeMethodStrConcat() );
        this.str.set( NativeMethodStrMays.EtqMthStrMays, new NativeMethodStrMays() );
        this.str.set( NativeMethodStrMins.EtqMthStrMins, new NativeMethodStrMins() );
        this.str.set( NativeMethodStrTrim.EtqMthStrTrim, new NativeMethodStrTrim() );
        this.str.set( NativeMethodStrToInt.EtqMthStrToInt, new NativeMethodStrToInt() );
        this.str.set( NativeMethodStrToReal.EtqMthStrToReal, new NativeMethodStrToReal() );
        this.str.set( NativeMethodStrIsNumber.EtqMthStrIsNumber, new NativeMethodStrIsNumber() );
        this.str.set( NativeMethodStrIsEqualTo.EtqMthStrIsEqualTo, new NativeMethodStrIsEqualTo() );
        this.str.set( NativeMethodStrIsLessThan.EtqMthStrIsLessThan, new NativeMethodStrIsLessThan() );
        this.str.set( NativeMethodStrIsGreaterThan.EtqMthStrIsGreaterThan, new NativeMethodStrIsGreaterThan() );
        this.str.set( NativeMethodStrIsEmpty.EtqMthStrIsEmpty, new NativeMethodStrIsEmpty() );
        this.str.set( NativeMethodStrCharAt.EtqMthStrCharAt, new NativeMethodStrCharAt() );
        this.str.set( NativeMethodStrLength.EtqMthStrLength, new NativeMethodStrLength() );
        this.str.set( NativeMethodStrSub.EtqMthStrSub, new NativeMethodStrSub() );
        this.str.set( NativeMethodStrLeft.EtqMthStrLeft, new NativeMethodStrLeft() );
        this.str.set( NativeMethodStrRight.EtqMthStrRight, new NativeMethodStrRight() );
    }


    public static Runtime getRuntime() throws InterpretError
    {
        if ( rt == null ) {
            createRuntime();
        }

        return rt;
    }

    public static Runtime createRuntime() throws InterpretError
    {
        rt = new Runtime();
        return rt;
    }

    public ObjectStr createString(String value) throws InterpretError
    {
        return this.createString( this.createNewLiteralName(), value );
    }

    public ObjectStr createString(String id, String value) throws InterpretError
    {
        ObjectStr toret = new ObjectStr(
                id, this.str, this.getLiteralsContainer()
        );

        this.getLiteralsContainer().set( toret.getName(), toret );
        toret.assign( value );
        return toret;
    }

    public ObjectDateTime createDateTime(long year, long month, long day, long hour, long minute, long second)
            throws InterpretError
    {
        final ObjectDateTime toret = new ObjectDateTime(
                this.createNewLiteralName(),
                this.dateTime,
                this.getLiteralsContainer() );

        // Date
        toret.set( ObjectDateTime.EtqDay, rt.createInt( day ) );
        toret.set( ObjectDateTime.EtqMonth, rt.createInt( month ) );
        toret.set( ObjectDateTime.EtqYear, rt.createInt( year ) );

        // Time
        toret.set( ObjectDateTime.EtqSecond, rt.createInt( second ) );
        toret.set( ObjectDateTime.EtqMinute, rt.createInt( minute ) );
        toret.set( ObjectDateTime.EtqHour, rt.createInt( hour ) );

        this.getLiteralsContainer().set( toret.getName(), toret );
        return toret;
    }

    public ObjectInt createInt(long num) throws InterpretError
    {
        return this.createInt( this.createNewLiteralName(), num );
    }

    public ObjectInt createInt(String id, long num) throws InterpretError
    {
        ObjectInt toret = new ObjectInt(
                id, this.integer, this.getLiteralsContainer()
        );

        this.getLiteralsContainer().set( toret.getName(), toret );
        toret.assign( num );
        return toret;
    }

    public ObjectReal createReal(double num)
        throws InterpretError
    {
        return this.createReal( this.createNewLiteralName(), num );
    }

    public ObjectReal createReal(String id, double num) throws InterpretError
    {
        ObjectReal toret = new ObjectReal(
                id, this.real, this.getLiteralsContainer()
        );

        this.getLiteralsContainer().set( toret.getName(), toret );
        toret.assign( num );
        return toret;
    }

    public ObjectBool createBool(boolean value)
            throws InterpretError
    {
        return this.createBool( this.createNewLiteralName(), value );
    }

    public ObjectBool createBool(String id, boolean value) throws InterpretError
    {
        ObjectBool toret = new ObjectBool(
                id, this.bool, this.getLiteralsContainer()
        );

        this.getLiteralsContainer().set( toret.getName(), toret );
        toret.assign( value );
        return toret;
    }

    public ObjectBag createObject(String name)
            throws InterpretError
    {
        final ObjectBag container = this.anObject.getContainer();
        final ObjectBag toret = new ObjectBag( name, this.anObject.getParentObject(), container );
        container.set( name, toret );
        return toret;
    }

    public ObjectBag solveToObject(Evaluable evaluable)
            throws InterpretError
    {
        ObjectBag toret = null;

        if ( evaluable instanceof Literal ) {
            toret = this.createLiteral( this.createNewLiteralName(), (Literal) evaluable );
        }
        else
        if ( evaluable instanceof Reference) {
            toret = this.findObjectByPath( (Reference) evaluable );
        }

        return toret;
    }

    public boolean isTypeObject(ObjectBag obj)
    {

        return ( obj == this.absParent
              || obj == this.str
              || obj == this.integer
              || obj == this.real
              || obj == this.anObject
              || obj == this.bool
        );
    }

    public boolean isSpecialObject(ObjectBag obj)
    {
        return ( obj == this.getAbsoluteParent()
              || obj == this.getRoot()
              || obj == this.getLiteralsContainer()
        );
    }

    public ObjectRoot getRoot()
    {
        return this.root;
    }

    public ObjectParent getAbsoluteParent()
    {
        return this.absParent;
    }

    public ObjectBag getLiteralsContainer()
    {
        return this.literals;
    }

    public ObjectBag getAnObject()
    {
        return this.anObject;
    }

    public ObjectBag createLiteral(Literal value)
            throws InterpretError
    {
        return this.createLiteral( this.createNewLiteralName(), value );
    }

    public ObjectBag createLiteral(String id, Literal value)
            throws InterpretError
    {
        ObjectBag toret = null;

        if ( value instanceof IntLiteral) {
            toret = this.createInt( id, ( (IntLiteral) value ).getValue() );
        }
        else
        if ( value instanceof RealLiteral) {
            toret = this.createReal( id, ( (RealLiteral) value ).getValue() );
        }
        else
        if ( value instanceof BoolLiteral) {
            toret = this.createBool( id, ( (BoolLiteral) value ).getValue() );
        }
        else
        if ( value instanceof StrLiteral) {
            toret = this.createString( id, ( (StrLiteral) value ).getValue() );
        }

        return toret;
    }

    public ObjectBag findObjectByPath(Reference ref)
            throws AttributeNotFound
    {
        return this.findObjectByPathInObject( this.getRoot(), ref );
    }

    public ObjectBag findObjectByPathInObject(ObjectBag self, Reference ref)
            throws AttributeNotFound
    {
        int numArg = 0;
        Attribute atr;
        ObjectBag toret = self;

        // Prepare starting attribute
        if ( self == this.getRoot() ) {
            if ( ref.getAttrs()[ 0 ].equals( Reserved.RootObject ) )
            {
                ++numArg;
            }
        } else {
            if ( ref.getAttrs()[ 0 ].equals( Reserved.SelfRef ) ) {
                ++numArg;
            }
            else
            if ( ref.getAttrs()[ 0 ].equals( Reserved.RootObject ) )
            {
                toret = self = this.getRoot();
                ++numArg;
            }
        }

        // Look for destination object
        while( numArg < ref.getAttrs().length ) {
            String atrPart = ref.getAttrs()[ numArg ].trim();
            atr = toret.lookUpAttribute( atrPart );

            if ( atr == null ) {
                throw new AttributeNotFound( toret.getName(), atrPart );
            }

            toret = atr.getReference();
            ++numArg;
        }

        return toret;
    }

    private String createNewLiteralName()
    {
        ++numLiterals;
        return ( EtqLit + Integer.toString( numLiterals ) );
    }

    private final ObjectRoot root;
    private final ObjectParent absParent;
    private final ObjectBag str;
    private final ObjectBag integer;
    private final ObjectBag real;
    private final ObjectBag bool;
    private final ObjectBag dateTime;
    private final ObjectBag anObject;
    private final ObjectBag literals;
    private final ObjectOs os;

    private static Runtime rt;
    private static int numLiterals = 0;
}
