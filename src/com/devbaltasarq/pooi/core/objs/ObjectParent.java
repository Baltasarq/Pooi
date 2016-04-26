/*
 * ObjectParent.java
 *
 * Created on 1 de febrero de 2008, 13:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.devbaltasarq.pooi.core.objs;

import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.evaluables.method.nativemethods.NativeMethodStr;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Reserved;
import com.devbaltasarq.pooi.core.evaluables.method.nativemethods.*;

/**
 * This is "Object" the parent for all objects
 * @author baltasarq
 */
public class ObjectParent extends ObjectBag {

    /**
     * Creates a new instance of ObjectParent
     * @param n         The name of the object
     */
    public ObjectParent(String n) throws InterpretError
    {
        super( n, null, null, ObjectBag.DontCheck );
        this.registerCommonMethods();
    }

    @Override
    public ObjectParent copy(String n, ObjectBag container) throws InterpretError
    {
        throw new InterpretError( "cannot be copied" );
    }

    @Override
    public void set(String atr, ObjectBag newValue) throws InterpretError
    {
        atr = atr.trim();

        if ( Reserved.ParentAttribute.equals( atr  ) ) {
            throw new InterpretError( "'parent' cannot be modified in Object" );
        }
        else {
            super.set( atr, newValue );
        }

        return;
    }

    public void registerCommonMethods()
    {
        try {
            this.set( NativeMethodStr.EtqMthToString, new NativeMethodStr() );
            this.set( NativeMethodList.EtqMthList, new NativeMethodList() );
            this.set( NativeMethodGetPath.EtqMthPath, new NativeMethodGetPath() );
            this.set( NativeMethodErase.EtqMthErase, new NativeMethodErase() );
            this.set( NativeMethodGetName.EtqMthGetName, new NativeMethodGetName() );
            this.set( NativeMethodSet.EtqMthSet, new NativeMethodSet() );
            this.set( NativeMethodCopy.EtqMthCopy, new NativeMethodCopy() );
            this.set( NativeMethodCreateChild.EtqMthCreateChild, new NativeMethodCreateChild() );
            this.set( NativeMethodSetName.EtqMthSetName, new NativeMethodSetName() );
            this.set( NativeMethodSetName.EtqMthRename, new NativeMethodSetName() );
            this.set( NativeMethodIs.EtqMthIs, new NativeMethodIs() );
        } catch(InterpretError ignored)
        {
        }
    }
}