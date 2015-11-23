package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.magic.SpellEffect;
import com.haniitsu.arcanebooks.runes.RuneDesign;
import org.apache.commons.lang3.NotImplementedException;

/**
 * The in-game item that contains spell-effects, for use in writing spell books/scrolls.
 */
public class RuneStoneIntricate extends RuneStoneCommon
{
    /** The spell effect held within this runestone. */
    SpellEffect containedSpellEffect;

    @Override
    public RuneDesign getRuneDesign()
    { throw new NotImplementedException("Not implemented yet."); }
}