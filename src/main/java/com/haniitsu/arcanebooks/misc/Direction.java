package com.haniitsu.arcanebooks.misc;

/**
 * Representation of a direction faceable by a player or entity.
 */
public class Direction
{
    /**
     * Creates the direction.
     * @param pitch The up/down direction.
     * @param yaw The cardinal direction.
     */
    public Direction(double pitch, double yaw)
    {
        this.pitch = pitch;
        this.yaw = yaw;
    }
    
    /** The up/down direction. */
    protected final double pitch;
    
    /** The cardinal direction. */
    protected final double yaw;
    
    /**
     * Gets the up/down direction.
     * @return The pitch.
     */
    public double getPitch()
    { return pitch; }
    
    /**
     * Gets the cardinal direction.
     * @return The yaw.
     */
    public double getYaw()
    { return yaw; }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.pitch) ^ (Double.doubleToLongBits(this.pitch) >>> 32));
        hash = 47 * hash + (int) (Double.doubleToLongBits(this.yaw) ^ (Double.doubleToLongBits(this.yaw) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        final Direction other = (Direction) obj;
        if(Double.doubleToLongBits(this.pitch) != Double.doubleToLongBits(other.pitch))
            return false;
        if(Double.doubleToLongBits(this.yaw) != Double.doubleToLongBits(other.yaw))
            return false;
        return true;
    }
}