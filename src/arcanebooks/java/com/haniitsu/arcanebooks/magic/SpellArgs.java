package com.haniitsu.arcanebooks.magic;

import com.haniitsu.arcanebooks.magic.Spell.SpellCast;
import com.haniitsu.arcanebooks.misc.Location;
import com.haniitsu.arcanebooks.magic.caster.SpellCaster;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOE;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOEShape;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOESize;
import com.haniitsu.arcanebooks.misc.BlockLocation;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellEffectModifier;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellStrength;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellTarget;
import com.haniitsu.arcanebooks.misc.Direction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.Entity;

/**
 * A representation of a spell phrase cast, and what is passed to spell effect definitions to help determine their
 * behaviour.
 */
public class SpellArgs
{
    // This class file would be so much smaller if Java supported C#-style properties. The need for almost all of the
    // getters and setters would just -~*disappear*~-. ~Hanii
    
    // TO DO: Add a lock that stops the spell args object from being modified after it's already been evaluated and
    //        sent out to spell effect definitions? i.e. from being modified by those definitions?
    
    /** Creates a new SpellArgs object. */
    public SpellArgs() {}
    
    /** The spell effect modifiers used in the spell phrase that was cast. */
    List<SpellEffectModifier> effectModifiers;
    
    /** The location where the spell phrase was burst. */
    Location burstLocation;
    
    /** The direction the spell phrase was burst in. */
    Direction burstDirection;
    
    /** The blocks in range of this spell burst, as determined by the AOE, the AOE size, and the AOE shape. */
    Collection<BlockLocation> blocksHit = new ArrayList<BlockLocation>();
    
    /** The entities in range of this spell burst, as determined by the AOE, the AOE size, and the AOE shape. */
    Collection<Entity> entitiesHit = new ArrayList<Entity>();
    
    /** The caster that cast the spell. */
    SpellCaster caster;
    
    /** The object representing the spell cast as a whole, rather than the specific spell phrase cast. */
    SpellCast cast;
    
    /** The AOE (just the target, around the target, etc.) used by this spell phrase cast. */
    AOE aoe;
    
    /** The AOE size used by this spell phrase cast. */
    AOESize aoeSize;
    
    /** The AOE shape used by this spell phrase cast. */
    AOEShape aoeShape;
    
    /** How strong this spell phrase cast should be. */
    SpellStrength spellStrength;
    
    /** The targeting method used by this spell phrase cast. (e.g. on self, via projectile, etc.) */
    SpellTarget spellTarget;
    
    /** Messages passed on by previous spell effect definitions. */
    Map<String, SpellMessage> messages = new HashMap<String, SpellMessage>();
    
    /**
     * Specifies the modifiers used by the phrase cast.
     * @param modifiers The modifiers used.
     */
    public void setEffectModifiers(Collection<? extends SpellEffectModifier> modifiers)
    { this.effectModifiers = Collections.unmodifiableList(new ArrayList<SpellEffectModifier>(modifiers)); }
    
    /**
     * Specifies where this casting was burst.
     * @param location The burst location.
     */
    public void setBurstLocation(Location location)
    { burstLocation = location; }
    
    /**
     * Specifies the direction this spell phrase burst in.
     * @param direction The direction.
     */
    public void setBurstDirection(Direction direction)
    { burstDirection = direction; }
    
    /**
     * Specifies the blocks that were within range of the AOE.
     * @param blocks The blocks affected.
     */
    public void setBlocksHit(Collection<? extends BlockLocation> blocks)
    { blocksHit = new HashSet<BlockLocation>(blocks); }
    
    /**
     * Specifies the entities that were within range of the AOE.
     * @param entities The entities affected.
     */
    public void setEntitiesHit(Collection<? extends Entity> entities)
    { entitiesHit = new HashSet<Entity>(entities); }
    
    /**
     * Specifies the caster that cast the spell.
     * @param caster The caster.
     */
    public void setCaster(SpellCaster caster)
    { this.caster = caster; }
    
    /**
     * Specifies the spell cast this spell phrase cast is a part of.
     * @param cast The spell cast this spell phrase cast is a part of.
     */
    public void setCast(SpellCast cast)
    { this.cast = cast; }
    
