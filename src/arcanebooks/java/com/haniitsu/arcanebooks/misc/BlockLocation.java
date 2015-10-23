package com.haniitsu.arcanebooks.misc;

// Does forge have an officially supported location class? I couldn't find it.

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;
import org.apache.commons.lang3.NotImplementedException;


/*

What's forge's class for referring to instances of blocks in the world - for instance, a dirt block at
x:50, y:50, z:50 in the overworld? I can't find it, it seems pretty much impossible to google, and IRC isn't being
much help - I keep getting told how to get the Block object of a block at a given set of coördinates, or the tile
entity, or the ID of a block at a set of coördinates.

If the world is defined as:

public class World
{
    List<List<List<SOMECLASS>>> worldBlocks;
}

where SomeClass contains the Block variable, or a reference to it to specify which block it is, what is SOMECLASS?
*/

public class BlockLocation
{
    public BlockLocation(int worldId, int x, int y, int z)
    {
        this.worldId = worldId;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public BlockLocation(int x, int y, int z)
    { this(0, x, y, z); }
    
    int worldId, x, y, z;
    
    public int getWorldId()
    { return worldId; }
    
    public World getWorld()
    { return DimensionManager.getWorld(worldId); }
    
    public int getX()
    { return x; }
    
    public int getY()
    { return y; }
    
    public int getZ()
    { return z; }
    
    public Block getBlockAt()
    { return getWorld().getBlock(x, y, z); }
    
    public TileEntity getTileEntityAt()
    { return getWorld().getTileEntity(x, y, z); }
    
    public Location toLocationCentre()
    { return new Location(worldId, 0.5 + x, 0.5 + y, 0.5 + z); }
    
    public Location toLocationFloored()
    { return new Location(worldId, x, y, z); }
    
    // The break block methods are written using the aid of Tinker's Construct and Block source code.
    // tconstruct.library.tools.HarvestTool
    // net.minecraft.block.Block
    
    public void breakBlock()
    { breakBlockWithFortune(0); }
    
    public void breakBlock(EntityPlayer player)
    { breakBlockWithFortune(player, 0); }
    
    public void breakBlockWithSilkTouch()
    {
        Block block = getBlockAt();
        World world = getWorld();
        int blockMeta = world.getBlockMetadata(x, y, z);
        
        BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world,
                                                                   GameType.NOT_SET,
                                                                   null,
                                                                   x, y, z);
        
        if(event.isCanceled())
            return;
        
        if(world.isRemote) // client-side
        {
            world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (blockMeta << 12));
            
            if(world.setBlockToAir(x, y, z))
                block.onBlockDestroyedByPlayer(world, x, y, z, blockMeta);
        }
        else // server-side
        {
            block.onBlockHarvested(world, x, y, z, blockMeta, null);
            
            if(world.setBlockToAir(x, y, z))
            {
                block.onBlockDestroyedByPlayer(world, x,y,z, blockMeta);
                
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                ItemStack itemStack = null;
                Method createStackedBlockMethod = null;
                Method dropBlockAsItemMethod = null;
                
                try
                {
                    createStackedBlockMethod = Block.class.getDeclaredMethod("createStackedBlock", int.class);
                    dropBlockAsItemMethod = Block.class.getDeclaredMethod("dropBlockAsItem", World.class, int.class, int.class, int.class, ItemStack.class);
                }
                catch(NoSuchMethodException ex)
                { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                catch(SecurityException ex)
                { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                
                if(createStackedBlockMethod != null)
                {
                    createStackedBlockMethod.setAccessible(true);
                
                    try
                    { itemStack = (ItemStack)createStackedBlockMethod.invoke(block, blockMeta); }
                    catch(IllegalAccessException ex)
                    { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                    catch(IllegalArgumentException ex)
                    { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                    catch(InvocationTargetException ex)
                    { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                }
                
                if(itemStack != null)
                    items.add(itemStack);
                
                // Is it okay to pass null to the entityplayer argument here?
                ForgeEventFactory.fireBlockHarvesting(items, world, block, x, y, z, blockMeta, 0, 1.0f, true, null);
                
                if(dropBlockAsItemMethod != null)
                {
                    dropBlockAsItemMethod.setAccessible(true);
                
                    for(ItemStack stack : items)
                        try
                        { dropBlockAsItemMethod.invoke(block, world, x, y, z, blockMeta, stack); }
                        catch(IllegalAccessException ex)
                        { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                        catch(IllegalArgumentException ex)
                        { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                        catch(InvocationTargetException ex)
                        { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                }
            }
        }
    }
    
    public void breakBlockWithSilkTouch(EntityPlayer player)
    {
        if(player == null)
            breakBlockWithSilkTouch();
        
        if(!(player instanceof EntityPlayerMP))
            return;
        
        Block block = getBlockAt();
        World world = getWorld();
        int blockMeta = world.getBlockMetadata(x, y, z);
        EntityPlayerMP playerMp = (EntityPlayerMP)player;
        
        BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world,
                                                                   playerMp.theItemInWorldManager.getGameType(),
                                                                   playerMp,
                                                                   x, y, z);
        
        if(event.isCanceled())
            return;
        
        if(world.isRemote) // client-side
        {
            world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (blockMeta << 12));
            
            if(block.removedByPlayer(world, player, x, y, z, true))
                block.onBlockDestroyedByPlayer(world, x, y, z, blockMeta);

            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(2, x,y,z, Minecraft.getMinecraft().objectMouseOver.sideHit));
        }
        else // server-side
        {
            block.onBlockHarvested(world, x, y, z, blockMeta, player);
            
            if(block.removedByPlayer(world, player, x, y, z, true))
            {
                block.onBlockDestroyedByPlayer(world, x,y,z, blockMeta);
                player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
                
                ArrayList<ItemStack> items = new ArrayList<ItemStack>();
                ItemStack itemStack = null;
                Method createStackedBlockMethod = null;
                Method dropBlockAsItemMethod = null;
                
                try
                {
                    createStackedBlockMethod = Block.class.getDeclaredMethod("createStackedBlock", int.class);
                    dropBlockAsItemMethod = Block.class.getDeclaredMethod("dropBlockAsItem", World.class, int.class, int.class, int.class, ItemStack.class);
                }
                catch(NoSuchMethodException ex)
                { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                catch(SecurityException ex)
                { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                
                if(createStackedBlockMethod != null)
                {
                    createStackedBlockMethod.setAccessible(true);
                
                    try
                    { itemStack = (ItemStack)createStackedBlockMethod.invoke(block, blockMeta); }
                    catch(IllegalAccessException ex)
                    { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                    catch(IllegalArgumentException ex)
                    { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                    catch(InvocationTargetException ex)
                    { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                }
                
                if(itemStack != null)
                    items.add(itemStack);
                
                ForgeEventFactory.fireBlockHarvesting(items, world, block, x, y, z, blockMeta, 0, 1.0f, true, player);
                
                if(dropBlockAsItemMethod != null)
                {
                    dropBlockAsItemMethod.setAccessible(true);
                
                    for(ItemStack stack : items)
                        try
                        { dropBlockAsItemMethod.invoke(block, world, x, y, z, blockMeta, stack); }
                        catch(IllegalAccessException ex)
                        { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                        catch(IllegalArgumentException ex)
                        { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                        catch(InvocationTargetException ex)
                        { Logger.getLogger(BlockLocation.class.getName()).log(Level.SEVERE, null, ex); }
                }
            }

            playerMp.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
        }
    }
    
    public void breakBlockWithFortune(int lvl)
    {
        Block block = getBlockAt();
        World world = getWorld();
        int blockMeta = world.getBlockMetadata(x, y, z);
        
        BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world,
                                                                   GameType.NOT_SET,
                                                                   null,
                                                                   x, y, z);
        
        if(event.isCanceled())
            return;
        
        if(world.isRemote) // client-side
        {
            world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (blockMeta << 12));
            
            if(world.setBlockToAir(x, y, z))
                block.onBlockDestroyedByPlayer(world, x, y, z, blockMeta);
        }
        else
        {
            // v v v Is this line okay? Is it okay passing null in place of player to this method?
            block.onBlockHarvested(world, x, y, z, blockMeta, null);
            block.dropBlockAsItem(world, x, y, z, blockMeta, lvl);
            block.dropXpOnBlockBreak(world, x, y, z, event.getExpToDrop());
        }
    }
    
    public void breakBlockWithFortune(EntityPlayer player, int lvl)
    {
        if(player == null)
            breakBlockWithFortune(lvl);
        
        if(!(player instanceof EntityPlayerMP))
            return;
        
        Block block = getBlockAt();
        World world = getWorld();
        int blockMeta = world.getBlockMetadata(x, y, z);
        EntityPlayerMP playerMp = (EntityPlayerMP)player;
        
        BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(world,
                                                                   playerMp.theItemInWorldManager.getGameType(),
                                                                   playerMp,
                                                                   x, y, z);
        
        if(event.isCanceled())
            return;
        
        if(world.isRemote) // client-side
        {
            world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (blockMeta << 12));
            
            if(block.removedByPlayer(world, player, x, y, z, true))
                block.onBlockDestroyedByPlayer(world, x, y, z, blockMeta);

            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C07PacketPlayerDigging(2, x,y,z, Minecraft.getMinecraft().objectMouseOver.sideHit));
        }
        else // server-side
        {
            block.onBlockHarvested(world, x, y, z, blockMeta, player);
            
            if(block.removedByPlayer(world, player, x, y, z, true))
            {
                block.onBlockDestroyedByPlayer(world, x,y,z, blockMeta);
                player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
                
                ThreadLocal<EntityPlayer> harvesters = null;
                
                // The following is because I need to access the harvesters field to run some of the code from 
                // harvestBlock(world, player, x, y, z, blockmeta);, but the harvesters field is set to protected.
                try
                {
                    Field harvestersField = Block.class.getDeclaredField("harvesters");
                    harvestersField.setAccessible(true);
                    harvesters = (ThreadLocal<EntityPlayer>)harvestersField.get(block);
                }
                catch(NoSuchFieldException ex) {}
                catch(SecurityException ex) {}
                catch(IllegalAccessException ex) {}
                
                if(harvesters != null)
                    harvesters.set(player);
                
                block.dropBlockAsItem(world, x, y, z, blockMeta, lvl);
                
                if(harvesters != null)
                    harvesters.set(null);
                
                block.dropXpOnBlockBreak(world, x, y, z, event.getExpToDrop());
            }

            playerMp.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
        }
    }
}
