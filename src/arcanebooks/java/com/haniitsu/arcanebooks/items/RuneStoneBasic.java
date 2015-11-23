package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellEffectModifier;
import com.haniitsu.arcanebooks.runes.RuneDesign;
import org.apache.commons.lang3.NotImplementedException;

/**
 * The in-game item that contains spell effect modifiers, for use in writing spell books/scrolls.
 */
public class RuneStoneBasic extends RuneStoneCommon
{
    /** The modifier held within this runestone. */
    SpellEffectModifier containedModifier;
    
    @Override
    public RuneDesign getRuneDesign()
    { throw new NotImplementedException("Not implemented yet."); }
}