package com.haviitsu.arcanebooks.misc;

// Does forge have an officially supported location class? I couldn't find it.

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;


/*

What's forge's class for referring to instances of blocks in the world - for instance, a dirt block at
x:50, y:50, z:50 in the overworld? I can't find it, it seems pretty much impossible to google, and IRC isn't being
much help - I keep getting told how to get the Block object of a block at a given set of coördinates, or the tile
entity, or the ID of a block at a set of coördinates.

If the world is defined as:

public class World
{
    List<List<List<SOMECLASS>>> worldBlocks;
}

where SomeClass contains the Block variable, or a reference to it to specify which block it is, what is SOMECLASS?
*/

public class BlockLocation
{
    public BlockLocation(int worldId, int x, int y, int z)
    {
        this.worldId = worldId;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public BlockLocation(int x, int y, int z)
    { this(0, x, y, z); }
    
    int worldId, x, y, z;
    
    public int getWorldId()
    { return worldId; }
    
    public World getWorld()
    { return DimensionManager.getWorld(worldId); }
    
    public int getX()
    { return x; }
    
    public int getY()
    { return y; }
    
    public int getZ()
    { return z; }
    
    public Block getBlockAt()
    { return getWorld().getBlock(x, y, z); }
    
    public TileEntity getTileEntityAt()
    { return getWorld().getTileEntity(x, y, z); }
    
    public Location toLocationCentre()
    { return new Location(worldId, 0.5 + x, 0.5 + y, 0.5 + z); }
    
    public Location toLocationFloored()
    { return new Location(worldId, x, y, z); }
}
