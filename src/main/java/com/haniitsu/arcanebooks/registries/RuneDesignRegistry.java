package com.haniitsu.arcanebooks.registries;

import com.haniitsu.arcanebooks.magic.SpellEffect;
import com.haniitsu.arcanebooks.magic.SpellWord;
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
    
    /** The rune designs for all spell words. (e.g. spell effects, spell effect modifiers) */
    protected final Map<SpellWord, RuneDesign> runeDesigns = new HashMap<SpellWord, RuneDesign>();
    
    /** The spell effect registry this registry is linked to. */
    protected final SpellEffectRegistry sourceEffectRegistry;
    
    /** The getters for getting what should be all possible spell effect modifiers. */
    protected final Collection<Getter<Collection<SpellEffectModifier>>> modifierGetters = new HashSet<Getter<Collection<SpellEffectModifier>>>();
    
    
    /** The width of rune designs in points. */
    protected final int runeGridWidth = 4;
    
    /** The height of rune designs in points. */
    protected final int runeGridHeight = 4;
    
    /**
     * Gets the rune design registered for the passed spell word.
     * @param spellWord The spell word (e.g. SpellEffect, SpellEffectModifier, etc.) to get the rune design for.
     * @return The rune design for the given spell word.
     */
    public RuneDesign get(SpellWord spellWord)
    { return runeDesigns.get(spellWord); }
    
    /**
     * Associates a spell word with the passed rune design.
     * @param spellWord The spell word (e.g. spell effect, spell effect modifier, etc.) to have a rune design.
     * @param rune The rune design to associate with the passed spell word.
     * @return The previously associated rune design for that spell word, or null if there was none.
     */
    public RuneDesign register(SpellWord spellWord, RuneDesign rune)
    { return runeDesigns.put(spellWord, rune); }
    
    /**
     * Disassociates any rune design with the passed spell word.
     * @param spellWord The spell word to disassociate any rune designs from.
     * @return The rune design now previously associated with the passed spell word.
     */
    public RuneDesign deregister(SpellWord spellWord)
    { return runeDesigns.remove(spellWord); }
    
    /** Disassociates all rune designs from all spell effects and spell effect modifiers. */
    public void clear()
    { runeDesigns.clear(); }
    
    /** Randomly assigns rune designs to all spell effects and spell effect modifiers, overwriting any already
     * registered. */
    public void randomlyAssignAll()
    {
        clear();
        randomlyAssignRest();
    }
    
    /**
     * Randomly assigns rune designs to all spell effects and spell effect modifiers that don't already have rune
     * designs associated with them.
     */
    public void randomlyAssignRest()
    {
        for(SpellEffect effect : sourceEffectRegistry.getEffects())
            if(!runeDesigns.containsKey(effect))
                randomlyAssign(effect);
        
        for(Getter<Collection<SpellEffectModifier>> getter : modifierGetters)
            for(SpellEffectModifier modifier : getter.get())
                if(!runeDesigns.containsKey(modifier))
                    randomlyAssign(modifier);
    }
    
    /**
     * Randomly assigns a unique rune design to the passed spell word.
     * @param spellWord The spell word to assign a rune design to.
     * @return The random rune design assigned to the passed spell word.
     */
    public RuneDesign randomlyAssign(SpellWord spellWord)
    {
        RuneDesign rune = spellWord instanceof SpellEffect ? generateUniqueRuneDesignForSpellEffect()
                                                           : generateUniqueRuneDesignForSpellEffectModifier();

        runeDesigns.put(spellWord, rune);
        return rune;
    }
    
    /**
     * Adds a way of accessing more spell effect modifiers.
     * @param getter The enclosed method for accessing a collection of spell effect modifiers.
     */
    public void addModifierGetter(Getter<Collection<SpellEffectModifier>> getter)
    { modifierGetters.add(getter); }
    
    /** Adds the default ways of accessing the standard spell effect modifiers. */
    protected final void addDefaultModifierGetters()
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
    
    /**
     * Randomly generates a rune design for a spell effect that isn't currently used.
     * @return A random, unique rune design for a spell effect.
     */
    private RuneDesign generateUniqueRuneDesignForSpellEffect()
    {
        RuneDesign rune;
        
        do rune = generateRuneDesignForSpellEffect();
        while(runeDesigns.containsValue(rune));
        
        return rune;
    }
    
    /**
     * Randomly generates a rune design for a spell effect modifier that isn't currently used.
     * @return A random, unique rune design for a spell effect modifier.
     */
    private RuneDesign generateUniqueRuneDesignForSpellEffectModifier()
    {
        RuneDesign rune;
        
        do rune = generateRuneDesignForSpellEffectModifier();
        while(runeDesigns.containsValue(rune));
        
        return rune;
    }
    
    /**
     * Randomly generates a rune design for a spell effect.
     * @return A random rune design for a spell effect.
     */
    protected RuneDesign generateRuneDesignForSpellEffect()
    { return new RuneDesignBuilder(runeGridWidth, runeGridHeight).addRandomLines(5).make(); }
    
    /**
     * Randomly generates a rune design for a spell effect modifier.
     * @return A random rune design for a spell effect modifier.
     */
    protected RuneDesign generateRuneDesignForSpellEffectModifier()
    { return new RuneDesignBuilder(runeGridWidth, runeGridHeight).addRandomLines(3).make(); }
}