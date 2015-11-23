package com.haniitsu.arcanebooks.magic.caster;

import com.haniitsu.arcanebooks.magic.Spell;
import com.haniitsu.arcanebooks.magic.mana.ManaStore;
import com.haniitsu.arcanebooks.misc.Direction;
import com.haniitsu.arcanebooks.misc.Location;
import com.haniitsu.arcanebooks.projectiles.EntitySpellProjectile;
import java.util.List;

/**
 * A thing (entity, player, block, etc.) with the capacity to cast spells.
 */
public interface SpellCaster
{
    /**
     * Gets the spell-caster's store of mana.
     * @return The mana store.
     */
    ManaStore getMana();
    
    /**
     * Gets the location in the world of the spell caster.
     * @return The caster's location.
     */
    Location getLocation();
    
    /**
     * Gets the direction that the spell caster is facing.
     * @return The caster's direction.
     */
    Direction getDirection();
    
    /**
     * Launches a spell projectile, which will burst the passed spell phrases, as part of the passed spell cast.
     * @param spellCast The spell cast that the phrases will be burst as part of.
     * @param phrases The spell phrases to be enclosed in the projectile and burst on landing.
     * @return The projectile entity created.
     */
    EntitySpellProjectile launchSpellPhrases(Spell.SpellCast spellCast, Spell.Phrase... phrases);
    
    /**
     * Launches a spell projectile, which will burst the passed spell phrases, as part of the passed spell cast.
     * @param spellCast The spell cast that the phrases will be burst as part of.
     * @param phrases The spell phrases to be enclosed in the projectile and burst on landing.
     * @return The projectile entity created.
     */
    EntitySpellProjectile launchSpellPhrases(Spell.SpellCast spellCast, List<? extends Spell.Phrase> phrases);
}