package com.haniitsu.arcanebooks.magic.modifiers.effect;

import com.haniitsu.arcanebooks.misc.BlockLocation;
import com.haniitsu.arcanebooks.misc.Direction;
import com.haniitsu.arcanebooks.misc.Location;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public abstract class AOEShape implements SpellEffectModifier
{
    public AOEShape()
    { this(1); }
    
    public AOEShape(double AOESizeModifier)
    { this.AOESizeModifier = AOESizeModifier; }
    
    protected final double AOESizeModifier;
    
    public static final AOEShape around;
    
    public static final AOEShape defaultValue;
    private static final Set<AOEShape> values;
    
    static
    {
        // Has to be down here rather than at the top of the class to avoid illegal forward references.
        around = getShapeAround();
        
        defaultValue = around;
        
        // Should be a set that checks reference equality rather than .equals equality.
        values = Collections.newSetFromMap(new IdentityHashMap<AOEShape, Boolean>());
        
        values.add(around);
    }
    
    public static void addValue(AOEShape size)
    { values.add(size); }
    
    public static Collection<AOEShape> getValues()
    { return new ArrayList<AOEShape>(values); }
    
    private static AOEShape getShapeAround()
    {
        return new AOEShape()
        {
            @Override
            public boolean coversLocation(double    AOESize,
                                          Location  burstLocation,
                                          Direction burstDirection,
                                          Location  checkLocation)
            {
                double distanceX = checkLocation.getX() - burstLocation.getX();
                double distanceY = checkLocation.getY() - burstLocation.getY();
                double distanceZ = checkLocation.getZ() - burstLocation.getZ();

                // Squaring that extra time is cheaper than square-rooting the XYZ distance.
                return ((distanceX * distanceX) + (distanceY * distanceY) + (distanceZ * distanceZ)) <= (AOESize * AOESize);
            }
        };
    }
    
    public double getAOESizeModifier()
    { return AOESizeModifier; }
    
    public Collection<Entity> getEntitiesInRange(double AOESize, Location burstLocation, Direction burstDirection)
    {
        Collection<Entity> entitiesInRange = new HashSet<Entity>();
        Collection<Chunk> affectedChunks = new HashSet<Chunk>();
        int chunkSize = 16;
        
        BlockLocation burstBlockLocation = burstLocation.toBlockLocation();
        World world = burstBlockLocation.getWorld();
        
        // Y isn't used, so I've 0'd it. These are only used in determining relevant chunks.
        BlockLocation minAffectedBlock = new Location(burstLocation.getX() - AOESize, 0, burstLocation.getZ() - AOESize).toBlockLocation();
        BlockLocation maxAffectedBlock = new Location(burstLocation.getX() + AOESize, 0, burstLocation.getZ() + AOESize).toBlockLocation();
        int xDistance = maxAffectedBlock.getX() - minAffectedBlock.getX();
        int zDistance = maxAffectedBlock.getZ() - maxAffectedBlock.getZ();
        
        for(int xAdjustment = 0; xAdjustment <= xDistance; xAdjustment += chunkSize)
            for(int zAdjustment = 0; zAdjustment <= zDistance; zAdjustment += chunkSize)
                affectedChunks.add(world.getChunkFromBlockCoords(minAffectedBlock.getX() + xAdjustment, minAffectedBlock.getZ() + zAdjustment));
        
        Collection<Entity> possibleEntities = new HashSet<Entity>();
        
        // The following code is written under the assumption that lists in the array returned by chunk.entityLists may
        // only be populated by instances of Entity. Please tell me if I'm wrong about this, but I don't think i am?
        for(Chunk chunk : affectedChunks)
            for(List list : chunk.entityLists)
                possibleEntities.addAll(list);
        
        for(Entity entity : possibleEntities)
            if(coversLocation(AOESize, burstLocation, burstDirection, new Location(world, entity.posX, entity.posY, entity.posZ)))
                entitiesInRange.add(entity);
        
        return entitiesInRange;
    }
    
    public Collection<BlockLocation> getBlocksInRange(double AOESize, Location burstLocation, Direction burstDirection)
    {
        BlockLocation min = new Location(burstLocation.getX() - AOESize,
                                         burstLocation.getY() - AOESize,
                                         burstLocation.getZ() - AOESize).toBlockLocation();
        
        BlockLocation max = new Location(burstLocation.getX() + AOESize,
                                         burstLocation.getY() + AOESize,
                                         burstLocation.getZ() + AOESize).toBlockLocation();
        
        Collection<BlockLocation> blocks = new HashSet<BlockLocation>();
        int worldId = burstLocation.getWorldId();
        
        for(int x = min.getX(); x <= max.getX(); x++)
            for(int y = min.getY(); y <= max.getY(); y++)
                for(int z = min.getZ(); z <= max.getZ(); z++)
                    if(coversLocation(AOESize, burstLocation, burstDirection, new Location(worldId, 0.5 + x, 0.5 + y, 0.5 + z)))
                        blocks.add(new BlockLocation(worldId, x, y, z));
        
        return blocks;
    }
    
    public abstract boolean coversLocation(double    AOESize,
                                           Location  burstLocation,
                                           Direction burstDirection,
                                           Location  checkLocation);
}