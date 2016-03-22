package com.haniitsu.arcanebooks.packets;

import com.haniitsu.arcanebooks.ArcaneBooks;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class SpellEffectsRemovedPacket implements IMessage
{
    public static class Handler implements IMessageHandler<SpellEffectsRemovedPacket, IMessage>
    {
        @Override
        public IMessage onMessage(SpellEffectsRemovedPacket message, MessageContext ctx)
        {
            BufferedReader reader = new BufferedReader(new StringReader(message.removedEffectNamesList));
            List<String> effectNamesToRemove = new ArrayList<String>();
            
            try
            {
                for(String line = ""; line != null; line = reader.readLine())
                {
                    line = line.trim();
                    
                    if(line.length() <= 0)
                        continue;
                        
                    effectNamesToRemove.add(line.trim());
                }
            }
            catch(IOException e)
            { throw new RuntimeException("IOException not currently handled. It shouldn't be thrown here anyway.", e); }
            
            ArcaneBooks.instance.registries.spellEffects.deregisterWithNames(effectNamesToRemove);
            return null;
        }
        
        /*
        
        Version of the above method for Minecraft v1.8.
        
        @Override
        public IMessage onMessage(SpellEffectsRemovedPacket message, MessageContext ctx)
        {
            IThreadListener mainThread = Minecraft.getMinecraft();
            BufferedReader reader = new BufferedReader(new StringReader(message.removedEffectNamesList));
            final List<String> effectNamesToRemove = new ArrayList<String>();
            
            try
            {
                for(String line = ""; line != null; line = reader.readLine())
                {
                    line = line.trim();
                    
                    if(line.length() <= 0)
                        continue;
                        
                    effectNamesToRemove.add(line.trim());
                }
            }
            catch(IOException e)
            { throw new RuntimeException("IOException not currently handled. It shouldn't be thrown here anyway.", e); }
            
            mainThread.addScheduledTask(new Runnable()
            {
                @Override
                public void run()
                { ArcaneBooks.instance.registries.spellEffects.deregisterWithNames(effectNamesToRemove); }
            });
            return null;
        }
        
        */
    }
    
    public SpellEffectsRemovedPacket(String removedEffectNamesList)
    { this.removedEffectNamesList = removedEffectNamesList; }
    
    public SpellEffectsRemovedPacket()
    { this(null); }
    
    String removedEffectNamesList;
    
    @Override
    public void fromBytes(ByteBuf buf)
    { removedEffectNamesList = ByteBufUtils.readUTF8String(buf); }

    @Override
    public void toBytes(ByteBuf buf)
    { ByteBufUtils.writeUTF8String(buf, removedEffectNamesList); }
}