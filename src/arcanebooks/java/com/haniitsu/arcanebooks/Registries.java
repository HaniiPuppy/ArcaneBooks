package com.haniitsu.arcanebooks;

import com.haniitsu.arcanebooks.registries.RuneDesignRegistry;
import com.haniitsu.arcanebooks.registries.SpellEffectDefinitionRegistry;
import com.haniitsu.arcanebooks.registries.SpellEffectRegistry;
import java.io.File;
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
    public SpellEffectDefinitionRegistry definitions;
    
    /**
     * The mod's registry of spell effects.
     * 
     * Spell effects are defined in-file using a combination of spell effect definitions, modifiers specific to each
     * definition, and possibly logical checks.
     */
    public SpellEffectRegistry spellEffects;
    
    /**
     * The mod's registry of visual rune designs.
     * 
     * This holds the visual design for items enclosing spell effects or spell effect modifiers, and visual designs
     * that represent them in places where they are used, such as in writing spell books, etc.
     */
    public RuneDesignRegistry runeDesigns;
    
    public void refreshRegistries()
    {
        definitions = new SpellEffectDefinitionRegistry();
        spellEffects = new SpellEffectRegistry(definitions);
        //runeDesigns = new RuneDesignRegistry(spellEffects);
    }
    
    public void load(File configDirectory)
    {
        refreshRegistries();
        definitions.loadDefaultValues();
        spellEffects.loadFromFile(new File(configDirectory, "ArcaneBooks/SpellEffects.cfg"));
        //runeDesigns.loadFromFile(new File(configDirectory, "ArcaneBooks/RuneDesigns.cfg"));
    }
}