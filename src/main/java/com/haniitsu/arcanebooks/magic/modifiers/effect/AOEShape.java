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

/**
 * Spell effect modifier used for determining the shape that the AOE will take.
 */
public abstract class AOEShape implements SpellEffectModifier
{
    /**
     * Creates a new AOE shape.
     * @param name The modifier name.
     */
    public AOEShape(String name)
    { this(name, 1); }
    
    /**
     * Creates a new AOE shape with the given size modifier.
     * @param name The modifier name.
     * @param AOESizeModifier The AOE size multiplier of the shape, where 1.0 is no change.
     */
    public AOEShape(String name, double AOESizeModifier)
    { this.AOESizeModifier = AOESizeModifier; this.name = name;}
    
    /** The size multiplier for this AOE shape. */
    protected final double AOESizeModifier;
    
    protected final String name;
    
    /** Targets entities and blocks in a sphere, with the target location at the centre. */
    public static final AOEShape around;
    
    /** The AOE shape used when none is specified. */
    public static final AOEShape defaultValue;
    
    /** All possible AOE shapes, including third-party ones. */
    private static final Set<AOEShape> values;
    
    /** Instantiates each of the members, sets the default, and populates the members set. */
    static
    {
        // Has to be down here rather than at the top of the class to avoid illegal forward references.
        around = getShapeAround();
        
        defaultValue = around;
        
        // Should be a set that checks reference equality rather than .equals equality.
        values = Collections.newSetFromMap(new IdentityHashMap<AOEShape, Boolean>());
        
        values.add(around);
    }
    
    /**
     * Adds a new AOE shape to the pseudo-enum, such that it's included in calls to .getValues().
     * @param shape The AOE shape to add.
     */
    public static void addValue(AOEShape shape)
    { values.add(shape); }
    
    @Override
    public String getModifierName()
    { return name; }
    
    /**
     * Gets all possible AOE shapes, including ones added by third parties.
     * @return A collection of all possible AOE shapes.
     */
    public static Collection<AOEShape> getValues()
    { return new ArrayList<AOEShape>(values); }
    
    /**
     * Creates a new instance of the the spherical "around" AOE shape.
     * @return The new AOE shape.
     */
    private static AOEShape getShapeAround()
    {
        return new AOEShape("around")
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
    
    /**
     * Gets the size multiplier for this AOE shape.
     * @return The size multiplier.
     */
    public double getAOESizeModifier()
    { return AOESizeModifier; }
    
    /**
     * Gets all entities within the shape (of the passed size) of the AOE with the passed burst location at the centre.
     * @note Limits search to relevant chunks, and checks all entities in those chunks to see if they're within the AOE.
     * @param AOESize The size of the AOE in metres.
     * @param burstLocation The centre-point of the AOE shape; the place where the spell effect was burst.
     * @param burstDirection The direction the spell burst was facing in.
     * @return A collection of all the entities that should be affected by a spell cast with this AOE shape, at the
     * passed location. with the passed size and direction.
     */
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
    
    /**
     * Gets all blocks within the shape (of the passed size) of the AOE with the passed burst location at the centre.
     * @param AOESize The size of the AOE in metres.
     * @param burstLocation The centre-point of the AOE shape; the place where the spell effect was burst.
     * @param burstDirection The direction the spell burst was facing in.
     * @return A collection of BlockLocations representing all of the blocks in the world that should be affected by
     * a spell cast with this AOE shape, at the passed location, with the passed size and direction.
     */
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
    
    /**
     * Checks whether or not a location falls within this AOE shape of the passed size, at the passed location, in the
     * passed direction.
     * @param AOESize The size of the AOE in metres.
     * @param burstLocation The centre-point of the AOE shape.
     * @param burstDirection The direction the AOE shape should be facing in.
     * @param checkLocation The location to check whether or not it's within the AOE shape.
     * @return True if the location is in the AOE shape. Otherwise, false.
     */
    public abstract boolean coversLocation(double    AOESize,
                                           Location  burstLocation,
                                           Direction burstDirection,
                                           Location  checkLocation);
}