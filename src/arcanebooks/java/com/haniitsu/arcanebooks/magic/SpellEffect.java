package com.haniitsu.arcanebooks.magic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpellEffect
{
    public SpellEffect(String name, ConfiguredDefinition definition)
    {
        this.name = name;
        definitions = new ArrayList<ConfiguredDefinition>();
        definitions.add(definition);
    }
    
    public SpellEffect(String name, ConfiguredDefinition... definitions)
    {
        this.name = name;
        this.definitions = new ArrayList<ConfiguredDefinition>(Arrays.asList(definitions));
    }
    
    public SpellEffect(String name, List<? extends ConfiguredDefinition> definitions)
    {
        this.name = name;
        this.definitions = new ArrayList<ConfiguredDefinition>(definitions);
    }
    
    final String name;
    List<ConfiguredDefinition> definitions;
    
    public String getName()
    { return name; }
    
    public void performEffect(SpellArgs spellArgs)
    {
        for(ConfiguredDefinition def : definitions)
            def.PerformEffect(spellArgs);
    }
}