package com.haniitsu.arcanebooks;

import com.haniitsu.arcanebooks.registries.RuneDesignRegistry;
import com.haniitsu.arcanebooks.registries.SpellEffectDefinitionRegistry;
import com.haniitsu.arcanebooks.registries.SpellEffectRegistry;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Holds the set of data registries specific to this mod.
 */
public class Registries
{
    /**
     * The mod's registry of spell effect definitions.
     * 
     * Definitions are the active component of spell effects.
     */
    public final SpellEffectDefinitionRegistry definitions  = new SpellEffectDefinitionRegistry();
    
    /**
     * The mod's registry of spell effects.
     * 
     * Spell effects are defined in-file using a combination of spell effect definitions, modifiers specific to each
     * definition, and possibly logical checks.
     */
    public final SpellEffectRegistry spellEffects = new SpellEffectRegistry(definitions);
    
    /**
     * The mod's registry of visual rune designs.
     * 
     * This holds the visual design for items enclosing spell effects or spell effect modifiers, and visual designs
     * that represent them in places where they are used, such as in writing spell books, etc.
     */
    public final RuneDesignRegistry runeDesigns  = new RuneDesignRegistry(spellEffects);
    
    /** Populates all data registries, mostly from files. */
    public void load()
    {
        throw new NotImplementedException("Not implemented yet.");
    }
}