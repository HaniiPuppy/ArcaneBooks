package com.haniitsu.arcanebooks;

import com.haniitsu.arcanebooks.eventlisteners.PlayerJoinServerListener;
import com.haniitsu.arcanebooks.items.Registry;
import com.haniitsu.arcanebooks.packets.PlayerJoinPacket;
import com.haniitsu.arcanebooks.projectiles.SpellProjectileCommon;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.DimensionManager;

/**
 * Main class mod class instantiated by forge.
 */
@Mod(modid = ArcaneIndex.MOD_ID, name = ArcaneIndex.MOD_NAME, version = ArcaneIndex.VERSION)
public class ArcaneBooks
{
    /** The mod instance. */
    @Mod.Instance(ArcaneIndex.MOD_ID)
    public static ArcaneBooks instance;
    
    /** The object containing the data registries pertinent to this mod. */
    public Registries registries;
    
    /** The packet sender/receiver used by this mod. */
    public SimpleNetworkWrapper packetChannel;

    @Mod.EventHandler
    public void PreInitializationEvent(FMLPreInitializationEvent event)
    {
        // Is there a way to establish this without grabbing a world instance?
        boolean runningOnServer = !DimensionManager.getWorld(0).isRemote;
        
        Registry.registerItems();
        registries = new Registries();
        packetChannel = NetworkRegistry.INSTANCE.newSimpleChannel("ArcaneBooks");
        
        packetChannel.registerMessage(PlayerJoinPacket.Handler.class, PlayerJoinPacket.class, 1, Side.CLIENT);
        
        if(runningOnServer)
            registries.load();
        
        FMLCommonHandler.instance().bus().register(new PlayerJoinServerListener());
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
    
    /** Registers the mod's entities. */
    public void EntityRegistration()
    {
        SpellProjectileCommon.proxy.registerRenderThings();
        SpellProjectileCommon.proxy.registerSounds();
    }
}
