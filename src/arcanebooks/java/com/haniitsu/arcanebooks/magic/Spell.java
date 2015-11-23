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

/**
 * A spell, as contained within a spell-book or scroll. Contains the spell phrases to be cast, each containing spell
 * effects and their effect modifiers.
 */
public class Spell
{
    /**
     * A spell effect, usually configured with modifiers as written into spell book or scroll.
     */
    public class Phrase
    {
        // TO DO: Add support for multiple spell effects and spell targets in a single phrase, where one effect will be
        //        chosen randomly on spell-cast. At the minute, which spell target is chosen is determined randomly on
        //        phrase creation, which means the same one would be used for every spell cast.
        
        /**
         * Creates an spell phrase from the spell effect and any possible modifiers.
         * @param effect The main spell effect to be performed.
         * @param modifiers The spell effect modifiers to be passed into the spell effect.
         */
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
        
        /**
         * Creates an spell phrase from the spell effect and any possible modifiers.
         * @param effect The main spell effect to be performed.
         * @param modifiers The spell effect modifiers to be passed into the spell effect.
         */
        public Phrase(SpellEffect effect, SpellEffectModifier... modifiers)
        { this(effect, Arrays.asList(modifiers)); }
        
        /** The main spell effect this spell phrase is based around. */
        protected SpellEffect effect;
        
        /** The modifiers to be passed into the spell effect. */
        protected List<SpellEffectModifier> modifiers;
        
        /**
         * The target that should be used when determining how spell.cast should proceed with executing this spell.
         * Target (effect modifier) has to be resolved before casting, as it's used in determining when it is burst.
         * ie it'll be burst later on if it's part of a projectile.
         */
        protected SpellTarget target;
        
        /**
         * Gets a list of all of the modifiers passed into this spell phrase.
         * @return An ordered list of all passed modifiers.
         */
        public List<SpellEffectModifier> getModifiers()
        { return new ArrayList<SpellEffectModifier>(modifiers); }
        
        /**
         * Gets the resolved target modifier to be used by this spell phrase.
         * @return The target modifier to be used when determining how to proceed with executing this phrase.
         */
        SpellTarget getTargetModifier()
        { return target; }
        
        /**
         * Gets all of the AOE modifiers passed to the spell phrase.
         * @return All of the AOE modifiers passed to the spell phrase.
         */
        List<AOE> getPossibleAOEs()
        {
            List<AOE> foundAOEs = new ArrayList<AOE>();
            
            for(SpellEffectModifier i : modifiers)
                if(i instanceof AOE)
                    foundAOEs.add((AOE)i);
            
            return foundAOEs;
        }
        
        /**
         * Gets all of the AOE shape modifiers passed to the spell phrase.
         * @return All of the AOE shape modifiers passed to the spell phrase.
         */
        List<AOEShape> getPossibleAOEShapes()
        {
            List<AOEShape> foundAOEShapes = new ArrayList<AOEShape>();
            
            for(SpellEffectModifier i : modifiers)
                if(i instanceof AOEShape)
                    foundAOEShapes.add((AOEShape)i);
            
            return foundAOEShapes;
        }
        
        /**
         * Gets all of the AOE size modifiers passed to the spell phrase.
         * @return All of the AOE size modifiers passed to the spell phrase.
         */
        List<AOESize> getPossibleAOESizes()
        {
            List<AOESize> foundAOESizes = new ArrayList<AOESize>();
            
            for(SpellEffectModifier i : modifiers)
                if(i instanceof AOESize)
                    foundAOESizes.add((AOESize)i);
            
            return foundAOESizes;
        }
        
        /**
         * Gets all of the spell strength modifiers passed to the spell phrase.
         * @return All of the spell strength modifiers passed to the spell phrase.
         */
        List<SpellStrength> getPossibleSpellStrengths()
        {
            List<SpellStrength> foundSpellStrengths = new ArrayList<SpellStrength>();
            
            for(SpellEffectModifier i : modifiers)
                if(i instanceof SpellStrength)
                    foundSpellStrengths.add((SpellStrength)i);
            
            return foundSpellStrengths;
        }
        
