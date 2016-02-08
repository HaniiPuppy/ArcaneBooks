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

/** Registry for storing rune designs and matching them to spell effects and spell effect modifiers. */
public class RuneDesignRegistry
{
    /**
     * Creates the registry.
     * @param spellEffectRegistry The spell effect registry to link this to.
     */
    public RuneDesignRegistry(SpellEffectRegistry spellEffectRegistry)
    {
        sourceEffectRegistry = spellEffectRegistry;
        addDefaultModifierGetters();
    }
    
    /** The rune designs for all spell effect modifiers. */
    protected final Map<SpellEffectModifier, RuneDesign> modifierRunes = new HashMap<SpellEffectModifier, RuneDesign>();
    
    /** The rune designs for all spell effects. */
    protected final Map<SpellEffect, RuneDesign> effectRunes = new HashMap<SpellEffect, RuneDesign>();
    
    /** The spell effect registry this registry is linked to. */
    protected final SpellEffectRegistry sourceEffectRegistry;
    
    /** The getters for getting what should be all possible spell effect modifiers. */
    protected final Collection<Getter<Collection<SpellEffectModifier>>> modifierGetters = new HashSet<Getter<Collection<SpellEffectModifier>>>();
    
    
    /** The width of rune designs in points. */
    protected final int runeGridWidth = 3;
    
    /** The height of rune designs in points. */
    protected final int runeGridHeight = 3;
    
    /** The minimum number of lines per rune design */
    protected final int minNumberOfRuneNodes = 3;
    
    /** The maximum number of lines per rune design. */
    protected final int maxNumberOfRuneNodes = 4;
    
    /**
     * Gets the rune design registered for the passed spell effect modifier.
     * @param modifier The modifier to get the rune design for.
     * @return The rune design for the given modifier.
     */
    public RuneDesign get(SpellEffectModifier modifier)
    { return modifierRunes.get(modifier); }
    
    /**
     * Gets the rune design registered for the passed spell effect.
     * @param effect The spell effect to get the rune design for.
     * @return The rune design for the given spell effect.
     */
    public RuneDesign get(SpellEffect effect)
    { return effectRunes.get(effect); }
    
    /**
     * Associates a spell effect modifier with the passed rune design.
     * @param modifier The modifier to have a rune design.
     * @param rune The rune design to associate with the passed modifier.
     * @return The previously associated rune design for that modifier, or null if there was none.
     */
    public RuneDesign register(SpellEffectModifier modifier, RuneDesign rune)
    { return modifierRunes.put(modifier, rune); }
    
    /**
     * Associates a spell effect with the passed rune design.
     * @param effect The spell effect to have a rune design.
     * @param rune The rune design to associate with the passed spell effect.
     * @return The previously associated rune design for that spell effect, or null if there was none.
     */
    public RuneDesign register(SpellEffect effect, RuneDesign rune)
    { return effectRunes.put(effect, rune); }
    
    /**
     * Disassociates any rune design with the passed spell effect modifier.
     * @param modifier The modifier to disassociate any rune design from.
     * @return The previously associated rune design for that modifier, or null if there was none.
     */
    public RuneDesign deregister(SpellEffectModifier modifier)
    { return modifierRunes.remove(modifier); }
    
    /**
     * Disassociates any rune design with the passed spell effect.
     * @param effect The spell effect to disassociate any rune design from.
     * @return The previously associated rune design for that spell effect, or null if there was none.
     */
    public RuneDesign deregister(SpellEffect effect)
    { return effectRunes.remove(effect); }
    
    /** Disassociates all rune designs from all spell effects and spell effect modifiers. */
    public void clear()
    {
        modifierRunes.clear();
        effectRunes.clear();
    }
    
    /** Randomly assigns rune designs to all spell effects and spell effect modifiers, overwriting any already
     * registered. */
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
    
    /**
     * Randomly assigns rune designs to all spell effects and spell effect modifiers that don't already have rune
     * designs associated with them.
     */
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
    
    /**
     * Creates a random rune design and associates it with the passed spell effect modifier.
     * @param modifier The modifier to associate a random rune design with.
     * @return The rune design previously associated with the passed spell effect modifier.
     */
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
    
    /**
     * Creates a random rune design and associates it with the passed spell effect.
     * @param effect The modifier to associate a random rune design with.
     * @return The rune design previously associated with the passed spell effect.
     */
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
    
    /**
     * Adds a way of accessing more spell effect modifiers.
     * @param getter The enclosed method for accessing a collection of spell effect modifiers.
     */
    public void addModifierGetter(Getter<Collection<SpellEffectModifier>> getter)
    { modifierGetters.add(getter); }
    
    /** Adds the default ways of accessing the standard spell effect modifiers. */
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