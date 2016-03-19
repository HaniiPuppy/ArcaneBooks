package com.haniitsu.arcanebooks.magic.modifiers.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Spell effect modifier that determines how big the AOE should be.
 */
public class AOESize implements SpellEffectModifier
{
    /**
     * Creates a new AOE size value.
     * @param distanceEffective How far away from the centre point this AOE size should be able to affect, in metres.
     */
    public AOESize(int distanceEffective)
    { distance = distanceEffective; }
    
    /** How far away from the centre point this AOE size should be able to affect, in metres */
    protected final double distance;
    
    /** The smallest possible distance affectable. */
    public static final AOESize tiny;
    
    /** A small distance affectable. */
    public static final AOESize small;
    
    /** The middle distance affectable. */
    public static final AOESize normal;
    
    /** A large distance affectable. */
    public static final AOESize big;
    
    /** The largest possible distance affectable. */
    public static final AOESize huge;
    
    /** The AOE size used when none is specified. */
    public static final AOESize defaultValue;
    
    /** All possible AOE sizes, including third-party ones. */
    private static final Set<AOESize> values;
    
    /** Instantiates each of the members, sets the default, and populates the members set. */
    static
    {
        // Has to be down here rather than at the top of the class to avoid illegal forward references.
        tiny   = new AOESize(1);
        small  = new AOESize(3);
        normal = new AOESize(5);
        big    = new AOESize(7);
        huge   = new AOESize(9);
        
        defaultValue = small;
        
        // Should be a set that checks reference equality rather than .equals equality.
        values = Collections.newSetFromMap(new IdentityHashMap<AOESize, Boolean>());
        
        values.add(tiny);
        values.add(small);
        values.add(normal);
        values.add(big);
        values.add(huge);
    }
    
    /**
     * Adds a new AOE size to the pseudo-enum, such that it's included in calls to .getValues().
     * @param size The AOE size to add.
     */
    public static void addValue(AOESize size)
    { values.add(size); }
    
    /**
     * Gets all possible AOE sizes, including ones added by third parties.
     * @return A collection of all possible AOE sizes.
     */
    public static Collection<AOESize> getValues()
    { return new ArrayList<AOESize>(values); }
    
    /**
     * Gets the distance, in metres, where blocks and entities should be able to be affected by a spell effect cast
     * using this AOE size.
     * @return The distance affectable in metres.
     */
    public double getDistance()
    { return distance; }
}