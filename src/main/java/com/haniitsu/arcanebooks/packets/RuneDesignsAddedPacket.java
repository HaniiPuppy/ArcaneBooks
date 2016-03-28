package com.haniitsu.arcanebooks.packets;

import com.haniitsu.arcanebooks.ArcaneBooks;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.NotImplementedException;

public class RuneDesignsAddedPacket implements IMessage
{
    public static class Handler implements IMessageHandler<SpellEffectsAddedPacket, IMessage>
    {
        @Override
        public IMessage onMessage(SpellEffectsAddedPacket message, MessageContext ctx)
        {
            throw new NotImplementedException("Not implemented yet.");
        }
        
        /*
        
        Version of the above method for Minecraft v1.8.
        
        @Override
        public IMessage onMessage(SpellEffectsAddedPacket message, MessageContext ctx)
        {
            IThreadListener mainThread = Minecraft.getMinecraft();
            
            mainThread.addScheduledTask(new Runnable()
            {
                @Override
                public void run()
                { ArcaneBooks.instance.registries.spellEffects.addFromString(message.unparsedNewEffects); }
            });
            return null;
        }
        
        */
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    { throw new NotImplementedException("Not implemented yet."); }

    @Override
    public void toBytes(ByteBuf buf)
    { throw new NotImplementedException("Not implemented yet."); }
}
