package com.haniitsu.arcanebooks.magic.caster;

import com.haniitsu.arcanebooks.misc.BlockLocation;
import com.haniitsu.arcanebooks.misc.Location;

public abstract class SpellCasterBlock implements SpellCaster
{
    public SpellCasterBlock(BlockLocation block)
    { this.block = block; }
    
    BlockLocation block;

    @Override
    public Location getLocation()
    { return block.toLocationCentre(); }
    
    public BlockLocation getBlockLocation()
    { return block; }
}