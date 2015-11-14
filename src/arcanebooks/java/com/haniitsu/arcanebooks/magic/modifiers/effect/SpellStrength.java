package com.haniitsu.arcanebooks.magic.modifiers.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class SpellStrength implements SpellEffectModifier
{
    public SpellStrength(double strengthModifier)
    { this.strengthModifier = strengthModifier; }
    
    protected final double strengthModifier;
    
    public static final SpellStrength veryWeak;
    public static final SpellStrength weak;
    public static final SpellStrength normal;
    public static final SpellStrength strong;
    public static final SpellStrength veryStrong;
    
    public static final SpellStrength defaultValue;
    private static final Set<SpellStrength> values;
    
    static
    {
        // Has to be down here rather than at the top of the class to avoid illegal forward references.
        veryWeak   = new SpellStrength(0.25);
        weak       = new SpellStrength(0.5);
        normal     = new SpellStrength(1);
        strong     = new SpellStrength(1.5);
        veryStrong = new SpellStrength(2.5);
        
        defaultValue = weak;
        
        // Should be a set that checks reference equality rather than .equals equality.
        values = Collections.newSetFromMap(new IdentityHashMap<SpellStrength, Boolean>());
        
        values.add(veryWeak);
        values.add(weak);
        values.add(normal);
        values.add(strong);
        values.add(veryStrong);
    }
    
    public static void addValue(SpellStrength strength)
    { values.add(strength); }
    
    public static Collection<SpellStrength> getValues()
    { return new ArrayList<SpellStrength>(values); }
    
    public double getStrengthModifier()
    { return strengthModifier; }
}