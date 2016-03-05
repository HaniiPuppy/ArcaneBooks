package com.haniitsu.arcanebooks.util;

import com.haniitsu.arcanebooks.magic.SpellArgs;
import net.minecraft.util.DamageSource;

public class ArcaneSpellGeneralDamageSource extends DamageSource implements ArcaneSpellDamageSource
{
    public ArcaneSpellGeneralDamageSource(SpellArgs spellArgs)
    {
        super("ArcaneSpell(" + spellArgs.getSpellEffect().getName() + ")");
        sourceSpellArgs = spellArgs;
    }
    
    protected final SpellArgs sourceSpellArgs;

    @Override
    public SpellArgs getSourceSpellArgs()
    { return sourceSpellArgs; }
}