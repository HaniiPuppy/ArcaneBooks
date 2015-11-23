package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.runes.RuneDesign;

/**
 * The common class inherited by all runestone items in-game.
 */
public abstract class RuneStoneCommon extends CommonItem
{
    /**
     * Gets the rune design to be used in drawing this runestone.
     * @return The rune design to be used in drawing this.
     */
    public abstract RuneDesign getRuneDesign();
}