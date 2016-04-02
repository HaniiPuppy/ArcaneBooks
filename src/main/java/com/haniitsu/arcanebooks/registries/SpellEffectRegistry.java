package com.haniitsu.arcanebooks.registries;

import com.google.common.primitives.Doubles;
import com.haniitsu.arcanebooks.magic.ConfiguredDefinition;
import com.haniitsu.arcanebooks.magic.SpellEffect;
import com.haniitsu.arcanebooks.magic.SpellEffectDefinition;
import com.haniitsu.arcanebooks.magic.modifiers.definition.BasicDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.LogicalCheckDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.ModifierValueDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.NumericDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import com.haniitsu.arcanebooks.misc.UtilMethods;
import com.haniitsu.arcanebooks.misc.events.BasicEvent;
import com.haniitsu.arcanebooks.misc.events.Event;
import com.haniitsu.arcanebooks.misc.events.args.BasicEventArgs;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Registry for storing spell effects, backed against a SpellEffectDefinitionRegistry which contains definitions for
 * the spell effects stored here.
 */
public class SpellEffectRegistry
{
    /**
     * Replacement for ConfiguredDefinition that uses the name of the spell effect definition that may or may not have
     * been registered yet, rather than a class instance. Used for eventually constructing an actual
     * ConfiguredDefinition object, once the required SpellEffectDefinition is actually registered.
     */
    protected static class ConfiguredDefinitionInstruction implements SpellEffectDefinitionModifier
    {
        /**
         * Creates a new instance with the given name and no value or submodifiers.
         * @param name The name of the conf. def. instruction.
         */
        public ConfiguredDefinitionInstruction(String name)
        {
            this.definitionName = name;
            this.value = null;
            this.modifiers = Collections.unmodifiableList(new ArrayList<SpellEffectDefinitionModifier>());
        }
        
        /**
         * Creates a new instance with the given name and value, but no submodifiers.
         * @param name The name of the conf. def. instruction.
         * @param value The modifier value.
         */
        public ConfiguredDefinitionInstruction(String name, String value)
        {
            this.definitionName = name;
            this.value = value;
            List<SpellEffectDefinitionModifier> subMods = new ArrayList<SpellEffectDefinitionModifier>();
            
            if(value != null)
                subMods.add(new ModifierValueDefinitionModifier(value));
            
            this.modifiers = Collections.unmodifiableList(subMods);
        }
        
        /**
         * Creates a new instance with the given name and submodifiers, but no value.
         * @param name The name of the conf. def. instruction.
         * @param arguments The submodifiers.
         */
        public ConfiguredDefinitionInstruction(String name, SpellEffectDefinitionModifier... arguments)
        { this(name, null, Arrays.asList(arguments)); }
        
        /**
         * Creates a new instance with the given name and submodifiers, but no value.
         * @param name The name of the conf. def. instruction.
         * @param arguments The submodifiers.
         */
        public ConfiguredDefinitionInstruction(String name, List<SpellEffectDefinitionModifier> arguments)
        { this(name, null, arguments); }
        
        /**
         * Creates a new instance with the given name, modifier value, and submodifiers.
         * @param name The name of the conf. def. instruction.
         * @param value The modifier value.
         * @param arguments The submodifiers.
         */
        public ConfiguredDefinitionInstruction(String name, String value, SpellEffectDefinitionModifier... arguments)
        { this(name, value, Arrays.asList(arguments)); }
        
        /**
         * Creates a new instance with the given name, modifier value, and submodifiers.
         * @param name The name of the conf. def. instruction.
         * @param value The modifier value.
         * @param arguments The submodifiers.
         */
        public ConfiguredDefinitionInstruction(String name, String value, List<SpellEffectDefinitionModifier> arguments)
        {
            this.definitionName = name;
            this.value = value;
            List<SpellEffectDefinitionModifier> subMods = new ArrayList<SpellEffectDefinitionModifier>(arguments);
            
            if(value != null)
                subMods.add(new ModifierValueDefinitionModifier(value));
            
            this.modifiers = Collections.unmodifiableList(subMods);
        }
        
        /** The name of this conf. def. instruction/the potential conf. definition's name. */
        final String definitionName;
        
        /** The submodifiers of this modifier/the modifier that will be created from this one. */
        final List<SpellEffectDefinitionModifier> modifiers;
        
        /** The modifier value of this modifier/the modifier that will be created from this one. */
        final String value;
        
        private List<LogicalCheckDefinitionModifier> logicalChecksCache = null;
        
        @Override
        public String getName()
        { return definitionName; }
        
        /**
         * Gets the submodifiers of this modifier.
         * @return This modifier's submodifiers as an immutable list.
         */
        public List<SpellEffectDefinitionModifier> getModifiers()
        { return modifiers; }

