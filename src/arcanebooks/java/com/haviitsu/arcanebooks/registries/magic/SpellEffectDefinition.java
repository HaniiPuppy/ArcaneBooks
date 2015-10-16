package com.haviitsu.arcanebooks.registries.magic;

import com.haviitsu.arcanebooks.misc.Location;
import com.haviitsu.arcanebooks.registries.magic.caster.SpellCaster;
import com.haviitsu.arcanebooks.registries.magic.modifiers.definition.SpellEffectDefinitionModifier;
import com.haviitsu.arcanebooks.registries.magic.modifiers.effect.SpellEffectModifier;
import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public abstract class SpellEffectDefinition
{
    public SpellEffectDefinition(String name)
    { this.name = name; }
    
    String name;
    
    public String getName()
    { return name; }
    
    public abstract void PerformEffect(SpellCaster caster,
                                       Location burstLocation,
                                       Block blockHit,
                                       Entity entityHit,
                                       Collection<? extends SpellEffectDefinitionModifier> definitionModifiers,
                                       Collection<? extends SpellEffectModifier> effectModifiers);
}