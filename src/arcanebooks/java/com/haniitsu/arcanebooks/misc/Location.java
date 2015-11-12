package com.haniitsu.arcanebooks.misc;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

// Does forge have an officially supported location class? I couldn't find it.

public class Location
{
    public Location(double x, double y, double z)
    { this(0, x, y, z); }
    
    public Location(int worldId, double x, double y, double z)
    {
        this.worldId = worldId;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Location(World world, double x, double y, double z)
    { this(world.provider.dimensionId, x, y, z); }
    
    int worldId;
    double x, y, z;
    
    public int getWorldId()
    { return worldId; }
    
    public World getWorld()
    { return DimensionManager.getWorld(worldId); }
    
    public double getX()
    { return x; }
    
    public double getY()
    { return y; }
    
    public double getZ()
    { return z; }
    
    public BlockLocation toBlockLocation()
    { return new BlockLocation(worldId, (int)x, (int)y, (int)z); }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 53 * hash + this.worldId;
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 53 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        final Location other = (Location) obj;
        if(this.worldId != other.worldId)
            return false;
        if(Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if(Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        if(Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z))
            return false;
        return true;
    }
}
