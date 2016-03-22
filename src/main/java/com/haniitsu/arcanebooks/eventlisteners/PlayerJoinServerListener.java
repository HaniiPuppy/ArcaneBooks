package com.haniitsu.arcanebooks.eventlisteners;

import com.haniitsu.arcanebooks.ArcaneBooks;
import com.haniitsu.arcanebooks.misc.UtilMethods;
import com.haniitsu.arcanebooks.packets.SpellEffectsSyncPacket;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Listens to players logging into the server, so that the server can send the player a packet containing registry
 * information/contents as loaded or otherwise held by the server.
 */
public class PlayerJoinServerListener
{
    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event)
    {
        if(UtilMethods.playerIsRunningServer(event.player))
        {
            ArcaneBooks.instance.registries.spellEffects = ArcaneBooks.instance.registries.baseSpellEffects;
            return;
        }

        ArcaneBooks.instance.packetChannel.sendTo(new SpellEffectsSyncPacket(), (EntityPlayerMP)event.player);
    }
}