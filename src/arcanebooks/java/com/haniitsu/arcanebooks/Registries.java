package com.haniitsu.arcanebooks;

import com.haniitsu.arcanebooks.registries.RuneDesignRegistry;
import com.haniitsu.arcanebooks.registries.SpellEffectDefinitionRegistry;
import com.haniitsu.arcanebooks.registries.SpellEffectRegistry;
import org.apache.commons.lang3.NotImplementedException;

public class Registries
{
    public final SpellEffectDefinitionRegistry definitions  = new SpellEffectDefinitionRegistry();
    public final SpellEffectRegistry           spellEffects = new SpellEffectRegistry();
    public final RuneDesignRegistry            runeDesigns  = new RuneDesignRegistry(spellEffects);
    
    public void load()
    {
        throw new NotImplementedException("Not implemented yet.");
    }
}