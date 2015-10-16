package com.haviitsu.arcanebooks.entities.registration.spellprojectile;

import cpw.mods.fml.common.SidedProxy;

public class SpellProjectileCommon
{
    public void registerRenderThings()
    { }
    
    public void registerSounds()
    { }
    
    @SidedProxy(clientSide = "com.haviitsu.arcanebooks.entities.registration.spellprojectile.GrenadeClient",
                serverSide = "com.haviitsu.arcanebooks.entities.registration.spellprojectile.GrenadeCommon")
    public static SpellProjectileCommon proxy;
}