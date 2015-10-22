package com.haniitsu.arcanebooks.magic;

import com.haniitsu.arcanebooks.misc.Location;
import com.haniitsu.arcanebooks.magic.caster.SpellCaster;
import com.haniitsu.arcanebooks.misc.BlockLocation;
import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellEffectModifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.Entity;

public class SpellArgs
{
    // This class file would be so much smaller if Java supported C#-style properties. The need for almost all of the
    // getters and setters would just -~*disappear*~-. ~Hanii
    
    public SpellArgs()
    {
        messages = new HashMap<String, SpellArgsMessage>();
    }
    
    Collection<SpellEffectDefinitionModifier> definitionModifiers;
    Collection<SpellEffectModifier> effectModifiers;
    
    Location burstLocation;
    Location affectedLocation;
    
    BlockLocation blockHit;
    Entity entityHit;
    
    SpellCaster caster;
    
    Map<String, SpellArgsMessage> messages;
    
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
    
    public SpellArgsMessage addMessage(SpellArgsMessage message)
    { return addMessage(message, false); }
    
    public SpellArgsMessage addMessage(SpellArgsMessage message, boolean force)
    {
        if(force)
            return messages.put(message.getName(), message);
        
        return messages.get(message.getName()) == null ? messages.put(message.getName(), message) : null;
    }
    
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
    
    public SpellArgsMessage getMessage(String name)
    { return messages.get(name); }
}