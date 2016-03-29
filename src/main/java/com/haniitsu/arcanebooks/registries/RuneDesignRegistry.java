package com.haniitsu.arcanebooks.registries;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Doubles;
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
import com.haniitsu.arcanebooks.misc.geometry.Line;
import com.haniitsu.arcanebooks.misc.geometry.PointInt2d;
import com.haniitsu.arcanebooks.runes.RuneDesign;
import com.haniitsu.arcanebooks.runes.RuneDesignBuilder;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        { this(added, new HashMap<String, RuneDesign>(), addedAll, clearedExisting); }
        
        /**
         * Creates a new instance with multiple rune designs added and backlogged.
         * @param added A map of the spell words and rune designs added.
         * @param backlogged A map of the names of spell effects and the rune designs backlogged.
         */
        public RuneDesignsAddedArgs(Map<SpellWord, RuneDesign> added, Map<String, RuneDesign> backlogged)
        { this(added, backlogged, false, false); }
        
        /**
         * Creates a new instance with multiple rune designs added and backlogged.
         * @param added A map of the spell words and rune designs added.
         * @param backlogged A map of the names of spell effects and the rune designs backlogged.
         * @param addedAll Whether or not rune designs were added for all possible spell words in this case.
         * @param clearedExisting Whether or not all existing spell word/rune design associated were removed prior to
         * adding.
         */
        public RuneDesignsAddedArgs(Map<SpellWord, RuneDesign> added, Map<String, RuneDesign> backlogged, boolean addedAll, boolean clearedExisting)
        {
            this.added = Collections.unmodifiableMap(added);
            this.addedToBacklog = Collections.unmodifiableMap(backlogged);
            this.addedAll = addedAll;
            this.clearedExisting = clearedExisting;
        }
        
        /** A map of the spell words and rune designs added. */
        final protected Map<SpellWord, RuneDesign> added;
        
        /** A map of the names of spell effects */
        final protected Map<String, RuneDesign> addedToBacklog;
        
        /** Whether or not rune designs were added for all possible spell words in this case. */
        final protected boolean addedAll;
        
        /** Whether or not all existing spell word/rune design associated were removed prior to adding. */
        final protected boolean clearedExisting;
        
        /**
         * Gets the rune designs added, but not active, as the spell effects they're for aren't active in the attached
         * spell effect registry yet.
         * @return A map of the names of the spell effects and the runedesigns for them.
         */
        public Map<String, RuneDesign> getDesignsBacklogged()
        { return addedToBacklog; }
        
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
        { this(removed, new HashMap<String, RuneDesign>(), clearedAll); }
        
        /**
         * Creates an instance where multiple spell words and rune designs are removed.
         * @param removed A map containing the spell words removed with the rune designs removed as values.
         * @param backlogRemoved A map containing the names of inactive spell effects removed with the rune designs removed as values.
         */
        public RuneDesignsRemovedArgs(Map<SpellWord, RuneDesign> removed, Map<String, RuneDesign> backlogRemoved)
        { this(removed, backlogRemoved, false); }
        
        /**
         * Creates an instance where multiple spell words and rune designs are removed.
         * @param removed A map containing the spell words removed with the rune designs removed as values.
         * @param backlogRemoved A map containing the names of inactive spell effects removed with the rune designs removed as values.
         * @param clearedAll Whether or not the entire registry was cleared.
         */
        public RuneDesignsRemovedArgs(Map<SpellWord, RuneDesign> removed, Map<String, RuneDesign> backlogRemoved, boolean clearedAll)
        {
            this.removed = Collections.unmodifiableMap(removed);
            this.backlogRemoved = Collections.unmodifiableMap(backlogRemoved);
            this.clearedAll = clearedAll;
        }
        
        /** A map containing the spell words removed with the rune designs removed as values. */
        final protected Map<SpellWord, RuneDesign> removed;
        
        /** A map containing the names of inactive spell effects removed with the rune designs removed as values. */
        final protected Map<String, RuneDesign> backlogRemoved;
        
        /** Whether or not the entire registry was cleared. */
        final protected boolean clearedAll;
        
        /**
         * Gets the spell words and rune designs disassociated in this case.
         * @return A map with the spell words concerned as the keys, and the rune designs removed as the values.
         */
        public Map<SpellWord, RuneDesign> getDesignsRemoved()
        { return removed; }
        
        /**
         * Gets the name of the spell effects and their potential rune designs disassociated in this case.
         * @return A map with the names of the concerned inactive spell effects removed with the rune designs removed
         * as the values.
         */
        public Map<String, RuneDesign> getBackloggedDesignsRemoved()
        { return backlogRemoved; }
        
        /**
         * Whether or not the registry had all values removed.
         * @return True if all values were removed. Otherwise, false.
         */
        public boolean clearedAll()
        { return clearedAll; }
    }
    
    /** Event args for when backlogged spell effects are un-backlogged. */
    public static class RuneDesignsBacklogClearedArgs extends BasicEventArgs
    {  }
    
    private static class BackloggedSpellEffect implements SpellWord
    {
        public BackloggedSpellEffect(String nameOfSpellEffectBacklogged)
        { this.name = nameOfSpellEffectBacklogged; }
        
        protected final String name;
        
        public String getName()
        { return name; }
    }
    
    protected static class SpellWordRuneDesignPair
    {
        public SpellWordRuneDesignPair(SpellWord word, RuneDesign design)
        {
            this.word = word;
            this.design = design;
        }
        
        protected SpellWord word;
        protected RuneDesign design;
        
        public SpellWord getWord()
        { return word; }
        
        public RuneDesign getDesign()
        { return design; }
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
    
    /** The rune designs made for spell effects that aren't yet accessible from the referenced spell effect registry. */
    protected final Map<String, RuneDesign> backloggedSpellEffectRuneDesigns = new HashMap<String, RuneDesign>();
    
    /** The spell effect registry this registry is linked to. */
    protected final SpellEffectRegistry sourceEffectRegistry;
    
    /** The getters for getting what should be all possible spell effect modifiers. */
    protected final Map<String, Getter<Collection<SpellEffectModifier>>> modifierGetters = new HashMap<String, Getter<Collection<SpellEffectModifier>>>();
    
    
    /** The width of rune designs in points. */
    protected final int runeGridWidth = 4;
    
    /** The height of rune designs in points. */
    protected final int runeGridHeight = 4;
    
    
    /** When rune designs are associated with spell words in this registry. */
    public final Event<RuneDesignsAddedArgs> itemsAdded = new BasicEvent<RuneDesignsAddedArgs>();
    
    /** When rune designs are disassociated with spell words in this registry. */
    public final Event<RuneDesignsRemovedArgs> itemsRemoved = new BasicEvent<RuneDesignsRemovedArgs>();
    
    /** When rune designs from the backlog are activated by the effects they're connected to */
    public final Event<RuneDesignsBacklogClearedArgs> backlogCleared = new BasicEvent<RuneDesignsBacklogClearedArgs>();
    
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
        RuneDesignsRemovedArgs args;
        
        synchronized(runeDesigns)
        {
            args = new RuneDesignsRemovedArgs(new HashMap<SpellWord, RuneDesign>(runeDesigns),
                                              new HashMap<String, RuneDesign>(backloggedSpellEffectRuneDesigns),
                                              true);
            
            runeDesigns.clear();
            backloggedSpellEffectRuneDesigns.clear();
        }
        
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
        { getters = new ArrayList<Getter<Collection<SpellEffectModifier>>>(modifierGetters.values()); }
        
        RuneDesignsRemovedArgs removedArgs = null;
        
        Map<SpellWord, RuneDesign> added = new HashMap<SpellWord, RuneDesign>();
        boolean backlogUpdated = false;
        
        synchronized(runeDesigns)
        {
            if(clearFirst)
            {
                removedArgs = new RuneDesignsRemovedArgs(runeDesigns, backloggedSpellEffectRuneDesigns, true);
                runeDesigns.clear();
                backloggedSpellEffectRuneDesigns.clear();
            }
            
            for(SpellEffect effect : sourceEffectRegistry.getEffects())
                if(!runeDesigns.containsKey(effect))
                {
                    RuneDesign rune = backloggedSpellEffectRuneDesigns.remove(effect.getName());
                    
                    if(rune == null)
                        rune = generateUniqueRuneDesignForSpellEffect();
                    else
                        backlogUpdated = true;
                    
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
        
        if(backlogUpdated)
            backlogCleared.raise(this, new RuneDesignsBacklogClearedArgs());
        
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
     * @param name the unique identifier within this registry for the getter being added.
     * @param getter The enclosed method for accessing a collection of spell effect modifiers.
     */
    public void addModifierGetter(String name, Getter<Collection<SpellEffectModifier>> getter)
    {
        synchronized(modifierGetters)
        { modifierGetters.put(name, getter); }
    }
    
    /** Adds the default ways of accessing the standard spell effect modifiers. */
    protected final void addDefaultModifierGetters()
    {
        synchronized(modifierGetters)
        { 
            modifierGetters.put(AOE.defaultValue.getModifierGroupName(), new Getter<Collection<SpellEffectModifier>>()
            {
                @Override
                public Collection<SpellEffectModifier> get()
                { return new ArrayList<SpellEffectModifier>(AOE.getValues()); }
            });

            modifierGetters.put(AOESize.defaultValue.getModifierGroupName(), new Getter<Collection<SpellEffectModifier>>()
            {
                @Override
                public Collection<SpellEffectModifier> get()
                { return new ArrayList<SpellEffectModifier>(AOESize.getValues()); }
            });

            modifierGetters.put(AOEShape.defaultValue.getModifierGroupName(), new Getter<Collection<SpellEffectModifier>>()
            {
                @Override
                public Collection<SpellEffectModifier> get()
                { return new ArrayList<SpellEffectModifier>(AOEShape.getValues()); }
            });

            modifierGetters.put(SpellStrength.defaultValue.getModifierGroupName(), new Getter<Collection<SpellEffectModifier>>()
            {
                @Override
                public Collection<SpellEffectModifier> get()
                { return new ArrayList<SpellEffectModifier>(SpellStrength.getValues()); }
            });

            modifierGetters.put(SpellTarget.defaultValue.getModifierGroupName(), new Getter<Collection<SpellEffectModifier>>()
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
    
    public void loadFromFile(File file)
    {
        StringBuilder sb = new StringBuilder();
        
        try
        {
            if(file.exists())
            {
                DataInputStream input = new DataInputStream(new FileInputStream(file));
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                
                try
                {
                    boolean first = true;
                    
                    for(String line = ""; line != null; line = reader.readLine())
                    {
                        if(first)
                            first = false;
                        else
                            sb.append('\n');
                        
                        sb.append(line);
                    }
                }
                finally
                {
                    input.close();
                    reader.close();
                }
            }
        }
        catch(IOException exception)
        { throw new RuntimeException("IO Exceptions not currently handled.", exception); }
        
        loadFromString(sb.toString());
    }
    
    public void loadFromString(String s)
    { addFromString(s, true); }
    
    public void addFromString(String s)
    { addFromString(s, false); }
    
    protected void addFromString(String s, boolean clearFirst)
    {
        BufferedReader reader = new BufferedReader(new StringReader(s));
        Map<SpellWord, RuneDesign> designs = new HashMap<SpellWord, RuneDesign>();
        Map<String, Getter<Collection<SpellEffectModifier>>> modifGetters;
        Map<String, RuneDesign> backloggedDesigns = new HashMap<String, RuneDesign>();
        boolean backlogUpdated = false;
        
        RuneDesignsAddedArgs addedArgs;
        RuneDesignsRemovedArgs removedArgs = null;
        
        try
        {
            synchronized(runeDesigns)
            {
                modifGetters = new HashMap<String, Getter<Collection<SpellEffectModifier>>>(this.modifierGetters);
                
                if(clearFirst)
                {
                    removedArgs = new RuneDesignsRemovedArgs(runeDesigns, backloggedSpellEffectRuneDesigns, true);
                    runeDesigns.clear();
                    backloggedSpellEffectRuneDesigns.clear();
                }
                
                for(String line = ""; line != null; line = reader.readLine())
                {
                    SpellWordRuneDesignPair pair = parseLine(line, modifGetters);

                    if(pair == null)
                        continue;

                    if(pair.getWord() instanceof BackloggedSpellEffect)
                    {
                        String effectName = ((BackloggedSpellEffect)pair.getWord()).getName();
                        
                        if(!clearFirst && backloggedSpellEffectRuneDesigns.containsKey(effectName))
                        {
                            backloggedDesigns.put(effectName, pair.getDesign());
                            backloggedSpellEffectRuneDesigns.put(effectName, pair.getDesign());
                        }
                    }
                    else
                    {
                        RuneDesign preExistingRune = null;
                        
                        if(!clearFirst && pair.getWord() instanceof SpellEffect)
                            preExistingRune = backloggedSpellEffectRuneDesigns.remove(((SpellEffect)pair.getWord()).getName());
                        
                        if(preExistingRune == null)
                        {
                            designs.put(pair.getWord(), pair.getDesign());
                            runeDesigns.put(pair.getWord(), pair.getDesign());
                        }
                        else
                        {
                            runeDesigns.put(pair.getWord(), preExistingRune);
                            backlogUpdated = true;
                        }
                    }
                }
            }
        }
        catch(IOException e)
        { throw new RuntimeException("IOException not currently handled. It shouldn't be thrown here anyway.", e); }
        
        if(backlogUpdated)
            this.backlogCleared.raise(this, new RuneDesignsBacklogClearedArgs());
        
        if(clearFirst)
            this.itemsRemoved.raise(this, removedArgs);
        
        this.itemsAdded.raise(this, new RuneDesignsAddedArgs(designs, backloggedDesigns, false, clearFirst));
    }
    
    private SpellWordRuneDesignPair parseLine(String s, Map<String, Getter<Collection<SpellEffectModifier>>> modifierGetters)
    {
        // Example line:
        // effect:heal=2,3>3,1_0,0>1,2_3,0>2,1
        
        if(s == null)
        {
            System.out.println("String passed to handleLine was null");
            return null;
        }
        
        String[] sParts = s.trim().split("=");
        
        if(sParts.length < 2)
        {
            System.out.println("String passed to handleLine could not be split up into an identifier and rune "
                             + "instructions");
            return null;
        }
        
        SpellWord spellWord = getSpellWordFromString(sParts[0], modifierGetters);
        
        if(spellWord == null)
        {
            System.out.println("The spell word in the string passed to handleLine could not be identified.");
            return null;
        }
        
        RuneDesign runeDesign = getRuneDesignFromString(sParts[1], runeGridWidth, runeGridHeight);
        
        if(runeDesign == null)
        {
            System.out.println("The rune design in the string passed to handleLine could not be parsed.");
            return null;
        }
        
        return new SpellWordRuneDesignPair(spellWord, runeDesign);
    }
    
    private SpellWord getSpellWordFromString(String s, Map<String, Getter<Collection<SpellEffectModifier>>> modifierGetters)
    {
        if(s == null)
            return null;
        
        String[] sParts = s.trim().split(":");
        
        if(sParts.length < 2)
            return null;
        
        if(sParts[0].equalsIgnoreCase("effect"))
        {
            SpellEffect effect = this.sourceEffectRegistry.getEffect(sParts[1].trim());
            return effect != null ? effect : new BackloggedSpellEffect(sParts[1].trim());
        }
        
        Getter<Collection<SpellEffectModifier>> getter = modifierGetters.get(sParts[0].trim());
        
        for(SpellEffectModifier modifier : getter.get())
            if(modifier.getModifierName().equalsIgnoreCase(sParts[1].trim()))
                return modifier;
        
        return null;
    }
    
    private static RuneDesign getRuneDesignFromString(String s, int maxX, int maxY)
    { return new RuneDesignBuilder(maxY, maxY, getLineListFromString(s)).make(); }
    
    private static List<Line<PointInt2d>> getLineListFromString(String s)
    {
        if(s == null || s.trim().equalsIgnoreCase("[blank]"))
            return new ArrayList<Line<PointInt2d>>();
        
        String[] sParts = s.trim().split("_");
        List<Line<PointInt2d>> lineList = new ArrayList<Line<PointInt2d>>();
        
        for(String i : sParts)
        {
            Line<PointInt2d> line = getLineFromString(i);
            
            if(line != null)
                lineList.add(line);
        }
        
        return lineList;
    }
    
    private static Line<PointInt2d> getLineFromString(String s)
    {
        if(s == null)
            return null;
        
        String[] sParts = s.trim().split(">");
        
        if(sParts.length < 2)
            return null;
        
        PointInt2d start = getPointFromString(sParts[0]);
        PointInt2d end = getPointFromString(sParts[1]);
        
        if(start == null || end == null)
            return null;
        
        return new Line<PointInt2d>(start, end);
    }
    
    private static PointInt2d getPointFromString(String s)
    {
        if(s == null)
            return null;
        
        String[] sParts = s.trim().split(",");
        
        if(sParts.length < 2)
            return null;
        
        Double x = Doubles.tryParse(sParts[0].trim());
        Double y = Doubles.tryParse(sParts[1].trim());
        
        if(x == null || y == null)
            return null;
        
        return new PointInt2d(x.intValue(), y.intValue());
    }
    
    @Override
    public String toString()
    {
        // Example line:
        // effect:heal=2,3>3,1_0,0>1,2_3,0>2,1
        
        StringBuilder sb = new StringBuilder();
        
        synchronized(runeDesigns)
        {
            boolean first = true;
            
            for(Map.Entry<SpellWord, RuneDesign> i : runeDesigns.entrySet())
            {
                if(first) first = false;
                else      sb.append('\n');
                
                sb.append(spellwordToString(i.getKey()));
                sb.append('=');
                sb.append(runeDesignToString(i.getValue()));
            }
            
            for(Map.Entry<String, RuneDesign> i : backloggedSpellEffectRuneDesigns.entrySet())
            {
                if(first) first = false;
                else      sb.append('\n');
                
                sb.append(backloggedEffectToString(i.getKey()));
                sb.append('=');
                sb.append(runeDesignToString(i.getValue()));
            }
        }
        
        return sb.toString();
    }
    
    private static String backloggedEffectToString(String effectName)
    {
        if(effectName == null)
            return null;
        
        return "effect:" + effectName;
    }
    
    private static String spellwordToString(SpellWord word)
    {
        if(word == null)
            return null;
        
        if(word instanceof SpellEffect)
            return "effect:" + ((SpellEffect)word).getName();
        
        if(word instanceof SpellEffectModifier)
        {
            SpellEffectModifier modifier = (SpellEffectModifier)word;
            return modifier.getModifierGroupName() + ":" + modifier.getModifierName();
        }
        
        throw new RuntimeException("Unsupported spellword passed to RuneDesignRegistry.spellwordToString(SpellWord), "
                                   + "of type: " + word.getClass().toString());
    }
    
    private static String runeDesignToString(RuneDesign design)
    {
        if(design == null)
            return null;
        
        StringBuilder sb = new StringBuilder();
        List<Line<PointInt2d>> lines = design.getLines();
        
        if(lines.isEmpty())
            return "[blank]";
        
        boolean first = true;
        
        for(Line<PointInt2d> line : lines)
        {
            if(first) first = false;
            else      sb.append('_');
            
            sb.append(lineToString(line));
        }
        
        return sb.toString();
    }
    
    private static String lineToString(Line<PointInt2d> line)
    { return pointToString(line.getStart()) + ">" + pointToString(line.getEnd()); }
    
    private static String pointToString(PointInt2d point)
    { return point.getX() + "," + point.getY(); }
}