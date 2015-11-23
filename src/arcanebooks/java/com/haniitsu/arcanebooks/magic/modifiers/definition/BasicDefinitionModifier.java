package com.haniitsu.arcanebooks.magic.modifiers.definition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The standard form of SpellEffectDefinitionModifier. Simply the name, along with any arguments and possible a value.
 */
public class BasicDefinitionModifier implements SpellEffectDefinitionModifier
{
    /**
     * Creates a modifier from the name, value, and additional modifiers provided.
     * @param name The name of the modifier.
     * @param value The single value of the modifier.
     * @param arguments The other modifiers passed to the modifier.
     */
    public BasicDefinitionModifier(String name,
                                   String value,
                                   List<? extends SpellEffectDefinitionModifier> arguments)
    {
        this.name = name;
        this.value = value;
        this.arguments = Collections.unmodifiableList(new ArrayList<SpellEffectDefinitionModifier>(arguments));
    }
    
    /**
     * Creates a modifier from the name and additional modifiers provided.
     * @param name The name of the modifier.
     * @param arguments The other modifiers passed to the modifier.
     */
    public BasicDefinitionModifier(String name,
                                   List<? extends SpellEffectDefinitionModifier> arguments)
    { this(name, null, arguments); }
    
    /**
     * Creates a modifier from the name, value, and additional modifiers provided.
     * @param name The name of the modifier.
     * @param value The single value of the modifier.
     * @param arguments The other modifiers passed to the modifier.
     */
    public BasicDefinitionModifier(String name, String value, SpellEffectDefinitionModifier... arguments)
    { this(name, value, Arrays.asList(arguments)); }
    
    /**
     * Creates a modifier from the name and additional modifiers provided.
     * @param name The name of the modifier.
     * @param arguments The other modifiers passed to the modifier.
     */
    public BasicDefinitionModifier(String name, SpellEffectDefinitionModifier... arguments)
    { this(name, null, arguments); }
    
    /** The name of the modifier. */
    final String name;
    
    /** The other modifiers passed to this modifier. */
    final List<SpellEffectDefinitionModifier> arguments;
    
    /** The single value passed to the modifier. */
    final String value;
    
    @Override
    public String getName()
    { return name; }
    
    @Override
    public List<SpellEffectDefinitionModifier> getSubModifiers()
    { return arguments; }
    
    @Override
    public String getValue()
    { return value; }
    
    @Override
    public String toString()
    {
        String asString = name;
        
        for(SpellEffectDefinitionModifier i : arguments)
            if(i instanceof LogicalCheckDefinitionModifier)
                asString += "[" + i.getName() + "]";
        
        boolean argumentsFound = false;
        
        for(SpellEffectDefinitionModifier i : arguments)
        {
            if(i instanceof LogicalCheckDefinitionModifier)
                continue;
            
            if(!argumentsFound)
            {
                argumentsFound = true;
                asString += "(";
            }
            
            asString += i.toString() + ", ";
        }
        
        if(argumentsFound)
        {
            asString = asString.substring(asString.length() - 2); // Remove the trailing ", ".
            asString += ")";
        }
        
        if(value != null && value.length() > 0)
            asString += ": " + value;
        
        return asString;
    }
}