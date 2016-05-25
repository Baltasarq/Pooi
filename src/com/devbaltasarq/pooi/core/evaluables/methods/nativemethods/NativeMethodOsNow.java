package com.devbaltasarq.pooi.core.evaluables.methods.nativemethods;

import com.devbaltasarq.pooi.core.Evaluable;
import com.devbaltasarq.pooi.core.ObjectBag;
import com.devbaltasarq.pooi.core.Runtime;
import com.devbaltasarq.pooi.core.evaluables.methods.NativeMethod;
import com.devbaltasarq.pooi.core.exceps.InterpretError;
import com.devbaltasarq.pooi.core.objs.ObjectDateTime;

import java.util.Calendar;

/**
 * A method that extracts the time and stores it in a DateTime object.
 * Created by Baltasar on 30/01/2016.
 */
public class NativeMethodOsNow extends NativeMethod {
    public static final String EtqMthOsNow = "now";

    public NativeMethodOsNow(Runtime rt) {
        super( rt, EtqMthOsNow );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Calendar now = Calendar.getInstance();
        final Runtime rt = this.getRuntime();

        chkParametersNumber( 0, params );

        // Create date-time object
        final ObjectDateTime toret = rt.createDateTime(
                now.get( Calendar.DAY_OF_MONTH ),
                now.get( Calendar.MONTH ) + 1,
                now.get( Calendar.YEAR ),
                now.get( Calendar.HOUR ),
                now.get( Calendar.MINUTE ),
                now.get( Calendar.SECOND )
        );

        msg.append( "Obtained current date and time" );
        return toret;
    }

    public int getNumParams() {
        return 0;
    }

    @Override
    public String[] getFormalParameters() {
        return new String[ 0 ];
    }
}
