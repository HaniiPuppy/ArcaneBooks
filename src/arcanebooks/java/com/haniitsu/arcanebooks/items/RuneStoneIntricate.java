package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.ArcaneBooks;
import com.haniitsu.arcanebooks.runes.RuneDesign;
import org.apache.commons.lang3.NotImplementedException;

/**
 * The in-game item that contains spell-effects, for use in writing spell books/scrolls.
 */
public class RuneStoneIntricate extends RuneStoneCommon
{
    public RuneStoneIntricate()
    { super(ArcaneBooks.Strings.itemId_runeStoneIntricate); }

    @Override
    public RuneDesign getRuneDesign()
    { throw new NotImplementedException("Not implemented yet."); }
}