package com.haniitsu.arcanebooks.registries;

import com.google.common.primitives.Doubles;
import com.haniitsu.arcanebooks.magic.ConfiguredDefinition;
import com.haniitsu.arcanebooks.magic.SpellArgs;
import com.haniitsu.arcanebooks.magic.SpellMessage;
import com.haniitsu.arcanebooks.magic.SpellEffectDefinition;
import com.haniitsu.arcanebooks.magic.caster.SpellCasterEntity;
import com.haniitsu.arcanebooks.magic.modifiers.definition.BasicDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.LogicalCheckDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.ModifierValueDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.NumericDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import com.haniitsu.arcanebooks.misc.BlockLocation;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
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
     * Invokes the passed configured definitions if any of the passed logical checks are true.
     * 
     * This currently allows for checks of messages passed either within the current spell phrase, or within the current
     * spell cast.
     * 
     * Passed configured definitions are invoked in the order they're passed into the args of the If call.
     * 
     * ! is evaluated before &&, which is evaluated before || - evaluation order can be controlled with (brackets)
     * 
     * @example ExampleEffect1: If[detected](Heal: 4) runs Heal if the "detected" message has been passed earlier in the
     * spell phrase.
     * @example ExampleEffect2: If[%detected](Heal: 4) runs Heal if the "detected" message has been passed earlier in
     * the *spell*.
     * @example ExampleEffect3: If[!detected](Heal: 4) runs Heal if the "detected" message has *not* been passed earlier
     * in the spell phrase.
     * @example ExampleEffect4: If[!%detected](Heal: 4) runs Heal if the "detected" message has not been passed earlier
     * in the spell.
     * @example ExampleEffect5: If[detected && %done](Heal: 4) runs Heal if the "detected" message has been passed
     * earlier in the spell phrase *and* the "done" message has been passed earlier in the spell.
     * @example ExampleEffect6: If[detected || %done](Heal: 4) runs Heal if the "detected" message has been passed
     * earlier in the spell phrase *or* the "done" message has been passed earlier in the spell.
     * @example ExampleEffect7: If[detected && (foo || bar)](Heal: 4) runs Heal if the "detected" message has been
     * passed earlier in the spell phrase, and either the foo or the bar message has been passed earlier in the spell
     * phrase.
     * @example ExampleEffect8: If[detected && done || foo && bar](Heal: 4) is the same as
     * [(detected && done) || (foo && bar)], because && (AND) statements are evaluated before || (OR) statements.
     * @example ExampleEffect9: If[detected && done && foo && bar](Heal: 4) is the same as
     * [(((detected && done) && foo) && bar)], because statements evaluated together are evaluated left-to-right.
     */
    static final SpellEffectDefinition logicalIf = new SpellEffectDefinition("If")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        {
            boolean result = false;
            
            for(SpellEffectDefinitionModifier i : defModifiers)
                if(i instanceof LogicalCheckDefinitionModifier)
                {
                    result = DefaultDefsUtilMethods.evaluateIf(i.getName(), spellArgs);
                    
                    if(result == true)
                        break;
                }
            
            if(result)
                for(SpellEffectDefinitionModifier i : defModifiers)
                    if(i instanceof ConfiguredDefinition)
                        ((ConfiguredDefinition)i).PerformEffect(spellArgs);
        }
    };
    
    
    
    /**
     * Temporarily activates all affected redstone blocks, as though they'd been activated by a button press.
     * 
     * Number of ticks a redstone signal is there for can be set as a numeric argument.
     */
    static final SpellEffectDefinition activateRedstone = new SpellEffectDefinition("ActivateRedstone")
    {
        // I need to find out how to set a strong or weak redstone signal on a block without creating a block that
        // passes a redstone signal in order to do this.
        
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Breaks affected blocks as though they'd been broken by a player.
     * 
     * Possible arguments:
     * 
     * stopnormaldrops: Prevents any item from being dropped as a result of this block break. Does not prevent other
     *                  spell effect definitions from spawning items.
     * 
     * dropexactitem: Drops the exact block that was broken as an item.
     * 
     * fortune: Takes a numeric value. Makes blocks broken break as though they'd been broken by a tool with the fortune
     *          enchantment of the passed level.
     * 
     * silkTouch: Makes blocks broken break as though they'd been broken by a tool with the silk touch enchantment.
     */
    static final SpellEffectDefinition breakBlock = new SpellEffectDefinition("BreakBlock")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        {
            boolean stopNormalDrops = false;
            boolean dropExactItem = false;
            boolean silkTouch = false;
            int fortuneLevel = 0;
            
            iLoop:
            for(SpellEffectDefinitionModifier i : defModifiers)
            {
                if(!(i instanceof BasicDefinitionModifier))
                    continue;
                
                if(i.getName().equalsIgnoreCase("stopnormaldrops"))
                {
                    stopNormalDrops = true;
                    break;
                }
                else if(i.getName().equalsIgnoreCase("dropexactitem")
                     || i.getName().equalsIgnoreCase("drop exact item")
                     || i.getName().equalsIgnoreCase("dropexact")
                     || i.getName().equalsIgnoreCase("drop exact"))
                {
                    dropExactItem = true;
                    break;
                }
                else if(i.getName().equalsIgnoreCase("silktouch")
                     || i.getName().equalsIgnoreCase("silk touch")
                     || i.getName().equalsIgnoreCase("silk"))
                {
                    silkTouch = true;
                    break;
                }
                else if(i.getName().equalsIgnoreCase("fortune"))
                {
                    String levelString = i.getValue();
                    Double level = Doubles.tryParse(levelString);

                    if(level == null)
                        for(SpellEffectDefinitionModifier j : i.getSubModifiers())
                            if(j instanceof NumericDefinitionModifier)
                            {
                                fortuneLevel = ((NumericDefinitionModifier)j).asInt();
                                break iLoop;
                            }
                }
            }
            
            for(BlockLocation block : spellArgs.getBlocksHit())
            {
                if(stopNormalDrops) 
                    block.setBlockToAir(); 
                else if(dropExactItem)
                {
                    Block blocktype = block.getBlockAt();
                    block.setBlockToAir();
                    block.getWorld().spawnEntityInWorld(new EntityItem(block.getWorld(),
                                                                       block.getX(), block.getY(), block.getZ(),
                                                                       new ItemStack(blocktype)));
                }
                else if(silkTouch)
                    block.breakBlockWithSilkTouch();
                else if(fortuneLevel > 0)
                    block.breakBlockWithFortune(fortuneLevel);
                else
                    block.breakBlock();
            }
        }
    };
    
    /**
     * Removes all spell effects, or specific spell effects if given the names of spell effects to clear.
     */
    static final SpellEffectDefinition clearPotionEffects = new SpellEffectDefinition("ClearPotionEffects")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs, List<SpellEffectDefinitionModifier> defModifiers)
        {
            List<String> potionNamesToClear = new ArrayList<String>();
            
            for(SpellEffectDefinitionModifier i : defModifiers)
                if(i instanceof BasicDefinitionModifier)
                    potionNamesToClear.add(i.getName());
            
            if(potionNamesToClear.isEmpty())
            {
                for(Entity entity : spellArgs.getEntitiesHit())
                    if(entity instanceof EntityLivingBase)
                        for(Object effect : ((EntityLivingBase)entity).getActivePotionEffects())
                            ((EntityLivingBase)entity).removePotionEffect(((PotionEffect)effect).getPotionID());
            }
            else
            {
                for(Entity entity : spellArgs.getEntitiesHit())
                    if(entity instanceof EntityLivingBase)
                        for(Object effect : ((EntityLivingBase)entity).getActivePotionEffects())
                            for(String effectName : potionNamesToClear)
                                if(((PotionEffect)effect).getEffectName().equalsIgnoreCase(effectName))
                                {
                                    ((EntityLivingBase)entity).removePotionEffect(((PotionEffect)effect).getPotionID());
                                    break;
                                }
            }
        }
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
            
            spellArgs.passMessage(new SpellMessage("detected"));

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