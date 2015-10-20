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
}
