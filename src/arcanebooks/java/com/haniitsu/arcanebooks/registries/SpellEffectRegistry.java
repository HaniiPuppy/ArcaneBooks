package com.haniitsu.arcanebooks.registries;

import com.haniitsu.arcanebooks.magic.SpellEffect;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;

public class SpellEffectRegistry
{
    public SpellEffectRegistry() {}
    
    final protected Map<String, SpellEffect> effects = new HashMap<String, SpellEffect>();
    final protected Map<String, String> backloggedEffects = new HashMap<String, String>();
    
    public SpellEffect getEffect(String name)
    { synchronized(effects) { return effects.get(name); } }
    
    public Collection<SpellEffect> getEffects()
    { return new ArrayList<SpellEffect>(effects.values()); }
    
    public void register(SpellEffect effect)
    { synchronized(effects) { effects.put(effect.getName(), effect); } }
    
    public void parseEffect(String name, String effect)
    {
        throw new NotImplementedException("Not implemented yet.");
    }
    
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
    
    public void LoadFromFile(File file)
    {
        throw new NotImplementedException("Not implemented yet.");
    }
}