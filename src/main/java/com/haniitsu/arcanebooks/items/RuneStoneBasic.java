package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.ArcaneBooks;
import com.haniitsu.arcanebooks.runes.RuneDesign;
import org.apache.commons.lang3.NotImplementedException;

/**
 * The in-game item that contains spell effect modifiers, for use in writing spell books/scrolls.
 */
public class RuneStoneBasic extends RuneStoneCommon
{
    public RuneStoneBasic()
    { super(ArcaneBooks.Strings.itemId_runeStoneBasic); }
    
    @Override
    public RuneDesign getRuneDesign()
    { throw new NotImplementedException("Not implemented yet."); }
}