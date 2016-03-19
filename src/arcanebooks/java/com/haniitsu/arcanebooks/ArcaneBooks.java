package com.haniitsu.arcanebooks;

import com.haniitsu.arcanebooks.eventlisteners.PlayerJoinServerListener;
import com.haniitsu.arcanebooks.items.ItemSpellBook;
import com.haniitsu.arcanebooks.items.ItemSpellScroll;
import com.haniitsu.arcanebooks.items.RuneStoneBasic;
import com.haniitsu.arcanebooks.items.RuneStoneIntricate;
import com.haniitsu.arcanebooks.packets.PlayerJoinPacket;
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

/**
 * Main class mod class instantiated by forge.
 */
@Mod(modid = ArcaneBooks.Strings.modId, name = ArcaneBooks.Strings.modName, version = ArcaneBooks.Strings.modVersion)
public class ArcaneBooks
{
    public static class Strings
    {
        public static final String modId      = "ArcaneBooks";
        public static final String modName    = "Arcane Books";
        public static final String modVersion = "1.7.10-1.0";
        
        public static final String itemIdPrefix              = "arcanebooks";
        public static final String itemId_spellScroll        = "spellscroll";
        public static final String itemId_spellBook          = "spellbook";
        public static final String itemId_runeStoneBasic     = "runebasic";
        public static final String itemId_runeStoneIntricate = "runeintricate";
        
        public static final String classPath_spellProjectile_server = "com.haniitsu.arcanebooks.projectiles.SpellProjectileCommon";
        public static final String classPath_spellProjectile_client = "com.haniitsu.arcanebooks.projectiles.SpellProjectileClient";
        
        public static final String fileName_spellScroll        = "SpellScroll.png";
        public static final String fileName_spellBook          = "SpellBook.png";
        public static final String fileName_runestoneBasic     = "RunestoneBasic.png";
        public static final String fileName_runestoneIntricate = "RunestoneIntricate.png";
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
        registerItems();
        registries = new Registries();
        packetChannel = NetworkRegistry.INSTANCE.newSimpleChannel("ArcaneBooks");
        
        packetChannel.registerMessage(PlayerJoinPacket.Handler.class, PlayerJoinPacket.class, 1, Side.CLIENT);
        
        if(event.getSide() == Side.SERVER)
            registries.load(event.getModConfigurationDirectory());
        
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
        GameRegistry.registerItem(Items.spellScroll,        "SpellScroll");
        GameRegistry.registerItem(Items.spellBook,          "SpellBook");
        GameRegistry.registerItem(Items.runestoneBasic,     "RuneStoneBasic");
        GameRegistry.registerItem(Items.runestoneIntricate, "RuneStoneIntricate");
        
        Items.spellScroll       .setTextureName(Strings.modId + ":" + Strings.fileName_spellScroll);
        Items.spellBook         .setTextureName(Strings.modId + ":" + Strings.fileName_spellBook);
        Items.runestoneBasic    .setTextureName(Strings.modId + ":" + Strings.itemId_runeStoneBasic);
        Items.runestoneIntricate.setTextureName(Strings.modId + ":" + Strings.itemId_runeStoneIntricate);
    }
}
