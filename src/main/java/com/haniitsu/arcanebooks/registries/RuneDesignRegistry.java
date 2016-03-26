package com.haniitsu.arcanebooks.registries;

import com.google.common.collect.ImmutableMap;
import com.haniitsu.arcanebooks.magic.SpellEffect;
import com.haniitsu.arcanebooks.magic.SpellWord;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOE;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOEShape;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOESize;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellEffectModifier;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellStrength;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellTarget;
import com.haniitsu.arcanebooks.misc.Getter;
import com.haniitsu.arcanebooks.misc.events.BasicEvent;
import com.haniitsu.arcanebooks.misc.events.Event;
import com.haniitsu.arcanebooks.misc.events.args.BasicEventArgs;
import com.haniitsu.arcanebooks.runes.RuneDesign;
import com.haniitsu.arcanebooks.runes.RuneDesignBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

// TO DO: Add a way of ensuring that different rune designs used here don't have the same node-connections/lines, but
//        in a different order.

/** Registry for storing rune designs and matching them to spell effects and spell effect modifiers. */
public class RuneDesignRegistry
{
    /** Event args for when rune designs are associated with spell words in this registry. */
    public static class RuneDesignsAddedArgs extends BasicEventArgs
    {
        /**
         * Creates a new instance with a single rune design added.
         * @param spellWord The spell word associated with a new rune design.
         * @param runeDesign The rune design associated with the spell word.
         */
        public RuneDesignsAddedArgs(final SpellWord spellWord, final RuneDesign runeDesign)
        { this(ImmutableMap.of(spellWord, runeDesign)); }
        
        /**
         * Creates a new instance with multiple rune designs added.
         * @param added A map of the spell words and rune designs added.
         */
        public RuneDesignsAddedArgs(Map<SpellWord, RuneDesign> added)
        { this(added, false, false); }
        
        /**
         * Creates a new instance with multiple rune designs added.
         * @param added A map of the spell words and rune designs added.
         * @param addedAll Whether or not rune designs were added for all possible spell words in this case.
         * @param clearedExisting Whether or not all existing spell word/rune design associated were removed prior to
         * adding.
         */
        public RuneDesignsAddedArgs(Map<SpellWord, RuneDesign> added, boolean addedAll, boolean clearedExisting)
        {
            this.added = Collections.unmodifiableMap(added);
            this.addedAll = addedAll;
            this.clearedExisting = clearedExisting;
        }
        
        /** A map of the spell words and rune designs added. */
        final protected Map<SpellWord, RuneDesign> added;
        
        /** Whether or not rune designs were added for all possible spell words in this case. */
        final protected boolean addedAll;
        
        /** Whether or not all existing spell word/rune design associated were removed prior to adding. */
        final protected boolean clearedExisting;
        
        /**
         * Gets the rune designs added.
         * @return A map containing the spell words that have had rune designs added to them, and the rune designs added
         * to them.
         */
        public Map<SpellWord, RuneDesign> getDesignsAdded()
        { return added; }
        
        /**
         * Gets whether or not all possible spell words had rune designs added to them in this case.
         * @return True if they did. Otherwise, false.
         */
        public boolean addedAll()
        { return addedAll; }
        
        /**
         * Gets whether or not all existing spell word/rune design associated were removed prior to adding.
         * @return True if they were cleared. Otherwise, false.
         */
        public boolean clearedExisting()
        { return clearedExisting; }
    }
    
    /** Event args for when rune designs are disassociated with spell words. */
    public static class RuneDesignsRemovedArgs extends BasicEventArgs
    {
        /**
         * Creates an instance where a single spell word and rune design are removed.
         * @param spellWord The spell word removed.
         * @param runeDesign The rune design removed.
         */
        public RuneDesignsRemovedArgs(SpellWord spellWord, RuneDesign runeDesign)
        { this(ImmutableMap.of(spellWord, runeDesign)); }
        
        /**
         * Creates an instance where multiple spell words and rune designs are removed.
         * @param removed A map containing the spell words removed with the rune designs removed as values.
         */
        public RuneDesignsRemovedArgs(Map<SpellWord, RuneDesign> removed)
        { this(removed, false); }
        
        /**
         * Creates an instance where multiple spell words and rune designs are removed.
         * @param removed A map containing the spell words removed with the rune designs removed as values.
         * @param clearedAll Whether or not the entire registry was cleared.
         */
        public RuneDesignsRemovedArgs(Map<SpellWord, RuneDesign> removed, boolean clearedAll)
        {
            this.removed = Collections.unmodifiableMap(removed);
            this.clearedAll = clearedAll;
        }
        
        /** A map containing the spell words removed with the rune designs removed as values. */
        final protected Map<SpellWord, RuneDesign> removed;
        
        /** Whether or not the entire registry was cleared. */
        final protected boolean clearedAll;
        
        /**
         * Gets the spell words and rune designs disassociated in this case.
         * @return A map with the spell words concerned as the keys, and the rune designs removed as the values.
         */
        public Map<SpellWord, RuneDesign> getDesignsRemoved()
        { return removed; }
        
        /**
         * Whether or not the registry had all values removed.
         * @return True if all values were removed. Otherwise, false.
         */
        public boolean clearedAll()
        { return clearedAll; }
    }
    
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
    
    
    /** When rune designs are associated with spell words in this registry. */
    public final Event<RuneDesignsAddedArgs> itemsAdded = new BasicEvent<RuneDesignsAddedArgs>();
    
    /** When rune designs are disassociated with spell words in this registry. */
    public final Event<RuneDesignsRemovedArgs> itemsRemoved = new BasicEvent<RuneDesignsRemovedArgs>();
    
