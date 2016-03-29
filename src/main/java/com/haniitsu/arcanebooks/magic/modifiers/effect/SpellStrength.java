package com.haniitsu.arcanebooks.magic.modifiers.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Spell effect modifier that determines how strong a spell effect should be.
 */
public class SpellStrength implements SpellEffectModifier
{
    /**
     * Creates a new spell strength value.
     * @param name The modifier name.
     * @param strengthModifier Abstract multiplier determining how weak or strong any spell effect should be.
     */
    public SpellStrength(String name, double strengthModifier)
    { this.strengthModifier = strengthModifier; this.name = name; }
    
    /** Abstract multiplier determining how weak or strong any spell effect should be. */
    protected final double strengthModifier;
    
    protected final String name;
    
    final static protected String groupName = "SpellStrength";
    
    /** The weakest possible spell strength. */
    public static final SpellStrength veryWeak;
    
    /** A weak spell strength. */
    public static final SpellStrength weak;
    
    /** The middle-strength spell strength. */
    public static final SpellStrength normal;
    
    /** A strong spell strength. */
    public static final SpellStrength strong;
    
    /** The strongest possible spell strength. */
    public static final SpellStrength veryStrong;
    
    /** The spell strength used when none is specified. */
    public static final SpellStrength defaultValue;
    
    /** All possible spell strengths, including third-party ones. */
    private static final Set<SpellStrength> values;
    
    /** Instantiates each of the members, sets the default, and populates the members set. */
    static
    {
        // Has to be down here rather than at the top of the class to avoid illegal forward references.
        veryWeak   = new SpellStrength("veryWeak",   0.25);
        weak       = new SpellStrength("weak",       0.5);
        normal     = new SpellStrength("normal",     1);
        strong     = new SpellStrength("strong",     1.5);
        veryStrong = new SpellStrength("veryStrong", 2.5);
        
        defaultValue = normal;
        
        // Should be a set that checks reference equality rather than .equals equality.
        values = Collections.newSetFromMap(new IdentityHashMap<SpellStrength, Boolean>());
        
        values.add(veryWeak);
        values.add(weak);
        values.add(normal);
        values.add(strong);
        values.add(veryStrong);
    }
    
    /**
     * Adds a new spell strength to the pseudo-enum, such that it's included in calls to .getValues().
     * @param strength The spell strength to add.
     */
    public static void addValue(SpellStrength strength)
    { values.add(strength); }
    
    /**
     * Gets all possible spell strengths, including ones added by third parties.
     * @return A collection of all possible spell strengths.
     */
    public static Collection<SpellStrength> getValues()
    { return new ArrayList<SpellStrength>(values); }
    
    /**
     * Gets the abstract multiplier that determines the strength of spell effects.
     * @return The spell strength multiplier.
     */
    public double getStrengthModifier()
    { return strengthModifier; }

    @Override
    public String getModifierName()
    { return name; }
    
    @Override
    public String getModifierGroupName()
    { return groupName; }
}