package com.haniitsu.arcanebooks.magic.modifiers.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class AOESize implements SpellEffectModifier
{
    public AOESize(int distanceEffective)
    { distance = distanceEffective; }
    
    protected final double distance;
    
    public static final AOESize tiny;
    public static final AOESize small;
    public static final AOESize normal;
    public static final AOESize big;
    public static final AOESize huge;
    
    public static final AOESize defaultValue;
    private static final Set<AOESize> values;
    
    static
    {
        // Has to be down here rather than at the top of the class to avoid illegal forward references.
        tiny   = new AOESize(0);
        small  = new AOESize(2);
        normal = new AOESize(4);
        big    = new AOESize(6);
        huge   = new AOESize(8);
        
        defaultValue = small;
        
        // Should be a set that checks reference equality rather than .equals equality.
        values = Collections.newSetFromMap(new IdentityHashMap<AOESize, Boolean>());
        
        values.add(tiny);
        values.add(small);
        values.add(normal);
        values.add(big);
        values.add(huge);
    }
    
    public static void addValue(AOESize size)
    { values.add(size); }
    
    public static Collection<AOESize> getValues()
    { return new ArrayList<AOESize>(values); }
    
    public double getDistance()
    { return distance; }
}