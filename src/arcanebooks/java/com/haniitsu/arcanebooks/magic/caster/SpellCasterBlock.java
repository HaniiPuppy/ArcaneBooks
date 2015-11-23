package com.haniitsu.arcanebooks.magic.caster;

import com.haniitsu.arcanebooks.misc.BlockLocation;
import com.haniitsu.arcanebooks.misc.Location;

/**
 * A block capable of casting spells.
 */
public abstract class SpellCasterBlock implements SpellCaster
{
    /**
     * Creates an instance of SpellCasterBlocks. Realistically, this will be called by subclasses.
     * @param block The location of the casting block.
     */
    public SpellCasterBlock(BlockLocation block)
    { this.block = block; }
    
    /** The casting block. (location of) */
    BlockLocation block;

    @Override
    public Location getLocation()
    { return block.toLocationCentre(); }
    
    /**
     * Gets this block's location.
     * @return The block location.
     */
    public BlockLocation getBlockLocation()
    { return block; }
}