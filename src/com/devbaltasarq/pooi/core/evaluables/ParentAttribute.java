/*
 * ParentAttribute.java
 *
 * Created on 1 de febrero de 2008, 19:21
 */

package com.devbaltasarq.pooi.core.evaluables;

import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Reserved;

/**
 * The parent attribute in the object
 * @author baltasarq
 */
public class ParentAttribute extends Attribute {
    public static final String ParentAttributeName = Reserved.ParentAttribute;

    /** Creates a new instance of ParentAttribute */
    public ParentAttribute(ObjectBag ref) {
        super( ParentAttributeName, ref );
    }
}
