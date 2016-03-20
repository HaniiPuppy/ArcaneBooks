package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.runes.RuneDesign;
import net.minecraft.creativetab.CreativeTabs;

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
    
    /**
     * Gets the rune design to be used in drawing this runestone.
     * @return The rune design to be used in drawing this.
     */
    public abstract RuneDesign getRuneDesign();
}