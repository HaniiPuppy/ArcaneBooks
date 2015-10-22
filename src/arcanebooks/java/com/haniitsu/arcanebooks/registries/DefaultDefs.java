package com.haniitsu.arcanebooks.registries;

import com.haniitsu.arcanebooks.magic.SpellArgs;
import com.haniitsu.arcanebooks.magic.SpellEffectDefinition;
import org.apache.commons.lang3.NotImplementedException;

// Just so the default effect definitions don't clutter up SpellEffectDefinitionRegistry

class DefaultDefs
{
    static final SpellEffectDefinition logicalIf = new SpellEffectDefinition("If")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    static final SpellEffectDefinition logicalIfNot = new SpellEffectDefinition("IfNot")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
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
        { throw new NotImplementedException("Not implemented yet."); }
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
        { throw new NotImplementedException("Not implemented yet."); }
    };
    
    static final SpellEffectDefinition detect = new SpellEffectDefinition("Detect")
    {
        @Override
        public void PerformEffect(SpellArgs spellArgs)
        { throw new NotImplementedException("Not implemented yet."); }
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