        /**
         * Performs the effect contained within this phrase, with the modifiers included.
         * @param cast The object representing the spell cast that this phrase cast is included in.
         * @param burstLocation The location in the world where this cast is taking effect.
         * @param burstDirection The direction that this cast should be considered to be "pointing in".
         */
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
    
    /**
     * Representation of a single instance of a spell being cast. Serves as the "arguments" class of an entire spell
     * cast.
     */
    public class SpellCast
    {
        /**
         * Creates a new instance.
         * @param spell The spell that is being cast.
         * @param caster The caster casting the spell.
         * @param location The location where the spell is being cast.
         * @param direction The direction the spell is be cast in.
         */
        public SpellCast(Spell spell, SpellCaster caster, Location location, Direction direction)
        {
            this.spell     = spell;
            this.caster    = caster;
            this.location  = location;
            this.direction = direction;
        }
        
        /** The spell that this is a casting of. */
        final Spell spell;
        
        /** The caster that cast the spell. */
        final SpellCaster caster;
        
        /**
         * The location where the spell was cast. Note that this isn't where the spell effect was burst, but where it
         * was initially cast. e.g. if a player cast the spell, the location of the plate.
         */
        final Location location;
        
        /**
         * The direction the spell was cast in. e.g. if a player cast the spell, the direction the player was facing.
         * (usually)
         */
        final Direction direction;
        
        /**
         * The individual phrase casts caused by this spell cast. This will change as the phrases are cast, and will
         * only ever contain the SpellArgs objects for the phrases already cast.
         */
        final List<SpellArgs> phrasesCast = new ArrayList<SpellArgs>();
        
        /** The phrases being cast that are fired as projectiles. */
        final List<Phrase> projectilePhrases = new ArrayList<Phrase>();
        
        /**
         * Adds a spell phrase's spell args object. i.e. the representation of the phrase being cast.
         * @param args The spell args object to add.
         */
        void addSpellArgs(SpellArgs args)
        { phrasesCast.add(args); }
        
        /**
         * Gets the caster that cast the spell.
         * @return The caster that cast the spell.
         */
        public SpellCaster getCaster()
        { return caster; }
        
        /**
         * Gets the spell that was cast.
         * @return The spell that was cast.
         */
        public Spell getSpell()
        { return spell; }
        
        /**
         * Gets the location where the spell was cast.
         * @return The location where the spell was cast.
         */
        public Location getLocation()
        { return location; }
        
        /**
         * Gets the direction that spell was cast in.
         * @return The direction the spell was cast in.
         */
        public Direction getDirection()
        { return direction; }
        
        /**
         * Gets all of the spell args objects for phrases that have already been cast.
         * @return The spell args objects already cast, in order of when they were cast. (first earliest)
         */
        public List<SpellArgs> getSpellArgsCast()
        { return new ArrayList<SpellArgs>(phrasesCast); }
        
        /**
         * Gets all of the spell phrases being cast that target via projectile.
         * @return The projectile spell phrases being cast, in order of when they'll be/they were cast upon bursting.
         */
        public List<Phrase> getProjectilePhrases()
        { return new ArrayList<Phrase>(projectilePhrases); }
        
        /**
         * Specifies that a spell phrase should be treated, for the purposes of this specific spell cast, as targeting
         * via projectile.
         * @param phrase The projectile phrase to demark as such.
         */
        public void markAsProjectilePhrase(Phrase phrase)
        { projectilePhrases.add(phrase); }
    }
    
    /**
     * Creates an instance of a spell.
     * @param phrases The phrases that should make up the spell.
     */
    public Spell(List<? extends Phrase> phrases)
    { this.phrases = new ArrayList<Phrase>(phrases); }
    
    /**
     * Creates an instance of a spell.
     * @param phrases The phrases that should make up the spell.
     */
    public Spell(Phrase... phrases)
    { this.phrases = new ArrayList<Phrase>(Arrays.asList(phrases)); }
    
    /** The phrases that make up the spell. That is, each set of spell effect(s) and spell effect modifier(s). */
    protected List<Phrase> phrases;
    
    /**
     * Performs the spell. That is, performs all of the spell phrases that are part of the spell sequentially.
     * @param caster 
     */
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
            caster.launchSpellPhrases(spellCast, projectilePhrases);
    }
}