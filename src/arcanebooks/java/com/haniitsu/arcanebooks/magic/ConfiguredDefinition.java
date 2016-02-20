package com.haniitsu.arcanebooks.magic;

import com.haniitsu.arcanebooks.magic.modifiers.definition.LogicalCheckDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A single spell effect definition, with value and possible definition modifiers added, some of which may be additional
 * configured spell effect definitions.
 */
public class ConfiguredDefinition implements SpellEffectDefinitionModifier
{
    /**
     * Creates a configured spell effect definition from a spell effect definition and the modifiers passed into it.
     * @param definition The core, unconfigured definition itself.
     * @param modifiers The definition modifiers, affecting how the definition behaves, to be considered in order.
     */
    public ConfiguredDefinition(SpellEffectDefinition definition,
                                SpellEffectDefinitionModifier... modifiers)
    { this(definition, null, modifiers); }
    
    /**
     * Creates a configured spell effect definition from a spell effect definition and the argument + modifiers passed
     * into it.
     * @param definition The core, unconfigured definition itself.
     * @param argumentValue The definition argument, usually affecting the behaviour of the most fundamental aspect of
     * the definition. (such as how much a "heal" effect heals by)
     * @param modifiers The definition modifiers, affecting how the definition behaves, to be considered in order.
     */
    public ConfiguredDefinition(SpellEffectDefinition definition,
                                String argumentValue,
                                SpellEffectDefinitionModifier... modifiers)
    { this(definition, argumentValue, Arrays.asList(modifiers)); }
    
    /**
     * Creates a configured spell effect definition from a spell effect definition and the modifiers passed into it.
     * @param definition The core, unconfigured definition itself.
     * @param modifiers The definition modifiers, affecting how the definition behaves, to be considered in order.
     */
    public ConfiguredDefinition(SpellEffectDefinition definition,
                                List<? extends SpellEffectDefinitionModifier> modifiers)
    { this(definition, null, modifiers); }
    
    /**
     * Creates a configured spell effect definition from a spell effect definition and the argument + modifiers passed
     * into it.
     * @param definition The core, unconfigured definition itself.
     * @param argumentValue The definition argument, usually affecting the behaviour of the most fundamental aspect of
     * the definition. (such as how much a "heal" effect heals by)
     * @param modifiers The definition modifiers, affecting how the definition behaves, to be considered in order.
     */
    public ConfiguredDefinition(SpellEffectDefinition definition,
                                String argumentValue,
                                List<? extends SpellEffectDefinitionModifier> modifiers)
    {
        this.definition = definition;
        this.argumentValue = argumentValue;
        this.defModifiers = Collections.unmodifiableList(new ArrayList<SpellEffectDefinitionModifier>(modifiers));
    }
    
    /** The core definition itself. */
    protected final SpellEffectDefinition definition;
    
    /** The definition modifiers, affecting how the definition behaves, to be considered in order. */
    protected final List<SpellEffectDefinitionModifier> defModifiers;
    
    /**
     * The definition argument, usually affecting the behaviour of the most fundamental aspect of the definition.
     * (such as how much a "heal" effect heals by)
     */
    protected final String argumentValue;
    
    /**
     * Gets the spell effect definition's name.
     * @return The name of the spell effect definition contained within.
     */
    @Override
    public String getName()
    { return definition.getName(); }
    
    /**
     * Gets the definition modifiers.
     * @return The definition modifiers, affecting the behaviour of the spell effect definition.
     */
    public List<SpellEffectDefinitionModifier> getModifiers()
    { return defModifiers; }
    
    /**
     * Gets the definition modifiers.
     * @note This method is an alias of .getModifiers(), and exists in this class solely to
     * adhere to the SpellEffectDefinitionModifier interface.
     * @return The definition modifiers, affecting the behaviour of the spell effect definition.
     */
    @Override
    public List<SpellEffectDefinitionModifier> getSubModifiers()
    { return getModifiers(); }
    
    /**
     * Gets the single argument value of the spell effect definition.
     * @return The single argument value passed to the spell effect definition.
     */
    @Override
    public String getValue()
    { return argumentValue; }
    
    @Override
    public ConfiguredDefinition getCopy()
    { return new ConfiguredDefinition(definition, argumentValue, defModifiers); }

    @Override
    public ConfiguredDefinition getCopyWithNewModifiers(List<SpellEffectDefinitionModifier> newModifiers)
    { return new ConfiguredDefinition(definition, argumentValue, newModifiers); }
    
    /**
     * Performs the spell effect definition's action for a spell cast.
     * @param spellArgs The spellargs object relating to the specific phrase cast that this performance will be related
     * to.
     */
    public void PerformEffect(SpellArgs spellArgs)
    { definition.PerformEffect(spellArgs, defModifiers); }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(definition.getName());
            
            for(SpellEffectDefinitionModifier modifier : defModifiers)
                if(modifier instanceof LogicalCheckDefinitionModifier)
                    sb.append('[').append(modifier.getName()).append(']');
            
            boolean atLeastOneModifier = false;
            
            for(SpellEffectDefinitionModifier modifier : defModifiers)
                if(!(modifier instanceof LogicalCheckDefinitionModifier))
                {
                    if(!atLeastOneModifier)
                    {
                        sb.append('(');
                        atLeastOneModifier = true;
                    }
                    else
                        sb.append(", ");
                    
                    sb.append(modifier.toString());
                }
            
            if(atLeastOneModifier)
                sb.append(')');
            
            if(argumentValue != null)
                sb.append(": ").append(argumentValue);
            
            return sb.toString();
    }
}