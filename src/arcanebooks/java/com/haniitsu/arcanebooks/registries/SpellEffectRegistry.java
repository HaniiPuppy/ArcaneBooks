package com.haniitsu.arcanebooks.registries;

import com.google.common.primitives.Doubles;
import com.haniitsu.arcanebooks.magic.SpellEffect;
import com.haniitsu.arcanebooks.magic.modifiers.definition.BasicDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.LogicalCheckDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.NumericDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import com.haniitsu.arcanebooks.misc.UtilMethods;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;

public class SpellEffectRegistry
{
    /**
     * Replacement for ConfiguredDefinition that uses the name of the spell effect definition that may or may not have
     * been registered yet, rather than a class instance. Used for eventually constructing an actual
     * ConfiguredDefinition object, once the required SpellEffectDefinition is actually registered.
     */
    private static class ConfiguredDefinitionInstruction implements SpellEffectDefinitionModifier
    {
        public ConfiguredDefinitionInstruction(String name)
        {
            this.definitionName = name;
            this.value = null;
            this.modifiers = null;
        }
        
        public ConfiguredDefinitionInstruction(String name, String value)
        {
            this.definitionName = name;
            this.value = value;
            this.modifiers = null;
        }
        
        public ConfiguredDefinitionInstruction(String name, SpellEffectDefinitionModifier... arguments)
        { this(name, null, Arrays.asList(arguments)); }
        
        public ConfiguredDefinitionInstruction(String name, List<SpellEffectDefinitionModifier> arguments)
        { this(name, null, arguments); }
        
        public ConfiguredDefinitionInstruction(String name, String value, SpellEffectDefinitionModifier... arguments)
        { this(name, value, Arrays.asList(arguments)); }
        
        public ConfiguredDefinitionInstruction(String name, String value, List<SpellEffectDefinitionModifier> arguments)
        {
            this.definitionName = name;
            this.value = value;
            this.modifiers = Collections.unmodifiableList(new ArrayList<SpellEffectDefinitionModifier>(arguments));
        }
        
        String definitionName;
        
        List<SpellEffectDefinitionModifier> modifiers;
        
        String value;
        
        @Override
        public String getName()
        { return definitionName; }
        
        public List<SpellEffectDefinitionModifier> getModifiers()
        { return modifiers; }

        @Override
        public List<SpellEffectDefinitionModifier> getSubModifiers()
        { return getModifiers(); }

        @Override
        public String getValue()
        { return value; }
    }
    
    public SpellEffectRegistry() {}
    
    final protected Map<String, SpellEffect> effects = new HashMap<String, SpellEffect>();
    final protected Map<String, List<ConfiguredDefinitionInstruction>> backloggedEffects
        = new HashMap<String, List<ConfiguredDefinitionInstruction>>();
    
    public SpellEffect getEffect(String name)
    { synchronized(effects) { return effects.get(name); } }
    
    public Collection<SpellEffect> getEffects()
    { return new ArrayList<SpellEffect>(effects.values()); }
    
    public void register(SpellEffect effect)
    { synchronized(effects) { effects.put(effect.getName(), effect); } }
    
    public void load(String effectName, String effectDefinitions)
    {
        throw new NotImplementedException("To be implemented.");
        
        /*
        
        This method should:
            split effectDefinitions with UtilMethods.splitCSVLine(String)
            Trim each resultant string.
            Turn each resultant string into a configureddefinitioninstruction with getModifierFromString.
            Use isRealisable to check if the resulting list from above is compilable into a single spell effect.
            if it is, do so and store the spell effect in effects.
            else, store the name and the resulting list in backloggedEffects.
        
        */
    }
    
    /**
     * Checks whether or not a spell effect with the passed configured definition instructions is currently able to be
     * turned into an actual, effective SpellEffect - that is, if all of the spell effect definitions used by the
     * purported spell effect have been registered in the spell effect definitions registry.
     * @param definitionInstructions The instructions representing the spell effect configured definitions.
     * @return True if the purported spell effect can be turned into an actual one. That is, if all spell effect
     * definitions referred to have been registered. Otherwise, false.
     */
    private boolean isRealisable(List<ConfiguredDefinitionInstruction> definitionInstructions)
    {
        throw new NotImplementedException("To be implemented");
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
        
        String logicalCheckString = UtilMethods.getTextBetween(modifierString, '[', ']');
        String argsString = UtilMethods.getTextBetween(modifierString, '(', ')');
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
    
    public void LoadFromFile(File file)
    {
        throw new NotImplementedException("Not implemented yet.");
    }
}