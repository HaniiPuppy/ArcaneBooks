package com.haniitsu.arcanebooks.magic.modifiers.definition;

import java.util.ArrayList;
import java.util.List;

public class ModifierValueDefinitionModifier implements SpellEffectDefinitionModifier
{
    public ModifierValueDefinitionModifier(String value)
    { this.value = value; }
    
    protected final String value;
    
    @Override
    public String getName()
    { return value; }

    @Override
    public List<SpellEffectDefinitionModifier> getSubModifiers()
    { return new ArrayList<SpellEffectDefinitionModifier>(); }

    @Override
    public String getValue()
    { return null; }

    @Override
    public SpellEffectDefinitionModifier getCopy()
    { return new ModifierValueDefinitionModifier(value); }

    @Override
    public SpellEffectDefinitionModifier getCopyWithNewModifiers(List<SpellEffectDefinitionModifier> newModifiers)
    { return getCopy(); }

    @Override
    public int hashCode()
    { return value.hashCode(); }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        
        return ((ModifierValueDefinitionModifier)obj).value.equals(this.value);
    }

    @Override
    public String toString()
    { return value; }
}