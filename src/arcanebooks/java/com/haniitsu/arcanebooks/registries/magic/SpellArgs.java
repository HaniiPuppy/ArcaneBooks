package com.haniitsu.arcanebooks.registries.magic;

import com.haniitsu.arcanebooks.misc.Location;
import com.haniitsu.arcanebooks.registries.magic.caster.SpellCaster;
import com.haniitsu.arcanebooks.misc.BlockLocation;
import com.haniitsu.arcanebooks.registries.magic.modifiers.definition.SpellEffectDefinitionModifier;
import com.haniitsu.arcanebooks.registries.magic.modifiers.effect.SpellEffectModifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.entity.Entity;

public class SpellArgs
{
    public SpellArgs() {}
    
    Collection<SpellEffectDefinitionModifier> definitionModifiers;
    Collection<SpellEffectModifier> effectModifiers;
    
    Location burstLocation;
    Location affectedLocation;
    
    BlockLocation blockHit;
    Entity entityHit;
    
    SpellCaster caster;
    
    void setDefinitionModifiers(Collection<? extends SpellEffectDefinitionModifier> modifiers)
    { this.definitionModifiers = Collections.unmodifiableList(new ArrayList<SpellEffectDefinitionModifier>(modifiers)); }
    
    public void setEffectModifiers(Collection<? extends SpellEffectModifier> modifiers)
    { this.effectModifiers = Collections.unmodifiableList(new ArrayList<SpellEffectModifier>(modifiers)); }
    
    public void setBurstLocation(Location location)
    { burstLocation = location; }
    
    public void setAffectedLocation(Location location)
    { affectedLocation = location; }
    
    public void setBlockHit(BlockLocation block)
    { blockHit = block; }
    
    public void setEntityHit(Entity entity)
    { entityHit = entity; }
    
    public Collection<SpellEffectDefinitionModifier> getDefinitionModifiers()
    { return definitionModifiers; }
    
    public Collection<SpellEffectModifier> getEffectModifiers()
    { return effectModifiers; }
    
    public Location getBurstLocation()
    { return burstLocation; }
    
    public Location getAffectedLocation()
    { return affectedLocation; }
    
    public BlockLocation getBlockHit()
    { return blockHit; }
    
    public Entity getEntityHit()
    { return entityHit; }
    
    public SpellCaster getCaster()
    { return caster; }
}