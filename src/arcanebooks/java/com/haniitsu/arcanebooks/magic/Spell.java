package com.haniitsu.arcanebooks.magic;

import com.haniitsu.arcanebooks.magic.caster.SpellCaster;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellEffectModifier;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellTarget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.NotImplementedException;

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
        
        public void cast(SpellCast cast)
        {
            throw new NotImplementedException("Not implemented yet.");
            /* I'll write this method once I've added and implemented the spell definitions, def modifiers, and effect
               modifiers. This is the point where certain actions should be taken with regard to spell effect modifiers
               and certain special definition modifiers. Especially determining how the spell should behave given the
               modifiers passed to it, include area-of-effect, target, area-of-effect size, projectile properties, etc.
            */
            
            /*
            
            SpellArgs args = new SpellArgs();
            
            args.setCaster(cast.getCaster());
            args.setCast(cast);
            args.setEffectModifiers(modifiers);
            cast.addSpellArgs(args); 
            
            */
        }
    }
    
    // SpellCast = a single instance of the spell being cast
    public class SpellCast
    {
        public SpellCast(Spell spell, SpellCaster caster)
        {
            this.spell = spell;
            this.caster = caster;
        }
        
        final Spell spell;
        final SpellCaster caster;
        final List<SpellArgs> phrasesCast = new ArrayList<SpellArgs>();
        final List<Phrase> projectilePhrases = new ArrayList<Phrase>();
        
        void addSpellArgs(SpellArgs args)
        { phrasesCast.add(args); }
        
        public SpellCaster getCaster()
        { return caster; }
        
        public Spell getSpell()
        { return spell; }
        
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
        SpellCast spellCast = new SpellCast(this, caster);
        List<Phrase> projectilePhrases = new ArrayList<Phrase>();
        
        for(Phrase phrase : phrases)
        {
            if(phrase.getTargetModifier() == SpellTarget.projectile)
                projectilePhrases.add(phrase);
            else
                phrase.cast(spellCast);
        }
        
        if(!projectilePhrases.isEmpty())
            caster.launchSpellPhrases(projectilePhrases);
    }
}