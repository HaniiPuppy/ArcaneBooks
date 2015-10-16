package com.haviitsu.arcanebooks.registries.magic;

import com.haviitsu.arcanebooks.misc.Location;
import com.haviitsu.arcanebooks.registries.magic.caster.SpellCaster;
import com.haviitsu.arcanebooks.registries.magic.modifiers.definition.SpellEffectDefinitionModifier;
import com.haviitsu.arcanebooks.registries.magic.modifiers.effect.SpellEffectModifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public class SpellEffect
{
    public SpellEffect(SpellEffectDefinition definition)
    {
        this.definition = definition;
        definitionModifiers = new ArrayList<SpellEffectDefinitionModifier>();
    }
    
    public SpellEffect(SpellEffectDefinition definition, Collection<? extends SpellEffectDefinitionModifier> modifiers)
    {
        this.definition = definition;
        this.definitionModifiers = new ArrayList<SpellEffectDefinitionModifier>(modifiers);
    }
    
    SpellEffectDefinition definition;
    List<SpellEffectDefinitionModifier> definitionModifiers;
    
    public void PerformEffect(SpellCaster caster,
                              Location burstLocation,
                              Block blockHit,
                              Entity entityHit,
                              Collection<? extends SpellEffectModifier> modifiers)
    {
        definition.PerformEffect(caster, burstLocation, blockHit, entityHit, definitionModifiers, modifiers);
    }
}