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
import org.apache.commons.lang3.NotImplementedException;

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
    
    /**
     * Creates a new SpellEffectRegistry, linked to the passed spell effect definition registry.
     * @param definitionRegistry The spell effect definition registry to have this registry be linked to.
     */
    public SpellEffectRegistry(SpellEffectDefinitionRegistry definitionRegistry)
    { linkedDefinitionRegistry = definitionRegistry; }
    
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
    { synchronized(effects) { effects.put(effect.getName(), effect); } }
    
    /**
     * Loads a spell effect. Converts a string and name into the needed instructions to construct a spell effect. If
     * the required spell effect definitions are registered in the linked spell effect definitions registry, then the
     * instructions are stored in a backlog against the potential spell effect's name.
     * @param effectName The name of the new spell effect.
     * @param effectDefinitions The unparsed string containing the information needed to construct a the spell effect
     * from spell effect definitions and modifiers.
     */
    public void load(String effectName, String effectDefinitions)
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
        
        synchronized(effects)
        {
            Collection<String> backloggedEffectsToRemove = new HashSet<String>();
            
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
    { synchronized(effects) { effects.clear(); backloggedEffects.clear(); } }
    
    /** Registers all default spell effects. */
    public void loadDefaultValues()
    {
        synchronized(effects)
        {
            clear();
            
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
        try
        {
            if(file.exists())
            {
                DataInputStream input = new DataInputStream(new FileInputStream(file));
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                
                try
                {
                    for(String line = ""; line != null; line = reader.readLine())
                        handleFileLine(line);
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
    
    /**
     * Loads a single line, e.g. from a file.
     * @param line The text to load as a spell effect.
     */
    protected void handleFileLine(String line)
    {
        String[] lineParts = line.split(":", 2);
        
        if(lineParts.length < 2)
        {
            System.out.println("Line cannot be split into spell effect name and definition: \n" + line);
            return;
        }
        
        load(lineParts[0], lineParts[1]);
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
        
        for(Map.Entry<String, SpellEffect> entry : effects.entrySet())
            effectStrings.add(entry.getValue().toString());
        
        for(Map.Entry<String, List<ConfiguredDefinitionInstruction>> entry : backloggedEffects.entrySet())
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
                
                sb.append(i.toString());
            }
        }
        
        return sb.toString();
    }
    
    public void fillFromString(String s)
    {
        BufferedReader reader = new BufferedReader(new StringReader(s));
        
        try
        {
            synchronized(effects)
            {
                effects.clear();
                
                for(String line = ""; line != null; line = reader.readLine())
                {
                    String[] parts = line.split(":", 2);

                    if(parts.length < 2)
                    {
                        System.out.println("Syncing server to client, the following line could not be split into spell "
                                         + "effect name and spell effect: " + line);

                        continue;
                    }

                    load(parts[0], parts[1]);
                }
            }
        }
        catch(IOException e)
        { throw new RuntimeException("IOException not currently handled. It shouldn't be thrown here anyway.", e); }
    }
}