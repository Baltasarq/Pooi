package core.evaluables;

import core.Evaluable;

import java.util.ArrayList;

/**
 * Represents a reference, i.e., a sequence of attribute id's.
 * User: baltasarq
 * Date: 11/19/12
 */
public class Reference extends Evaluable {

    public Reference(String id)
    {
        this();
        this.setAttrs( new String[]{ id } );
    }

    public Reference(String[] ids)
    {
        this();
        this.setAttrs( ids );
    }

    public Reference()
    {
        this.attrs = new String[ 0 ];
    }

    /**
     * @return the sequence of attribute names
     */
    public String[] getAttrs() {
        return attrs;
    }

    /**
     * Is this a valid reference?
     * @return
     */
    public boolean isValid()
    {
        return ( this.attrs.length > 0 );
    }

    /**
     * @param attrs the attrs to set
     */
    public void setAttrs(String[] attrs)
    {
        ArrayList<String> attrsList = new ArrayList<>();

        // Trim all attributes
        for(int i = 0; i < attrs.length; ++i) {
            attrs[ i ] = attrs[ i ].trim();

            if ( !attrs[ i ].isEmpty() ) {
                attrsList.add( attrs[ i ] );
            }
        }

        this.attrs = attrsList.toArray( new String[ attrsList.size() ] );
    }

    /**
     * Returns the reference, i.e., the sequence of attribute names.
     * @return The reference, as a single String.
     */
    public String toString()
    {
        StringBuilder toret = new StringBuilder();
        final String[] attrs = this.getAttrs();

        for(int i = 0; i < attrs.length; ++i) {
            toret.append( attrs[ i ] );

            if ( i < ( attrs.length -1 ) ) {
                toret.append( '.' );
            }
        }

        return toret.toString();
    }

    /**
     * Returns the last attribute of the reference
     * @return The last attribute, as a string
     */
    public String top()
    {
        return this.attrs[ this.attrs.length - 1 ];
    }

    /**
     * Deletes the last attribute of the reference
     */
    public void pop()
    {
        final int newLength = attrs.length -1;
        final String[] newAttrs = new String[ newLength ];

        for(int i = 0; i < newLength; ++i) {
            newAttrs[ i ] = this.attrs[ i ];
        }

        this.attrs = newAttrs;
    }

    private String[] attrs;
}
