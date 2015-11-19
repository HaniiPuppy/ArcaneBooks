package com.haniitsu.arcanebooks.magic.modifiers.definition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BasicDefinitionModifier implements SpellEffectDefinitionModifier
{
    public BasicDefinitionModifier(String name,
                                   String value,
                                   List<? extends SpellEffectDefinitionModifier> arguments)
    {
        this.name = name;
        this.value = value;
        this.arguments = Collections.unmodifiableList(new ArrayList<SpellEffectDefinitionModifier>(arguments));
    }
    
    public BasicDefinitionModifier(String name,
                                   List<? extends SpellEffectDefinitionModifier> arguments)
    { this(name, null, arguments); }
    
    public BasicDefinitionModifier(String name, String value, SpellEffectDefinitionModifier... arguments)
    { this(name, value, Arrays.asList(arguments)); }
    
    public BasicDefinitionModifier(String name, SpellEffectDefinitionModifier... arguments)
    { this(name, null, arguments); }
    
    final String name;
    final List<SpellEffectDefinitionModifier> arguments;
    final String value;
    
    @Override
    public String getName()
    { return name; }
    
    @Override
    public List<SpellEffectDefinitionModifier> getArguments()
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