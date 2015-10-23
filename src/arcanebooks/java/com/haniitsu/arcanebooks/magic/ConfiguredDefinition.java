package com.haniitsu.arcanebooks.magic;

import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfiguredDefinition implements SpellEffectDefinitionModifier
{
    public ConfiguredDefinition(SpellEffectDefinition definition,
                                SpellEffectDefinitionModifier... modifiers)
    { this(definition, null, modifiers); }
    
    public ConfiguredDefinition(SpellEffectDefinition definition,
                                String argumentValue,
                                SpellEffectDefinitionModifier... modifiers)
    { this(definition, argumentValue, Arrays.asList(modifiers)); }
    
    public ConfiguredDefinition(SpellEffectDefinition definition,
                                List<? extends SpellEffectDefinitionModifier> modifiers)
    { this(definition, null, modifiers); }
    
    public ConfiguredDefinition(SpellEffectDefinition definition,
                                String argumentValue,
                                List<? extends SpellEffectDefinitionModifier> modifiers)
    {
        this.definition = definition;
        this.argumentValue = argumentValue;
        this.defModifiers = Collections.unmodifiableList(new ArrayList<SpellEffectDefinitionModifier>(modifiers));
    }
    
    protected final SpellEffectDefinition definition;
    protected final List<SpellEffectDefinitionModifier> defModifiers;
    protected final String argumentValue;
    
    @Override
    public String getName()
    { return definition.getName(); }
    
    public List<SpellEffectDefinitionModifier> getModifiers()
    { return defModifiers; }
    
    // alias of getModifiers, solely to adhere to SpellEffectDefinitionModifier
    @Override
    public List<SpellEffectDefinitionModifier> getArguments()
    { return getModifiers(); }
    
    @Override
    public String getValue()
    { return argumentValue; }
    
    public void PerformEffect(SpellArgs spellArgs)
    {
        spellArgs.setDefinitionModifiers(defModifiers);
        definition.PerformEffect(spellArgs);
    }
}