package com.haniitsu.arcanebooks.util;

import com.haniitsu.arcanebooks.magic.SpellArgs;
import com.haniitsu.arcanebooks.magic.caster.SpellCasterEntity;
import net.minecraft.util.EntityDamageSource;

public class ArcaneSpellEntityDamageSource extends EntityDamageSource implements ArcaneSpellDamageSource
{
    public ArcaneSpellEntityDamageSource(SpellArgs spellArgs)
    {
        super("ArcaneSpell(" + spellArgs.getSpellEffect().getName() + ")",
              ((SpellCasterEntity)spellArgs.getCaster()).getCasterEntity());
        
        sourceSpellArgs = spellArgs;
    }
    
    protected final SpellArgs sourceSpellArgs;
    
    @Override
    public SpellArgs getSourceSpellArgs()
    { return sourceSpellArgs; }
}