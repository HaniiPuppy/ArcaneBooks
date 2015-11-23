package com.haniitsu.arcanebooks.registries;

import com.haniitsu.arcanebooks.magic.ConfiguredDefinition;
import com.haniitsu.arcanebooks.magic.SpellArgs;
import com.haniitsu.arcanebooks.magic.SpellArgsMessage;
import com.haniitsu.arcanebooks.magic.SpellEffectDefinition;
import com.haniitsu.arcanebooks.magic.caster.SpellCasterEntity;
import com.haniitsu.arcanebooks.magic.modifiers.definition.LogicalCheckDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.NumericDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import com.haniitsu.arcanebooks.misc.BlockLocation;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import org.apache.commons.lang3.NotImplementedException;

// Just so the default effect definitions don't clutter up SpellEffectDefinitionRegistry

/**
 * Class for holding static references to standard spell effect definitions.
 */
class DefaultDefs
{
    /**
     * Checks whether the specified message has been passed, and performs all spell effect definitions passed into it
     * as arguments in order if it is.
     * 
     * @example SomeSpellEffect: If[detected](Heal(OnMobs("player")): 4)
     */
    static final SpellEffectDefinition logicalIf = new SpellEffectDefinition("If")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        {
            boolean bool = false;
            
            List<LogicalCheckDefinitionModifier> logicalChecks = new ArrayList<LogicalCheckDefinitionModifier>();
            List<ConfiguredDefinition> definitions = new ArrayList<ConfiguredDefinition>();
            
            for(SpellEffectDefinitionModifier i : defModifiers)
                if(i instanceof LogicalCheckDefinitionModifier)
                    logicalChecks.add((LogicalCheckDefinitionModifier)i);
                else if(i instanceof ConfiguredDefinition)
                    definitions.add((ConfiguredDefinition)i);
            
            for(LogicalCheckDefinitionModifier i : logicalChecks)
                if(spellArgs.getMessage(i.getName().trim().toLowerCase()) != null)
                {
                    bool = true;
                    break;
                }
            
            if(bool)
                for(ConfiguredDefinition i : definitions)
                    i.PerformEffect(spellArgs);
        }
    };
    
    /**
     * Checks whether the specified message has been passed, and performs all spell effect definitions passed into it
     * as arguments in order if it's not.
     * 
     * @example SomeSpellEffect: IfNot[detected](Heal(OnMobs("player")): 4)
     */
    static final SpellEffectDefinition logicalIfNot = new SpellEffectDefinition("IfNot")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        {
            boolean bool = false;
            
            List<LogicalCheckDefinitionModifier> logicalChecks = new ArrayList<LogicalCheckDefinitionModifier>();
            List<ConfiguredDefinition> definitions = new ArrayList<ConfiguredDefinition>();
            
            for(SpellEffectDefinitionModifier i : defModifiers)
                if(i instanceof LogicalCheckDefinitionModifier)
                    logicalChecks.add((LogicalCheckDefinitionModifier)i);
                else if(i instanceof ConfiguredDefinition)
                    definitions.add((ConfiguredDefinition)i);
            
            for(LogicalCheckDefinitionModifier i : logicalChecks)
                if(spellArgs.getMessage(i.getName().trim().toLowerCase()) != null)
                {
                    bool = true;
                    break;
                }
            
            if(!bool)
                for(ConfiguredDefinition i : definitions)
                    i.PerformEffect(spellArgs);
        }
    };
    
    
    
    /**
     * Temporarily activates all affected redstone blocks, as though they'd been activated by a button press.
     */
    static final SpellEffectDefinition activateRedstone = new SpellEffectDefinition("ActivateRedstone")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Breaks affected blocks as though they'd been broken by a player.
     * 
     * TO DO: Add support for arguments allowing fortune and silk-touch to be emulated.
     */
    static final SpellEffectDefinition breakBlock = new SpellEffectDefinition("BreakBlock")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        {
            for(BlockLocation block : spellArgs.getBlocksHit())
                block.breakBlock();
        }
    };
    
    /**
     * Checks to see if a given message has been passed in an earlier spell effect in the spell cast, and leaves a
     * message in the current spell effect if the given message is found.
     */
    static final SpellEffectDefinition checkForMessage = new SpellEffectDefinition("CheckForMessage")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Removes all spell effects, or specific spell effects if given the names of spell effects to clear.
     */
    static final SpellEffectDefinition clearPotionEffect = new SpellEffectDefinition("ClearPotionEffect")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Causes the specified damage to affected entities.
     */
    static final SpellEffectDefinition damage = new SpellEffectDefinition("Damage")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        {
            // To do: Add support for decaying damage as it gets away from the burst location.
            //        Make this take armour into account. Add option to allow it to ignore armour.
            
            double damage = 1;
            DamageSource damageSource = DamageSource.magic;
            
            for(SpellEffectDefinitionModifier i : defModifiers)
            {
                if(i instanceof NumericDefinitionModifier)
                    damage = ((NumericDefinitionModifier)i).asDouble();
                
                if(i.getName().equalsIgnoreCase("Damage"))
                {
                    if(i.getValue() == null)
                        continue;
                    
                    try
                    { damage = Double.parseDouble(i.getValue()); }
                    catch(NumberFormatException e)
                    { }
                }
            }
            
            if(spellArgs.getCaster() instanceof SpellCasterEntity)
            {
                Entity caster = ((SpellCasterEntity)spellArgs.getCaster()).getCasterEntity();
                
                if(caster instanceof EntityPlayer)
                    damageSource = DamageSource.causePlayerDamage((EntityPlayer)caster);
                else if(caster instanceof EntityLivingBase)
                    damageSource = DamageSource.causeMobDamage((EntityLivingBase)caster);
            }
            
            for(Entity i : spellArgs.getEntitiesHit())
                i.attackEntityFrom(damageSource, (float)damage);
        }
    };
    
    /**
     * Checks whether specified blocks or entities are affected (or any at all if none are specified), and passes a
     * message (by default: "detected") if they are.
     */
    static final SpellEffectDefinition detect = new SpellEffectDefinition("Detect")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        {
            if(spellArgs.getBlocksHit().size() <= 0 || spellArgs.getEntitiesHit().size() <= 0)
                return;
            
            String verboseText = null;
            String message = "detected";
            
            for(SpellEffectDefinitionModifier i : defModifiers)
            {
                if(i.getName().equalsIgnoreCase("verbose"))
                    verboseText = i.getValue() == null ? "Detected!" : i.getValue();
                else if(i.getName().equalsIgnoreCase("message") || i.getName().equalsIgnoreCase("msg"))
                    verboseText = i.getValue();
            }
            
            spellArgs.addMessage(new SpellArgsMessage("detected"));

            if(verboseText != null)
            {
                if(spellArgs.getCaster() instanceof SpellCasterEntity)
                {
                    Entity caster = ((SpellCasterEntity)spellArgs.getCaster()).getCasterEntity();

                    if(caster instanceof EntityPlayer)
                        ((EntityPlayer)caster).addChatComponentMessage(new ChatComponentText(verboseText));
                }

                System.out.println(verboseText);
            }
        }
    };
    
    /**
     * Gives any affected entities the specified potion effect, or does nothing if no potion effect is specified.
     */
    static final SpellEffectDefinition givePotionEffect = new SpellEffectDefinition("GivePotionEffect")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Modifies the health of any affected entities by the specified amount.
     */
    static final SpellEffectDefinition heal = new SpellEffectDefinition("Heal")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Modifies the mana of any affected entities by the specified amount.
     */
    static final SpellEffectDefinition modifyMana = new SpellEffectDefinition("ModifyMana")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Creates particle effects on spell-burst, applying any properties passed as arguments to the spell burst.
     */
    static final SpellEffectDefinition particle = new SpellEffectDefinition("Particle")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Passes all specified properties to any spell projectile created when a spell containing this spell effect
     * definition is cast.
     */
    static final SpellEffectDefinition projectile = new SpellEffectDefinition("Projectile")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Replaces all affected blocks with the a new block with the specified string ID and data value.
     */
    static final SpellEffectDefinition replaceBlock = new SpellEffectDefinition("ReplaceBlock")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Replaces any affected itemstacks with the a new itemstack with the specified string ID and data value.
     */
    static final SpellEffectDefinition replaceItem = new SpellEffectDefinition("ReplaceItem")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Sets the mana of any affected spell casters to the specified amount.
     */
    static final SpellEffectDefinition setMana = new SpellEffectDefinition("Mana")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Forcibly sets a player's shader to the specified one, one of a specified number, or a random one for a determined
     * or predetermined period of time.
     */
    static final SpellEffectDefinition shader = new SpellEffectDefinition("Shader")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Causes any affected spell casters to cast the currently equipped spell, or a specified spell passed as an
     * argument.
     */
    static final SpellEffectDefinition triggerSpell = new SpellEffectDefinition("TriggerSpell")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
}