        @Override
        public List<SpellEffectDefinitionModifier> getSubModifiers()
        { return getModifiers(); }
        
        private void fillLogicalModifiersCache()
        {
            List<LogicalCheckDefinitionModifier> cache = new ArrayList<LogicalCheckDefinitionModifier>();

            for(SpellEffectDefinitionModifier i : modifiers)
                if(i instanceof LogicalCheckDefinitionModifier)
                    cache.add((LogicalCheckDefinitionModifier)i);

            logicalChecksCache = Collections.unmodifiableList(cache);
        }
        
        @Override
        public List<LogicalCheckDefinitionModifier> getLogicalModifiers()
        {
            if(logicalChecksCache == null)
            fillLogicalModifiersCache();
        
        return logicalChecksCache;
        }

        @Override
        public String getValue()
        { return value; }

        @Override
        public SpellEffectDefinitionModifier getCopy()
        { return new ConfiguredDefinitionInstruction(definitionName, value, modifiers); }

        @Override
        public SpellEffectDefinitionModifier getCopyWithNewModifiers(List<SpellEffectDefinitionModifier> newModifiers)
        { return new ConfiguredDefinitionInstruction(definitionName, value, newModifiers); }
        
        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder(definitionName);
            
            for(SpellEffectDefinitionModifier modifier : modifiers)
                if(modifier instanceof LogicalCheckDefinitionModifier)
                    sb.append('[').append(modifier.getName()).append(']');
            
            boolean atLeastOneModifier = false;
            
            for(SpellEffectDefinitionModifier modifier : modifiers)
                if(!(modifier instanceof LogicalCheckDefinitionModifier))
                {
                    if(!atLeastOneModifier)
                    {
                        sb.append('(');
                        atLeastOneModifier = true;
                    }
                    else
                        sb.append(", ");
                    
                    sb.append(modifier.toString());
                }
            
            if(atLeastOneModifier)
                sb.append(')');
            
            if(value != null)
                sb.append(": ").append(value);
            
