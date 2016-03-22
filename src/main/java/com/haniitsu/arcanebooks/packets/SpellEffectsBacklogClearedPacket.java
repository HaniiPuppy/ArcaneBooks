package com.haniitsu.arcanebooks.packets;
import com.haniitsu.arcanebooks.ArcaneBooks;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class SpellEffectsBacklogClearedPacket implements IMessage
{
    public static class Handler implements IMessageHandler<SpellEffectsBacklogClearedPacket, IMessage>
    {
        @Override
        public IMessage onMessage(SpellEffectsBacklogClearedPacket message, MessageContext ctx)
        {
            ArcaneBooks.instance.registries.spellEffects.updateBackloggedEffects();
            return null;
        }
        
        /*
        
        Version of the above method for Minecraft v1.8.
        
        @Override
        public IMessage onMessage(SpellEffectsBacklogClearedPacket message, MessageContext ctx)
        {
            IThreadListener mainThread = Minecraft.getMinecraft();
            
            mainThread.addScheduledTask(new Runnable()
            {
                @Override
                public void run()
                { ArcaneBooks.instance.registries.spellEffects.updateBackloggedEffects(); }
            });
            return null;
        }
        
        */
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    { }

    @Override
    public void toBytes(ByteBuf buf)
    { }
}