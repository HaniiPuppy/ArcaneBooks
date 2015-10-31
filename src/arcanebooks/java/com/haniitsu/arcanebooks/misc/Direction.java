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
}