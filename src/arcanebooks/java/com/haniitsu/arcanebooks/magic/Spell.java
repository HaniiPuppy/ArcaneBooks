package com.haniitsu.arcanebooks.magic;

import com.haniitsu.arcanebooks.magic.caster.SpellCaster;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellEffectModifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.NotImplementedException;

// This is the class that should be contained in signed spellbooks and scrolls.

public class Spell
{
    // phrase = a spell effect + modifiers.
    public class Phrase
    {
        public Phrase(SpellEffect effect, Collection<? extends SpellEffectModifier> modifiers)
        {  }
        
        public Phrase(SpellEffect effect, SpellEffectModifier... modifiers)
        {  }
        
        protected SpellEffect effect;
        protected List<SpellEffectModifier> modifiers;
        
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
        
        void addSpellArgs(SpellArgs args)
        { phrasesCast.add(args); }
        
        public SpellCaster getCaster()
        { return caster; }
        
        public Spell getSpell()
        { return spell; }
        
        public List<SpellArgs> getSpellArgsCast()
        { return new ArrayList<SpellArgs>(phrasesCast); }
    }
    
    public Spell(List<? extends Phrase> phrases)
    { this.phrases = new ArrayList<Phrase>(phrases); }
    
    public Spell(Phrase... phrases)
    { this.phrases = new ArrayList<Phrase>(Arrays.asList(phrases)); }
    
    protected List<Phrase> phrases;
    
    public void cast(SpellCaster caster)
    {
        SpellCast spellCast = new SpellCast(this, caster);
        
        for(Phrase phrase : phrases)
            phrase.cast(spellCast);
    }
}