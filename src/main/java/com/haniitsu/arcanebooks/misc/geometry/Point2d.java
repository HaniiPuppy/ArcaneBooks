package com.haniitsu.arcanebooks.misc.geometry;

/**
 * A point in 2d space.
 * @param <T> The type of the numbers in which to ordinate this point.
 */
public interface Point2d <T extends Number>
{
    /**
     * Gets the point's X coördinate.
     * @return The X coördinate.
     */
    T getX();
    
    /**
     * Gets the point's Y coördinate.
     * @return The Y coördinate.
     */
    T getY();
    
    /**
     * Gets the distance from this point to another passed point in 2d space.
     * @param other The other point to get the distance from this point from.
     * @return The distance from this point to the passed point.
     */
    double getDistanceFrom(Point2d<T> other);
    
    /**
     * Gets the distance from this point to another passed point in 2d space, squared. This is useful where the distance
     * is only needed for comparison purposes, and performance is of concern. This method should normally skip the
     * square-root part of the function, which can be expensive under some circumstances.
     * @param other The other point to get the distance^2 from this point from.
     * @return The distance^2 from this point to the passed point.
     */
    double getDistanceFromSquared(Point2d<T> other);
}