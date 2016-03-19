package com.haniitsu.arcanebooks.magic.modifiers.definition;

import java.util.ArrayList;
import java.util.List;

/*

Special-case modifier. In text, this is the bit in square-brackets of a logical spell effect definition.

e.g. "detected" in: SomeEffect: if[detected](Damage: 5)

Should values/additional modifiers be allowed with this, tatsu? thoughts?

*/

/**
 * A special-case modifier, used for filling in arguments of a logical check (such as if/ifnot) definition or
 * definition modifier.
 */
public class LogicalCheckDefinitionModifier implements SpellEffectDefinitionModifier
{
    /**
     * Creates a modifier from the provided name.
     * @param name The name of the modifier.
     */
    public LogicalCheckDefinitionModifier(String name)
    { this.name = name; }
    
    /** The name of the modifier. */
    protected final String name;
    
    @Override
    public String getName()
    { return name; }

    /**
     * Gets the additional modifiers passed into this modifier, which will always yield an empty list, as a modifier of
     * this type can't hold anything except for the name. Exists solely to adhere to the SpellEffectDefinitionModifier
     * interface.
     * @return An empty List.
     */
    @Override
    public List<SpellEffectDefinitionModifier> getSubModifiers()
    { return new ArrayList<SpellEffectDefinitionModifier>(); }
    
    /**
     * Gets the additional logical check modifiers passed into this modifier, which will always yield an empty list, as
     * a modifier of this type can't hold anything except for the name. Exists solely to adhere to the
     * SpellEffectDefinitionModifier interface.
     * @return An empty list.
     */
    @Override
    public List<LogicalCheckDefinitionModifier> getLogicalModifiers()
    { return new ArrayList<LogicalCheckDefinitionModifier>(); }

    /**
     * Gets the single value argument of this modifier, which will always yield null, as a modifier of this type can't
     * hold anything except for the name. Exists solely to adhere to the SpellEffectDefinitionModifier interface.
     * @return null.
     */
    @Override
    public String getValue()
    { return null; }
    
    @Override
    public LogicalCheckDefinitionModifier getCopy()
    { return new LogicalCheckDefinitionModifier(name); }

    @Override
    public LogicalCheckDefinitionModifier getCopyWithNewModifiers(List<SpellEffectDefinitionModifier> newModifiers)
    { return new LogicalCheckDefinitionModifier(name); }
    
    /**
     * Gets the name of the modifier as a string.
     * @return This as a string.
     */
    @Override
    public String toString()
    { return name; }
}