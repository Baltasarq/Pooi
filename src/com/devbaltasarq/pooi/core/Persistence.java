// Pooi (c) 2014 Baltasar MIT License <jbgarcia@uvigo.es>

package com.devbaltasarq.pooi.core;

import java.io.File;

/**
 * Generic class for different persistence methods.
 * @author baltasarq
 */
public abstract class Persistence {
    public static final String ObjectNameMember = "__name__";

    public Persistence(ObjectBag obj)
    {
        this.objs = new ObjectBag[]{ obj };
    }

    public Persistence(ObjectBag[] objs)
    {
        this.objs = objs;
    }
    
    public ObjectBag getObj()
    {
        return this.objs[ 0 ];
    }

    public ObjectBag[] getObjs()
    {
        return this.objs;
    }

    public abstract ObjectBag[] load(File path);
    public abstract boolean save(File path);
    
    protected ObjectBag[] objs = null;
}
