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
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// TO DO: Add a way of ensuring that different rune designs used here don't have the same node-connections/lines, but
//        in a different order.

/** Registry for storing rune designs and matching them to spell effects and spell effect modifiers. */
public class RuneDesignRegistry
{
    //<editor-fold defaultstate="collapsed" desc="Inner Classes">
    /** Event args for when rune designs are associated with spell words in this registry. */
    public static class RuneDesignsAddedArgs extends BasicEventArgs
    {
        //<editor-fold defaultstate="collapsed" desc="Constructors">
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
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Variables">
        /** A map of the spell words and rune designs added. */
        final protected Map<SpellWord, RuneDesign> added;
        
        /** A map of the names of spell effects */
        final protected Map<String, RuneDesign> addedToBacklog;
        
        /** Whether or not rune designs were added for all possible spell words in this case. */
        final protected boolean addedAll;
        
        /** Whether or not all existing spell word/rune design associated were removed prior to adding. */
        final protected boolean clearedExisting;
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Methods">
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
        //</editor-fold>
    }
    
    /** Event args for when rune designs are disassociated with spell words. */
    public static class RuneDesignsRemovedArgs extends BasicEventArgs
    {
        //<editor-fold defaultstate="collapsed" desc="Constructors">
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
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Variables">
        /** A map containing the spell words removed with the rune designs removed as values. */
        final protected Map<SpellWord, RuneDesign> removed;
        
        /** A map containing the names of inactive spell effects removed with the rune designs removed as values. */
        final protected Map<String, RuneDesign> backlogRemoved;
        
        /** Whether or not the entire registry was cleared. */
        final protected boolean clearedAll;
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Methods">
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
        //</editor-fold>
    }
    
    /** Event args for when backlogged spell effects are un-backlogged. */
    public static class RuneDesignsBacklogClearedArgs extends BasicEventArgs
    {  }
    
    /** Representation of a backlogged effect for passing/returning where an argument *must* implement SpellWord. */
    private static class BackloggedSpellEffect implements SpellWord
    {
        /**
         * Creates a new BackloggedSpellEffect from the name of the expected spell effect.
         * @param nameOfSpellEffectBacklogged The name of the expected spell effect.
         */
        public BackloggedSpellEffect(String nameOfSpellEffectBacklogged)
        { this.name = nameOfSpellEffectBacklogged; }
        
        /** The name of the expected spell effect. */
        protected final String name;
        
        /**
         * Gets the name of the expected spell effect.
         * @return The name of the expected spell effect.
         */
        public String getName()
        { return name; }
    }
    
    /** Pair combining a SpellWord and a RuneDesign in a single object, without using a generic tuple. */
    protected static class SpellWordRuneDesignPair
    {
        /**
         * Creates a new pair from the passed SpellWord and RuneDesign.
         * @param word The SpellWord to include.
         * @param design The RuneDesign to include.
         */
        public SpellWordRuneDesignPair(SpellWord word, RuneDesign design)
        {
            this.word = word;
            this.design = design;
        }
        
        /** The stored SpellWord. */
        protected SpellWord word;
        
        /** The stored RuneDesign. */
        protected RuneDesign design;
        
        /**
         * Gets the stored SpellWord.
         * @return The stored SpellWord.
         */
        public SpellWord getWord()
        { return word; }
        
        /**
         * Gets the stored RuneDesign.
         * @return The stored RuneDesign.
         */
        public RuneDesign getDesign()
        { return design; }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates the registry.
     * @param spellEffectRegistry The spell effect registry to link this to.
     */
    public RuneDesignRegistry(SpellEffectRegistry spellEffectRegistry)
    {
        sourceEffectRegistry = spellEffectRegistry;
        addDefaultModifierGetters();
    }
    
