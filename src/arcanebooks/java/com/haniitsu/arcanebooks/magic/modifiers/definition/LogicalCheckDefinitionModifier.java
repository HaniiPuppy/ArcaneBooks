package com.haniitsu.arcanebooks.magic.modifiers.definition;

import java.util.ArrayList;
import java.util.List;

/*

Special-case modifier. In text, this is the bit in square-brackets of a logical spell effect definition.

e.g. "detected" in: SomeEffect: if[detected](Damage: 5)

Should values/additional modifiers be allow with this, tatsu? thoughts?

*/

public class LogicalCheckDefinitionModifier implements SpellEffectDefinitionModifier
{
    public LogicalCheckDefinitionModifier(String name)
    { this.name = name; }
    
    final String name;
    
    @Override
    public String getName()
    { return name; }

    @Override
    public List<SpellEffectDefinitionModifier> getArguments()
    { return new ArrayList<SpellEffectDefinitionModifier>(); }

    @Override
    public String getValue()
    { return null; }
    
    @Override
    public String toString()
    { return name; }
}