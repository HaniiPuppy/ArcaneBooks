package com.haviitsu.arcanebooks.entities.registration.spellprojectile;

import com.haviitsu.arcanebooks.ArcaneIndex;
import cpw.mods.fml.common.SidedProxy;

public class SpellProjectileCommon
{
    public void registerRenderThings()
    { }
    
    public void registerSounds()
    { }
    
    @SidedProxy(clientSide = ArcaneIndex.PROJECTILE_CLIENT,
                serverSide = ArcaneIndex.PROJECTILE_COMMON)
    public static SpellProjectileCommon proxy;
}