package com.haniitsu.arcanebooks.magic.caster;

import com.haniitsu.arcanebooks.magic.Spell;
import com.haniitsu.arcanebooks.magic.mana.ManaStore;
import com.haniitsu.arcanebooks.misc.Direction;
import com.haniitsu.arcanebooks.misc.Location;
import com.haniitsu.arcanebooks.projectiles.EntitySpellProjectile;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.NotImplementedException;

public class SpellCasterEntity implements SpellCaster
{
    public SpellCasterEntity(Entity caster)
    { this.caster = caster; }
    
    Entity caster;
    
    public Entity getCasterEntity()
    { return caster; }
    
    public String getName()
    {
        if(caster instanceof EntityPlayer)
            return "Player";
        
        // return getCasterEntity().???; // How do I get an entity's name?
        throw new NotImplementedException("To be implemented");
    }

    @Override
    public ManaStore getMana()
    {
        throw new NotImplementedException("To be implemented");
    }
    
    @Override
    public Location getLocation()
    { return new Location(caster.dimension, caster.posX, caster.posY, caster.posZ); }
    
    @Override
    public Direction getDirection()
    { return new Direction(caster.rotationPitch, caster.rotationYaw); }

    @Override
    public EntitySpellProjectile launchSpellPhrases(Spell.Phrase... phrases)
    {
        throw new NotImplementedException("Not implemented yet.");
    }

    @Override
    public EntitySpellProjectile launchSpellPhrases(List<? extends Spell.Phrase> phrases)
    {
        throw new NotImplementedException("Not implemented yet.");
    }
}