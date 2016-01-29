package core.evaluables.method.nativemethods;

import core.Evaluable;
import core.ObjectBag;
import core.Runtime;
import core.evaluables.method.NativeMethod;
import core.exceps.InterpretError;
import core.objs.ObjectDateTime;

import java.util.Calendar;

/**
 * A method that extracts the time and stores it in a DateTime object.
 * Created by Baltasar on 30/01/2016.
 */
public class NativeMethodOsNow extends NativeMethod {
    public static final String EtqMthOsNow = "now";

    public NativeMethodOsNow() {
        super( EtqMthOsNow );
    }

    @Override
    public ObjectBag doIt(ObjectBag ref, Evaluable[] params, StringBuilder msg)
            throws InterpretError
    {
        final Calendar now = Calendar.getInstance();
        final Runtime rt = Runtime.getRuntime();

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
}
