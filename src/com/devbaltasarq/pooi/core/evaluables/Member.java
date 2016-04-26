package com.devbaltasarq.pooi.core.evaluables;

import com.devbaltasarq.pooi.core.Evaluable;

/**
 * Represents member inside an object.
 * User: baltasarq
 * Date: 11/23/12
 */
public abstract class Member extends Evaluable {
    public Member(String n)
    {
        this.setName( n );
    }

    /**
     * Get the name of the member
     * @return the name as a String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Change the name of the member
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    private String name;
}