    /**
     * Specifies the AOE.
     * @param aoe the AOE.
     */
    public void setAOE(AOE aoe)
    { this.aoe = aoe; }
    
    /**
     * Specifies the AOE size.
     * @param size The AOE size.
     */
    public void setAOESize(AOESize size)
    { this.aoeSize = size; }
    
    /**
     * Specifies the shape of the AOE.
     * @param shape The AOE shape.
     */
    public void setAOEShape(AOEShape shape)
    { this.aoeShape = shape; }
    
    /**
     * Specifies the strength of the spell.
     * @param strength The spell strength.
     */
    public void setSpellStrength(SpellStrength strength)
    { this.spellStrength = strength; }
    
    /**
     * Specifies the targeting mechanism used by this spell phrase cast.
     * @param target The spell target.
     */
    public void setSpellTarget(SpellTarget target)
    { this.spellTarget = target; }
    
    /**
     * Passes on a message which will be accessible to later spell effect definitions via .getMessage(string);
     * @param message The message to pass.
     * @return The message already present with the same name, or null if none was present. An object (as opposed to
     * null) being returned means the operation failed, as there was already a message with that name.
     */
    public SpellMessage passMessage(SpellMessage message)
    { return passMessage(message, false); }
    
    /**
     * Passes on a message which will be accessible to later spell effect definitions via .getMessage(string);
     * @param message The message to pass.
     * @param force Whether or not to overwrite any messages already present with the same name.
     * @return The message already present with the same name, or null if none was present. If the message isn't
     * forced, then this will be null if the operation succeeded, or another value otherwise.
     */
    public SpellMessage passMessage(SpellMessage message, boolean force)
    {
        if(force)
            return messages.put(message.getName(), message);
        
        SpellMessage oldMessage = messages.get(message.getName());
        
        if(oldMessage == null)
            messages.put(message.getName(), message);
        
        return oldMessage;
    }
    
    /**
     * Gets the modifiers used in the spell phrase cast.
     * @return The modifiers passed to the spell effect.
     */
    public List<SpellEffectModifier> getEffectModifiers()
    { return effectModifiers; }
    
    /**
     * Gets the location this spell phrase cast burst at.
     * @return The burst location.
     */
    public Location getBurstLocation()
    { return burstLocation; }
    
    /**
     * Gets the direction this spell phrase cast burst in.
     * @return The burst direction.
     */
    public Direction getBurstDirection()
    { return burstDirection; }
    
    /**
     * Gets the blocks within the AOE.
     * @return The blocks affected.
     */
    public Collection<BlockLocation> getBlocksHit()
    { return new ArrayList<BlockLocation>(blocksHit); }
    
    /**
     * Gets the entities within the AOE.
     * @return The entities affected.
     */
    public Collection<Entity> getEntitiesHit()
    { return new ArrayList<Entity>(entitiesHit); }
    
    /**
     * Gets the caster that cast the spell.
     * @return The caster.
     */
    public SpellCaster getCaster()
    { return caster; }
    
    /**
     * Gets the spell cast that this spell phrase cast is a part of.
     * @return The object representing the entire spell cast.
     */
    public SpellCast getCast()
    { return cast; }
    
    /**
     * Gets the AOE. (target-only, around the target, etc.)
     * @return The AOE.
     */
    public AOE getAOE()
    { return aoe; }
    
    /**
     * Gets the size of the AOE.
     * @return The AOE size.
     */
    public AOESize getAOESize()
    { return aoeSize; }
    
    /**
     * Gets the shape of the AOE.
     * @return The AOE shape.
     */
    public AOEShape getAOEShape()
    { return aoeShape; }
    
    /**
     * Gets the strength of the spell phrase being cast.
     * @return The spell strength.
     */
    public SpellStrength getSpellStrength()
    { return spellStrength; }
    
    /**
     * Gets the targeting mechanism used by this spell phrase cast.
     * @return The spell target.
     */
    public SpellTarget getSpellTarget()
    { return spellTarget; }
    
    /**
     * Gets any previously passed message with the passed name, or null if none exists.
     * @param name The name of the message to get.
     * @return Any previously passed message with the passed name, or null if none exists.
     */
    public SpellMessage getMessage(String name)
    { return messages.get(name); }
}