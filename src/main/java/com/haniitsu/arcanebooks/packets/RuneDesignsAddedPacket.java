package com.haniitsu.arcanebooks.packets;

import com.haniitsu.arcanebooks.ArcaneBooks;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class RuneDesignsAddedPacket implements IMessage
{
    public static class Handler implements IMessageHandler<RuneDesignsAddedPacket, IMessage>
    {
        @Override
        public IMessage onMessage(RuneDesignsAddedPacket message, MessageContext ctx)
        {
            ArcaneBooks.instance.registries.runeDesigns.registerFromString(message.unparsedRuneDesigns);
            return null;
        }
        
        /*
        
        Version of the above method for Minecraft v1.8.
        
        @Override
        public IMessage onMessage(RuneDesignsAddedPacket message, MessageContext ctx)
        {
            IThreadListener mainThread = Minecraft.getMinecraft();
            
            mainThread.addScheduledTask(new Runnable()
            {
                @Override
                public void run()
                { ArcaneBooks.instance.registries.runeDesigns.addFromString(message.unparsedRuneDesigns); }
            });
            return null;
        }
        
        */
    }
    
    public RuneDesignsAddedPacket(String unparsedRuneDesigns)
    { this.unparsedRuneDesigns = unparsedRuneDesigns; }
    
    public RuneDesignsAddedPacket()
    { this(null); }
    
    String unparsedRuneDesigns;
    
    @Override
    public void fromBytes(ByteBuf buf)
    { unparsedRuneDesigns = ByteBufUtils.readUTF8String(buf); }

    @Override
    public void toBytes(ByteBuf buf)
    { ByteBufUtils.writeUTF8String(buf, unparsedRuneDesigns); }
}
