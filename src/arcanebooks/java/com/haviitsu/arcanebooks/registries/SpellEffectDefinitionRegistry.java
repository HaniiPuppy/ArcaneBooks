package com.haviitsu.arcanebooks.registries;

import com.haviitsu.arcanebooks.registries.magic.SpellEffectDefinition;
import java.util.HashMap;
import java.util.Map;

// Should I make this threadsafe?

public class SpellEffectDefinitionRegistry
{
    public SpellEffectDefinitionRegistry() {}
    
    Map<String, SpellEffectDefinition> definitions = new HashMap<String, SpellEffectDefinition>();
    
    public SpellEffectDefinition getByName(String name)
    { return definitions.get(name); }
    
    public void register(SpellEffectDefinition definition)
    { definitions.put(definition.getName(), definition); }
    
    public void clear()
    { definitions.clear(); }
}