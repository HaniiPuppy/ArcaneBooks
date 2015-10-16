package com.haviitsu.arcanebooks.entities.registration.spellprojectile;

import com.haviitsu.arcanebooks.entities.EntitySpellProjectile;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSnowball;

public class SpellProjectileClient extends SpellProjectileCommon
{
    private static final Item itemSnowball = new ItemSnowball();
    
    @Override
    public void registerRenderThings()
    {
        // TO DO: Make it not look like a snowball.
        RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new RenderSnowball(itemSnowball));
    }
    
    @Override
    public void registerSounds()
    {
        
    }
}