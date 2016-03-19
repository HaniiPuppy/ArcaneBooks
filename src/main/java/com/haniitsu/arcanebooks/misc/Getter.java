package com.haniitsu.arcanebooks.misc;

/**
 * Generic getter, that gets things, upon calling .get();
 * @param <T> The thing to get.
 */
public interface Getter<T>
{
    /**
     * Gets the thing!
     * @return The thing.
     */
    T get();
}