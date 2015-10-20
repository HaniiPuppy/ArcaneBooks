package com.haniitsu.arcanebooks.registries.magic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpellEffect
{
    public SpellEffect(ConfiguredDefinition definition)
    {
        definitions = new ArrayList<ConfiguredDefinition>();
        definitions.add(definition);
    }
    
    public SpellEffect(ConfiguredDefinition... definitions)
    { this.definitions = new ArrayList<ConfiguredDefinition>(Arrays.asList(definitions)); }
    
    public SpellEffect(List<? extends ConfiguredDefinition> definitions)
    { this.definitions = new ArrayList<ConfiguredDefinition>(definitions); }
    
    List<ConfiguredDefinition> definitions;
    
//    public void PerformEffect(SpellArgs spellArgs)
//    {
//        spellArgs.setDefinitionModifiers(definitionModifiers);
//        definition.PerformEffect(spellArgs);
//    }
    
    public void PerformEffect(SpellArgs spellArgs)
    {
        for(ConfiguredDefinition def : definitions)
            def.PerformEffect(spellArgs);
    }
}