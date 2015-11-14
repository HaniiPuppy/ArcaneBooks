package com.haniitsu.arcanebooks.magic.modifiers.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class SpellTarget implements SpellEffectModifier
{
    public SpellTarget() {}
    
    public static final SpellTarget self;
    public static final SpellTarget projectile;
    
    public static final SpellTarget defaultValue;
    private static final Set<SpellTarget> values;
    
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
    
    public static void addValue(SpellTarget target)
    { values.add(target); }
    
    public static Collection<SpellTarget> getValues()
    { return new ArrayList<SpellTarget>(values); }
}