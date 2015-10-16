package com.haviitsu.arcanebooks;

import com.haviitsu.arcanebooks.entities.registration.spellprojectile.SpellProjectileCommon;
import com.haviitsu.arcanebooks.items.Registry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Tatsu on 10/15/2015.
 */
@Mod(modid = ArcaneIndex.MOD_ID, name = ArcaneIndex.MOD_NAME, version = ArcaneIndex.VERSION)
public class ArcaneBooks
{
    @Mod.Instance(ArcaneIndex.MOD_ID)
    public static ArcaneBooks instance;
    

    @Mod.EventHandler
    public void PreInitializationEvent(FMLPreInitializationEvent event)
    {
        Registry.registerItems();
    }

    @Mod.EventHandler
    public void InitializationEvent(FMLInitializationEvent event)
    {
        EntityRegistration();
    }

    @Mod.EventHandler
    public void PostInitializationEvent(FMLPostInitializationEvent event)
    {

    }
    
    public void EntityRegistration()
    {
        SpellProjectileCommon.proxy.registerRenderThings();
        SpellProjectileCommon.proxy.registerSounds();
    }
}
