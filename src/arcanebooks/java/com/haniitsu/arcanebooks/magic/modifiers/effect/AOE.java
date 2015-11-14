package com.haniitsu.arcanebooks.magic.modifiers.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class AOE implements SpellEffectModifier
{   
    public AOE() {}
    
    public static final AOE targetOnly;
    public static final AOE aroundTarget;
    public static final AOE targetAndAroundTarget;
    
    public static final AOE defaultValue;
    private static final Set<AOE> values;
    
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
    
    public static void addValue(AOE aoe)
    { values.add(aoe); }
    
    public static Collection<AOE> getValues()
    { return new ArrayList<AOE>(values); }
}