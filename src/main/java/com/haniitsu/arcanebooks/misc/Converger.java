package com.haniitsu.arcanebooks.misc;

/**
 * An encapsulated method for deriving a value from two passed in values.
 * @author Hanii Puppy <hanii.puppy@googlemail.com>
 * @param <T1> The type of the first source object.
 * @param <T2> The type of the second source object.
 * @param <TResult> The type of the object to be produced from the other two objects.
 */
public interface Converger<T1, T2, TResult>
{
    /**
     * Gets the resultant object from manipulating the passed in objects.
     * @param first The first source object.
     * @param second The second source object.
     * @return The object produced by manipulating the source objects.
     */
    TResult get(T1 first, T2 second);
}