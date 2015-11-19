package com.haniitsu.arcanebooks.magic.modifiers.definition;

import java.util.ArrayList;
import java.util.List;

public class NumericDefinitionModifier implements SpellEffectDefinitionModifier
{
    public NumericDefinitionModifier(double name)
    { this.name = name; }
    
    protected final double name;
    
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
    { return new ArrayList<SpellEffectDefinitionModifier>(); }
    
    @Override
    public String getValue()
    { return null; }
    
    @Override
    public String toString()
    { return Double.toString(name); }
}