package com.haniitsu.arcanebooks.registries;

import com.google.common.primitives.Doubles;
import com.haniitsu.arcanebooks.magic.ConfiguredDefinition;
import com.haniitsu.arcanebooks.magic.SpellEffect;
import com.haniitsu.arcanebooks.magic.SpellEffectDefinition;
import com.haniitsu.arcanebooks.magic.modifiers.definition.BasicDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.LogicalCheckDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.NumericDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import com.haniitsu.arcanebooks.misc.UtilMethods;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
            this.modifiers = Collections.unmodifiableList(new ArrayList<SpellEffectDefinitionModifier>());
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
            this.modifiers = Collections.unmodifiableList(new ArrayList<SpellEffectDefinitionModifier>(arguments));
        }
        
        /** The name of this conf. def. instruction/the potential conf. definition's name. */
        final String definitionName;
        
        /** The submodifiers of this modifier/the modifier that will be created from this one. */
        final List<SpellEffectDefinitionModifier> modifiers;
        
        /** The modifier value of this modifier/the modifier that will be created from this one. */
        final String value;
        
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

        @Override
        public String getValue()
        { return value; }

        @Override
        public SpellEffectDefinitionModifier getCopy()
        { return new ConfiguredDefinitionInstruction(definitionName, value, modifiers); }

        @Override
        public SpellEffectDefinitionModifier getCopyWithNewModifiers(List<SpellEffectDefinitionModifier> newModifiers)
        { return new ConfiguredDefinitionInstruction(definitionName, value, newModifiers); }
    }
    
    public SpellEffectRegistry(SpellEffectDefinitionRegistry definitionRegistry)
    { linkedDefinitionRegistry = definitionRegistry; }
    
    final protected Map<String, SpellEffect> effects = new HashMap<String, SpellEffect>();
    final protected Map<String, List<ConfiguredDefinitionInstruction>> backloggedEffects
        = new HashMap<String, List<ConfiguredDefinitionInstruction>>();
    final protected SpellEffectDefinitionRegistry linkedDefinitionRegistry;
    
    public SpellEffect getEffect(String name)
    { synchronized(effects) { return effects.get(name); } }
    
    public Collection<SpellEffect> getEffects()
    { return new ArrayList<SpellEffect>(effects.values()); }
    
    public void register(SpellEffect effect)
    { synchronized(effects) { effects.put(effect.getName(), effect); } }
    
    public void load(String effectName, String effectDefinitions)
    {
        List<String> definitionStrings = UtilMethods.splitCSVLine(effectDefinitions);
        List<ConfiguredDefinitionInstruction> defInstructions = new ArrayList<ConfiguredDefinitionInstruction>();
        
        for(String i : definitionStrings)
            defInstructions.add((ConfiguredDefinitionInstruction)getModifierFromString(i.trim(), true));
        
        SpellEffect effect = realise(effectName, defInstructions);
        
        if(effect == null)
            synchronized(backloggedEffects)
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
    
    public void updateBackloggedEffects()
    {
        Map<String, SpellEffect> newEffects = new HashMap<String, SpellEffect>();
        
        synchronized(backloggedEffects)
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
        }
        
        synchronized(effects)
        { effects.putAll(newEffects); }
    }
    
    private String getArgumentName(String argumentString)
    {
        int breakPoint = -1;
        
        for(int i = 0; i < argumentString.length(); i++)
            if(argumentString.charAt(i) == '[' || argumentString.charAt(i) == '(' || argumentString.charAt(i) == ':')
            {
                breakPoint = i;
                break;
            }
        
        if(breakPoint < 0)
            return null;
        
        return argumentString.substring(0, breakPoint);
    }
    
    private String getValueString(String argumentString)
    {
        int breakPoint = -1;
        
        for(int i = argumentString.length() - 1; i >= 0; i--)
            if(argumentString.charAt(i) == ':')
            {
                breakPoint = i;
                break;
            }
        
        if(breakPoint < 0)
            return null;
        
        return argumentString.substring(breakPoint + 1);
    }
    
    private SpellEffectDefinitionModifier getModifierFromString(String modifierString, boolean definitelyEffect)
    {
        String modifierName = getArgumentName(modifierString).trim();
        Double numericValue = definitelyEffect ? null : Doubles.tryParse(modifierName);
        // faster than using Double.parseDouble with exception handling.
        
        if(numericValue != null)
            return new NumericDefinitionModifier(numericValue);
        
        String[] logicalAndArgsStrings = getLogicalCheckAndArgsStrings(modifierString);
        String logicalCheckString = logicalAndArgsStrings[0];
        String argsString = logicalAndArgsStrings[1];
        String valueString = getValueString(modifierString);
        
        List<SpellEffectDefinitionModifier> subModifiers = new ArrayList<SpellEffectDefinitionModifier>();
        List<String> argStrings = argsString == null ? new ArrayList<String>() : UtilMethods.splitCSVLine(argsString);
        
        if(logicalCheckString != null)
            subModifiers.add(new LogicalCheckDefinitionModifier(logicalCheckString));
        
        for(int i = 0; i < argStrings.size(); i++)
            subModifiers.add(getModifierFromString(argStrings.get(i), false));
        
        if(definitelyEffect || Character.isUpperCase(modifierName.codePointAt(0)))
            return valueString == null ? new ConfiguredDefinitionInstruction(modifierName, subModifiers)
                                       : new ConfiguredDefinitionInstruction(modifierName, valueString, subModifiers);
        else
            return valueString == null ? new BasicDefinitionModifier(modifierName, subModifiers)
                                       : new BasicDefinitionModifier(modifierName, valueString, subModifiers);
    }
    
    /**
     * Gets the logical check and (unsplit) args strings from the full modifier string.
     * @note This was originally going to be two methods, but doing so resulted in duplication of work, since getting
     * the args string requires knowing the positions of the starting and ending chars for the logical check string.
     * @note The logical check string takes precedence when determining positions with brackets and square brackets.
     * That is, the logical check substring is determined first, and the args string will not overlap nor contain the
     * logical check string - its start or end may not be found within it, and the logical check string will not be
     * entirely within the args string.
     * @note The logical check string may contain a [ or ] character, but ] must be escaped with a backslash character.
     * @example ModifierNameWhichIsIgnored[SomeLogic](submodifier1, Submodifier2[PartOf](argString))
     * @param modifierString The full modifier string that may contain args and logical check strings.
     * @return An array containing two strings: [0] is the logical check string, and [1] is the args string. Either
     * may be null, and a null in that position means that there way so valid logical check or args string.
     */
    private String[] getLogicalCheckAndArgsStrings(String modifierString)
    {
        String logicalCheckString = null;
        String argsString = null;
        
        int logicalCheckOpeningCharPosition = -1;
        int logicalCheckClosingCharPosition = -1;
        boolean logicalCheckCancelNext = false;
        int argsOpeningCharPosition = -1;
        int argsClosingCharPosition = -1;
        
        for(int i = 0; i < modifierString.length(); i++)
        {
            if(logicalCheckOpeningCharPosition < 0)
            {
                if(modifierString.charAt(i) == '[')
                    logicalCheckOpeningCharPosition = i;
            }
            else
            {
                if(modifierString.charAt(i) == ']' && !logicalCheckCancelNext)
                {
                    logicalCheckClosingCharPosition = i;
                    break;
                }
            }
            
            if(modifierString.charAt(i) == '\\' && !logicalCheckCancelNext)
                logicalCheckCancelNext = true;
            else if(logicalCheckCancelNext)
                logicalCheckCancelNext = false;
        }
        
        if(logicalCheckClosingCharPosition >= 0)
            logicalCheckString = UtilMethods.deEscape(modifierString.substring(logicalCheckOpeningCharPosition + 1,
                                                                               logicalCheckClosingCharPosition));
        
        for(int i = 0; i < modifierString.length(); i++)
        {
            if(i >= logicalCheckOpeningCharPosition && i < logicalCheckClosingCharPosition)
            {
                i = logicalCheckClosingCharPosition;
                argsOpeningCharPosition = -1;
                continue;
            }
            
            if(argsOpeningCharPosition < 0 && modifierString.charAt(i) == '(')
                argsOpeningCharPosition = i;
            
            if(i > logicalCheckClosingCharPosition && argsOpeningCharPosition >= 0)
                break;
        }
        
        if(argsOpeningCharPosition >= 0)
        {
            for(int i = modifierString.length() - 1; i >= 0; i--)
            {
                if(i <= logicalCheckClosingCharPosition && i > logicalCheckOpeningCharPosition)
                {
                    i = logicalCheckOpeningCharPosition;
                    argsClosingCharPosition = -1;
                    continue;
                }

                if(argsClosingCharPosition < 0 && modifierString.charAt(i) == ')')
                    argsClosingCharPosition = i;

                if(i < logicalCheckOpeningCharPosition && argsClosingCharPosition >= 0)
                    break;
            }

            if(argsClosingCharPosition >= 0)
                argsString = modifierString.substring(argsOpeningCharPosition + 1, argsClosingCharPosition);
        }
        
        return new String[]{ logicalCheckString, argsString };
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
    
    public void handleFileLine(String line)
    {
        String[] lineParts = line.split(":", 2);
        
        if(lineParts.length < 2)
            return;
        
        load(lineParts[0], lineParts[1]);
    }
    
    public void saveToFile(File file)
    { throw new NotImplementedException("Not implemented yet."); }
}