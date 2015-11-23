package com.haniitsu.arcanebooks.magic.modifiers.definition;

import java.util.List;

/**
 * Modifier established by the config and passed to the spell effect definition to affect basic behaviour.
 */
public interface SpellEffectDefinitionModifier
{
    /**
     * Gets the name of the spell effect modifier.
     * @return The name.
     */
    String getName();
    
    /**
     * Gets a list of all of the other modifiers passed to this modifier, in order.
     * @return The other modifiers passed to this one.
     */
    List<SpellEffectDefinitionModifier> getSubModifiers();
    
    /**
     * Gets the single value argument passed to the modifier.
     * @return The single value argument.
     */
    String getValue();
}