package com.haniitsu.arcanebooks.magic.modifiers.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * A spell effect modifier that determines the targeting mechanism used for a spell phrase. e.g. on the caster, as a
 * projectile, etc.
 */
public class SpellTarget implements SpellEffectModifier
{
    /** Creates a new SpellTarget. */
    public SpellTarget() {}
    
    /** Targets the person who cast the spell. */
    public static final SpellTarget self;
    
    /** Encloses the spell in a projectile, and targets the block/entity/location the projectile hits. */
    public static final SpellTarget projectile;
    
    /** The spell targeting mechanism used when none is specified. */
    public static final SpellTarget defaultValue;
    
    /** All possible spell targeting mechanisms, including third-party ones. */
    private static final Set<SpellTarget> values;
    
    /** Instantiates each of the members, sets the default, and populates the members set. */
    static
    {
        // Has to be down here rather than at the top of the class to avoid illegal forward references.
        self       = new SpellTarget();
        projectile = new SpellTarget();
        
        defaultValue = self;
        
        // Should be a set that checks reference equality rather than .equals equality.
        values = Collections.newSetFromMap(new IdentityHashMap<SpellTarget, Boolean>());
        
        values.add(self);
        values.add(projectile);
    }
    /**
     * Adds a new spell targeting mechanism to the pseudo-enum, such that it's included in calls to .getValues().
     * @param target The SpellTarget to add.
     */
    public static void addValue(SpellTarget target)
    { values.add(target); }
    
    /**
     * Gets all possible spell targeting mechanisms, including ones added by third parties.
     * @return A collection of all possible SpellTargets.
     */
    public static Collection<SpellTarget> getValues()
    { return new ArrayList<SpellTarget>(values); }
}