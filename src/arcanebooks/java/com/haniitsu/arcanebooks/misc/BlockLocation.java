package com.haniitsu.arcanebooks.misc;

// Does forge have an officially supported location class? I couldn't find it.

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;

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
    
    private static final UUID fakePlayerId = UUID.fromString("03c6c547-d960-4a43-99f0-bdd07a3afe29");
    private FakePlayer fakePlayer = null;
    private boolean fakePlayerHasEnchant = false;
    
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
    
    public void breakBlock()
    {
        if(fakePlayerHasEnchant)
            fakePlayer.setCurrentItemOrArmor(0, new ItemStack(Item.getItemById(1)));
        
        fakePlayerHasEnchant = false;
        breakBlockByPlayer(fakePlayer);
    }
    
    public void breakBlockWithFortune(int lvl)
    { breakBlockWithEnchant(Enchantment.fortune, lvl); }
    
    public void breakBlockWithSilkTouch()
    { breakBlockWithEnchant(Enchantment.silkTouch, 1); }
    
    public void breakBlockWithEnchant(Enchantment enchant, int lvl)
    {
        ItemStack fakeItemStack = new ItemStack(Item.getItemById(403));
        // enchanted book. There doesn't seem to be .getItemByName?
        fakeItemStack.addEnchantment(enchant, lvl);
        fakePlayer.setCurrentItemOrArmor(0, fakeItemStack);
        fakePlayerHasEnchant = true;
        breakBlockByPlayer(fakePlayer);
        // Block.harvestBlock doesn't seem to check to make sure the item makes sense for the block it's breaking, just
        // what enchantments it has.
    }
    
    public void breakBlockByPlayer(EntityPlayer player)
    {
        World world = getWorld();
        Block block = this.getBlockAt();
        int blockMeta = world.getBlockMetadata(x, y, z);
        TileEntity tile = world.getTileEntity(x, y, z);

        if(tile != null && !tile.isInvalid() && tile instanceof IInventory && !world.isRemote)
        {
            IInventory inv = (IInventory)tile;
            
            for (int slot = 0; slot < inv.getSizeInventory(); ++slot)
            {
                ItemStack items = inv.getStackInSlot(slot);

                if (items == null || items.stackSize <= 0)
                    continue;

                float f1 = 0.7F;
                double d = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
                double d1 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
                double d2 = (world.rand.nextFloat() * f1) + (1.0F - f1) * 0.5D;
                EntityItem entityitem = new EntityItem(world, x + d, y + d1, z + d2, items);
                entityitem.delayBeforeCanPickup = 10;

                world.spawnEntityInWorld(entityitem);
                inv.setInventorySlotContents(slot, null);
            }
        }
        
        block.harvestBlock(world, player, x, y, z, blockMeta);
    }
    
    protected FakePlayer getFakePlayer()
    {
        if(fakePlayer == null)
            fakePlayer = new FakePlayer(MinecraftServer.getServer().worldServerForDimension(worldId),
                                        new GameProfile(fakePlayerId, "[ArcaneBooks-BlockBreaking]"));
        
        return fakePlayer;
    }
}
