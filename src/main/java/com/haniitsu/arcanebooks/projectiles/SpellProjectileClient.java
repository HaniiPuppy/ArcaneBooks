package com.haniitsu.arcanebooks.projectiles;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSnowball;

// TO DO: Write, then document.

public class SpellProjectileClient extends SpellProjectileCommon
{
    private static final Item itemSnowball = new ItemSnowball();
    
    @Override
    public void registerRenderThings()
    {
        // TO DO: Make it not look like a snowball.
        // TO DO: make this actually fire properly...
        RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new RenderSnowball(itemSnowball));
        //RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new RenderItem().);
    }
    
    @Override
    public void registerSounds()
    {
        
    }
}