package com.haniitsu.arcanebooks.registries.magic.caster;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class SpellCasterPlayer extends SpellCasterEntity
{
    public SpellCasterPlayer(EntityPlayer caster)
    { super(caster); }
    
    @Override
    public EntityPlayer getCasterEntity()
    { return (EntityPlayer)super.getCasterEntity(); }
    
    public UUID getId()
    { return getCasterEntity().getGameProfile().getId(); }
    
    public String getPlayerName()
    { return getCasterEntity().getGameProfile().getName(); }
}