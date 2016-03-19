package com.haniitsu.arcanebooks.magic.modifiers.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Spell effect modifier used for determining if the spell effect affects just the target, around the target, or both.
 */
public class AOE implements SpellEffectModifier
{
    /** Creates a new AOE. */
    public AOE() {}
    
    /** Targets only the entity/block the spell burst at. */
    public static final AOE targetOnly;
    
    /** Targets entities and blocks around the hit target, within the AOE, but not the hit target itself. */
    public static final AOE aroundTarget;
    
    /** Combination of targetOnly and aroundTarget. Targets the hit target and entities and blocks within the AOE. */
    public static final AOE targetAndAroundTarget;
    
    /** The AOE value used when none is specified. */
    public static final AOE defaultValue;
    
    /** All possible AOE values, including third-party ones. */
    private static final Set<AOE> values;
    
    /** Instantiates each of the members, sets the default, and populates the members set. */
    static
    {
        // Has to be down here rather than at the top of the class to avoid illegal forward references.
        targetOnly            = new AOE();
        aroundTarget          = new AOE();
        targetAndAroundTarget = new AOE();
        
        defaultValue = targetOnly;
        
        // Should be a set that checks reference equality rather than .equals equality.
        values = Collections.newSetFromMap(new IdentityHashMap<AOE, Boolean>());
        
        values.add(targetOnly);
        values.add(aroundTarget);
        values.add(targetAndAroundTarget);
    }
    
    /**
     * Adds a new AOE value to the pseudo-enum, such that it's included in calls to .getValues().
     * @param aoe The AOE value to add.
     */
    public static void addValue(AOE aoe)
    { values.add(aoe); }
    
    /**
     * Gets all possible AOE values, including ones added by third parties.
     * @return A collection of all possible AOE values.
     */
    public static Collection<AOE> getValues()
    { return new ArrayList<AOE>(values); }
}