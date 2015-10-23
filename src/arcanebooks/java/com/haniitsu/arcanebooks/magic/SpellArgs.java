package com.haniitsu.arcanebooks.magic;

import com.haniitsu.arcanebooks.magic.Spell.SpellCast;
import com.haniitsu.arcanebooks.misc.Location;
import com.haniitsu.arcanebooks.magic.caster.SpellCaster;
import com.haniitsu.arcanebooks.misc.BlockLocation;
import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellEffectModifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.Entity;

public class SpellArgs
{
    // This class file would be so much smaller if Java supported C#-style properties. The need for almost all of the
    // getters and setters would just -~*disappear*~-. ~Hanii
    
    public SpellArgs() {}
    
    List<SpellEffectDefinitionModifier> definitionModifiers;
    List<SpellEffectModifier> effectModifiers;
    
    Location burstLocation;
    Collection<Location> affectedLocations = new ArrayList<Location>();
    
    Collection<BlockLocation> blocksHit = new ArrayList<BlockLocation>();
    Collection<Entity> entitiesHit = new ArrayList<Entity>();
    
    SpellCaster caster;
    SpellCast cast;
    
    Map<String, SpellArgsMessage> messages = new HashMap<String, SpellArgsMessage>();
    String logicalCheck;
    
    void setDefinitionModifiers(Collection<? extends SpellEffectDefinitionModifier> modifiers)
    { this.definitionModifiers = Collections.unmodifiableList(new ArrayList<SpellEffectDefinitionModifier>(modifiers)); }
    
    public void setEffectModifiers(Collection<? extends SpellEffectModifier> modifiers)
    { this.effectModifiers = Collections.unmodifiableList(new ArrayList<SpellEffectModifier>(modifiers)); }
    
    public void setBurstLocation(Location location)
    { burstLocation = location; }
    
    public void addAffectedLocation(Location location)
    { affectedLocations.add(location); }
    
    public void addBlockHit(BlockLocation block)
    { blocksHit.add(block); }
    
    public void addEntityHit(Entity entity)
    { entitiesHit.add(entity); }
    
    public void setCaster(SpellCaster caster)
    { this.caster = caster; }
    
    public void setCast(SpellCast cast)
    { this.cast = cast; }
    
    public SpellArgsMessage addMessage(SpellArgsMessage message)
    { return addMessage(message, false); }
    
    public SpellArgsMessage addMessage(SpellArgsMessage message, boolean force)
    {
        if(force)
            return messages.put(message.getName(), message);
        
        return messages.get(message.getName()) == null ? messages.put(message.getName(), message) : null;
    }
    
    public void setLogicalCheck(String check)
    { logicalCheck = check; }
    
    public List<SpellEffectDefinitionModifier> getDefinitionModifiers()
    { return definitionModifiers; }
    
    public List<SpellEffectModifier> getEffectModifiers()
    { return effectModifiers; }
    
    public Location getBurstLocation()
    { return burstLocation; }
    
    public Collection<Location> getAffectedLocations()
    { return new ArrayList<Location>(affectedLocations); }
    
    public Collection<BlockLocation> getBlocksHit()
    { return new ArrayList<BlockLocation>(blocksHit); }
    
    public Collection<Entity> getEntitiesHit()
    { return new ArrayList<Entity>(entitiesHit); }
    
    public SpellCaster getCaster()
    { return caster; }
    
    public SpellCast getCast()
    { return cast; }
    
    public SpellArgsMessage getMessage(String name)
    { return messages.get(name); }
    
    public String getLogicalCheck()
    { return logicalCheck; }
}