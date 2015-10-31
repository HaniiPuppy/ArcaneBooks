package com.haniitsu.arcanebooks.magic.modifiers.effect;

public class AOE implements SpellEffectModifier
{
    public AOE() {}
    
    public static final AOE targetOnly = new AOE();
    public static final AOE aroundTarget = new AOE();
    public static final AOE targetAndAroundTarget = new AOE();
}