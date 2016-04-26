/*
 * AttributeNotFound.java
 *
 * Created on 4 de febrero de 2008, 13:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.devbaltasarq.pooi.core.exceps;

/**
 *
 * @author baltasarq
 */
public class AttributeNotFound extends InterpretError {
    
    /** Creates a new instance of AttributeNotFound */
    public AttributeNotFound(String obj, String atr) {
        super( "Attribute not found: '" + atr + "'" + " in '" + obj + "'" );
    }
    
}
