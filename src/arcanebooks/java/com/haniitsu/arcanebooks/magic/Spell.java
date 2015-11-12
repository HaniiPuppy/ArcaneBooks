package com.haniitsu.arcanebooks.magic;

import com.haniitsu.arcanebooks.magic.caster.SpellCaster;
import com.haniitsu.arcanebooks.magic.caster.SpellCasterBlock;
import com.haniitsu.arcanebooks.magic.caster.SpellCasterEntity;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOE;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOEShape;
import com.haniitsu.arcanebooks.magic.modifiers.effect.AOESize;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellEffectModifier;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellStrength;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellTarget;
import com.haniitsu.arcanebooks.misc.BlockLocation;
import com.haniitsu.arcanebooks.misc.Direction;
import com.haniitsu.arcanebooks.misc.Location;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;

// This is the class that should be contained in signed spellbooks and scrolls.

public class Spell
{
    // phrase = a spell effect + modifiers.
    public class Phrase
    {
        public Phrase(SpellEffect effect, List<? extends SpellEffectModifier> modifiers)
        {
            this.effect = effect;
            this.modifiers = new ArrayList<SpellEffectModifier>(modifiers);
            
            List<SpellTarget> possibleTargets = new ArrayList<SpellTarget>();
            
            for(SpellEffectModifier i : modifiers)
                if(i instanceof SpellTarget)
                    possibleTargets.add((SpellTarget)i);
            
            this.target = possibleTargets.isEmpty()
                          ? SpellTarget.defaultValue
                          : possibleTargets.get(new Random().nextInt(possibleTargets.size()));
        }
        
        public Phrase(SpellEffect effect, SpellEffectModifier... modifiers)
        { this(effect, Arrays.asList(modifiers)); }
        
        protected SpellEffect effect;
        protected List<SpellEffectModifier> modifiers;
        
        protected SpellTarget target;
        
        public List<SpellEffectModifier> getModifiers()
        { return new ArrayList<SpellEffectModifier>(modifiers); }
        
        // Example of the below: getModifiersOfType(AOE.class)
        
        // Like this, as opposed to multiple methods, or taking an enum for this, so that this method can be used the
        // same for third-party or future effect modifiers.
        
        // Target (effect modifier) has to be resolved before casting, as it's used in determining when it is burst.
        // ie it'll be burst later on if it's part of a projectile.
        
        public List<SpellEffectModifier> getModifiersOfType(Class<?> modifierParentClass)
        {
            List<SpellEffectModifier> foundModifiers = new ArrayList<SpellEffectModifier>();
            
            for(SpellEffectModifier i : modifiers)
                if(i.getClass().isAssignableFrom(modifierParentClass))
                    foundModifiers.add(i);
            
            return foundModifiers;
        }
        
        SpellTarget getTargetModifier()
        { return target; }
        
        List<AOE> getPossibleAOEs()
        {
            List<AOE> foundAOEs = new ArrayList<AOE>();
            
            for(SpellEffectModifier i : modifiers)
                if(i instanceof AOE)
                    foundAOEs.add((AOE)i);
            
            return foundAOEs;
        }
        
        List<AOEShape> getPossibleAOEShapes()
        {
            List<AOEShape> foundAOEShapes = new ArrayList<AOEShape>();
            
            for(SpellEffectModifier i : modifiers)
                if(i instanceof AOEShape)
                    foundAOEShapes.add((AOEShape)i);
            
            return foundAOEShapes;
        }
        
        List<AOESize> getPossibleAOESizes()
        {
            List<AOESize> foundAOESizes = new ArrayList<AOESize>();
            
            for(SpellEffectModifier i : modifiers)
                if(i instanceof AOESize)
                    foundAOESizes.add((AOESize)i);
            
            return foundAOESizes;
        }
        
        List<SpellStrength> getPossibleSpellStrengths()
        {
            List<SpellStrength> foundSpellStrengths = new ArrayList<SpellStrength>();
            
            for(SpellEffectModifier i : modifiers)
                if(i instanceof SpellStrength)
                    foundSpellStrengths.add((SpellStrength)i);
            
            return foundSpellStrengths;
        }
        
