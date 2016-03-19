package com.haniitsu.arcanebooks.magic.caster;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

/**
 * A wrapper for a player, allowing the casting of spells from that player.
 */
public class SpellCasterPlayer extends SpellCasterEntity
{
    /**
     * Creates the wrapper.
     * @param caster The player to be able to cast spells.
     */
    public SpellCasterPlayer(EntityPlayer caster)
    { super(caster); }
    
    /**
     * Gets the player entity capable of casting spells.
     * @return The player entity.
     */
    @Override
    public EntityPlayer getCasterEntity()
    { return (EntityPlayer)super.getCasterEntity(); }
    
    /**
     * Gets the player's UUID.
     * @return The player's UUID.
     */
    public UUID getId()
    { return getCasterEntity().getGameProfile().getId(); }
    
    /**
     * Gets the player's account name.
     * @return The player's name.
     */
    public String getPlayerName()
    { return getCasterEntity().getGameProfile().getName(); }
}