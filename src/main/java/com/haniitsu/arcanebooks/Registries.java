package com.haniitsu.arcanebooks;

import com.haniitsu.arcanebooks.misc.UtilMethods;
import com.haniitsu.arcanebooks.misc.events.EventListener;
import com.haniitsu.arcanebooks.packets.RuneDesignsBacklogClearedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsBacklogClearedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsAddedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsClearedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsRemovedPacket;
import com.haniitsu.arcanebooks.registries.RuneDesignRegistry;
import com.haniitsu.arcanebooks.registries.SpellEffectDefinitionRegistry;
import com.haniitsu.arcanebooks.registries.SpellEffectRegistry;
import java.io.File;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Holds the set of data registries specific to this mod.
 */
public class Registries
{
    /**
     * The mod's registry of spell effect definitions.
     * 
     * Definitions are the active component of spell effects.
     */
    public SpellEffectDefinitionRegistry definitions;
    
    /**
     * The mod's registry of spell effects.
     * 
     * Spell effects are defined in-file using a combination of spell effect definitions, modifiers specific to each
     * definition, and possibly logical checks.
     */
    public SpellEffectRegistry spellEffects;
    
    /**
     * The backing spell effect registry. This is the registry that actually loads files. The contents of this registry
     * is transferred to the "spellEffects" spell effects registry of players upon joining the server, and of the server
     * upon start-up.
     */
    public SpellEffectRegistry baseSpellEffects;
    
    /**
     * The mod's registry of visual rune designs.
     * 
     * This holds the visual design for items enclosing spell effects or spell effect modifiers, and visual designs
     * that represent them in places where they are used, such as in writing spell books, etc.
     */
    public RuneDesignRegistry runeDesigns;
    
    /** The backing rune design registry. This is the registry that actually loads files. The contents of this registry
     is transferred to the "runeDesigns" rune design registry of players upon joining the server, and of the server
     upon start-up.*/
    public RuneDesignRegistry baseRuneDesigns;
    
    protected void refreshRegistries()
    {
        definitions = new SpellEffectDefinitionRegistry();
        baseSpellEffects = new SpellEffectRegistry(definitions);
        
        baseSpellEffects.backlogCleared.registerListener(new EventListener<SpellEffectRegistry.BacklogClearedArgs>()
        {
            @Override
            public void onEvent(Object sender, SpellEffectRegistry.BacklogClearedArgs args)
            { UtilMethods.sendPacketToAllExceptPlayerRunningServer(ArcaneBooks.instance.packetChannel, new SpellEffectsBacklogClearedPacket()); }
        });
        
        baseSpellEffects.effectsAdded.registerListener(new EventListener<SpellEffectRegistry.EffectsAddedArgs>()
        {
            @Override
            public void onEvent(Object sender, SpellEffectRegistry.EffectsAddedArgs args)
            {
                StringBuilder combinedString = new StringBuilder();
                boolean first = true;
                
                for(String effectString : args.getEffectStrings())
                {
                    if(first) first = false;
                    else      combinedString.append('\n');
                    
                    combinedString.append(effectString);
                }
                
                UtilMethods.sendPacketToAllExceptPlayerRunningServer(ArcaneBooks.instance.packetChannel, new SpellEffectsAddedPacket(combinedString.toString()));
            }
        });
        
        baseSpellEffects.effectsRemoved.registerListener(new EventListener<SpellEffectRegistry.EffectsRemovedArgs>()
        {
            @Override
            public void onEvent(Object sender, SpellEffectRegistry.EffectsRemovedArgs args)
            {
                if(args.wasCleared())
                {
                    UtilMethods.sendPacketToAllExceptPlayerRunningServer(ArcaneBooks.instance.packetChannel, new SpellEffectsClearedPacket());
                    return;
                }
                
                StringBuilder combinedString = new StringBuilder();
                boolean first = true;
                
                for(String effectName : args.getEffectNames())
                {
                    if(first) first = false;
                    else      combinedString.append('\n');
                    
                    combinedString.append(effectName);
                }
                
                UtilMethods.sendPacketToAllExceptPlayerRunningServer(ArcaneBooks.instance.packetChannel, new SpellEffectsRemovedPacket(combinedString.toString()));
            }
        });
    }
    
    protected void refreshRegistriesOnWorldLoad()
    {
        baseRuneDesigns = new RuneDesignRegistry(baseSpellEffects);
        
        baseRuneDesigns.backlogCleared.registerListener(new EventListener<RuneDesignRegistry.RuneDesignsBacklogClearedArgs>()
        {
            @Override
            public void onEvent(Object sender, RuneDesignRegistry.RuneDesignsBacklogClearedArgs args)
            { UtilMethods.sendPacketToAllExceptPlayerRunningServer(ArcaneBooks.instance.packetChannel, new RuneDesignsBacklogClearedPacket()); }
        });
        
        baseRuneDesigns.itemsAdded.registerListener(new EventListener<RuneDesignRegistry.RuneDesignsAddedArgs>()
        {
            @Override
            public void onEvent(Object sender, RuneDesignRegistry.RuneDesignsAddedArgs args)
            {
                throw new NotImplementedException("Not implemented yet.");
            }
        });
        
        baseRuneDesigns.itemsRemoved.registerListener(new EventListener<RuneDesignRegistry.RuneDesignsRemovedArgs>()
        {
            @Override
            public void onEvent(Object sender, RuneDesignRegistry.RuneDesignsRemovedArgs args)
            {
                throw new NotImplementedException("Not implemented yet.");
            }
        });
    }
    
    public void load(File configDirectory)
    {
        refreshRegistries();
        definitions.loadDefaultValues();
        baseSpellEffects.loadFromFile(new File(configDirectory, "ArcaneBooks/SpellEffects.cfg"));
    }
    
    public void loadWithWorld(File worldDirectory)
    {
        refreshRegistriesOnWorldLoad();
        baseRuneDesigns.loadFromFile(new File(worldDirectory, "ArcaneBooks/RuneDesigns.dat"));
    }
}