package com.haniitsu.arcanebooks.registries;

import com.haniitsu.arcanebooks.magic.SpellEffect;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpellEffectRegistry
{
    public SpellEffectRegistry() {}
    
    final protected Map<String, SpellEffect> effects = new HashMap<String, SpellEffect>();
    
    public SpellEffect getEffect(String name)
    { synchronized(effects) { return effects.get(name); } }
    
    public Collection<SpellEffect> getEffects()
    { return new ArrayList<SpellEffect>(effects.values()); }
    
    public SpellEffect register(SpellEffect effect)
    { synchronized(effects) { return effects.put(effect.getName(), effect); } }
    
    public void clear()
    { synchronized(effects) { effects.clear(); } }
    
    public void loadDefaultValues()
    {
        synchronized(effects)
        {
            effects.clear();
            
            // register effect
            // register effect
            // register effect
            // etc.
        }
    }
}