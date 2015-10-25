package com.haniitsu.arcanebooks.registries;

import com.haniitsu.arcanebooks.magic.SpellArgs;
import com.haniitsu.arcanebooks.magic.SpellArgsMessage;
import com.haniitsu.arcanebooks.magic.SpellEffectDefinition;
import com.haniitsu.arcanebooks.magic.caster.SpellCasterEntity;
import com.haniitsu.arcanebooks.magic.modifiers.definition.NumericDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import com.haniitsu.arcanebooks.misc.BlockLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import org.apache.commons.lang3.NotImplementedException;

// Just so the default effect definitions don't clutter up SpellEffectDefinitionRegistry

class DefaultDefs
{
    static final SpellEffectDefinition logicalIf = new SpellEffectDefinition("If")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        {
            if(spellArgs.getMessage(spellArgs.getLogicalCheck().trim().toLowerCase()) != null)
                for(SpellEffectDefinitionModifier modifier : spellArgs.getDefinitionModifiers())
                    if(modifier instanceof SpellEffectDefinition)
                        ((SpellEffectDefinition)modifier).PerformEffect(spellArgs);
        }
    };
    
    static final SpellEffectDefinition logicalIfNot = new SpellEffectDefinition("IfNot")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        {
            if(spellArgs.getMessage(spellArgs.getLogicalCheck().trim().toLowerCase()) == null)
                for(SpellEffectDefinitionModifier modifier : spellArgs.getDefinitionModifiers())
                    if(modifier instanceof SpellEffectDefinition)
                        ((SpellEffectDefinition)modifier).PerformEffect(spellArgs);
        }
    };
    
    
    
    static final SpellEffectDefinition activateRedstone = new SpellEffectDefinition("ActivateRedstone")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    static final SpellEffectDefinition breakBlock = new SpellEffectDefinition("BreakBlock")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        {
            for(BlockLocation block : spellArgs.getBlocksHit())
                block.breakBlock();
        }
    };
    
    static final SpellEffectDefinition checkForMessage = new SpellEffectDefinition("CheckForMessage")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    static final SpellEffectDefinition clearPotionEffect = new SpellEffectDefinition("ClearPotionEffect")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    static final SpellEffectDefinition damage = new SpellEffectDefinition("Damage")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        {
            // Todo: Add support for decaying damage as it gets away from the burst location.
            //       Make this take armour into account. Add option to allow it to ignore armour.
            
            double damage = 1;
            DamageSource damageSource = DamageSource.magic;
            
            for(SpellEffectDefinitionModifier i : spellArgs.getDefinitionModifiers())
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
    
    static final SpellEffectDefinition detect = new SpellEffectDefinition("Detect")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        {
            if(spellArgs.getBlocksHit().size() <= 0 || spellArgs.getEntitiesHit().size() <= 0)
                return;
            
            String verboseText = null;
            String message = "detected";
            
            for(SpellEffectDefinitionModifier i : spellArgs.getDefinitionModifiers())
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
    
    static final SpellEffectDefinition givePotionEffect = new SpellEffectDefinition("GivePotionEffect")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    static final SpellEffectDefinition heal = new SpellEffectDefinition("Heal")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    static final SpellEffectDefinition modifyMana = new SpellEffectDefinition("ModifyMana")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    static final SpellEffectDefinition particle = new SpellEffectDefinition("Particle")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    static final SpellEffectDefinition projectile = new SpellEffectDefinition("Projectile")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    static final SpellEffectDefinition replaceBlock = new SpellEffectDefinition("ReplaceBlock")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    static final SpellEffectDefinition replaceItem = new SpellEffectDefinition("ReplaceItem")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        {
            throw new NotImplementedException("Not implemented yet.");
        }
    };
    
    static final SpellEffectDefinition setMana = new SpellEffectDefinition("Mana")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
    };
}