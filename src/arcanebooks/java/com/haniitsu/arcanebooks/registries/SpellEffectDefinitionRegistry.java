package com.haniitsu.arcanebooks.registries;

import com.haniitsu.arcanebooks.magic.SpellEffectDefinition;

import java.util.HashMap;
import java.util.Map;

/** Registry of all spell effect definitions, the actual hard-coded component of every spell. */
public class SpellEffectDefinitionRegistry
{
    /** Creates the spell effect definition registry. */
    public SpellEffectDefinitionRegistry() {}
    
    /** The spell effect definitions. */
    final protected Map<String, SpellEffectDefinition> definitions = new HashMap<String, SpellEffectDefinition>();
    
    /**
     * Gets a spell effect definition with the given name.
     * @param name The name of the spell effect definition to get.
     * @return A registered spell effect definition with the given name, or null if there is none.
     */
    public SpellEffectDefinition getByName(String name)
    { synchronized(definitions) { return definitions.get(name); } }
    
    /**
     * Registers the passed spell effect definition.
     * @note This overwrites any other registered spell effect definition with the same name. *DO NOT USE THE SAME NAME
     * FOR MULTIPLE SPELL EFFECT DEFINITIONS.* Third parties should do something like use a unique prefix, or something.
     * @param definition The definition to register.
     * @return The spell effect definition previously registered with the same name, or null if there was none.
     */
    public SpellEffectDefinition register(SpellEffectDefinition definition)
    { synchronized(definitions) { return definitions.put(definition.getName(), definition); } }
    
    /** Deregisters all spell effect definitions. */
    public void clear()
    { synchronized(definitions) { definitions.clear(); } }
    
    /** Loads the standard spell effect definitions. */
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