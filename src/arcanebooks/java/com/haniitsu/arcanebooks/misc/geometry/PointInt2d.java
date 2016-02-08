package com.haniitsu.arcanebooks.misc.geometry;

import java.util.Random;

/** Implementation of Point2d using integer-based coördinates. */
public class PointInt2d implements Point2d<Integer>
{
    /**
     * Creates a new point from the passed coördinates.
     * @param x The X coördinate of the new point.
     * @param y The Y coördinate of the new point.
     */
    public PointInt2d(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets a random point from 0, 0 to maxX, maxY.
     * @param maxX The maximum possible X value of the random point.
     * @param maxY The maximum possible Y value of the random point.
     * @return A random point from 0, 0 to maxX, maxY.
     */
    public static PointInt2d getRandom(int maxX, int maxY)
    {
        Random rand = new Random();
        return new PointInt2d(rand.nextInt(maxX + 1), rand.nextInt(maxY + 1));
    }
    
    /**
     * Gets a random point from minX, minY to maxX, maxY.
     * @param minX The minimum possible X value of the random point.
     * @param maxX The minimum possible Y value of the random point.
     * @param minY The maximum possible X value of the random point.
     * @param maxY The maximum possible Y value of the random point.
     * @return A random point from minX, minY to maxX, maxY.
     */
    public static PointInt2d getRandom(int minX, int maxX, int minY, int maxY)
    {
        Random rand = new Random();
        return new PointInt2d(rand.nextInt(maxX - minX + 1) + minX, rand.nextInt(maxY - minY + 1) + minY);
    }
    
    /** The X coördinate of this point. */
    protected final int x;
    
    /** The Y coördinate of this point. */
    protected final int y;
    
    @Override
    public Integer getX()
    { return x; }
    
    @Override
    public Integer getY()
    { return y; }
    
    @Override
    public double getDistanceFrom(Point2d<Integer> other)
    { return Math.sqrt(getDistanceFromSquared(other)); }
    
    @Override
    public double getDistanceFromSquared(Point2d<Integer> other)
    {
        double xDist = other.getX() - getX();
        double yDist = other.getY() - getY();
        return xDist * xDist + yDist * yDist;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null || getClass() != obj.getClass())
            return false;
        
        final PointInt2d other = (PointInt2d)obj;
        
        return this.x == other.x && this.y == other.y;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 37 * hash + this.x;
        hash = 37 * hash + this.y;
        return hash;
    }
}
