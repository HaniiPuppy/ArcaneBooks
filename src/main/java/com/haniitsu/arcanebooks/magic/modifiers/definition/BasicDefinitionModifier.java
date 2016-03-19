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
        List<SpellEffectDefinitionModifier> args = new ArrayList<SpellEffectDefinitionModifier>(arguments);
        
        if(value != null)
            args.add(new ModifierValueDefinitionModifier(value));
        
        this.arguments = Collections.unmodifiableList(args);
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
    protected final String name;
    
    /** The other modifiers passed to this modifier. */
    protected final List<SpellEffectDefinitionModifier> arguments;
    
    /** The single value passed to the modifier. */
    protected final String value;
    
    private List<LogicalCheckDefinitionModifier> logicalChecksCache = null;
    
    @Override
    public String getName()
    { return name; }
    
    @Override
    public List<SpellEffectDefinitionModifier> getSubModifiers()
    { return arguments; }
    
    private void fillLogicalModifiersCache()
    {
        List<LogicalCheckDefinitionModifier> cache = new ArrayList<LogicalCheckDefinitionModifier>();
        
        for(SpellEffectDefinitionModifier i : arguments)
            if(i instanceof LogicalCheckDefinitionModifier)
                cache.add((LogicalCheckDefinitionModifier)i);
        
        logicalChecksCache = Collections.unmodifiableList(cache);
    }
    
    @Override
    public List<LogicalCheckDefinitionModifier> getLogicalModifiers()
    {
        if(logicalChecksCache == null)
            fillLogicalModifiersCache();
        
        return logicalChecksCache;
    }
    
    @Override
    public String getValue()
    { return value; }
    
    @Override
    public BasicDefinitionModifier getCopy()
    { return new BasicDefinitionModifier(name, value, arguments); }

    @Override
    public BasicDefinitionModifier getCopyWithNewModifiers(List<SpellEffectDefinitionModifier> newModifiers)
    { return new BasicDefinitionModifier(name, value, newModifiers); }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(name);
            
            for(SpellEffectDefinitionModifier modifier : arguments)
                if(modifier instanceof LogicalCheckDefinitionModifier)
                    sb.append('[').append(modifier.getName()).append(']');
            
            boolean atLeastOneModifier = false;
            
            for(SpellEffectDefinitionModifier modifier : arguments)
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
            
            if(value != null)
                sb.append(": ").append(value);
            
            return sb.toString();
    }
}