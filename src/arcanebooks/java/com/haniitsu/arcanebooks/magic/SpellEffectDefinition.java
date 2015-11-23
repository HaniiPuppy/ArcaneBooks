package com.haniitsu.arcanebooks.magic;

import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import java.util.List;

/**
 * The hard-coded meaty chunks of a spell. The encapsulated code that actually does things that can be manipulated into
 * spell effects and spells.
 */
public abstract class SpellEffectDefinition
{
    /**
     * Creates a spell effect definition with the given name.
     * @param name The name of the spell effect definition.
     */
    public SpellEffectDefinition(String name)
    { this.name = name; }
    
    /** The spell effect definition's name. */
    String name;
    
    /**
     * Gets the spell effect definition's name.
     * @return The name.
     */
    public String getName()
    { return name; }
    
    /**
     * Performs the spell effect definition's code, as part of the spell phrase cast represented by the passed
     * SpellArgs object, with the passed spell effect definition modifiers.
     * @param spellArgs The spell args object representing the spell phrase cast that this execution should be a part
     * of.
     * @param defModifiers The spell effect definition modifiers contained by the spell effect being cast.
     */
    public abstract void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers);
}