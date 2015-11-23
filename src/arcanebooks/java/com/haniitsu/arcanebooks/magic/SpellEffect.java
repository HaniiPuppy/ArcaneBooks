package com.haniitsu.arcanebooks.magic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A combination of configured spell effect definitions as defined in the config file, to be executed along with spell
 * effect modifiers, from a spell phrase. Id est, the basic, core, unmodified form of an in-game spell.
 */
public class SpellEffect
{
    /**
     * Creates a spell effect with the given name, containing the passed configured definition.
     * @param name The name of the spell effect.
     * @param definition The configured spell effect definition.
     */
    public SpellEffect(String name, ConfiguredDefinition definition)
    {
        this.name = name;
        definitions = new ArrayList<ConfiguredDefinition>();
        definitions.add(definition);
    }
    
    /**
     * Creates a spell effect with the given name, containing the passed configured definitions.
     * @param name The name of the spell effect.
     * @param definitions The configured spell effect definitions.
     */
    public SpellEffect(String name, ConfiguredDefinition... definitions)
    {
        this.name = name;
        this.definitions = new ArrayList<ConfiguredDefinition>(Arrays.asList(definitions));
    }
    
    /**
     * Creates a spell effect with the given name, containing the passed configured definitions.
     * @param name The name of the spell effect.
     * @param definitions The configured spell effect definitions.
     */
    public SpellEffect(String name, List<? extends ConfiguredDefinition> definitions)
    {
        this.name = name;
        this.definitions = new ArrayList<ConfiguredDefinition>(definitions);
    }
    
    /** The name of the spell effect. */
    final String name;
    
    /** The configured spell effect definition that define this spell effect. */
    List<ConfiguredDefinition> definitions;
    
    /**
     * Gets the name of this spell effect.
     * @return The name.
     */
    public String getName()
    { return name; }
    
    /**
     * Performs this spell effect's effect.
     * @param spellArgs The spell args representing the spell phrase cast that this spell effect should be cast under.
     */
    public void performEffect(SpellArgs spellArgs)
    {
        for(ConfiguredDefinition def : definitions)
            def.PerformEffect(spellArgs);
    }
    
    @Override
    public String toString()
    {
        String asString = name + ": ";
        
        for(ConfiguredDefinition i : definitions)
            asString += i.toString() + ", ";
        
        return asString.substring(asString.length() - 2); // Get rid of last trailing ", ".
    }
}