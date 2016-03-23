package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.runes.RuneDesign;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * The common class inherited by all runestone items in-game.
 */
public abstract class RuneStoneCommon extends CommonItem
{
    public RuneStoneCommon(String uniqueItemName)
    {
        super(uniqueItemName);
        this.setTextureName(this.getUnlocalizedNameWithoutModName());
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setMaxStackSize(1);
    }
    
    public abstract RuneDesign getRuneDesign(ItemStack stack);
    
    // Temporary. getRuneDesign will later pull from a rune design registry.
    public abstract RuneDesign setRuneDesign(ItemStack stack);
}