            return sb.toString();
        }
    }
    
    protected static class ConfiguredDefinitionStrings
    {
        public ConfiguredDefinitionStrings(String name, List<String> logicalChecks, String args, String value)
        {
            this.name = name;
            this.logicalChecks = logicalChecks;
            this.args = args;
            this.value = value;
        }
        
        public final String name, args, value;
        public final List<String> logicalChecks;
    }
    
    /** Event-args for when the backlog is partially cleared. */
    public static class BacklogClearedArgs extends BasicEventArgs
    { }
    
    /** Event-args for when spell effects are added to this registry. */
    public static class EffectsAddedArgs extends BasicEventArgs
    {
        /**
         * Creates a new instance of this event args with the passed args.
         * @param effectStrings The parsable string representations of the spell effects added.
         */
        public EffectsAddedArgs(Collection<String> effectStrings)
        { this.effectStrings = Collections.unmodifiableCollection(new ArrayList<String>(effectStrings)); }
        
        /** The parsable string representations of the spell effects added. */
        Collection<String> effectStrings;
        
        /**
         * Gets the parsable string representations of the spell effects added.
         * @return The string representations of the spell effects added.
         */
        public Collection<String> getEffectStrings()
        { return effectStrings; }
    }
    
    /** Event-args for when spell effects are removed from this registry. */
    public static class EffectsRemovedArgs extends BasicEventArgs
    {
        /**
         * Creates a new instance of this event args with the passed args.
         * @param effectNames The names of the spell effects removed.
         */
        public EffectsRemovedArgs(Collection<String> effectNames)
        { this(effectNames, false); }
        
        /**
         * Creates a new instance of this event args with the passed args.
         * @param effectNames The names of the spell effects removed.
         * @param cleared Whether or not the spell effects were removed as a result of the entire registry being
         * cleared.
         */
        public EffectsRemovedArgs(Collection<String> effectNames, boolean cleared)
        {
            this.effectNames = Collections.unmodifiableCollection(new ArrayList<String>(effectNames));
            this.cleared = cleared;
        }
        
        /** The names of the spell effects removed. */
        Collection<String> effectNames;
        
        /** Whether or not the spell effects were removed as a result of the entire registry being cleared. */
        boolean cleared;
        
        /**
         * Gets the names of the spell effects removed.
         * @return The names of the spell effects removed.
         */
        public Collection<String> getEffectNames()
        { return effectNames; }
        
        /**
         * Gets whether or not the spell effects were removed as a result of the entire registry being cleared.
         * @return True if it was cleared, otherwise false.
         */
        public boolean wasCleared()
        { return cleared; }
    }
    
    /**
     * Creates a new SpellEffectRegistry, linked to the passed spell effect definition registry.
     * @param definitionRegistry The spell effect definition registry to have this registry be linked to.
     */
    public SpellEffectRegistry(SpellEffectDefinitionRegistry definitionRegistry)
    { linkedDefinitionRegistry = definitionRegistry; }
    
    /**
     * Creates a new SpellEffectRegistry, linked to the passed spell effect definition registry, and pre-filled with
     * the parsable spell effects in the string passed.
     * @param definitionRegistry The spell effect definition registry to have this registry be linked to.
     * @param stringToLoadFrom The string to be parsed into the spell effects that will populate this registry.
     */
    public SpellEffectRegistry(SpellEffectDefinitionRegistry definitionRegistry, String stringToLoadFrom)
    {
        this(definitionRegistry);
        loadFromString(stringToLoadFrom);
    }
    
    /**
     * Creates a new SpellEffectRegistry with the same contents as the passed SpellEffectRegistry. The resulting
     * SpellEffectRegistry will be backed against the same SpellEffectDefinitionRegistry as the other.
     * @param other The SpellEffectRegistry to base this one off of.
     */
    public SpellEffectRegistry(SpellEffectRegistry other)
    {
        synchronized(other.effects)
        {
            effects.putAll(other.effects);
            backloggedEffects.putAll(other.backloggedEffects);
            linkedDefinitionRegistry = other.linkedDefinitionRegistry;
        }
    }
    
    /** The current active spell effects. */
    final protected Map<String, SpellEffect> effects = new HashMap<String, SpellEffect>();
    
    /**
     * The backlogged spell effects not yet compiled into active, actual Spell Effects. Uses effects as a
     * synchronisation lock.
     */
    final protected Map<String, List<ConfiguredDefinitionInstruction>> backloggedEffects
        = new HashMap<String, List<ConfiguredDefinitionInstruction>>();
    
    /** The spell effect definition registry providing spell effect definitions for spell effects in this registry. */
    final protected SpellEffectDefinitionRegistry linkedDefinitionRegistry;
    
    /** When the backlogged is cleared of some of its values. That is, when some backlogged spell effects are turned
        into actual functioning spell effects. */
    final public Event<BacklogClearedArgs> backlogCleared = new BasicEvent<BacklogClearedArgs>();
    
    /** When spell effects are added to the registry, regardless of if they're backlogged or not. */
    final public Event<EffectsAddedArgs> effectsAdded = new BasicEvent<EffectsAddedArgs>();
    
    /** When spell effects are removed from the registry. */
    final public Event<EffectsRemovedArgs> effectsRemoved = new BasicEvent<EffectsRemovedArgs>();
    
    final Event<BacklogClearedArgs> backlogClearedForRuneDesigns = new BasicEvent<BacklogClearedArgs>();
    
    final Event<EffectsAddedArgs> effectsAddedForRuneDesigns = new BasicEvent<EffectsAddedArgs>();
    
    /** Prints the contents of this registry to console. */
    public void printContents()
    {
        System.out.println("Compiled effects: " + effects.size() + ", Backlogged effects: " + backloggedEffects.size());
        
        for(SpellEffect i : effects.values())
            System.out.println("Compiled effect - " + i.toString());
        
        for(Map.Entry<String, List<ConfiguredDefinitionInstruction>> i : backloggedEffects.entrySet())
        {
            String line = "Backlogged effect - " + i.getKey() + ": ";
            
            boolean first = true;
            
            for(ConfiguredDefinitionInstruction j : i.getValue())
            {
                if(first)
                    first = false;
                else
                    line += ", ";
                
                line += j.toString();
            }
            
            System.out.println(line);
        }
    }
    
    /**
     * Gets the spell effect registered in this registry against the given name.
     * @param name The name of the spell effect to get.
     * @return The SpellEffect object registered to the given name, or null if there is none. There may not be a spell
     * effect registered for a given name if a spell effect string is registered, but not yet matched against a
     * spell effect definition.
     */
    public SpellEffect getEffect(String name)
    { synchronized(effects) { return effects.get(name); } }
    
    /**
     * Gets all of the currently registered spell effects.
     * @return The currently registered spell effects as a list. Does not include spell effects registered by string
     * but not yet matched up against a spell effect definition from the linked spell effect definitions registry.
     */
    public Collection<SpellEffect> getEffects()
    { return new ArrayList<SpellEffect>(effects.values()); }
    
    /**
     * Registers a spell effect object. The effect is registered against the name stored in the spell effect object.
     * @param effect The spell effect to register.
     */
    public void register(SpellEffect effect)
    {
        synchronized(effects)
        { effects.put(effect.getName(), effect); }
        
        Collection<String> effectStrings = new ArrayList<String>();
        effectStrings.add(effect.toString());
        this.effectsAdded.raise(this, new EffectsAddedArgs(effectStrings));
        this.effectsAddedForRuneDesigns.raise(this, new EffectsAddedArgs(effectStrings));
    }
    
    /**
     * Loads a spell effect. Converts a string and name into the needed instructions to construct a spell effect. If
     * the required spell effect definitions are registered in the linked spell effect definitions registry, then the
     * instructions are stored in a backlog against the potential spell effect's name.
     * @param effectName The name of the new spell effect.
     * @param effectDefinitions The unparsed string containing the information needed to construct a the spell effect
     * from spell effect definitions and modifiers.
     */
    public void load(String effectName, String effectDefinitions)
    { load(effectName, effectDefinitions, true); }
    
    /**
     * Loads a spell effect. Converts a string and name into the needed instructions to construct a spell effect. If
     * the required spell effect definitions are registered in the linked spell effect definitions registry, then the
     * instructions are stored in a backlog against the potential spell effect's name.
     * @param effectName The name of the new spell effect.
     * @param effectDefinitions The unparsed string containing the information needed to construct a the spell effect
     * from spell effect definitions and modifiers.
     * @param fireEvent Whether or not to fire the effectsAdded event.
     */
    protected void load(String effectName, String effectDefinitions, boolean fireEvent)
    {
        List<String> definitionStrings = UtilMethods.splitCSVLine(effectDefinitions);
        List<ConfiguredDefinitionInstruction> defInstructions = new ArrayList<ConfiguredDefinitionInstruction>();
        
        for(String i : definitionStrings)
            defInstructions.add((ConfiguredDefinitionInstruction)getModifierFromString(i.trim(), true));
        
        SpellEffect effect = realise(effectName, defInstructions);
        
        if(effect == null)
            synchronized(effects)
            { backloggedEffects.put(effectName, defInstructions); }
        else
            synchronized(effects)
            { effects.put(effectName, effect); }
        
        if(fireEvent)
        {
            Collection<String> effectStrings = new ArrayList<String>();
            effectStrings.add(effectName + ": " + effectDefinitions);
            this.effectsAdded.raise(this, new EffectsAddedArgs(effectStrings));
            this.effectsAddedForRuneDesigns.raise(this, new EffectsAddedArgs(effectStrings));
        }
    }
    
    /**
     * Attempts to turn a name and a list of ConfiguredDefinitionInstructions into an actual SpellEffect backed by a
     * SpellEffectDefinition.
     * @param name The name of the spell effect.
     * @param toBeRealised The list of configured spell effect definition instructions to be part of the spell effect.
     * @return The created SpellEffect object, or null if not all spell effect definitions referenced are registered
     * in the relevant spell effects definition registry.
     */
    private SpellEffect realise(String name, List<ConfiguredDefinitionInstruction> toBeRealised)
    {
        List<ConfiguredDefinition> configuredDefs = new ArrayList<ConfiguredDefinition>();
        
        for(int i = 0; i < toBeRealised.size(); i++)
        {
            ConfiguredDefinition currentRealised = (ConfiguredDefinition)realiseSingleModifier(toBeRealised.get(i));
            
            if(currentRealised == null)
                return null;
            
            configuredDefs.add(currentRealised);
        }
        
        return new SpellEffect(name, configuredDefs);
    }
    
    /**
     * Turns a single modifier into a ConfiguredDefinition if it's a ConfiguredDefinitionInstruction, and does the same
     * to all submodifiers, their submodifiers, etc.
     * @param toRealise The unrealised spell effect definition modifier to turn into a realised one.
     * @return A spell effect modifier realised using the method described above, or null if not all required spell
     * effect definitions have been registered yet in the linked spell effect definition registry.
     */
    private SpellEffectDefinitionModifier realiseSingleModifier(SpellEffectDefinitionModifier toRealise)
    {
        List<SpellEffectDefinitionModifier> realisedSubModifiers = new ArrayList<SpellEffectDefinitionModifier>();
        List<SpellEffectDefinitionModifier> unrealisedSubModifiers = toRealise.getSubModifiers();
        SpellEffectDefinition relevantDefinition = null;
        
        if(toRealise instanceof ConfiguredDefinitionInstruction)
        {
            relevantDefinition = linkedDefinitionRegistry.getByName(toRealise.getName());
            
            if(relevantDefinition == null)
                return null;
        }
        
        for(int i = 0; i < unrealisedSubModifiers.size(); i++)
        {
            SpellEffectDefinitionModifier currentSubModifier = realiseSingleModifier(unrealisedSubModifiers.get(i));
            
            if(currentSubModifier == null)
                return null;
            
            realisedSubModifiers.add(currentSubModifier);
        }
        
        if(relevantDefinition != null) // AKA (toRealise instanceof ConfiguredDefinitionInstruction)
            return new ConfiguredDefinition(relevantDefinition, toRealise.getValue(), realisedSubModifiers);
        else
            return toRealise.getCopyWithNewModifiers(realisedSubModifiers);
    }
    
    /** realises all backlogged spell effects that don't refer to any spell effect definitions not currently registered
     in the linked spell effect definition registry. */
    public void updateBackloggedEffects()
    {
        Map<String, SpellEffect> newEffects = new HashMap<String, SpellEffect>();
        Collection<String> backloggedEffectsToRemove = new HashSet<String>();
        
        synchronized(effects)
        {
            for(Map.Entry<String, List<ConfiguredDefinitionInstruction>> i : backloggedEffects.entrySet())
            {
                SpellEffect iEffect = realise(i.getKey(), i.getValue());
                
                if(iEffect != null)
                {
                    newEffects.put(null, iEffect);
                    backloggedEffectsToRemove.add(i.getKey());
                }
            }
            
            for(String i : backloggedEffectsToRemove)
                backloggedEffects.remove(i);
        
            effects.putAll(newEffects);
        }
        
        this.backlogCleared.raise(this, new BacklogClearedArgs());
        this.backlogClearedForRuneDesigns.raise(this, new BacklogClearedArgs());
    }
    
    /**
     * Converts a string into an unrealised definition modifier.
     * @param modifierString The string form of the definition modifier to convert.
     * @param definitelyEffect True to force the returned modifier to be a ConfiguredDefinitionIntruction.
     * @return If definitelyEffect = true, a ConfiguredDefinitionInstruction. Else, a NumericDefinitionModifier if it
     * can be parsed into a number, a BasicDefinitionModifier if the modifier name starts in a lower case character,
     * and a ConfiguredDefinitionIntruction if it starts in an upper case character. The text in the [square brackets]
     * becomes the logical check submodifier, the text in the (round brackets) becomes the submodifiers, the text
     * after the colon: becomes the modifier argument, and the text before any square brackets, round brackets, or
     * colons becomes the name of the modifier.
     */
    private SpellEffectDefinitionModifier getModifierFromString(String modifierString, boolean definitelyEffect)
    {
        ConfiguredDefinitionStrings confDefStrings = getConfDefinitionStrings(modifierString);
        String modifierName = confDefStrings.name;
        Double numericValue = definitelyEffect ? null : Doubles.tryParse(modifierName);
        // faster than using Double.parseDouble with exception handling.
        
        if(numericValue != null)
            return new NumericDefinitionModifier(numericValue);
        
        List<String> logicalCheckStrings = confDefStrings.logicalChecks;
        String argsString = confDefStrings.args;
        String valueString = confDefStrings.value;
        
        List<SpellEffectDefinitionModifier> subModifiers = new ArrayList<SpellEffectDefinitionModifier>();
        List<String> argStrings = argsString == null ? new ArrayList<String>() : UtilMethods.splitCSVLine(argsString);
        
        for(String i : logicalCheckStrings)
            subModifiers.add(new LogicalCheckDefinitionModifier(i));
        
        for(int i = 0; i < argStrings.size(); i++)
            subModifiers.add(getModifierFromString(argStrings.get(i).trim(), false));
        
        if(definitelyEffect || Character.isUpperCase(modifierName.codePointAt(0)))
            return valueString == null ? new ConfiguredDefinitionInstruction(modifierName, subModifiers)
                                       : new ConfiguredDefinitionInstruction(modifierName, valueString, subModifiers);
        else
            return valueString == null ? new BasicDefinitionModifier(modifierName, subModifiers)
                                       : new BasicDefinitionModifier(modifierName, valueString, subModifiers);
    }
    
    /**
     * Attains the substrings from a modifier strings. These are the modifier's name, the modifier's (separate) logical
     * check strings, the modifier's modifier value, and the modifiers args. (as a single, unsplit string).
     * @note The args string is the text in [square brackets], the args string is the text in (round brackets), the
     * value is the text after the: colon, and the name is part of the string before any other parts.
     * @note Special characters are ( ) [ ] : \
     * @note Any special character can be treated as a normal character by preceding it with a backslash, which will be
     * stripped upon parsing. This also applies to backslashes themselves.
     * @note The args string is a comma-separated list of strings parsable by this method.
     * @note Multiple (args strings) will be concatenated into a single, comma-separated string.
     * @example Modifier Name[Logical check 1][Logical check 2](Arg1, Arg2(foo): bar, arg3[lc3]): Modifier value.
     * @param modifierString The raw, unsplit string to be parsed.
     * @return A ConfiguredDefinitionStrings object containing the modifier name, the (args string) (concatenated
     * together if there's multiple) or null if there's not text in round brackets, a list of the [logical check
     * strings], and the: modifier value, or null if there was no unbracketed colon.
     */
    protected static ConfiguredDefinitionStrings getConfDefinitionStrings(String modifierString)
    {
        class PositionPair
        {
            public PositionPair(int openingPosition, int closingPosition)
            {
                this.openingPosition = openingPosition;
                this.closingPosition = closingPosition;
            }
            
            public final int openingPosition, closingPosition;
        }
        
        List<PositionPair> sqBracketsOpenClosePositions = new ArrayList<PositionPair>();
        List<PositionPair> bracketsOpenClosePositions = new ArrayList<PositionPair>();
        int valueSeparatorPosition = -1;
        int defNameTerminatorPosition = -1;
        
        int openingBracketPosition = -1;
        int openingSqBracketPosition = -1;
        int bracketDepth = 0;
        boolean cancelNextChar = false;
        boolean inSquareBrackets = false;
        boolean partOfAValueString = false;
        
        CharacterIteration:
        for(int i = 0; i < modifierString.length(); i++)
        {
            boolean shouldBeCancelled = false;
            
            switch(modifierString.charAt(i))
            {
                case ':':
                {
                    if(!cancelNextChar && !partOfAValueString && !inSquareBrackets)
                    {
                        partOfAValueString = true;
                        
                        if(defNameTerminatorPosition < 0)
                            defNameTerminatorPosition = i;

                        if(bracketDepth <= 0)
                        {
                            valueSeparatorPosition = i;
                            break CharacterIteration;
                        }
                    }
                } break;
                case '[':
                {
                    if(!cancelNextChar && !partOfAValueString && !inSquareBrackets)
                    {
                        if(defNameTerminatorPosition < 0)
                            defNameTerminatorPosition = i;
                        
                        // Look forward to see if there's a closing bracket.
                        boolean cancelNextInJLoop = false;

                        for(int j = i + 1; j < modifierString.length(); j++)
                        {
                            char jCurrent = modifierString.charAt(j);

                            if(jCurrent == ']' && !cancelNextInJLoop)
                            {
                                if(bracketDepth <= 0)
                                    openingSqBracketPosition = i;

                                inSquareBrackets = true;
                                break;
                            }

                            cancelNextInJLoop = jCurrent == '\\' && !cancelNextInJLoop;
                        }
                    }
                } break;
                case ']':
                {
                    if(!cancelNextChar && !partOfAValueString && inSquareBrackets)
                    {
                        inSquareBrackets = false;

                        if(bracketDepth <= 0)
                        {
                            sqBracketsOpenClosePositions.add(new PositionPair(openingSqBracketPosition, i));
                            openingSqBracketPosition = -1;
                        }
                    }
                } break;
                case '(':
                {
                    if(!cancelNextChar && !partOfAValueString && !inSquareBrackets)
                    {
                        if(bracketDepth++ <= 0)
                            openingBracketPosition = i;
                        
                        if(defNameTerminatorPosition < 0)
                            defNameTerminatorPosition = i;
                    }
                } break;
                case ')':
                {
                    if(!cancelNextChar && !inSquareBrackets && bracketDepth > 0)
                    {
                        if(--bracketDepth <= 0)
                            bracketsOpenClosePositions.add(new PositionPair(openingBracketPosition, i));
                        
                        partOfAValueString = false;
                    }
                } break;
                case ',':
                {
                    if(!cancelNextChar && partOfAValueString)
                        partOfAValueString = false;
                } break;
                case '\\':
                {
                    if(!cancelNextChar)
                        shouldBeCancelled = true;
                } break;
            }
            
            cancelNextChar = shouldBeCancelled;
        }
        
        if(defNameTerminatorPosition < 0)
            defNameTerminatorPosition = modifierString.length();
        
        String nameString = UtilMethods.deEscape(modifierString.substring(0, defNameTerminatorPosition)).trim();
        List<String> logicalCheckStrings = new ArrayList<String>();
        String argsString = "";
        String valueString = valueSeparatorPosition < 0 ? null : UtilMethods.deEscape(modifierString.substring(valueSeparatorPosition + 1)).trim();
        
        for(PositionPair i : sqBracketsOpenClosePositions)
            logicalCheckStrings.add(UtilMethods.deEscape(modifierString.substring(i.openingPosition + 1, i.closingPosition)));
        
        if(bracketsOpenClosePositions.size() > 0)
            for(PositionPair i : bracketsOpenClosePositions)
            {
                if(!argsString.equals("")) // always first iteration only.
                    argsString += ", ";

                argsString += UtilMethods.deEscape(modifierString.substring(i.openingPosition + 1, i.closingPosition)).trim();
            }
        else
            argsString = null;
        
        return new ConfiguredDefinitionStrings(nameString, logicalCheckStrings, argsString, valueString);
    }
    
    /** Removes all registered spell effects and backlogged spell effects from the registry. */
    public void clear()
    {
        Collection<String> effectNames = new ArrayList<String>();
        
        synchronized(effects)
        {
            effectNames.addAll(effects.keySet());
            effectNames.addAll(backloggedEffects.keySet());
            effects.clear();
            backloggedEffects.clear();
        }
        
        this.effectsRemoved.raise(this, new EffectsRemovedArgs(effectNames, true));
    }
    
    /** Registers all default spell effects. */
    public void loadDefaultValues()
    {
        synchronized(effects)
        {
            clear();
            
            load("TestHealAlpha", "Heal: 5");
            load("TestHealBeta",  "Heal: 7");
            // register effect
            // register effect
            // register effect
            // etc.
        }
    }
    
    /**
     * Fills the registry from the passed file. Parses each line into a spell effect, and backlogs the ones that can't
     * yet be compiled. (If the some of the spell effect definitions references, for instance, hasn't been registered
     * yet.)
     * @param file The file containing the spell effects to load.
     */
    public void loadFromFile(File file)
    {
        List<String> loadedLines = new ArrayList<String>();
        List<String> removedEffectNames = new ArrayList<String>();
        
        synchronized(effects)
        {
            removedEffectNames.addAll(effects.keySet());
            removedEffectNames.addAll(backloggedEffects.keySet());
            
            effects.clear();
            backloggedEffects.clear();

            try
            {
                if(file.exists())
                {
                    DataInputStream input = new DataInputStream(new FileInputStream(file));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                    try
                    {
                        for(String line = ""; line != null; line = reader.readLine())
                            if(handleFileLine(line))
                                loadedLines.add(line);
                    }
                    finally
                    {
                        input.close();
                        reader.close();
                    }
                }
                else
                {
                    loadDefaultValues();
                    saveToFile(file);
                }

            }
            catch(IOException exception)
            { throw new RuntimeException("IO Exceptions not currently handled.", exception); }
        }
        
        if(!removedEffectNames.isEmpty())
            this.effectsRemoved.raise(this, new EffectsRemovedArgs(removedEffectNames, true));
        
        this.effectsAdded.raise(this, new EffectsAddedArgs(loadedLines));
        this.effectsAddedForRuneDesigns.raise(this, new EffectsAddedArgs(loadedLines));
    }
    
    /**
     * Loads a single line, e.g. from a file.
     * @param line The text to load as a spell effect.
     * @return True if the line was able to be split properly. Otherwise, false.
     */
    private boolean handleFileLine(String line)
    {
        String[] lineParts = line.split(":", 2);
        
        if(lineParts.length < 2)
        {
            System.out.println("Line cannot be split into spell effect name and definition: \n" + line);
            return false;
        }
        
        load(lineParts[0], lineParts[1], false);
        return true;
    }
    
    /**
     * Saves the contents of the registry to the passed file.
     * @param file The file to save the contents of the registry to.
     */
    public void saveToFile(File file)
    {
        Comparator<String> alphabeticalOrder = new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                int result = String.CASE_INSENSITIVE_ORDER.compare(o1, o2);
                return result != 0 ? result : o1.compareTo(o2);
            }
        };
        
        List<String> effectStrings = new ArrayList<String>();
        Collection<Map.Entry<String, SpellEffect>> effectEntries = null;
        Collection<Map.Entry<String, List<ConfiguredDefinitionInstruction>>> backloggedEffectEntries = null;
        
        synchronized(effects)
        {
            effectEntries = effects.entrySet();
            backloggedEffectEntries = backloggedEffects.entrySet();
        }
        
        for(Map.Entry<String, SpellEffect> entry : effectEntries)
            effectStrings.add(entry.getValue().toString());
        
        for(Map.Entry<String, List<ConfiguredDefinitionInstruction>> entry : backloggedEffectEntries)
        {
            StringBuilder sb = new StringBuilder(entry.getKey());
            sb.append(": ");
            
            boolean first = true;
            
            for(ConfiguredDefinitionInstruction i : entry.getValue())
            {
                if(!first)
                    sb.append(", ");
                else
                    first = false;
                
                sb.append(i);
            }
            
            effectStrings.add(sb.toString());
        }
        
        Collections.sort(effectStrings, alphabeticalOrder);
        
        System.out.println("Effect strings count: " + effectStrings.size());
        
        try
        {
            file.mkdirs();

            if(file.exists())
                file.delete();

            file.createNewFile();

            FileWriter fw = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(fw);

            for(int i = 0; i < effectStrings.size(); i++)
            {
                if(i > 0)
                    pw.println();
                
                pw.print(effectStrings.get(i));
            }

            pw.flush();
            pw.close();
            fw.close();
        }
        catch(IOException exception)
        { exception.printStackTrace(); }
    }
    
    /**
     * Gets a string representation of all of the non-backlogged (aka active) spell effects stored in this registry,
     * where each line is a spell effect.
     * @return The aforementioned string representation.
     */
    public String getActiveEffectsAsString()
    {
        StringBuilder sb = new StringBuilder();
        
        synchronized(effects)
        {
            boolean first = true;
            
            for(SpellEffect i : effects.values())
            {
                if(!first)
                    sb.append("\n");
                else
                    first = false;
                
                sb.append(i.toString());
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Replaces the contents of this registry with the spell effects that can be parsed from the passed string, where
     * each line is taken to be a separate spell effect to be parsed.
     * @param s The string from which to parse spell effects.
     */
    public void loadFromString(String s)
    { addFromString(s, true); }
    
    /**
     * Adds to the current values stored in the registry, the spell effects that can be parsed from the passed string,
     * where each line is taken to be a separate spell effect to be parsed.
     * @param s The string from which to parse spell effects.
     */
    public void addFromString(String s)
    { addFromString(s, false); }
    
    private void addFromString(String s, boolean clearFirst)
    {
        BufferedReader reader = new BufferedReader(new StringReader(s));
        List<String> loadedLines = new ArrayList<String>();
        List<String> removedEffectNames = null;
        
        try
        {
            synchronized(effects)
            {
                if(clearFirst)
                {
                    removedEffectNames = new ArrayList<String>(effects.keySet());
                    removedEffectNames.addAll(backloggedEffects.keySet());
                    effects.clear();
                    backloggedEffects.clear();
                }
                
                for(String line = ""; line != null; line = reader.readLine())
                {
                    String[] parts = line.split(":", 2);

                    if(parts.length < 2)
                    {
                        System.out.println("Syncing server to client, the following line could not be split into spell "
                                         + "effect name and spell effect: " + line);

                        continue;
                    }

                    loadedLines.add(line);
                    load(parts[0], parts[1], false);
                }
            }
        }
        catch(IOException e)
        { throw new RuntimeException("IOException not currently handled. It shouldn't be thrown here anyway.", e); }
        
        if(clearFirst)
            this.effectsRemoved.raise(this, new EffectsRemovedArgs(removedEffectNames, true));
        
        this.effectsAdded.raise(this, new EffectsAddedArgs(loadedLines));
        this.effectsAddedForRuneDesigns.raise(this, new EffectsAddedArgs(loadedLines));
    }
    
    /**
     * Removes the spell effect registered if one exists with the given name.
     * @param effectName The name of the spell effect to deregister.
     */
    public void deregisterWithName(String effectName)
    {
        boolean removed = false;
        
        synchronized(effects)
        { removed = effects.remove(effectName) != null || backloggedEffects.remove(effectName) != null; }
        
        if(removed)
        {
            List<String> removedEffectNames = new ArrayList<String>();
            removedEffectNames.add(effectName);
            this.effectsRemoved.raise(this, new EffectsRemovedArgs(removedEffectNames));
        }
    }
    
    /**
     * Removes the spell effects whose names are included in the passed string collection.
     * @param effectNames A collection of all of the spell effect names to deregister.
     */
    public void deregisterWithNames(Collection<String> effectNames)
    {
        List<String> removedEffectNames = new ArrayList<String>();
        
        synchronized(effects)
        {
            for(String effectName : effectNames)
                if(effects.remove(effectName) != null || backloggedEffects.remove(effectName) != null)
                    removedEffectNames.add(effectName);
        }
        
        this.effectsRemoved.raise(this, new EffectsRemovedArgs(removedEffectNames));
    }
    
    Map<String, SpellEffect> getActiveSpellEffectsWithNames(Collection<String> names)
    {
        Map<String, SpellEffect> matchingEffects = new HashMap<String, SpellEffect>();
        
        synchronized(effects)
        {
            for(String name : names)
            {
                SpellEffect found = effects.get(name);
                
                if(found != null)
                    matchingEffects.put(found.getName(), found);
            }
        }
        
        return matchingEffects;
    }
    
    /**
     * Gets a string representation of the contents of this registry, that can be fed into another SpellEffectRegistry
     * via .loadFromString, to gain the same spell effects and backlogged spell effects.
     * @return The string representation of the contents of this registry.
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        synchronized(effects)
        {
            boolean first = true;
            
            for(SpellEffect i : effects.values())
            {
                if(first)
                    first = false;
                else
                    sb.append("\n");
                
                sb.append(i.toString());
            }
            
            for(Map.Entry<String, List<ConfiguredDefinitionInstruction>> i : backloggedEffects.entrySet())
            {
                if(first)
                    first = false;
                else
                    sb.append("\n");
                
                StringBuilder backloggedLineSb = new StringBuilder();
                backloggedLineSb.append(i.getKey());
                backloggedLineSb.append(": ");
                
                boolean firstBackloggedEffect = true;
                
                for(ConfiguredDefinitionInstruction j : i.getValue())
                {
                    if(firstBackloggedEffect)
                        firstBackloggedEffect = false;
                    else
                        backloggedLineSb.append(", ");
                    
                    backloggedLineSb.append(j.toString());
                }
                
                sb.append(backloggedLineSb.toString());
            }
        }
        
        return sb.toString();
    }
}