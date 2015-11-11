package com.haniitsu.arcanebooks.magic.modifiers.effect;

public class SpellTarget implements SpellEffectModifier
{
    public SpellTarget() {}
    
    public static final SpellTarget self = new SpellTarget();
    public static final SpellTarget projectile = new SpellTarget();
    
    public static final SpellTarget defaultValue = self;
}