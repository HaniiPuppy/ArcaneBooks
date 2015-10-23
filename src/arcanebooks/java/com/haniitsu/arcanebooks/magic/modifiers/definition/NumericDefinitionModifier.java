package com.haniitsu.arcanebooks.magic.modifiers.definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import scala.actors.threadpool.Arrays;

public class NumericDefinitionModifier implements SpellEffectDefinitionModifier
{
    public NumericDefinitionModifier(double name)
    { init(name, null, null); }
    
    public NumericDefinitionModifier(double name, List<? extends SpellEffectDefinitionModifier> arguments)
    { init(name, null, arguments); }
    
    public NumericDefinitionModifier(double name, SpellEffectDefinitionModifier... arguments)
    { init(name, null, Arrays.asList(arguments)); }
    
    public NumericDefinitionModifier(double name, String value)
    { init(name, value, null); }
    
    public NumericDefinitionModifier(double name, String value, List<? extends SpellEffectDefinitionModifier> arguments)
    { init(name, value, arguments); }
    
    public NumericDefinitionModifier(double name, String value, SpellEffectDefinitionModifier... arguments)
    { init(name, value, Arrays.asList(arguments)); }
    
    private void init(double name, String value, List<? extends SpellEffectDefinitionModifier> arguments)
    {
        this.name = name;
        this.value = value;
        this.arguments = arguments == null
                         ? null
                         : Collections.unmodifiableList(new ArrayList<SpellEffectDefinitionModifier>(arguments));
    }
    
    protected double name;
    protected List<SpellEffectDefinitionModifier> arguments;
    protected String value;
    
    public boolean isWholeNumber()
    { return name % 1 == 0; }
    
    public double asDouble()
    { return name; }
    
    public int asInt()
    { return (int)name; }
    
    @Override
    public String getName()
    { return Double.toString(name); }
    
    @Override
    public List<SpellEffectDefinitionModifier> getArguments()
    { return arguments; }
    
    @Override
    public String getValue()
    { return value; }
}