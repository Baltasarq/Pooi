// Runtime.java

package com.devbaltasarq.pooi.core;

import com.devbaltasarq.pooi.core.Interpreter.InterpretError;
import com.devbaltasarq.pooi.core.evaluables.Attribute;
import com.devbaltasarq.pooi.core.evaluables.Literal;
import com.devbaltasarq.pooi.core.evaluables.Reference;
import com.devbaltasarq.pooi.core.evaluables.literals.BoolLiteral;
import com.devbaltasarq.pooi.core.evaluables.literals.IntLiteral;
import com.devbaltasarq.pooi.core.evaluables.literals.RealLiteral;
import com.devbaltasarq.pooi.core.evaluables.literals.StrLiteral;
import com.devbaltasarq.pooi.core.evaluables.methods.InterpretedMethod;
import com.devbaltasarq.pooi.core.evaluables.methods.nativemethods.*;
import com.devbaltasarq.pooi.core.objs.*;

/**
 * The runtime, i.e. preexisting objects, root, etc.
 * User: baltasarq
 * Date: 11/16/12
 */
public final class Runtime {

    public static final String EtqNameRoot = Reserved.RootObject;
    public static final String EtqNameLib = Reserved.LibObject;
    public static final String EtqTopParentObject = Reserved.TopParentObject;
    public static final String EtqNameInt = Reserved.IntObject;
    public static final String EtqNameBool = Reserved.BoolObject;
    public static final String EtqNameReal = Reserved.RealObject;
    public static final String EtqNameNothing = Reserved.NothingObject;
    public static final String EtqNameStr = Reserved.StrObject;
    public static final String EtqNameDateTime = "DateTime";
    public static final String EtqNameAnObject = "anObject";
    public static final String EtqNameLiterals = "bin";
    public static final String EtqLit = "lit";

    private Runtime() throws InterpretError
    {
        // The inheritance root and the main container. Chicken&egg problem.
        this.absParent = new ObjectParent( this, EtqTopParentObject );
        this.root = new ObjectRoot( this, EtqNameRoot, absParent );
        this.absParent.setContainer( this.root );
        this.root.set( EtqTopParentObject, this.absParent );

        // Nothing
        this.nothing = new SysObject( this, EtqNameNothing, absParent, root );
        this.root.set( EtqNameNothing, this.nothing );

        // Main "type" objects
        this.str = new SysObject( this, EtqNameStr, absParent, root );
        this.root.set( EtqNameStr, this.str );

        this.integer = new SysObject( this, EtqNameInt, absParent, root );
        this.root.set( EtqNameInt, this.integer );

        this.real = new SysObject( this, EtqNameReal, absParent, root );
        this.root.set( EtqNameReal, this.real );

        this.bool = new SysObject( this, EtqNameBool, absParent, root );
        this.root.set( EtqNameBool, this.bool );

        this.dateTime = new SysObject( this, EtqNameDateTime, absParent, root );
        this.root.set( EtqNameDateTime, this.dateTime );

        this.lib = new SysObject( this, EtqNameLib, absParent, root );
        this.root.set( EtqNameLib, this.lib );

        // The first prototype
        this.anObject = new SysObject( this, EtqNameAnObject, absParent, root );
        this.root.set( EtqNameAnObject, this.anObject );

        // An object to hold literals
        this.literals = new SysObject( this, EtqNameLiterals, absParent, root );
        this.root.set( EtqNameLiterals, this.literals );

        // The operating system rep
        this.os = new ObjectOs( this, absParent, root );
        this.root.set( ObjectOs.Name, this.os );

        this.addMethodsToRuntimeObjects();
        this.getLiteralsContainer().clear( false );
    }

