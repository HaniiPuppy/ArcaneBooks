package com.haniitsu.arcanebooks.registries;

import com.haniitsu.arcanebooks.magic.SpellEffectDefinition;

import java.util.HashMap;
import java.util.Map;

public class SpellEffectDefinitionRegistry
{
    public SpellEffectDefinitionRegistry() {}
    
    final protected Map<String, SpellEffectDefinition> definitions = new HashMap<String, SpellEffectDefinition>();
    
    public SpellEffectDefinition getByName(String name)
    { synchronized(definitions) { return definitions.get(name); } }
    
    public SpellEffectDefinition register(SpellEffectDefinition definition)
    { synchronized(definitions) { return definitions.put(definition.getName(), definition); } }
    
    public void clear()
    { synchronized(definitions) { definitions.clear(); } }
    
    public void loadDefaultValues()
    {
        synchronized(definitions)
        {
            definitions.clear();
            
            register(DefaultDefs.logicalIf);
            register(DefaultDefs.logicalIfNot);
            
            register(DefaultDefs.activateRedstone);
            register(DefaultDefs.breakBlock);
            register(DefaultDefs.checkForMessage);
            register(DefaultDefs.clearPotionEffect);
            register(DefaultDefs.damage);
            register(DefaultDefs.detect);
            register(DefaultDefs.givePotionEffect);
            register(DefaultDefs.heal);
            register(DefaultDefs.modifyMana);
            register(DefaultDefs.particle);
            register(DefaultDefs.projectile);
            register(DefaultDefs.replaceBlock);
            register(DefaultDefs.replaceItem);
            register(DefaultDefs.setMana);
            register(DefaultDefs.shader);
            register(DefaultDefs.triggerSpell);
        }
    }
}