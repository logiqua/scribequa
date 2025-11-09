package com.javax0.logiqua.commands.utils;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;

public class Array {
    private final Executor executor;

    public Array(Executor executor) {
        this.executor = executor;
    }

    /**
     * Flattens the given object by checking if it can be accessed as a collection-like structure.
     * If the object is indexed (list-like), it converts the elements of the list into an array.
     * Otherwise, it wraps the object in a single-element array.
     *
     * @param arg the object to flatten, which can either be a collection-like structure or a single value
     * @return an array of objects; if the input is a list-like structure, returns an array containing all elements of the list;
     * if the input is a single value, returns an array containing the value as its only element
     */
    public Object[] flat(Object arg){
        if (executor.getContext().accessor(arg) instanceof Context.IndexedProxy list) {
            final var returnValue = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                returnValue[i] = list.get(i).get();
            }
            return returnValue;
        }
        return new Object[]{arg};
    }

}
