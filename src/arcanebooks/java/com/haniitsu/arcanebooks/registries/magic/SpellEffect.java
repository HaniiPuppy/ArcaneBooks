package com.haniitsu.arcanebooks.registries.magic;

import com.haniitsu.arcanebooks.registries.magic.modifiers.definition.SpellEffectDefinitionModifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpellEffect
{
    public SpellEffect(SpellEffectDefinition definition)
    {
        this.definition = definition;
        definitionModifiers = new ArrayList<SpellEffectDefinitionModifier>();
    }
    
    public SpellEffect(SpellEffectDefinition definition, Collection<? extends SpellEffectDefinitionModifier> modifiers)
    {
        this.definition = definition;
        this.definitionModifiers = new ArrayList<SpellEffectDefinitionModifier>(modifiers);
    }
    
    SpellEffectDefinition definition;
    List<SpellEffectDefinitionModifier> definitionModifiers;
    
    public void PerformEffect(SpellArgs spellArgs)
    {
        spellArgs.setDefinitionModifiers(definitionModifiers);
        definition.PerformEffect(spellArgs);
    }
}