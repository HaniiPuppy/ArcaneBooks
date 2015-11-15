package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellEffectModifier;
import com.haniitsu.arcanebooks.runes.RuneDesign;
import org.apache.commons.lang3.NotImplementedException;

public class RuneBasic extends RuneCommon
{
    SpellEffectModifier containedModifier;
    
    @Override
    public RuneDesign getRuneDesign()
    { throw new NotImplementedException("Not implemented yet."); }
}