    public void addMethodsToRuntimeObjects() throws InterpretError
    {
        // Int
        this.integer.set( NativeMethodIntSum.EtqMthIntSum, new NativeMethodIntSum( this ) );
        this.integer.set( NativeMethodIntSumAssign.EtqMthIntSumAssign, new NativeMethodIntSumAssign( this ) );
        this.integer.set( NativeMethodIntSubstract.EtqMthIntSub, new NativeMethodIntSubstract( this ) );
        this.integer.set( NativeMethodIntSubstractAssign.EtqMthIntSubAssign, new NativeMethodIntSubstractAssign( this ) );
        this.integer.set( NativeMethodIntMultiplyBy.EtqMthIntMul, new NativeMethodIntMultiplyBy( this ) );
        this.integer.set( NativeMethodIntMultiplyByAssign.EtqMthIntMulAssign, new NativeMethodIntMultiplyByAssign( this ) );
        this.integer.set( NativeMethodIntDivideBy.EtqMthIntDiv, new NativeMethodIntDivideBy( this ) );
        this.integer.set( NativeMethodIntDivideByAssign.EtqMthIntDivAssign, new NativeMethodIntDivideByAssign( this ) );
        this.integer.set( NativeMethodIntIsNegative.EtqMthIntIsNegative, new NativeMethodIntIsNegative( this ) );
        this.integer.set( NativeMethodIntAbs.EtqMthIntAbs, new NativeMethodIntAbs( this ) );
        this.integer.set( NativeMethodIntIsEqualTo.EtqMthIntIsEqualTo, new NativeMethodIntIsEqualTo( this ) );
        this.integer.set( NativeMethodIntIsGreaterThan.EtqMthIntIsGreaterThan, new NativeMethodIntIsGreaterThan( this) );
        this.integer.set( NativeMethodIntIsLessThan.EtqMthIntIsLessThan, new NativeMethodIntIsLessThan( this ) );

        // Real
        this.real.set( NativeMethodRealSum.EtqMthRealSum, new NativeMethodRealSum( this ) );
        this.real.set( NativeMethodRealSumAssign.EtqMthRealSumAssign, new NativeMethodRealSumAssign( this ) );
        this.real.set( NativeMethodRealSubstract.EtqMthRealSub, new NativeMethodRealSubstract( this ) );
        this.real.set( NativeMethodRealSubstractAssign.EtqMthRealSubAssign, new NativeMethodRealSubstractAssign( this ) );
        this.real.set( NativeMethodRealMultiplyBy.EtqMthRealMul, new NativeMethodRealMultiplyBy( this ) );
        this.real.set( NativeMethodRealMultiplyByAssign.EtqMthRealMulAssign, new NativeMethodRealMultiplyByAssign( this ) );
        this.real.set( NativeMethodRealDivideBy.EtqMthRealDiv, new NativeMethodRealDivideBy( this ) );
        this.real.set( NativeMethodRealDivideByAssign.EtqMthRealDivAssign, new NativeMethodRealDivideByAssign( this ) );
        this.real.set( NativeMethodRealIsNegative.EtqMthRealIsNegative, new NativeMethodRealIsNegative( this ) );
        this.real.set( NativeMethodRealAbs.EtqMthRealAbs, new NativeMethodRealAbs( this ) );
        this.real.set( NativeMethodRealIsEqualTo.EtqMthRealIsEqualTo, new NativeMethodRealIsEqualTo( this ) );
        this.real.set( NativeMethodRealIsGreaterThan.EtqMthRealIsGreaterThan, new NativeMethodRealIsGreaterThan( this ) );
        this.real.set( NativeMethodRealIsLessThan.EtqMthRealIsLessThan, new NativeMethodRealIsLessThan( this ) );

        // Str
        this.str.set( NativeMethodStrConcat.EtqMthStrConcat, new NativeMethodStrConcat( this ) );
        this.str.set( NativeMethodStrConcatAssign.EtqMthStrConcatAssign, new NativeMethodStrConcatAssign( this ) );
        this.str.set( NativeMethodStrMays.EtqMthStrMays, new NativeMethodStrMays( this ) );
        this.str.set( NativeMethodStrMins.EtqMthStrMins, new NativeMethodStrMins( this ) );
        this.str.set( NativeMethodStrTrim.EtqMthStrTrim, new NativeMethodStrTrim( this ) );
        this.str.set( NativeMethodStrToInt.EtqMthStrToInt, new NativeMethodStrToInt( this ) );
        this.str.set( NativeMethodStrToReal.EtqMthStrToReal, new NativeMethodStrToReal( this ) );
        this.str.set( NativeMethodStrIsNumber.EtqMthStrIsNumber, new NativeMethodStrIsNumber( this ) );
        this.str.set( NativeMethodStrIsEqualTo.EtqMthStrIsEqualTo, new NativeMethodStrIsEqualTo( this ) );
        this.str.set( NativeMethodStrIsLessThan.EtqMthStrIsLessThan, new NativeMethodStrIsLessThan( this ) );
        this.str.set( NativeMethodStrIsGreaterThan.EtqMthStrIsGreaterThan, new NativeMethodStrIsGreaterThan( this ) );
        this.str.set( NativeMethodStrIsEmpty.EtqMthStrIsEmpty, new NativeMethodStrIsEmpty( this ) );
        this.str.set( NativeMethodStrCharAt.EtqMthStrCharAt, new NativeMethodStrCharAt( this ) );
        this.str.set( NativeMethodStrLength.EtqMthStrLength, new NativeMethodStrLength( this ) );
        this.str.set( NativeMethodStrSub.EtqMthStrSub, new NativeMethodStrSub( this ) );
        this.str.set( NativeMethodStrLeft.EtqMthStrLeft, new NativeMethodStrLeft( this ) );
        this.str.set( NativeMethodStrRight.EtqMthStrRight, new NativeMethodStrRight( this ) );

        // DateTime
        this.dateTime.set( ObjectDateTime.EtqDateAsStr, new InterpretedMethod( this, ObjectDateTime.EtqDateAsStr,
                "( ( ( ( self." + ObjectDateTime.EtqDay + " str ) + \"-\" ) + ( self."
                        + ObjectDateTime.EtqMonth + " str ) ) + \"-\" ) + ( self."
                        + ObjectDateTime.EtqYear + " str )" ) );

        this.dateTime.set( ObjectDateTime.EtqTimeAsStr, new InterpretedMethod( this, ObjectDateTime.EtqTimeAsStr,
                "( ( ( ( self." + ObjectDateTime.EtqHour + " str ) + \":\" ) + ( self."
                        + ObjectDateTime.EtqMinute + " str ) ) + \":\" ) + ( self."
                        + ObjectDateTime.EtqSecond + " str )" ) );

        this.dateTime.set( ObjectDateTime.EtqStr, new InterpretedMethod( this, ObjectDateTime.EtqStr,
                "( ( self dateAsStr ) + \" \" ) + ( self timeAsStr )" ) );
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
                this, id, this.str, this.getLiteralsContainer()
        );

