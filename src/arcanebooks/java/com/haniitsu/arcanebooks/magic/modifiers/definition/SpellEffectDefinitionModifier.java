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
    
    /**
     * Gets a copy of the SpellEffectDefinitionModifier object, of the same type as the object it's called on (ideally,
     * but not necessarily guaranteed in the case of children classes), containing the same data. (Usually, the name,
     * the value, and the contents of the modifiers list.
     * @note This method was created instead of using .clone due to wariness about using and modifying the functionality
     * of cloneable and .clone().
     * @return A slightly-deeper-than-shallow copy of the object it's called on.
     */
    SpellEffectDefinitionModifier getCopy();
    
    /**
     * Does the same as .getCopy(), but replaces the sub modifiers in the returned object with the passed modifiers.
     * @param newModifiers The sub modifiers that the returned modifier should have, in place of the the original ones.
     * @return A copy of the object it's called on, with the modifiers replaced by the passed modifiers.
     */
    SpellEffectDefinitionModifier getCopyWithNewModifiers(List<SpellEffectDefinitionModifier> newModifiers);
}