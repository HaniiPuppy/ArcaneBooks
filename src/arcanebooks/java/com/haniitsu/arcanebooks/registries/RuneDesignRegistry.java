package com.haniitsu.arcanebooks.registries;

import com.haniitsu.arcanebooks.magic.SpellEffect;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOE;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOEShape;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOESize;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellEffectModifier;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellStrength;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellTarget;
import com.haniitsu.arcanebooks.misc.Getter;
import com.haniitsu.arcanebooks.runes.RuneDesign;
import com.haniitsu.arcanebooks.runes.RuneDesignBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

// TO DO: Add a way of ensuring that different rune designs used here don't have the same node-connections/lines, but
//        in a different order.

public class RuneDesignRegistry
{
    public RuneDesignRegistry(SpellEffectRegistry spellEffectRegistry)
    {
        sourceEffectRegistry = spellEffectRegistry;
        addDefaultModifierGetters();
    }
    
    protected final Map<SpellEffectModifier, RuneDesign> modifierRunes = new HashMap<SpellEffectModifier, RuneDesign>();
    protected final Map<SpellEffect, RuneDesign> effectRunes = new HashMap<SpellEffect, RuneDesign>();
    
    protected final SpellEffectRegistry sourceEffectRegistry;
    protected final Collection<Getter<Collection<SpellEffectModifier>>> modifierGetters = new HashSet<Getter<Collection<SpellEffectModifier>>>();
    
    protected final int runeGridWidth = 3;
    protected final int runeGridHeight = 3;
    protected final int minNumberOfRuneNodes = 3;
    protected final int maxNumberOfRuneNodes = 4;
    
    public RuneDesign get(SpellEffectModifier modifier)
    { return modifierRunes.get(modifier); }
    
    public RuneDesign get(SpellEffect effect)
    { return effectRunes.get(effect); }
    
    public RuneDesign register(SpellEffectModifier modifier, RuneDesign rune)
    { return modifierRunes.put(modifier, rune); }
    
    public RuneDesign register(SpellEffect effect, RuneDesign rune)
    { return effectRunes.put(effect, rune); }
    
    public RuneDesign deregister(SpellEffectModifier modifier)
    { return modifierRunes.remove(modifier); }
    
    public RuneDesign deregister(SpellEffect effect)
    { return effectRunes.remove(effect); }
    
    public void clear()
    {
        modifierRunes.clear();
        effectRunes.clear();
    }
    
    public void randomlyAssignAll()
    {
        clear();
        
        for(SpellEffect effect : sourceEffectRegistry.getEffects())
        {
            RuneDesign rune;
            
            do
            {
                rune = new RuneDesignBuilder(runeGridWidth, runeGridHeight)
                            .addRandomLines(minNumberOfRuneNodes, maxNumberOfRuneNodes)
                            .flipVertically(true)
                            .make();
            }
            while(effectRunes.containsValue(rune));
            
            register(effect, rune);
        }
        
        for(Getter<Collection<SpellEffectModifier>> getter : modifierGetters)
            for(SpellEffectModifier modifier : getter.get())
            {
                RuneDesign rune;
                
                do
                {
                    rune = new RuneDesignBuilder(runeGridWidth, runeGridHeight)
                                .addRandomLines(minNumberOfRuneNodes, maxNumberOfRuneNodes)
                                .make();
                }
                while(modifierRunes.containsValue(rune));
                
                register(modifier, rune);
            }
    }
    
    public void randomlyAssignRest()
    {
        for(SpellEffect effect : sourceEffectRegistry.getEffects())
        {
            if(effectRunes.containsKey(effect))
                continue;
            
            RuneDesign rune;
            
            do
            {
                rune = new RuneDesignBuilder(runeGridWidth, runeGridHeight)
                            .addRandomLines(minNumberOfRuneNodes, maxNumberOfRuneNodes)
                            .flipVertically(true)
                            .make();
            }
            while(effectRunes.containsValue(rune));
            
            register(effect, rune);
        }
        
        for(Getter<Collection<SpellEffectModifier>> getter : modifierGetters)
            for(SpellEffectModifier modifier : getter.get())
            {
                if(modifierRunes.containsKey(modifier))
                    continue;
                
                RuneDesign rune;
                
                do
                {
                    rune = new RuneDesignBuilder(runeGridWidth, runeGridHeight)
                                .addRandomLines(minNumberOfRuneNodes, maxNumberOfRuneNodes)
                                .make();
                }
                while(modifierRunes.containsValue(rune));
                
                register(modifier, rune);
            }
    }
    
    public RuneDesign randomlyAssign(SpellEffectModifier modifier)
    {
        RuneDesign rune;
        
        do
        {
            rune = new RuneDesignBuilder(runeGridWidth, runeGridHeight)
                        .addRandomLines(minNumberOfRuneNodes, maxNumberOfRuneNodes)
                        .make();
        }
        while(modifierRunes.containsValue(rune));
        
        register(modifier, rune);
        return rune;
    }
    
    public RuneDesign randomlyAssign(SpellEffect effect)
    {
        RuneDesign rune;
        
        do
        {
            rune = new RuneDesignBuilder(runeGridWidth, runeGridHeight)
                        .addRandomLines(minNumberOfRuneNodes, maxNumberOfRuneNodes)
                        .flipVertically(true)
                        .make();
        }
        while(effectRunes.containsValue(rune));
        
        register(effect, rune);
        return rune;
    }
    
    public void addModifierGetter(Getter<Collection<SpellEffectModifier>> getter)
    { modifierGetters.add(getter); }
    
    protected void addDefaultModifierGetters()
    {
        modifierGetters.add(new Getter<Collection<SpellEffectModifier>>()
        {
            @Override
            public Collection<SpellEffectModifier> get()
            { return new ArrayList<SpellEffectModifier>(AOE.getValues()); }
        });
        
        modifierGetters.add(new Getter<Collection<SpellEffectModifier>>()
        {
            @Override
            public Collection<SpellEffectModifier> get()
            { return new ArrayList<SpellEffectModifier>(AOESize.getValues()); }
        });
        
        modifierGetters.add(new Getter<Collection<SpellEffectModifier>>()
        {
            @Override
            public Collection<SpellEffectModifier> get()
            { return new ArrayList<SpellEffectModifier>(AOEShape.getValues()); }
        });
        
        modifierGetters.add(new Getter<Collection<SpellEffectModifier>>()
        {
            @Override
            public Collection<SpellEffectModifier> get()
            { return new ArrayList<SpellEffectModifier>(SpellStrength.getValues()); }
        });
        
        modifierGetters.add(new Getter<Collection<SpellEffectModifier>>()
        {
            @Override
            public Collection<SpellEffectModifier> get()
            { return new ArrayList<SpellEffectModifier>(SpellTarget.getValues()); }
        });
    }
}