    /**
     * Creates the registry, parsing the passed string into RuneDesigns and assignments to SpellWords, and using the
     * results as prefilled values.
     * @param spellEffectRegistry The spell effect registry to link this to.
     * @param unparsedRuneDesigns The unparsed string containing all of the RuneDesigns along with the SpellWords to
     * assign them to.
     */
    public RuneDesignRegistry(SpellEffectRegistry spellEffectRegistry, String unparsedRuneDesigns)
    {
        this(spellEffectRegistry);
        setFromString(unparsedRuneDesigns);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    /** The rune designs for all spell words. (e.g. spell effects, spell effect modifiers) */
    protected final Map<SpellWord, RuneDesign> runeDesigns = new HashMap<SpellWord, RuneDesign>();
    
    /** The rune designs made for spell effects that aren't yet accessible from the referenced spell effect registry. */
    protected final Map<String, RuneDesign> backloggedSpellEffectRuneDesigns = new HashMap<String, RuneDesign>();
    
    /** The spell effect registry this registry is linked to. */
    protected final SpellEffectRegistry sourceEffectRegistry;
    
    /** The getters for getting what should be all possible spell effect modifiers. */
    protected final Map<String, Getter<Collection<SpellEffectModifier>>> modifierGetters = new HashMap<String, Getter<Collection<SpellEffectModifier>>>();
    
    
    //<editor-fold defaultstate="collapsed" desc="Constants">
    /** The width of rune designs in points. */
    protected final int runeGridWidth = 4;
    
    /** The height of rune designs in points. */
    protected final int runeGridHeight = 4;
    //</editor-fold>
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Events">
    /** When rune designs are associated with spell words in this registry. */
    public final Event<RuneDesignsAddedArgs> itemsAdded = new BasicEvent<RuneDesignsAddedArgs>();
    
    /** When rune designs are disassociated with spell words in this registry. */
    public final Event<RuneDesignsRemovedArgs> itemsRemoved = new BasicEvent<RuneDesignsRemovedArgs>();
    
    /** When rune designs from the backlog are activated by the effects they're connected to */
    public final Event<RuneDesignsBacklogClearedArgs> backlogCleared = new BasicEvent<RuneDesignsBacklogClearedArgs>();
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Methods">
    //<editor-fold defaultstate="collapsed" desc="Accessors">
    /**
     * Gets the rune design registered for the passed spell word.
     * @param spellWord The spell word (e.g. SpellEffect, SpellEffectModifier, etc.) to get the rune design for.
     * @return The rune design for the given spell word.
     */
    public RuneDesign getRuneDesignFor(SpellWord spellWord)
    { synchronized(runeDesigns) { return runeDesigns.get(spellWord); } }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Mutators">
    public void updateBackloggedDesigns()
    {
        synchronized(runeDesigns)
        {
            for(Map.Entry<String, SpellEffect> entry : (sourceEffectRegistry.getActiveSpellEffectsWithNames(backloggedSpellEffectRuneDesigns.keySet())).entrySet())
                runeDesigns.put(entry.getValue(), backloggedSpellEffectRuneDesigns.remove(entry.getKey()));
        }
        
        this.backlogCleared.raise(this, new RuneDesignsBacklogClearedArgs());
    }
    
    //<editor-fold defaultstate="collapsed" desc="Registration">
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
    
    public void registerFromString(String s)
    { registerFromString(s, false); }
    
    public void setFromString(String s)
    { registerFromString(s, true); }
    
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
     * Randomly assigns rune designs to all spell effects and spell effect modifiers that don't already have rune
     * designs associated with them.
     */
    public void randomlyAssignRest()
    { randomlyAssignRest(false); }
    
    /**
     * Randomly assigns rune designs to all spell effects and spell effect modifiers, overwriting any already
     * registered.
     */
    public void randomlyAssignAll()
    { randomlyAssignRest(true); }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Deregistration">
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
    
    public void deregister(Collection<? extends SpellWord> spellWords)
    {
        Map<SpellWord, RuneDesign> removed = new HashMap<SpellWord, RuneDesign>();
        boolean cleared;
        
        synchronized(runeDesigns)
        {
            for(SpellWord i : spellWords)
            {
                RuneDesign match = runeDesigns.remove(i);
                
                if(match != null)
                    removed.put(i, match);
            }
            
            cleared = runeDesigns.isEmpty() && backloggedSpellEffectRuneDesigns.isEmpty();
        }
        
        if(!removed.isEmpty())
            this.itemsRemoved.raise(this, new RuneDesignsRemovedArgs(removed, cleared));
    }
    
    public void deregisterByStrings(Collection<? extends String> spellWordStrings)
    {
        Map<SpellWord, RuneDesign> removed = new HashMap<SpellWord, RuneDesign>();
        Map<String, RuneDesign> backlogRemoved = new HashMap<String, RuneDesign>();
        boolean cleared;
        
        synchronized(runeDesigns)
        {
            for(String i : spellWordStrings)
            {
                if(i.startsWith("effect:"))
                {
                    i = i.substring(7); // "effect:".length()
                    
                    RuneDesign match = backloggedSpellEffectRuneDesigns.remove(i);
                    
                    if(match != null)
                    {
                        backlogRemoved.put(i, match);
                        continue;
                    }
                    
                    SpellWord wordToRemove = null;
                    
                    for(Map.Entry<SpellWord, RuneDesign> entry : runeDesigns.entrySet())
                    {
                        SpellWord currentSpellWord = entry.getKey();
                        
                        if(currentSpellWord instanceof SpellEffect && ((SpellEffect)currentSpellWord).getName().equalsIgnoreCase(i))
                        {
                            wordToRemove = currentSpellWord;
                            break;
                        }
                    }
                    
                    if(wordToRemove != null)
                        removed.put(wordToRemove, runeDesigns.remove(wordToRemove));
                    
                    continue;
                }
                
                String[] iParts = i.split(":", 2);
                
                if(iParts.length < 2)
                {
                    System.out.println("String passed to deregisterWithSpellWordStrings could not be split into a "
                                       + "spell effect modifier type and spell effect modifier name:\n" + i);
                    continue;
                }
                
                String modifierEnumName = iParts[0];
                String modifierName = iParts[1];
                SpellWord wordToRemove = null;
                
                for(Map.Entry<SpellWord, RuneDesign> entry : runeDesigns.entrySet())
                {
                    if(entry.getKey() instanceof SpellEffectModifier
                       && ((SpellEffectModifier)entry.getKey()).getModifierGroupName().equalsIgnoreCase(modifierEnumName)
                       && ((SpellEffectModifier)entry.getKey()).getModifierName()     .equalsIgnoreCase(modifierName))
                    {
                        wordToRemove = entry.getKey();
                        break;
                    }
                }
                
                if(wordToRemove != null)
                    removed.put(wordToRemove, runeDesigns.remove(wordToRemove));
            }
            
            cleared = !removed.isEmpty()
                      && !backlogRemoved.isEmpty()
                      && runeDesigns.isEmpty()
                      && backloggedSpellEffectRuneDesigns.isEmpty();
        }
        
        if(!(removed.isEmpty() && backlogRemoved.isEmpty()))
            this.itemsRemoved.raise(this, new RuneDesignsRemovedArgs(removed, backlogRemoved, cleared));
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
    //</editor-fold>
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Filehandling">
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
        
        setFromString(sb.toString());
    }
    
    public void saveToFile(File file)
    {
        try
        {
            file.mkdirs();
            
            if(file.exists())
                file.delete();
            
            file.createNewFile();
            
            FileWriter fw = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fw);
            
            pw.print(this.toString());
            
            pw.flush();
            pw.close();
            fw.close();
        }
        catch(IOException exception)
        { exception.printStackTrace(); }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="To methods">
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
                sb.append(i.getValue().toString());
            }
            
            for(Map.Entry<String, RuneDesign> i : backloggedSpellEffectRuneDesigns.entrySet())
            {
                if(first) first = false;
                else      sb.append('\n');
                
                sb.append(backloggedEffectToString(i.getKey()));
                sb.append('=');
                sb.append(i.getValue().toString());
            }
        }
        
        return sb.toString();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Internal methods">
    //<editor-fold defaultstate="collapsed" desc="Mutators">
    protected void registerFromString(String s, boolean clearFirst)
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
    
    /**
     * Randomly assigns rune designs to all spell effects and spell effect modifiers that don't already have rune
     * designs associated with them, clearing all rune designs associations first if clearFirst is true.
     * @param clearFirst Whether or not the clear all existing rune design associations first.
     */
    protected void randomlyAssignRest(boolean clearFirst)
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="RuneDesign random generation">
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Parsing">
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
        
        //RuneDesign runeDesign = getRuneDesignFromString(sParts[1], runeGridWidth, runeGridHeight);
        RuneDesign runeDesign = RuneDesign.fromString(sParts[1]);
        
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Encoding">
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
    
    private static String backloggedEffectToString(String effectName)
    {
        if(effectName == null)
            return null;
        
        return "effect:" + effectName;
    }
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
}