package com.haniitsu.arcanebooks.projectiles;

import com.haniitsu.arcanebooks.ArcaneBooks;
import cpw.mods.fml.common.SidedProxy;

// TO DO: Write, then document.

public class SpellProjectileCommon
{
    public void registerRenderThings()
    { }
    
    public void registerSounds()
    { }
    
    @SidedProxy(clientSide = ArcaneBooks.Strings.classPath_spellProjectile_client,
                serverSide = ArcaneBooks.Strings.classPath_spellProjectile_server)
    public static SpellProjectileCommon proxy;
}