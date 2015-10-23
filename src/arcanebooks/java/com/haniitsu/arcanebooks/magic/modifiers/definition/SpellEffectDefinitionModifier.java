package com.haniitsu.arcanebooks.magic.modifiers.definition;

import java.util.List;

public interface SpellEffectDefinitionModifier
{
    String getName();
    List<SpellEffectDefinitionModifier> getArguments();
    String getValue();
}