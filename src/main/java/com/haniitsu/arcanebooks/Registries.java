package com.haniitsu.arcanebooks;

import com.haniitsu.arcanebooks.misc.UtilMethods;
import com.haniitsu.arcanebooks.misc.events.EventListener;
import com.haniitsu.arcanebooks.packets.SpellEffectsBacklogClearedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsAddedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsClearedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsRemovedPacket;
import com.haniitsu.arcanebooks.registries.RuneDesignRegistry;
import com.haniitsu.arcanebooks.registries.SpellEffectDefinitionRegistry;
import com.haniitsu.arcanebooks.registries.SpellEffectRegistry;
import java.io.File;

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
    
    protected void refreshRegistries()
    {
        definitions = new SpellEffectDefinitionRegistry();
        baseSpellEffects = new SpellEffectRegistry(definitions);
        //runeDesigns = new RuneDesignRegistry(spellEffects);
        
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
    
    public void load(File configDirectory)
    {
        refreshRegistries();
        definitions.loadDefaultValues();
        baseSpellEffects.loadFromFile(new File(configDirectory, "ArcaneBooks/SpellEffects.cfg"));
        //runeDesigns.loadFromFile(new File(configDirectory, "ArcaneBooks/RuneDesigns.cfg"));
    }
}