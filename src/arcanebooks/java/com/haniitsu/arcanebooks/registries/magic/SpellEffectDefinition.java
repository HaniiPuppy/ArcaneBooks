package com.haniitsu.arcanebooks.registries.magic;

public abstract class SpellEffectDefinition
{
    public SpellEffectDefinition(String name)
    { this.name = name; }
    
    String name;
    
    public String getName()
    { return name; }
    
    public abstract void PerformEffect(SpellArgs spellArgs);
}