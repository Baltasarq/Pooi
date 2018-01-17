package com.devbaltasarq.pooi.core;

import com.devbaltasarq.pooi.core.evaluables.Command;

/**
 * Represents all evaluable parts inside a method
 * User: baltasarq
 * Date: 11/19/12
 */
public abstract class Evaluable {
    /** Determines whether this evaluable is just a POP or not.
     *  @return true when it is a POP, false otherwise.
     */
    public boolean isPopTask()
    {
        return false;
    }
}
