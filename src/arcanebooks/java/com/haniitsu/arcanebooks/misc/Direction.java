package com.haniitsu.arcanebooks.misc;

public class Direction
{
    public Direction(double pitch, double yaw)
    {
        this.pitch = pitch;
        this.yaw = yaw;
    }
    
    protected final double pitch;
    protected final double yaw;
    
    public double getPitch()
    { return pitch; }
    
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