package com.haniitsu.arcanebooks.magic;

import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import java.util.List;

public abstract class SpellEffectDefinition
{
    public SpellEffectDefinition(String name)
    { this.name = name; }
    
    String name;
    
    public String getName()
    { return name; }
    
    public abstract void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers);
}