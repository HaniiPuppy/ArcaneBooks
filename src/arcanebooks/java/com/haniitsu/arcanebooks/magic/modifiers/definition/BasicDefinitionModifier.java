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
    
    String name;
    List<SpellEffectDefinitionModifier> arguments;
    String value;
    
    @Override
    public String getName()
    { return name; }
    
    @Override
    public List<SpellEffectDefinitionModifier> getArguments()
    { return arguments; }
    
    @Override
    public String getValue()
    { return value; }
}