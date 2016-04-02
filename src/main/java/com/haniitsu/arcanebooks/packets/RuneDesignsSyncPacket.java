package com.haniitsu.arcanebooks.packets;

import com.haniitsu.arcanebooks.ArcaneBooks;
import com.haniitsu.arcanebooks.registries.RuneDesignRegistry;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class RuneDesignsSyncPacket implements IMessage
{
    public static class Handler implements IMessageHandler<RuneDesignsSyncPacket, IMessage>
    {
        @Override
        public IMessage onMessage(RuneDesignsSyncPacket message, MessageContext ctx)
        {
            ArcaneBooks.instance.registries.runeDesigns = new RuneDesignRegistry(ArcaneBooks.instance.registries.spellEffects, message.unparsedRuneDesigns);
            return null;
        }
        
        /*
        
        Version of the above method for Minecraft v1.8.
        
        @Override
        public IMessage onMessage(SpellEffectsSyncPacket message, MessageContext ctx)
        {
            IThreadListener mainThread = Minecraft.getMinecraft();
            
            mainThread.addScheduledTask(new Runnable()
            {
                @Override
                public void run()
                { ArcaneBooks.instance.registries.runeDesigns = new RuneDesignRegistry(ArcaneBooks.instance.registries.spellEffects, message.unparsedRuneDesigns); }
            });
            return null;
        }
        
        */
    }
    
    String unparsedRuneDesigns = null;
    
    @Override
    public void fromBytes(ByteBuf buf)
    { unparsedRuneDesigns = ByteBufUtils.readUTF8String(buf); }

    @Override
    public void toBytes(ByteBuf buf)
    { ByteBufUtils.writeUTF8String(buf, ArcaneBooks.instance.registries.baseRuneDesigns.toString()); }
}