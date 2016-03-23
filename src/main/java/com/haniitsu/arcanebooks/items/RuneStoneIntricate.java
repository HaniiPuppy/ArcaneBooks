package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.ArcaneBooks;
import com.haniitsu.arcanebooks.magic.SpellEffect;
import com.haniitsu.arcanebooks.runes.RuneDesign;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;

/**
 * The in-game item that contains spell-effects, for use in writing spell books/scrolls.
 */
public class RuneStoneIntricate extends RuneStoneCommon
{
    public RuneStoneIntricate()
    { super(ArcaneBooks.Strings.itemId_runeStoneIntricate); }
    
    public SpellEffect getSpellEffect(ItemStack stack)
    {
        throw new NotImplementedException("Not implemented yet.");
    }

    @Override
    public RuneDesign getRuneDesign(ItemStack stack)
    {
        throw new NotImplementedException("Not implemented yet.");
    }

    @Override
    public RuneDesign setRuneDesign(ItemStack stack)
    {
        throw new NotImplementedException("Not implemented yet.");
    }
}