    /**
     * Gets the rune design registered for the passed spell word.
     * @param spellWord The spell word (e.g. SpellEffect, SpellEffectModifier, etc.) to get the rune design for.
     * @return The rune design for the given spell word.
     */
    public RuneDesign get(SpellWord spellWord)
    { synchronized(runeDesigns) { return runeDesigns.get(spellWord); } }
    
    /**
     * Associates a spell word with the passed rune design.
     * @param spellWord The spell word (e.g. spell effect, spell effect modifier, etc.) to have a rune design.
     * @param rune The rune design to associate with the passed spell word.
     * @return The previously associated rune design for that spell word, or null if there was none.
     */
    public RuneDesign register(SpellWord spellWord, RuneDesign rune)
    {
        RuneDesign previous;
        
        synchronized(runeDesigns)
        { previous = runeDesigns.put(spellWord, rune); }
        
        itemsAdded.raise(this, new RuneDesignsAddedArgs(spellWord, rune));
        return previous;
    }
    
    /**
     * Disassociates any rune design with the passed spell word.
     * @param spellWord The spell word to disassociate any rune designs from.
     * @return The rune design now previously associated with the passed spell word.
     */
    public RuneDesign deregister(SpellWord spellWord)
    {
        RuneDesign previous;
        
        synchronized(runeDesigns)
        { previous = runeDesigns.remove(spellWord); }
        
        itemsRemoved.raise(this, new RuneDesignsRemovedArgs(spellWord, previous));
        return previous;
    }
    
    /** Disassociates all rune designs from all spell effects and spell effect modifiers. */
    public void clear()
    {
        RuneDesignsRemovedArgs args = new RuneDesignsRemovedArgs(new HashMap<SpellWord, RuneDesign>(runeDesigns), true);
        
        synchronized(runeDesigns)
        { runeDesigns.clear(); }
        
        itemsRemoved.raise(this, args);
    }
    
    /**
     * Randomly assigns rune designs to all spell effects and spell effect modifiers that don't already have rune
     * designs associated with them.
     */
    public void randomlyAssignRest()
    { randomlyAssignAll(false); }
    
    /** Randomly assigns rune designs to all spell effects and spell effect modifiers, overwriting any already
     * registered. */
    public void randomlyAssignAll()
    { randomlyAssignAll(true); }
    
    /**
     * Randomly assigns rune designs to all spell effects and spell effect modifiers that don't already have rune
     * designs associated with them, clearing all rune designs associations first if clearFirst is true.
     * @param clearFirst Whether or not the clear all existing rune design associations first.
     */
    protected void randomlyAssignAll(boolean clearFirst)
    {
        Collection<Getter<Collection<SpellEffectModifier>>> getters;
        
        synchronized(modifierGetters)
        { getters = new ArrayList<Getter<Collection<SpellEffectModifier>>>(modifierGetters); }
        
        RuneDesignsRemovedArgs removedArgs = null;
        RuneDesignsAddedArgs addedArgs;
        
        if(clearFirst)
            removedArgs = new RuneDesignsRemovedArgs(runeDesigns, true);
        
        Map<SpellWord, RuneDesign> added = new HashMap<SpellWord, RuneDesign>();
        
        synchronized(runeDesigns)
        {
            if(clearFirst)
                runeDesigns.clear();
            
            for(SpellEffect effect : sourceEffectRegistry.getEffects())
                if(!runeDesigns.containsKey(effect))
                {
                    RuneDesign rune = generateUniqueRuneDesignForSpellEffect();
                    added.put(effect, rune);
                    runeDesigns.put(effect, rune);
                }

            for(Getter<Collection<SpellEffectModifier>> getter : getters)
                for(SpellEffectModifier modifier : getter.get())
                    if(!runeDesigns.containsKey(modifier))
                    {
                        RuneDesign rune = generateRuneDesignForSpellEffectModifier();
                        added.put(modifier, rune);
                        runeDesigns.put(modifier, rune);
                    }
        }
        
        if(clearFirst)
            itemsRemoved.raise(this, removedArgs);
        
        itemsAdded.raise(this, new RuneDesignsAddedArgs(added, true, clearFirst));
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

        synchronized(runeDesigns)
        { runeDesigns.put(spellWord, rune); }
        
        itemsAdded.raise(this, new RuneDesignsAddedArgs(spellWord, rune));
        return rune;
    }
    
    /**
     * Adds a way of accessing more spell effect modifiers.
     * @param getter The enclosed method for accessing a collection of spell effect modifiers.
     */
    public void addModifierGetter(Getter<Collection<SpellEffectModifier>> getter)
    {
        synchronized(modifierGetters)
        { modifierGetters.add(getter); }
    }
    
    /** Adds the default ways of accessing the standard spell effect modifiers. */
    protected final void addDefaultModifierGetters()
    {
        synchronized(modifierGetters)
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
    
    /**
     * Randomly generates a rune design for a spell effect that isn't currently used.
     * @return A random, unique rune design for a spell effect.
     */
    private RuneDesign generateUniqueRuneDesignForSpellEffect()
    {
        RuneDesign rune;
        
        synchronized(runeDesigns)
        {
            do rune = generateRuneDesignForSpellEffect();
            while(runeDesigns.containsValue(rune));
        }
        
        return rune;
    }
    
    /**
     * Randomly generates a rune design for a spell effect modifier that isn't currently used.
     * @return A random, unique rune design for a spell effect modifier.
     */
    private RuneDesign generateUniqueRuneDesignForSpellEffectModifier()
    {
        RuneDesign rune;
        
        synchronized(runeDesigns)
        {
            do rune = generateRuneDesignForSpellEffectModifier();
            while(runeDesigns.containsValue(rune));
        }
        
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