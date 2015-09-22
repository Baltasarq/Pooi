/*
 * ParentAttribute.java
 *
 * Created on 1 de febrero de 2008, 19:21
 */

package core.evaluables;

import core.ObjectBag;
import core.Reserved;
import core.evaluables.Attribute;

/**
 * The parent attribute in the object
 * @author baltasarq
 */
public class ParentAttribute extends Attribute {
    public static final String ParentAttributeName = Reserved.ParentAttribute;

    /** Creates a new instance of AttributeSpecial */
    public ParentAttribute(ObjectBag o) {
        super( ParentAttributeName, o );
    }
    
}
