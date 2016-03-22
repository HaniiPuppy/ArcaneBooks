package com.haniitsu.arcanebooks;

import com.haniitsu.arcanebooks.eventlisteners.PlayerJoinServerListener;
import com.haniitsu.arcanebooks.items.ItemSpellBook;
import com.haniitsu.arcanebooks.items.ItemSpellScroll;
import com.haniitsu.arcanebooks.items.RuneStoneBasic;
import com.haniitsu.arcanebooks.items.RuneStoneIntricate;
import com.haniitsu.arcanebooks.misc.UtilMethods;
import com.haniitsu.arcanebooks.packets.SpellEffectsAddedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsBacklogClearedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsClearedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsRemovedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsSyncPacket;
import com.haniitsu.arcanebooks.projectiles.SpellProjectileCommon;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;

/**
 * Main class mod class instantiated by forge.
 */
@Mod(modid = ArcaneBooks.Strings.modId, name = ArcaneBooks.Strings.modName, version = ArcaneBooks.Strings.modVersion)
public class ArcaneBooks
{
    public static class Strings
    {
        public static final String modId      = "arcanebooks";
        public static final String modName    = "Arcane Books";
        public static final String modVersion = "1.7.10-1.0";
        
        public static final String itemId_spellScroll        = "spellscroll";
        public static final String itemId_spellBook          = "spellbook";
        public static final String itemId_runeStoneBasic     = "runebasic";
        public static final String itemId_runeStoneIntricate = "runeintricate";
        
        public static final String classPath_spellProjectile_server = "com.haniitsu.arcanebooks.projectiles.SpellProjectileCommon";
        public static final String classPath_spellProjectile_client = "com.haniitsu.arcanebooks.projectiles.SpellProjectileClient";
    }
    
    public static class Items
    {
        public static final ItemSpellScroll    spellScroll        = new ItemSpellScroll();
        public static final ItemSpellBook      spellBook          = new ItemSpellBook();
        public static final RuneStoneBasic     runestoneBasic     = new RuneStoneBasic();
        public static final RuneStoneIntricate runestoneIntricate = new RuneStoneIntricate();
    }
    
    /** The mod instance. */
    @Mod.Instance(Strings.modId)
    public static ArcaneBooks instance;
    
    /** The object containing the data registries pertinent to this mod. */
    public Registries registries;
    
    /** The packet sender/receiver used by this mod. */
    public SimpleNetworkWrapper packetChannel;

    @Mod.EventHandler
    public void PreInitializationEvent(FMLPreInitializationEvent event)
    {
        if(event.getSide() == Side.CLIENT)
            UtilMethods.runningPlayerId = Minecraft.getMinecraft().getSession().func_148256_e().getId();
        
        registerItems();
        registries = new Registries();
        registries.load(event.getModConfigurationDirectory());
        
        packetChannel = NetworkRegistry.INSTANCE.newSimpleChannel("ArcaneBooks");
        registerPackets();
        
        FMLCommonHandler.instance().bus().register(new PlayerJoinServerListener());
    }

    @Mod.EventHandler
    public void InitializationEvent(FMLInitializationEvent event)
    {
        registerEntities();
    }

    @Mod.EventHandler
    public void PostInitializationEvent(FMLPostInitializationEvent event)
    {

    }
    
    /** Registers the mod's entities. */
    public void registerEntities()
    {
        SpellProjectileCommon.proxy.registerRenderThings();
        SpellProjectileCommon.proxy.registerSounds();
    }
    
    public void registerItems()
    {
        GameRegistry.registerItem(Items.spellScroll,        Strings.itemId_spellScroll);
        GameRegistry.registerItem(Items.spellBook,          Strings.itemId_spellBook);
        GameRegistry.registerItem(Items.runestoneBasic,     Strings.itemId_runeStoneBasic);
        GameRegistry.registerItem(Items.runestoneIntricate, Strings.itemId_runeStoneIntricate);
    }
    
    public void registerPackets()
    {
        packetChannel.registerMessage(SpellEffectsSyncPacket          .Handler.class, SpellEffectsSyncPacket          .class, 1, Side.CLIENT);
        packetChannel.registerMessage(SpellEffectsBacklogClearedPacket.Handler.class, SpellEffectsBacklogClearedPacket.class, 2, Side.CLIENT);
        packetChannel.registerMessage(SpellEffectsAddedPacket         .Handler.class, SpellEffectsAddedPacket         .class, 3, Side.CLIENT);
        packetChannel.registerMessage(SpellEffectsRemovedPacket       .Handler.class, SpellEffectsRemovedPacket       .class, 4, Side.CLIENT);
        packetChannel.registerMessage(SpellEffectsClearedPacket       .Handler.class, SpellEffectsClearedPacket       .class, 5, Side.CLIENT);
    }
}
