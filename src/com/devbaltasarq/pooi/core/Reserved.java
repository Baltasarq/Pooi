package com.devbaltasarq.pooi.core;

/**
 * Reserved identifiers
 * User: baltasarq
 * Date: 11/16/12
 */
public class Reserved {

    public static final String ParentAttribute = "parent";

    public static final String RootObject = "Root";
    public static final String LibObject = "Lib";
    public static final String TopParentObject = "Object";
    public static final String IntObject = "Int";
    public static final String BoolObject = "Bool";
    public static final String RealObject = "Real";
    public static final String StrObject = "Str";
    public static final String SelfRef = "self";
    public static final String AssignmentOperator = "=";

    public static final String ReservedIdsForObjects =
            ' ' + ParentAttribute + ' '
            + ' ' + RootObject + ' '
            + ' ' + TopParentObject + ' '
            + ' ' + IntObject + ' '
            + ' ' + BoolObject + ' '
            + ' ' + RealObject + ' '
            + ' ' + StrObject + ' '
            + ' ' + AssignmentOperator + ' '
    ;

    public static boolean isReservedForObjects(String id)
    {
        return ( ReservedIdsForObjects.contains( id.trim() ) );
    }
}
