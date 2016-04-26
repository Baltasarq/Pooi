package com.devbaltasarq.pooi.core;

/**
 * Templates created in order to avoid typing it all.
 * Created by baltasarq on 06/07/2014.
 */
public class TemplateEngine {
    public static final String NewObjectTemplate = "(Root.anObject copy) rename ${Str:Name}";
    public static final String NewMethodTemplate = "${Obj:Object}.${Str:Name} = {$Mth:Method body}";
}
