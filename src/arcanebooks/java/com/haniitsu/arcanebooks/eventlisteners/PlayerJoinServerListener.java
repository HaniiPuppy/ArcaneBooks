package com.haniitsu.arcanebooks.eventlisteners;

import com.haniitsu.arcanebooks.ArcaneBooks;
import com.haniitsu.arcanebooks.packets.PlayerJoinPacket;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;

/**
 * Listens to players logging into the server, so that the server can send the player a packet containing registry
 * information/contents as loaded or otherwise held by the server.
 */
public class PlayerJoinServerListener
{
    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event)
    {
        boolean runningOnServer = !DimensionManager.getWorld(0).isRemote;
        
        if(runningOnServer)
            ArcaneBooks.instance.packetChannel.sendTo(new PlayerJoinPacket(), (EntityPlayerMP)event.player);
    }
}