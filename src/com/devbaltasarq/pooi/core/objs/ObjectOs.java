package com.devbaltasarq.pooi.core.objs;

import com.devbaltasarq.pooi.core.evaluables.methods.nativemethods.NativeMethodOsExit;
import com.devbaltasarq.pooi.core.evaluables.methods.nativemethods.NativeMethodOsInput;
import com.devbaltasarq.pooi.core.evaluables.methods.nativemethods.NativeMethodOsNow;
import com.devbaltasarq.pooi.core.evaluables.methods.nativemethods.NativeMethodOsPrint;
import com.devbaltasarq.pooi.core.exceps.InterpretError;

/**
 * The OS (Operating System) object which will provide services from the underlying
 * system software layer.
 * Created by Baltasar on 29/01/2016.
 */
public class ObjectOs extends SysObject {
    public static final String Name = "os";

    /**
     * Creates the object representing the system
     * When an empty name is passed, an automatic name is created.
     *
     * @param rt        The runtime for this object.
     * @param absParent The parent object in which to register it.
     * @param container The object this is contained into.
     */
    public ObjectOs(com.devbaltasarq.pooi.core.Runtime rt, ObjectParent absParent, SysObject container)
            throws InterpretError
    {
        super( rt, Name, absParent, container  );

        this.set( NativeMethodOsNow.EtqMthOsNow, new NativeMethodOsNow( rt ) );
        this.set( NativeMethodOsPrint.EtqMthOsPrint, new NativeMethodOsPrint( rt ) );
        this.set( NativeMethodOsInput.EtqMthOsInput, new NativeMethodOsInput( rt ) );
        this.set( NativeMethodOsExit.EtqMthOsExit, new NativeMethodOsExit( rt ) );
    }
}
