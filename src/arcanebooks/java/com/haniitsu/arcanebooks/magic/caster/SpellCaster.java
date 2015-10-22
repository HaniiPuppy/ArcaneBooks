package com.haniitsu.arcanebooks.magic.caster;

import com.haniitsu.arcanebooks.magic.mana.ManaStore;
import com.haniitsu.arcanebooks.misc.Location;

public interface SpellCaster
{
    ManaStore getMana();
    Location getLocation();
}