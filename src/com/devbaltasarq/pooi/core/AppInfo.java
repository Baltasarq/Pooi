// Pooi (c) 2014 Baltasar MIT License <jbgarcia@uvigo.es>

package com.devbaltasarq.pooi.core;

/**
 * Info about app's version
 * @author baltasarq
 */
public class AppInfo {
    public static final String Name = "Pooi";
    public static final String Email = "jbgarcia@uvigo.es";
    public static final String Author = "Baltasar García Perez-Schofield";
    public static final String Version = "2.2.3 20230909";
    public static final String License = "MIT License: https://opensource.org/licenses/MIT";
    public static final String SessionFileExt = ".txt";
    public static final String ScriptFileExt = ".poi";
    public static final String JsonFileExt = ".jsn";

    public static String getMsgVersion()
    {
        if ( msgVersion.isEmpty() ) {
            msgVersion = Name + " v" + Version;
        }

        return msgVersion;
    }

    private static String msgVersion = "";
}
