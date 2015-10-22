package com.haniitsu.arcanebooks.magic;

import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;

public abstract class SpellEffectDefinition implements SpellEffectDefinitionModifier
{
    public SpellEffectDefinition(String name)
    { this.name = name; }
    
    String name;
    
    public String getName()
    { return name; }
    
    public abstract void PerformEffect(SpellArgs spellArgs);
}