        this.getLiteralsContainer().set( toret.getName(), toret );
        toret.assign( value );
        return toret;
    }

    public ObjectDateTime createDateTime(long year, long month, long day, long hour, long minute, long second)
            throws InterpretError
    {
        final ObjectDateTime toret = new ObjectDateTime(
                this,
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
                this, id, this.integer, this.getLiteralsContainer()
        );

        this.getLiteralsContainer().set( toret.getName(), toret );
        toret.assign( num );
        return toret;
    }

    public ObjectReal createReal(double num) throws InterpretError
    {
        return this.createReal( this.createNewLiteralName(), num );
    }

    public ObjectReal createReal(String id, double num) throws InterpretError
    {
        ObjectReal toret = new ObjectReal(
                this, id, this.real, this.getLiteralsContainer()
        );

        this.getLiteralsContainer().set( toret.getName(), toret );
        toret.assign( num );
        return toret;
    }

    public ObjectBool createBool(boolean value) throws InterpretError
    {
        return this.createBool( this.createNewLiteralName(), value );
    }

    public ObjectBool createBool(String id, boolean value) throws InterpretError
    {
        ObjectBool toret = new ObjectBool(
                this, id, this.bool, this.getLiteralsContainer()
        );

        this.getLiteralsContainer().set( toret.getName(), toret );
        toret.assign( value );
        return toret;
    }

    public ObjectBag createObject(String name) throws InterpretError
    {
        final ObjectBag container = this.anObject.getContainer();
        final ObjectBag toret = new ObjectBag( this, name, this.anObject.getParentObject(), container );
        container.set( name, toret );
        return toret;
    }

    public ObjectBag solveToObject(Evaluable evaluable) throws InterpretError
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
              || obj == this.dateTime
        );
    }

    public boolean isSpecialObject(ObjectBag obj)
    {
        return ( obj == this.getAbsoluteParent()
              || obj == this.getRoot()
              || obj == this.getLiteralsContainer()
              || obj == this.getOs()
              || obj == this.getNothingObject()
        );
    }

    public ObjectRoot getRoot()
    {
        return this.root;
    }

    public SysObject getNothingObject()
    {
        return this.nothing;
    }

    public ObjectOs getOs() {
        return this.os;
    }

    public ObjectParent getAbsoluteParent()
    {
        return this.absParent;
    }

    public SysObject getLiteralsContainer()
    {
        return this.literals;
    }

    public SysObject getAnObject()
    {
        return this.anObject;
    }

    public ObjectBag createLiteral(Literal value) throws InterpretError
    {
        return this.createLiteral( this.createNewLiteralName(), value );
    }

    public ObjectBag createLiteral(String id, Literal value) throws InterpretError
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
            throws Interpreter.AttributeNotFound
    {
        return this.findObjectByPathInObject( this.getRoot(), ref );
    }

    public ObjectBag findObjectByPathInObject(ObjectBag self, Reference ref)
            throws Interpreter.AttributeNotFound
    {
        final String firstPart = ref.getAttrs()[ 0 ];
        int numArg = 0;
        Attribute atr;
        ObjectBag toret = self;

        // Prepare starting attribute
        if ( self == this.getRoot() ) {
            if ( firstPart.equals( Reserved.RootObject ) )
            {
                ++numArg;
            }
        } else {
            if ( firstPart.equals( Reserved.RootObject ) )
            {
                toret = this.getRoot();
            }

            ++numArg;
        }

        // Look for destination object
        while( numArg < ref.getAttrs().length ) {
            String atrPart = ref.getAttrs()[ numArg ].trim();
            atr = toret.lookUpAttribute( atrPart );

            if ( atr == null ) {
                throw new Interpreter.AttributeNotFound( toret.getName(), atrPart );
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
    private final SysObject nothing;
    private final SysObject str;
    private final SysObject integer;
    private final SysObject real;
    private final SysObject bool;
    private final SysObject dateTime;
    private final SysObject anObject;
    private final SysObject literals;
    private final ObjectOs  os;
    private final SysObject lib;

    private static Runtime rt;
    private static int numLiterals = 0;
}
