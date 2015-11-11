package com.haniitsu.arcanebooks.magic.caster;

import com.haniitsu.arcanebooks.magic.Spell.Phrase;
import com.haniitsu.arcanebooks.magic.mana.ManaStore;
import com.haniitsu.arcanebooks.misc.Location;
import com.haniitsu.arcanebooks.projectiles.EntitySpellProjectile;
import java.util.List;

public interface SpellCaster
{
    ManaStore getMana();
    Location getLocation();
    
    EntitySpellProjectile launchSpellPhrases(Phrase... phrases);
    EntitySpellProjectile launchSpellPhrases(List<? extends Phrase> phrases);
}