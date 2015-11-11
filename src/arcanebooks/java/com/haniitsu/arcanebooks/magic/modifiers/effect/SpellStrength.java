package com.haniitsu.arcanebooks.magic.modifiers.effect;

public class SpellStrength implements SpellEffectModifier
{
    public SpellStrength(double strengthModifier)
    { this.strengthModifier = strengthModifier; }
    
    protected final double strengthModifier;
    
    public static final SpellStrength veryWeak   = new SpellStrength(0.25);
    public static final SpellStrength weak       = new SpellStrength(0.5);
    public static final SpellStrength normal     = new SpellStrength(1);
    public static final SpellStrength strong     = new SpellStrength(1.5);
    public static final SpellStrength veryStrong = new SpellStrength(2.5);
    
    public static final SpellStrength defaultValue = weak;
    
    public double getStrengthModifier()
    { return strengthModifier; }
}