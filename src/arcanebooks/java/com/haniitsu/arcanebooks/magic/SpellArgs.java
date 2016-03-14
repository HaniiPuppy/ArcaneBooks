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
import net.minecraft.entity.EntityLivingBase;

/**
 * A representation of a spell phrase cast, and what is passed to spell effect definitions to help determine their
 * behaviour.
 */
public class SpellArgs
{
    // This class file would be so much smaller if Java supported C#-style properties. The need for almost all of the
    // getters and setters would just -~*disappear*~-. ~Hanii
    
    /**
     * Creates a new SpellArgs object with the passed initial values.
     * @param effect The spell effect concerned.
     * @param caster The spellcaster that cast the spell this is a result of.
     * @param cast The object representing the actual spellcast.
     * @param modifiers The spell effect modifiers passed to the spell effect.
     * @param location The location that the spell effect burst at.
     * @param direction The direction the spell effect burst in.
     * @param aoe The AOE - whether it's just the target, just around the target, or both.
     * @param aoeSize The size of the AOE.
     * @param aoeShape The shape of the AOE.
     * @param spellStrength The strength of the spell.
     * @param spellTarget The spell target. (if it was cast on the caster, as a projectile, etc.)
     * @param blocksAffected The blocks affected by this spell effect burst.
     * @param entitiesAffected The entities affected by this spell effect burst.
     * @param blockHit The block hit by this spell effect burst, or the casting block if not projectile, or null if it's
     * not a block.
     * @param entityHit The entity hit by this spell effect burst, or the casting entity if not projectile, or null if
     * it's not an entity.
     */
    public SpellArgs(SpellEffect effect, SpellCaster caster, SpellCast cast,
                     Collection<? extends SpellEffectModifier> modifiers,
                     Location location, Direction direction,
                     AOE aoe, AOESize aoeSize, AOEShape aoeShape, SpellStrength spellStrength, SpellTarget spellTarget,
                     Collection<? extends BlockLocation> blocksAffected, Collection<? extends Entity> entitiesAffected,
                     BlockLocation blockHit, Entity entityHit)
    {
        this.effect           = effect;
        this.caster           = caster;
        this.cast             = cast;
        this.effectModifiers  = Collections.unmodifiableList(new ArrayList<SpellEffectModifier>(modifiers));
        
        this.burstLocation    = location;
        this.burstDirection   = direction;
        
        this.aoe              = aoe;
        this.aoeSize          = aoeSize;
        this.aoeShape         = aoeShape;
        this.spellStrength    = spellStrength;
        this.spellTarget      = spellTarget;
        
        this.blocksAffected   = Collections.unmodifiableCollection(new HashSet<BlockLocation>(blocksAffected));
        this.entitiesAffected = Collections.unmodifiableCollection(new HashSet<Entity>(entitiesAffected));
        
        this.blockHit         = blockHit;
        this.entityHit        = entityHit;
    }
    
    /** The spell effect modifiers used in the spell phrase that was cast. */
    final List<SpellEffectModifier> effectModifiers;
    
    /** The location where the spell phrase was burst. */
    final Location burstLocation;
    
    /** The direction the spell phrase was burst in. */
    final Direction burstDirection;
    
    /** The block hit by the spell effect. */
    final BlockLocation blockHit;
    
    /** The entity hit by the spell effect. */
    final Entity entityHit;
    
    /** The blocks in range of this spell burst, as determined by the AOE, the AOE size, and the AOE shape. */
    final Collection<BlockLocation> blocksAffected;
    
    /** The entities in range of this spell burst, as determined by the AOE, the AOE size, and the AOE shape. */
    final Collection<Entity> entitiesAffected;
    
    /** The caster that cast the spell. */
    final SpellCaster caster;
    
    /** The object representing the spell cast as a whole, rather than the specific spell phrase cast. */
    final SpellCast cast;
    
    /** The actual spell effect being burst. */
    final SpellEffect effect;
    
    /** The AOE (just the target, around the target, etc.) used by this spell phrase cast. */
    final AOE aoe;
    
    /** The AOE size used by this spell phrase cast. */
    final AOESize aoeSize;
    
    /** The AOE shape used by this spell phrase cast. */
    final AOEShape aoeShape;
    
    /** How strong this spell phrase cast should be. */
    final SpellStrength spellStrength;
    
    /** The targeting method used by this spell phrase cast. (e.g. on self, via projectile, etc.) */
    final SpellTarget spellTarget;
    
    /** Messages passed on by previous spell effect definitions. */
    final Map<String, SpellMessage> messages = new HashMap<String, SpellMessage>();
    
    /**
     * Passes on a message which will be accessible to later spell effect definitions via .getMessage(string);
     * @param message The message to pass.
     * @return The message already present with the same name, or null if none was present. An object (as opposed to
     * null) being returned means the operation failed, as there was already a message with that name.
     */
    public SpellMessage passMessage(SpellMessage message)
    { return passMessage(message, false); }
    
    /**
     * Passs on a basic message with the passed name which will be accessible to later spell effect definition via
     * .getMessage(string);
     * @param message The message to pass.
     * @return The message already present with the same name, or null if none was present. An object (as opposed to
     * null) being returned means the operation failed, as there was already a message with that name.
     */
    public SpellMessage passMessage(String message)
    { return passMessage(new SpellMessage(message)); }
    
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
        
        this.getCast().passMessage(message, force);
        return oldMessage;
    }
    
    /**
     * Passed on a basic message with the passed name which will be accessible to later spell effect definition via
     * .getMessage(string);
     * @param message The message to pass.
     * @param force Whether or not to overwrite any messages already present with the same name.
     * @return The message already present with the same name, or null if none was present. If the message isn't
     * forced, then this will be null if the operation succeeded, or another value otherwise.
     */
    public SpellMessage passMessage(String message, boolean force)
    { return passMessage(new SpellMessage(message), force); }
    
    /**
     * Gets the actual spell effect being burst, in this instance.
     * @return The concerned spell effect.
     */
    public SpellEffect getSpellEffect()
    { return effect; }
    
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
     * Gets the block hit by the spell effect, or null if a block wasn't hit.
     * @return The location of the block hit by the spell effect, or null if no block was hit. (i.e. if an entity was
     * hit instead) If the spell effect isn't a projectile, will be the casting block, or null if the caster wasn't a
     * block.
     */
    public BlockLocation getBlockHit()
    { return blockHit; }
    
    /**
     * Gets the entity hit by the spell effect, or null if an entity wasn't hit.
     * @return The entity hit by the spell effect, or null if no entity was hit. (i.e. if a block was hit instead) If
     * the spell effect isn't a projectile, will be the casting entity, or null if the caster wasn't an entity.
     */
    public Entity getEntityHit()
    { return entityHit; }
    
    /**
     * Gets the blocks within the AOE.
     * @return The blocks affected.
     */
    public Collection<BlockLocation> getBlocksAffected()
    { return new ArrayList<BlockLocation>(blocksAffected); }
    
    /**
     * Gets the entities within the AOE.
     * @return The entities affected.
     */
    public Collection<Entity> getEntitiesAffected()
    { return new ArrayList<Entity>(entitiesAffected); }
    
    public Collection<EntityLivingBase> getMobsAffected()
    {
        List<EntityLivingBase> mobs = new ArrayList<EntityLivingBase>();
        
        for(Entity i : getEntitiesAffected())
            if(i instanceof EntityLivingBase)
                mobs.add((EntityLivingBase)i);
        
        return mobs;
    }
    
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