        public void cast(SpellCast cast, Location burstLocation, Direction burstDirection)
        {
            /* This is the point where certain actions should be taken with regard to spell effect modifiers
               and certain special definition modifiers. Especially determining how the spell should behave given the
               modifiers passed to it, include area-of-effect, target, area-of-effect size, projectile properties, etc.
            */
            
            SpellArgs args = new SpellArgs();
            Random    rand = new Random();
            Entity        casterEntity = cast.getCaster() instanceof SpellCasterEntity ? ((SpellCasterEntity)cast.getCaster()).getCasterEntity()  : null;
            BlockLocation casterBlock  = cast.getCaster() instanceof SpellCasterBlock  ? ((SpellCasterBlock) cast.getCaster()).getBlockLocation() : null;
            
            args.setCaster(cast.getCaster());
            args.setCast(cast);
            args.setEffectModifiers(modifiers);
            args.setBurstLocation(burstLocation);
            args.setBurstDirection(burstDirection);
            
            cast.addSpellArgs(args);
            
            List<AOE>           possibleAOEs      = getPossibleAOEs();
            List<AOESize>       possibleSizes     = getPossibleAOESizes();
            List<AOEShape>      possibleShapes    = getPossibleAOEShapes();
            List<SpellStrength> possibleStrengths = getPossibleSpellStrengths();
            
            AOE           aoe      = possibleAOEs     .isEmpty() ? AOE          .defaultValue : possibleAOEs     .get(rand.nextInt(possibleAOEs     .size()));
            AOESize       aoeSize  = possibleSizes    .isEmpty() ? AOESize      .defaultValue : possibleSizes    .get(rand.nextInt(possibleSizes    .size()));
            AOEShape      aoeShape = possibleShapes   .isEmpty() ? AOEShape     .defaultValue : possibleShapes   .get(rand.nextInt(possibleSizes    .size()));
            SpellStrength strength = possibleStrengths.isEmpty() ? SpellStrength.defaultValue : possibleStrengths.get(rand.nextInt(possibleStrengths.size()));
            
            args.setAOE(aoe);
            args.setAOESize(aoeSize);
            args.setAOEShape(aoeShape);
            args.setSpellStrength(strength);
            args.setSpellTarget(getTargetModifier());
            
            Collection<Entity>        affectedEntities = new HashSet<Entity>();
            Collection<BlockLocation> affectedBlocks   = new HashSet<BlockLocation>();
            
            if(aoe == AOE.targetOnly)
            {
                if(casterEntity != null)
                    affectedEntities.add(casterEntity);
                
                if(casterBlock != null)
                    affectedBlocks.add(casterBlock);
            }
            else if(aoe == AOE.aroundTarget || aoe == AOE.targetAndAroundTarget)
            {
                affectedEntities.addAll(aoeShape.getEntitiesInRange(aoeSize.getDistance()*aoeShape.getAOESizeModifier(), burstLocation, burstDirection));
                affectedBlocks  .addAll(aoeShape.getBlocksInRange  (aoeSize.getDistance()*aoeShape.getAOESizeModifier(), burstLocation, burstDirection));
                
                if(aoe == AOE.aroundTarget)
                {
                    if(casterEntity != null)
                        affectedEntities.remove(casterEntity);
                    
                    if(casterBlock != null)
                        affectedBlocks.remove(casterBlock);
                }
                else if(aoe == AOE.targetAndAroundTarget)
                {
                    if(casterEntity != null)
                        affectedEntities.add(casterEntity);
                    
                    if(casterBlock != null)
                        affectedBlocks.add(casterBlock);
                }
            }
            
            args.setEntitiesHit(affectedEntities);
            args.setBlocksHit(affectedBlocks);
            
            effect.performEffect(args);
            // TO DO: Finish this method.
        }
    }
    
    // SpellCast = a single instance of the spell being cast
    public class SpellCast
    {
        public SpellCast(Spell spell, SpellCaster caster, Location location, Direction direction)
        {
            this.spell     = spell;
            this.caster    = caster;
            this.location  = location;
            this.direction = direction;
        }
        
        final Spell spell;
        final SpellCaster caster;
        final Location location;
        final Direction direction;
        final List<SpellArgs> phrasesCast = new ArrayList<SpellArgs>();
        final List<Phrase> projectilePhrases = new ArrayList<Phrase>();
        
        void addSpellArgs(SpellArgs args)
        { phrasesCast.add(args); }
        
        public SpellCaster getCaster()
        { return caster; }
        
        public Spell getSpell()
        { return spell; }
        
        public Location getLocation()
        { return location; }
        
        public Direction getDirection()
        { return direction; }
        
        public List<SpellArgs> getSpellArgsCast()
        { return new ArrayList<SpellArgs>(phrasesCast); }
        
        public List<Phrase> getProjectilePhrases()
        { return new ArrayList<Phrase>(projectilePhrases); }
        
        public void markAsProjectilePhrase(Phrase phrase)
        { projectilePhrases.add(phrase); }
    }
    
    public Spell(List<? extends Phrase> phrases)
    { this.phrases = new ArrayList<Phrase>(phrases); }
    
    public Spell(Phrase... phrases)
    { this.phrases = new ArrayList<Phrase>(Arrays.asList(phrases)); }
    
    protected List<Phrase> phrases;
    
    public void cast(SpellCaster caster)
    {
        SpellCast spellCast = new SpellCast(this, caster, caster.getLocation(), caster.getDirection());
        List<Phrase> projectilePhrases = new ArrayList<Phrase>();
        
        for(Phrase phrase : phrases)
        {
            if(phrase.getTargetModifier() == SpellTarget.projectile)
                projectilePhrases.add(phrase);
            else
                phrase.cast(spellCast, caster.getLocation(), caster.getDirection());
        }
        
        if(!projectilePhrases.isEmpty())
            caster.launchSpellPhrases(projectilePhrases);
    }
}