package com.haniitsu.arcanebooks.magic.modifiers.effect;

public class AOESize implements SpellEffectModifier
{
    public AOESize(int distanceEffective)
    { distance = distanceEffective; }
    
    protected int distance;
    
    public static final AOESize tiny   = new AOESize(0);
    public static final AOESize small  = new AOESize(2);
    public static final AOESize normal = new AOESize(4);
    public static final AOESize big    = new AOESize(6);
    public static final AOESize huge   = new AOESize(8);
    
    public static final AOESize defaultValue = small;
}