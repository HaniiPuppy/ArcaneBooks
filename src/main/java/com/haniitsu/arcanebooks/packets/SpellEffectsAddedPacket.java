package com.haniitsu.arcanebooks.packets;

import com.haniitsu.arcanebooks.ArcaneBooks;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class SpellEffectsAddedPacket implements IMessage
{
    public static class Handler implements IMessageHandler<SpellEffectsAddedPacket, IMessage>
    {
        @Override
        public IMessage onMessage(SpellEffectsAddedPacket message, MessageContext ctx)
        {
            ArcaneBooks.instance.registries.spellEffects.addFromString(message.unparsedNewEffects);
            return null;
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
    
    public SpellEffectsAddedPacket(String unparsedNewEffects)
    { this.unparsedNewEffects = unparsedNewEffects; }
    
    public SpellEffectsAddedPacket()
    { this(null); }
    
    String unparsedNewEffects;
    
    @Override
    public void fromBytes(ByteBuf buf)
    { unparsedNewEffects = ByteBufUtils.readUTF8String(buf); }

    @Override
    public void toBytes(ByteBuf buf)
    { ByteBufUtils.writeUTF8String(buf, unparsedNewEffects); }
}