package com.haniitsu.arcanebooks.magic.modifiers.definition;

import java.util.ArrayList;
import java.util.List;

/**
 * A numeric modifier passed to a definition or other modifier, representing a passed numeric value with no additional
 * elements.
 */
public class NumericDefinitionModifier implements SpellEffectDefinitionModifier
{
    /**
     * Creates a new modifier from the passed numeric name.
     * @param name The name to use.
     */
    public NumericDefinitionModifier(double name)
    { this.name = name; }
    
    /** The name. */
    protected final double name;
    
    /**
     * Gets whether or not the value (name) of the numeric modifier is a whole number.
     * @return True if it's a whole number. Otherwise, false.
     */
    public boolean isWholeNumber()
    { return name % 1 == 0; }
    
    /**
     * Gets the value (name) of the modifier as a double.
     * @return This as a double.
     */
    public double asDouble()
    { return name; }
    
    /**
     * Gets the value (name) of the modifier as an integer.
     * @return This as an integer.
     */
    public int asInt()
    { return (int)name; }
    
    /**
     * Gets the value (name) of the modifier as a string.
     * @return This as a string.
     */
    @Override
    public String getName()
    { return Double.toString(name); }
    
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
     * Gets the logical check modifiers passed into this modifier, which will always yield an empty list, as a modifier
     * of this type can't hold anything except for the name. Exists solely to adhere to the
     * SpellEffectDefinitionModifier interface.
     * @return An empty List.
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
    public NumericDefinitionModifier getCopy()
    { return new NumericDefinitionModifier(name); }

    @Override
    public NumericDefinitionModifier getCopyWithNewModifiers(List<SpellEffectDefinitionModifier> newModifiers)
    { return new NumericDefinitionModifier(name); }
    
    /**
     * Gets the value (name) of the modifier as a string.
     * @return This as a string.
     */
    @Override
    public String toString()
    { return Double.toString(name); }
}