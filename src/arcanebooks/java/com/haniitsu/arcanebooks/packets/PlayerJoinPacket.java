package com.haniitsu.arcanebooks.packets;

import com.haniitsu.arcanebooks.ArcaneBooks;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/**
 * Packet sent to players upon joining the server, for sending registry information as loaded or otherwise held by the
 * server.
 */
public class PlayerJoinPacket implements IMessage
{
    /**
     * The packet handler, for copying config information from the server into the client.
     */
    public static class Handler implements IMessageHandler<PlayerJoinPacket, IMessage>
    {
        @Override
        public IMessage onMessage(PlayerJoinPacket message, MessageContext ctx)
        {
            ArcaneBooks.instance.registries.spellEffects.loadFromString(message.unparsedSpellEffects);
            return null;
        }
        
        /*
        
        Version of the above method for Minecraft v1.8.
        
        @Override
        public IMessage onMessage(PlayerJoinPacket message, MessageContext ctx)
        {
            IThreadListener mainThread = Minecraft.getMinecraft();
            
            mainThread.addScheduledTask(new Runnable()
            {
                @Override
                public void run()
                { ArcaneBooks.instance.registries.spellEffects.fillFromString(message.unparsedSpellEffects); }
            });
            return null;
        }
        
        */
    }
    
    String unparsedSpellEffects = null;
    
    @Override
    public void fromBytes(ByteBuf buf)
    { unparsedSpellEffects = ByteBufUtils.readUTF8String(buf); }

    @Override
    public void toBytes(ByteBuf buf)
    { ByteBufUtils.writeUTF8String(buf, ArcaneBooks.instance.registries.spellEffects.toString()); }
}