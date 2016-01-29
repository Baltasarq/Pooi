package core.objs;

import core.ObjectBag;
import core.evaluables.method.InterpretedMethod;
import core.exceps.InterpretError;

/**
 * Represents a time instant (date and time)
 * Created by Baltasar on 29/01/2016.
 */
public class ObjectDateTime extends ValueObject {
    public static final String EtqSecond = "second";
    public static final String EtqMinute = "minute";
    public static final String EtqHour = "hour";
    public static final String EtqYear = "year";
    public static final String EtqMonth = "month";
    public static final String EtqDay = "day";
    public static final String EtqDateAsStr = "dateAsStr";
    public static final String EtqTimeAsStr = "timeAsStr";
    public static final String EtqStr = "str";

    public ObjectDateTime(String n, ObjectBag parent, ObjectBag container) throws InterpretError {
        super( n, parent, container );

        this.set( EtqDateAsStr, new InterpretedMethod( EtqDateAsStr,
                "( ( ( ( self." + EtqDay + " str ) + \"-\" ) + ( self."
                + EtqMonth + " str ) ) + \"-\" ) + ( self."
                + EtqYear + " str )" ) );

        this.set( EtqTimeAsStr, new InterpretedMethod( EtqTimeAsStr,
                "( ( ( ( self." + EtqHour + " str ) + \":\" ) + ( self."
                        + EtqMinute + " str ) ) + \":\" ) + ( self."
                        + EtqSecond + " str )" ) );

        this.set( EtqStr, new InterpretedMethod( EtqStr,
                "( ( self dateAsStr ) + \" \" ) + ( self timeAsStr )" ) );
    }

    @Override
    public Object getValueObject() {
        return null;
    }
}
