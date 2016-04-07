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

public class RuneDesignsRemovedPacket implements IMessage
{
    public static class Handler implements IMessageHandler<RuneDesignsRemovedPacket, IMessage>
    {
        @Override
        public IMessage onMessage(RuneDesignsRemovedPacket message, MessageContext ctx)
        {
            BufferedReader reader = new BufferedReader(new StringReader(message.removedRuneDesignSpellwordNames));
            List<String> runeDesignsToRemove = new ArrayList<String>();
            
            try
            {
                for(String line = ""; line != null; line = reader.readLine())
                {
                    line = line.trim();
                    
                    if(line.length() <= 0)
                        continue;
                        
                    runeDesignsToRemove.add(line.trim());
                }
            }
            catch(IOException e)
            { throw new RuntimeException("IOException not currently handled. It shouldn't be thrown here anyway.", e); }
            
            ArcaneBooks.instance.registries.runeDesigns.deregisterByStrings(runeDesignsToRemove);
            return null;
        }
        
        /*
        
        Version of the above method for Minecraft v1.8.
        
        @Override
        public IMessage onMessage(RuneDesignsRemovedPacket message, MessageContext ctx)
        {
            IThreadListener mainThread = Minecraft.getMinecraft();
            BufferedReader reader = new BufferedReader(new StringReader(message.removedRuneDesignSpellwordNames));
            final List<String> runeDesignsToRemove = new ArrayList<String>();
            
            try
            {
                for(String line = ""; line != null; line = reader.readLine())
                {
                    line = line.trim();
                    
                    if(line.length() <= 0)
                        continue;
                        
                    runeDesignsToRemove.add(line.trim());
                }
            }
            catch(IOException e)
            { throw new RuntimeException("IOException not currently handled. It shouldn't be thrown here anyway.", e); }
            
            mainThread.addScheduledTask(new Runnable()
            {
                @Override
                public void run()
                { ArcaneBooks.instance.registries.runeDesigns.deregisterWithSpellWordStrings(runeDesignsToRemove); }
            });
            return null;
        }
        
        */
    }
    
    public RuneDesignsRemovedPacket(String removedRuneDesignSpellwordNames)
    { this.removedRuneDesignSpellwordNames = removedRuneDesignSpellwordNames; }
    
    public RuneDesignsRemovedPacket()
    { this(null); }
    
    String removedRuneDesignSpellwordNames;
    
    @Override
    public void fromBytes(ByteBuf buf)
    { removedRuneDesignSpellwordNames = ByteBufUtils.readUTF8String(buf); }

    @Override
    public void toBytes(ByteBuf buf)
    { ByteBufUtils.writeUTF8String(buf, removedRuneDesignSpellwordNames); }
}