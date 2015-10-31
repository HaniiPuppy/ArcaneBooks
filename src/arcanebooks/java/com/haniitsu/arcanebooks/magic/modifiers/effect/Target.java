package com.haniitsu.arcanebooks.magic.modifiers.effect;

public class Target implements SpellEffectModifier
{
    public Target() {}
    
    public static final Target self = new Target();
    public static final Target projectile = new Target();
}