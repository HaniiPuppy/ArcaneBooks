package com.haniitsu.arcanebooks.eventlisteners;

import com.haniitsu.arcanebooks.ArcaneBooks;
import com.haniitsu.arcanebooks.packets.PlayerJoinPacket;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;

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