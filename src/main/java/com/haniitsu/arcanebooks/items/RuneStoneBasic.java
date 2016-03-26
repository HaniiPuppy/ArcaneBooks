package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.ArcaneBooks;
import com.haniitsu.arcanebooks.magic.modifiers.effect.SpellEffectModifier;
import com.haniitsu.arcanebooks.runes.RuneDesign;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;

/**
 * The in-game item that contains spell effect modifiers, for use in writing spell books/scrolls.
 */
public class RuneStoneBasic extends RuneStoneCommon
{
    public RuneStoneBasic()
    { super(ArcaneBooks.Strings.itemId_runeStoneBasic); }
    
    public SpellEffectModifier getModifier(ItemStack stack)
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