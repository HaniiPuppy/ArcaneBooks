package com.haniitsu.arcanebooks.misc;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

// Does forge have an officially supported location class? I couldn't find it.

/**
 * Representation of a specific point in the world.
 */
public class Location
{
    /**
     * Creates a new location from the given XYZ coördinates.
     * @param x The X coördinate.
     * @param y The Y coördinate.
     * @param z The Z coördinate.
     */
    public Location(double x, double y, double z)
    { this(0, x, y, z); }
    
    /**
     * Creates a new location from the given world ID and XYZ coördinates.
     * @param worldId The ID of the world this location should be in.
     * @param x The X coördinate.
     * @param y The Y coördinate.
     * @param z The Z coördinate.
     */
    public Location(int worldId, double x, double y, double z)
    {
        this.worldId = worldId;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Creates a new location from the given world and XYZ coördinates.
     * @param world The world this location should be in.
     * @param x The X coördinate.
     * @param y The Y coördinate.
     * @param z The Z coördinate.
     */
    public Location(World world, double x, double y, double z)
    { this(world.provider.dimensionId, x, y, z); }
    
    /** The world this location is in. */
    int worldId;
    
    /** The X coördinate of the location. */
    double x;
    
    /** The Y coördinate of the location. */
    double y;
    
    /** The Z coördinate of the location. */
    double z;
    
    /**
     * Gets the ID of the world this location is in.
     * @return The world ID.
     */
    public int getWorldId()
    { return worldId; }
    
    /**
     * Gets the world this location is in.
     * @return The world.
     */
    public World getWorld()
    { return DimensionManager.getWorld(worldId); }
    
    /**
     * Gets the X coördinate of this location.
     * @return The X coördinate.
     */
    public double getX()
    { return x; }
    
    /**
     * Gets the Y coördinate of this location.
     * @return The Y coördinate.
     */
    public double getY()
    { return y; }
    
    /**
     * Gets the Z coördinate of this location.
     * @return The Z coördinate.
     */
    public double getZ()
    { return z; }
    
    /**
     * Gets the block (location) at this location.
     * @return The block at this location.
     */
    public BlockLocation toBlockLocation()
    { return new BlockLocation(worldId, (int)x, (int)y, (int)z); }
    
    /**
     * Gets the distance between this location and another passed the location.
     * @param other The other location to get the distance from this one.
     * @return The distance between this location and the passed one.
     */
    public double getDistanceFrom(Location other)
    {
        double xDist = other.getX() - getX();
        double yDist = other.getY() - getY();
        double zDist = other.getZ() - getZ();
        return Math.sqrt((xDist * xDist) + (yDist * yDist) + (zDist * zDist));
    }

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
