package com.haniitsu.arcanebooks.registries;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.haniitsu.arcanebooks.magic.ConfiguredDefinition;
import com.haniitsu.arcanebooks.magic.SpellArgs;
import com.haniitsu.arcanebooks.magic.SpellMessage;
import com.haniitsu.arcanebooks.magic.SpellEffectDefinition;
import com.haniitsu.arcanebooks.magic.castcaches.GivePotionEffectCache;
import com.haniitsu.arcanebooks.magic.caster.SpellCasterEntity;
import com.haniitsu.arcanebooks.magic.modifiers.definition.BasicDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.LogicalCheckDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.ModifierValueDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.NumericDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.definition.SpellEffectDefinitionModifier;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOESize;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellTarget;
import com.haniitsu.arcanebooks.misc.BlockLocation;
import com.haniitsu.arcanebooks.misc.Location;
import com.haniitsu.arcanebooks.util.ArcaneSpellEntityDamageSource;
import com.haniitsu.arcanebooks.util.ArcaneSpellGeneralDamageSource;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
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
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        {
            boolean result = false;
            
            for(SpellEffectDefinitionModifier i : def.getModifiers())
                if(i instanceof LogicalCheckDefinitionModifier)
                {
                    result = DefaultDefsUtilMethods.evaluateIf(i.getName(), spellArgs);
                    
                    if(result == true)
                        break;
                }
            
            if(result)
                for(SpellEffectDefinitionModifier i : def.getModifiers())
                    if(i instanceof ConfiguredDefinition)
                        ((ConfiguredDefinition)i).PerformEffect(spellArgs);
        }
    };
    
    
    
    static final SpellEffectDefinition affectsEntities = new SpellEffectDefinition("AffectsOnlyEntities")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        {
            throw new NotImplementedException("Not implemented yet.");
        }
    };
    
    static final SpellEffectDefinition affectsMobs = new SpellEffectDefinition("AffectsOnlyMobs")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        {
            throw new NotImplementedException("Not implemented yet.");
        }
    };
    
    static final SpellEffectDefinition affectsBlocks = new SpellEffectDefinition("AffectsOnlyBlocks")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        {
            throw new NotImplementedException("Not implemented yet.");
        }
    };
    
    static final SpellEffectDefinition doesntAffectEntites = new SpellEffectDefinition("DoesNotAffectEntities")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        {
            throw new NotImplementedException("Not implemented yet.");
        }
    };
    
    static final SpellEffectDefinition doesntAffectMobs = new SpellEffectDefinition("DoesNotAffectMobs")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        {
            throw new NotImplementedException("Not implemented yet.");
        }
    };
    
    static final SpellEffectDefinition doesntAffectBlocks = new SpellEffectDefinition("DoesNotAffectBlocks")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        {
            throw new NotImplementedException("Not implemented yet.");
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
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
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
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        {
            boolean stopNormalDrops = false;
            boolean dropExactItem = false;
            boolean silkTouch = false;
            int fortuneLevel = 0;
            
            iLoop:
            for(SpellEffectDefinitionModifier i : def.getModifiers())
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
            
            for(BlockLocation block : spellArgs.getBlocksAffected())
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
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        {
            List<String> potionNamesToClear = new ArrayList<String>();
            
            for(SpellEffectDefinitionModifier i : def.getModifiers())
                if(i instanceof BasicDefinitionModifier)
                    potionNamesToClear.add(i.getName());
            
            if(potionNamesToClear.isEmpty())
            {
                for(EntityLivingBase entity : spellArgs.getMobsAffected())
                    for(Object effect : entity.getActivePotionEffects())
                        entity.removePotionEffect(((PotionEffect)effect).getPotionID());
            }
            else
            {
                for(EntityLivingBase entity : spellArgs.getMobsAffected())
                    for(Object effect : entity.getActivePotionEffects())
                        for(String effectName : potionNamesToClear)
                            if(((PotionEffect)effect).getEffectName().equalsIgnoreCase(effectName))
                            {
                                entity.removePotionEffect(((PotionEffect)effect).getPotionID());
                                break;
                            }
            }
        }
    };
    
    /**
     * Causes the specified damage to affected entities.
     * 
     * possible arguments:
     * 
     * The amount of damage to deal is the numeric argument or the modifier value of the spell effect.
     * 
     * ignore armour: Makes the damage from this spell ignore any armour.
     * 
     * ignore buffs/absolute: Makes the damage from this spell ignore any potion effects, armour enchantments, etc.
     * 
     * ignore spell strength: Makes the damage from this spell not be affected by the spell strength.
     * 
     * fire: Makes the damage from this spell effect fire damage.
     * 
     * explosion: Makes the damage from this spell effect explosion damage.
     * 
     * not magic: Makes the damage from this spell effect not magic damage. (it is magic damage by default)
     * 
     * projectile: Makes the damage from this spell effect projectile damage. (It is by default only if the spell is
     *             cast as a projectile spell)
     * 
     * not projectile: Makes the damage from this spell effect not projectile damage. (It is by default only if the
     *                 spell is cast as a projectile spell)
     * 
     * ignore non living: non-living entities are unaffected. (e.g. paintings, armour stands, minecarts, etc.)
     * 
     * distance multiplier: As a percentage (where 0 is 0%, 1.0 is 100%) of the specified amount of damage, how much
     *                      damage those at the very edge of the spell's affected area take. This progresses from 1.0 to
     *                      the value passed to this argument linearly depending on how close to the centre the affected
     *                      entity is.
     * 
     *                      e.g. where a value of 0.3 is used, and the damage specified is 5, the actual entity hit
     *                      would take 5 (1.0 * 5) damage, an entity at the edge of the affected area would
     *                      take 1.5 (0.3 * 5) damage, and an entity halfway between the two would take 3.25 (0.65 * 5)
     *                      damage.
     */
    static final SpellEffectDefinition damage = new SpellEffectDefinition("Damage")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        {
            double damage = 1;
            double percentOfDamageAtEdge = 1; // i.e. as a percent, how much damage is taken by those at the max distance.
            
            boolean ignoreArmour = false;
            boolean ignoreBuffs = false;
            boolean ignoreSpellStrength = false;
            
            boolean isFireDamage = false;
            boolean isMagicDamage = true;
            boolean isExplosionDamage = false;
            boolean isProjectile = spellArgs.getSpellTarget() == SpellTarget.projectile;
            
            boolean ignoreNonLivingEntities = false;
            
            DamageSource dmgSrc = spellArgs.getCaster() instanceof SpellCasterEntity
                    ? new ArcaneSpellEntityDamageSource(spellArgs)
                    : new ArcaneSpellGeneralDamageSource(spellArgs);
            
            for(SpellEffectDefinitionModifier modifier : def.getModifiers())
            {
                if(modifier instanceof NumericDefinitionModifier)
                    damage = ((NumericDefinitionModifier)modifier).asDouble();
                else if(modifier instanceof ModifierValueDefinitionModifier)
                {
                    Double newDamage = Doubles.tryParse(modifier.getName());
                    
                    if(newDamage != null)
                        damage = newDamage;
                }
                else if(modifier instanceof BasicDefinitionModifier)
                {
                    // TO DO: Change this to a switch/case statement once I drop support for Java 1.6
                    
                    if(modifier.getName().equalsIgnoreCase("ignorearmour")
                    || modifier.getName().equalsIgnoreCase("ignore armour"))
                    { ignoreArmour = true; }
                    else if(modifier.getName().equalsIgnoreCase("ignorebuffs")
                         || modifier.getName().equalsIgnoreCase("ignore buffs")
                         || modifier.getName().equalsIgnoreCase("absolute"))
                    { ignoreBuffs = true; }
                    else if(modifier.getName().equalsIgnoreCase("ignorespellstrength")
                         || modifier.getName().equalsIgnoreCase("ignore spell strength")
                         || modifier.getName().equalsIgnoreCase("ignorestrength")
                         || modifier.getName().equalsIgnoreCase("ignore strength"))
                    { ignoreSpellStrength = true; }
                    else if(modifier.getName().equalsIgnoreCase("fire"))
                    { isFireDamage = true; }
                    else if(modifier.getName().equalsIgnoreCase("explosion"))
                    { isExplosionDamage = true; }
                    else if(modifier.getName().equalsIgnoreCase("notmagic")
                         || modifier.getName().equalsIgnoreCase("not magic"))
                    { isMagicDamage = false; }
                    else if(modifier.getName().equalsIgnoreCase("projectile"))
                    { isProjectile = true; }
                    else if(modifier.getName().equalsIgnoreCase("notprojectile")
                         || modifier.getName().equalsIgnoreCase("not projectile"))
                    { isProjectile = false; }
                    else if(modifier.getName().equalsIgnoreCase("ignorenonliving")
                         || modifier.getName().equalsIgnoreCase("ignore non living")
                         || modifier.getName().equalsIgnoreCase("ignore nonliving"))
                    { ignoreNonLivingEntities = true; }
                    else if(modifier.getName().equalsIgnoreCase("distancemultiplier")
                         || modifier.getName().equalsIgnoreCase("distance multiplier"))
                    {
                        Double newMultiplier = Doubles.tryParse(modifier.getValue());
                        
                        if(newMultiplier != null)
                            percentOfDamageAtEdge = newMultiplier;
                    }
                }
            }
            
            if(ignoreArmour)      dmgSrc.setDamageBypassesArmor();
            if(ignoreBuffs)       dmgSrc.setDamageIsAbsolute();
            if(isFireDamage)      dmgSrc.setFireDamage();
            if(isMagicDamage)     dmgSrc.setMagicDamage();
            if(isExplosionDamage) dmgSrc.setExplosion();
            if(isProjectile)      dmgSrc.setProjectile();
            
            if(!ignoreSpellStrength)
                damage = damage * spellArgs.getSpellStrength().getStrengthModifier();
            
            for(Entity entity : spellArgs.getEntitiesAffected())
            {
                if(ignoreNonLivingEntities && !(entity instanceof EntityLivingBase))
                    continue;
                
                double damageToTake;
                
                // The below if statement shouldn't actually be necessary - it'll work fine without it, it just saves
                // having to process the below chunk of code where percentageOfDamageToTake will always be 1.
                if(percentOfDamageAtEdge == 1 || entity == spellArgs.getEntityHit())
                    damageToTake = damage;
                else
                {
                    double actualDistance = spellArgs.getBurstLocation().getDistanceFrom(new Location(entity.posX, entity.posY, entity.posZ));
                    double distanceAsPercentOfMax = actualDistance / spellArgs.getAOESize().getDistance();
                    double percentageOfDamageToTake = percentOfDamageAtEdge + ((1.0 - distanceAsPercentOfMax) * (1.0 - percentOfDamageAtEdge));
                    damageToTake = damage * percentageOfDamageToTake;
                }
                
                entity.attackEntityFrom(dmgSrc, (float)damageToTake);
            }
        }
    };
    
    /**
     * Checks whether specified blocks or entities are affected (or any at all if none are specified), and passes a
     * message (by default: "detected") if they are.
     */
    static final SpellEffectDefinition detect = new SpellEffectDefinition("Detect")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        {
            if(spellArgs.getBlocksAffected().size() <= 0 || spellArgs.getEntitiesAffected().size() <= 0)
                return;
            
            String message = "detected";
            
            boolean allBlocks   = false;
            boolean allEntities = false;
            boolean allMobs     = false;
            
            List<SpellEffectDefinitionModifier> blocksToCheckFor   = new ArrayList<SpellEffectDefinitionModifier>();
            List<SpellEffectDefinitionModifier> entitiesToCheckFor = new ArrayList<SpellEffectDefinitionModifier>();
            List<SpellEffectDefinitionModifier> mobsToCheckFor     = new ArrayList<SpellEffectDefinitionModifier>();
            
            boolean detected = false;
            
            for(SpellEffectDefinitionModifier i : def.getModifiers())
            {
                if(i.getName().equalsIgnoreCase("message") || i.getName().equalsIgnoreCase("msg"))
                {
                    if(i.getValue() != null)
                        message = i.getValue();
                }
                else if(i.getName().equalsIgnoreCase("block") || i.getName().equalsIgnoreCase("blocks"))
                {
                    if(allBlocks) continue;
                    
                    if(i.getSubModifiers().isEmpty())
                    {
                        allBlocks = true;
                        blocksToCheckFor.clear();
                    }
                    else
                        blocksToCheckFor.addAll(i.getSubModifiers());
                }
                else if(i.getName().equalsIgnoreCase("entities") || i.getName().equalsIgnoreCase("entities"))
                {
                    if(allEntities) continue;
                    
                    if(i.getSubModifiers().isEmpty())
                    {
                        allEntities = true;
                        entitiesToCheckFor.clear();
                    }
                    else
                        entitiesToCheckFor.addAll(i.getSubModifiers());
                }
                else if(i.getName().equalsIgnoreCase("mob") || i.getName().equalsIgnoreCase("mobs"))
                {
                    if(allMobs) continue;
                    
                    if(i.getSubModifiers().isEmpty())
                    {
                        allMobs = true;
                        mobsToCheckFor.clear();
                    }
                    else
                        mobsToCheckFor.addAll(i.getSubModifiers());
                }
            }
            
            detected = allBlocks && !spellArgs.getBlocksAffected()    .isEmpty()
                    || allEntities && !spellArgs.getEntitiesAffected().isEmpty()
                    || allMobs && !spellArgs.getMobsAffected()        .isEmpty();
            
            if(!detected)
                iLoop:
                for(SpellEffectDefinitionModifier i : blocksToCheckFor)
                    for(BlockLocation block : spellArgs.getBlocksAffected())
                        if(block.getBlockAt().getUnlocalizedName().substring(5).equalsIgnoreCase(i.getName()))
                        {
                            detected = true;
                            break iLoop;
                        }

            if(!detected)
                iLoop:
                for(SpellEffectDefinitionModifier i : entitiesToCheckFor)
                    for(Entity entity : spellArgs.getEntitiesAffected())
                        if(EntityList.getEntityString(entity).equalsIgnoreCase(i.getName()))
                        {
                            detected = true;
                            break iLoop;
                        }

            if(!detected)
                iLoop:
                for(SpellEffectDefinitionModifier i : entitiesToCheckFor)
                    for(Entity entity : spellArgs.getEntitiesAffected())
                        if(entity instanceof EntityLivingBase && EntityList.getEntityString(entity).equalsIgnoreCase(i.getName()))
                        {
                            detected = true;
                            break iLoop;
                        }
            
            if(detected)
                spellArgs.passMessage(message);
        }
    };
    
    /**
     * Gives any affected entities the specified potion effect, or does nothing if no potion effect is specified.
     */
    static final SpellEffectDefinition givePotionEffect = new SpellEffectDefinition("GivePotionEffect")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        {
            GivePotionEffectCache cache = (GivePotionEffectCache)def.getCastCache();
            
            if(cache == null)
            {
                List<PotionEffect> potionEffects = new ArrayList<PotionEffect>();

                for(SpellEffectDefinitionModifier modifier : def.getModifiers())
                {
                    Potion potionEffectType = null;
                    int duration = 0;
                    int amplifier = -1;
                    boolean ambient = false;

                    for(Potion i : Potion.potionTypes)
                        if(i.getName().equalsIgnoreCase(modifier.getName()))
                        {
                            potionEffectType = i;
                            break;
                        }

                    if(potionEffectType == null)
                        continue;

                    for(SpellEffectDefinitionModifier potionArg : modifier.getSubModifiers())
                    {
                        if(potionArg.getName().equalsIgnoreCase("duration")
                        || potionArg.getName().equalsIgnoreCase("time")
                        || potionArg.getName().equalsIgnoreCase("ticks"))
                        {
                            Integer ticks = Ints.tryParse(potionArg.getValue());

                            if(ticks != null)
                                duration += ticks;
                        }
                        else if(potionArg.getName().equalsIgnoreCase("seconds"))
                        {
                            Integer seconds = Ints.tryParse(potionArg.getValue());

                            if(seconds != null)
                                duration += seconds * 20;
                        }
                        else if(potionArg.getName().equalsIgnoreCase("minutes"))
                        {
                            Integer minutes = Ints.tryParse(potionArg.getValue());

                            if(minutes != null)
                                duration += minutes * 1200;
                        }
                        else if(potionArg.getName().equalsIgnoreCase("amplifier")
                             || potionArg.getName().equalsIgnoreCase("level"))
                        {
                            if(amplifier >= 0)
                                continue;

                            Integer lvl = Ints.tryParse(potionArg.getValue());

                            if(lvl != null)
                                amplifier = lvl;
                        }
                        else if(potionArg.getName().equalsIgnoreCase("ambient"))
                            ambient = true;
                    }

                    if(duration <= 0)
                        duration = 200; // 10 seconds.

                    if(amplifier <= 0)
                        amplifier = 1;

                    potionEffects.add(new PotionEffect(potionEffectType.getId(), duration, amplifier, ambient));
                }
                
                cache = new GivePotionEffectCache(potionEffects);
                def.setCastCache(cache);
            }
            
            for(EntityLivingBase mob : spellArgs.getMobsAffected())
                for(PotionEffect pEffect : cache.getFreshPotionEffects())
                    mob.addPotionEffect(pEffect);
        }
    };
    
    /**
     * Modifies the health of any affected entities by the specified amount.
     */
    static final SpellEffectDefinition heal = new SpellEffectDefinition("Heal")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Modifies the mana of any affected entities by the specified amount.
     */
    static final SpellEffectDefinition modifyMana = new SpellEffectDefinition("ModifyMana")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Creates particle effects on spell-burst, applying any properties passed as arguments to the spell burst.
     */
    static final SpellEffectDefinition particle = new SpellEffectDefinition("Particle")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Passes all specified properties to any spell projectile created when a spell containing this spell effect
     * definition is cast.
     */
    static final SpellEffectDefinition projectile = new SpellEffectDefinition("Projectile")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Replaces all affected blocks with the a new block with the specified string ID and data value.
     */
    static final SpellEffectDefinition replaceBlock = new SpellEffectDefinition("ReplaceBlock")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Replaces any affected itemstacks with the a new itemstack with the specified string ID and data value.
     */
    static final SpellEffectDefinition replaceItem = new SpellEffectDefinition("ReplaceItem")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Sets the mana of any affected spell casters to the specified amount.
     */
    static final SpellEffectDefinition setMana = new SpellEffectDefinition("Mana")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Forcibly sets a player's shader to the specified one, one of a specified number, or a random one for a determined
     * or predetermined period of time.
     */
    static final SpellEffectDefinition shader = new SpellEffectDefinition("Shader")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    /**
     * Causes any affected spell casters to cast the currently equipped spell, or a specified spell passed as an
     * argument.
     */
    static final SpellEffectDefinition triggerSpell = new SpellEffectDefinition("TriggerSpell")
    {
        @Override
        public void performEffect(SpellArgs spellArgs, ConfiguredDefinition def)
        { throw new NotImplementedException("Not implemented yet."); }
    };
}