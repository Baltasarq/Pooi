// ObjectJsonPersistence.java

package core.persistence;

import core.ObjectBag;
import core.Persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Is able to retrieve and store object in the JSON format.
 * @author baltasarq
 */
public class JsonPersistence extends Persistence {

    public static final String JsonExt = "js";

    public JsonPersistence(ObjectBag ob)
    {
        super( ob );
    }

    public JsonPersistence(ObjectBag[] obs)
    {
        super( obs );
    }

    public ObjectBag[] load(File path)
    {
        this.objs = null;

        return this.objs;
    }

    public boolean save(File path)
    {
        boolean toret = false;
        FileWriter outFile = null;

        try {
            outFile = new FileWriter( path );
            int pos = 0;

            // Save objects as JSON
            for(ObjectBag obj : this.objs) {
                outFile.write( obj.toJson() );

                if ( pos < ( this.objs.length -1 ) ) {
                    outFile.write( ',' );
                }

                outFile.write( '\n' );
                ++pos;
            }

            outFile.close();
            toret = true;
        } catch (IOException ex) {
            toret = false;
        }

        return toret;
    }
}
