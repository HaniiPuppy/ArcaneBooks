package com.haniitsu.arcanebooks.magic.caster;

import com.haniitsu.arcanebooks.magic.mana.ManaStore;
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
}