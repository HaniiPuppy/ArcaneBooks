package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.runes.RuneDesign;

/**
 * The common class inherited by all runestone items in-game.
 */
public abstract class RuneStoneCommon extends CommonItem
{
    public RuneStoneCommon(String uniqueItemName)
    { super(uniqueItemName); }
    
    /**
     * Gets the rune design to be used in drawing this runestone.
     * @return The rune design to be used in drawing this.
     */
    public abstract RuneDesign getRuneDesign();
}