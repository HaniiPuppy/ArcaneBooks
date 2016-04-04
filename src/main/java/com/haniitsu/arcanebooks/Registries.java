package com.haniitsu.arcanebooks;

import com.haniitsu.arcanebooks.magic.SpellEffect;
import com.haniitsu.arcanebooks.magic.SpellWord;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellEffectModifier;
import com.haniitsu.arcanebooks.misc.UtilMethods;
import com.haniitsu.arcanebooks.misc.events.EventListener;
import com.haniitsu.arcanebooks.packets.RuneDesignsAddedPacket;
import com.haniitsu.arcanebooks.packets.RuneDesignsBacklogClearedPacket;
import com.haniitsu.arcanebooks.packets.RuneDesignsClearedPacket;
import com.haniitsu.arcanebooks.packets.RuneDesignsRemovedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsBacklogClearedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsAddedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsClearedPacket;
import com.haniitsu.arcanebooks.packets.SpellEffectsRemovedPacket;
import com.haniitsu.arcanebooks.registries.RuneDesignRegistry;
import com.haniitsu.arcanebooks.registries.SpellEffectDefinitionRegistry;
import com.haniitsu.arcanebooks.registries.SpellEffectRegistry;
import com.haniitsu.arcanebooks.runes.RuneDesign;
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
                StringBuilder sb = new StringBuilder();
                boolean first = true;
                
                for(Map.Entry<SpellWord, RuneDesign> i : args.getDesignsAdded().entrySet())
                {
                    if(first) first = false;
                    else      sb.append("\n");
                    
                    // Example line:
                    // effect:heal=2,3>3,1_0,0>1,2_3,0>2,1
                    
                    if(i.getKey() instanceof SpellEffect)
                    {
                        sb.append("effect:");
                        sb.append(((SpellEffect)i).getName());
                    }
                    else if(i.getKey() instanceof SpellEffectModifier)
                    {
                        sb.append(((SpellEffectModifier)i).getModifierGroupName());
                        sb.append(':');
                        sb.append(((SpellEffectModifier)i).getModifierName());
                    }
                    else
                    {
                        throw new NotImplementedException("Support for SpellWord not implemented in the registries "
                                                        + "listener of RuneDesignRegistry.itemsAdded: \n"
                                                        + i.getKey().getClass().toString());
                    }
                    
                    sb.append('=');
                    sb.append(i.getValue().toString());
                }
                
                for(Map.Entry<String, RuneDesign> i : args.getDesignsBacklogged().entrySet())
                {
                    if(first) first = false;
                    else      sb.append("\n");
                    
                    sb.append("effect:");
                    sb.append(i.getKey());
                    sb.append('=');
                    sb.append(i.getValue().toString());
                }
                
                UtilMethods.sendPacketToAllExceptPlayerRunningServer(ArcaneBooks.instance.packetChannel, new RuneDesignsAddedPacket(sb.toString()));
            }
        });
        
        baseRuneDesigns.itemsRemoved.registerListener(new EventListener<RuneDesignRegistry.RuneDesignsRemovedArgs>()
        {
            @Override
            public void onEvent(Object sender, RuneDesignRegistry.RuneDesignsRemovedArgs args)
            {
                if(args.clearedAll())
                {
                    UtilMethods.sendPacketToAllExceptPlayerRunningServer(ArcaneBooks.instance.packetChannel, new RuneDesignsClearedPacket());
                    return;
                }
                
                StringBuilder sb = new StringBuilder();
                boolean first = true;
                
                for(Map.Entry<SpellWord, RuneDesign> i : args.getDesignsRemoved().entrySet())
                {
                    if(first) first = false;
                    else      sb.append("\n");
                    
                    if(i.getKey() instanceof SpellEffect)
                    {
                        sb.append("effect:");
                        sb.append(((SpellEffect)i).getName());
                    }
                    else if(i.getKey() instanceof SpellEffectModifier)
                    {
                        sb.append(((SpellEffectModifier)i).getModifierGroupName());
                        sb.append(':');
                        sb.append(((SpellEffectModifier)i).getModifierName());
                    }
                }
                
                for(Map.Entry<String, RuneDesign> i : args.getBackloggedDesignsRemoved().entrySet())
                {
                    if(first) first = false;
                    else      sb.append("\n");
                    
                    sb.append("effect:");
                    sb.append(i.getKey());
                }
                
                UtilMethods.sendPacketToAllExceptPlayerRunningServer(ArcaneBooks.instance.packetChannel, new RuneDesignsRemovedPacket(sb.toString()));
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