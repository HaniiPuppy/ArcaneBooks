package com.haniitsu.arcanebooks.misc.geometry;

/**
 * A point in 3d space.
 * @param <T> The type of the numbers in which to ordinate this point.
 */
public interface Point3d <T extends Number> extends Point2d<T>
{
    /**
     * Gets the point's Z coördinate.
     * @return The Z coördinate.
     */
    T getZ();
    
    /**
     * Gets the distance from this point to another passed point in 3d space, or 2d space if the passed point is not a
     * 3d point as well.
     * @param other The other point to get the distance from this point from.
     * @return The distance from this point to the passed point.
     */
    @Override
    double getDistanceFrom(Point2d<T> other);
    
    /**
     * Gets the distance from this point to another passed point in 3d space, or 2d space if the passed point is not a
     * 3d point as well, squared. This is useful where the distance is only needed for comparison purposes, and
     * performance is of concern. This method should normally skip the square-root part of the function, which can be
     * expensive under some circumstances.
     * @param other The other point to get the distance^2 from this point from.
     * @return The distance^2 from this point to the passed point.
     */
    @Override
    double getDistanceFromSquared(Point2d<T> other);
}