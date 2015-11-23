package com.haniitsu.arcanebooks.misc;

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
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;

// Does forge have an officially supported location class? I couldn't find it.

// TO DO: Move the fake player to a hashmap of world IDs and fakeplayers, so that there's only one fakeplayer per world,
//        rather than one for every single block broken.

/**
 * Representation of a single block in the world.
 */
public class BlockLocation
{
    /**
     * Creates a new BlockLocation object.
     * @param worldId The world the block this represents is in.
     * @param x The block's X coördinate.
     * @param y The block's Y coördinate.
     * @param z The block's Z coördinate.
     */
    public BlockLocation(int worldId, int x, int y, int z)
    {
        this.worldId = worldId;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Creates a new BlockLocation object.
     * @param x The block's X coördinate.
     * @param y The block's Y coördinate.
     * @param z The block's Z coördinate.
     */
    public BlockLocation(int x, int y, int z)
    { this(0, x, y, z); }
    
    /** The ID of the world the block this represents is in. */
    int worldId;
    
    /** The represented block's X coördinate. */
    int x;
    
    /** The represented block's Y coördinate. */
    int y;
    
    /** The represented block's Z coördinate. */
    int z;
    
    /** The player ID used by the fake player used in certain actions pertaining to this block. */
    private static final UUID fakePlayerId = UUID.fromString("03c6c547-d960-4a43-99f0-bdd07a3afe29");
    
    /** the fake player object used for certain actions pertaining to this block. */
    private FakePlayer fakePlayer = null;
    
    /** Whether or not the fake player is equipped with an enchanted item. */
    private boolean fakePlayerHasEnchant = false;
    
    /**
     * Gets the ID of the world the block this represents is in.
     * @return The world ID.
     */
    public int getWorldId()
    { return worldId; }
    
    /**
     * Gets the world the block this represents is in.
     * @return The world.
     */
    public World getWorld()
    { return DimensionManager.getWorld(worldId); }
    
    /**
     * Gets the X coördinate of the block this represents.
     * @return The block's X coördinate.
     */
    public int getX()
    { return x; }
    
    /**
     * Gets the Y coördinate of the block this represents.
     * @return The block's Y coördinate.
     */
    public int getY()
    { return y; }
    
    /**
     * Gets the Z coördinate of the block this represents.
     * @return The block's Z coördinate.
     */
    public int getZ()
    { return z; }
    
    /**
     * Gets the minecraft Block object specifying the block at this BlockLocation's coördinates.
     * @return The Block object at this block location's coördinates.
     */
    public Block getBlockAt()
    { return getWorld().getBlock(x, y, z); }
    
    /**
     * Gets the tile entity of the block at this BlockLocation's coördinates.
     * @return The tile entity at the coördinates, or null if there is none.
     */
    public TileEntity getTileEntityAt()
    { return getWorld().getTileEntity(x, y, z); }
    
    /**
     * Gets the location at the centre of the represented block.
     * @return The location at the centre of the block.
     */
    public Location toLocationCentre()
    { return new Location(worldId, 0.5 + x, 0.5 + y, 0.5 + z); }
    
    /**
     * Gets the location at the minimum corner of the represented block.
     * @return The location at the min corner of the represented block.
     */
    public Location toLocationFloored()
    { return new Location(worldId, x, y, z); }
    
    /** Breaks the block, as though a player had broken it. */
    public void breakBlock()
    {
        if(fakePlayerHasEnchant)
            fakePlayer.setCurrentItemOrArmor(0, new ItemStack(Item.getItemById(1)));
        
        fakePlayerHasEnchant = false;
        breakBlockByPlayer(fakePlayer);
    }
    
    /**
     * Breaks the block, as though a player with a tool with the fortune enchantment had broken it.
     * @param lvl The fortune enchantment level to break the block with.
     */
    public void breakBlockWithFortune(int lvl)
    { breakBlockWithEnchant(Enchantment.fortune, lvl); }
    
    /** Breaks the block, as though a player with a tool with the silk touch enchantment had broken it. */
    public void breakBlockWithSilkTouch()
    { breakBlockWithEnchant(Enchantment.silkTouch, 1); }
    
    /**
     * Breaks the block, as though a player with a tool with the passed enchantment had broken it.
     * @param enchant The enchantment to use in breaking the block.
     * @param lvl The level of the enchantment.
     */
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
    
    /**
     * Breaks the block, as though the player passed had broken it.
     * @param player The player to break the block.
     */
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
    
    /**
     * Gets the fake player. Instantiates it if it hasn't been already.
     * @return The fake players.
     */
    protected FakePlayer getFakePlayer()
    {
        if(fakePlayer == null)
            fakePlayer = new FakePlayer(MinecraftServer.getServer().worldServerForDimension(worldId),
                                        new GameProfile(fakePlayerId, "[ArcaneBooks-BlockBreaking]"));
        
        return fakePlayer;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 13 * hash + this.worldId;
        hash = 13 * hash + this.y;
        hash = 13 * hash + this.z;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        final BlockLocation other = (BlockLocation) obj;
        if(this.worldId != other.worldId)
            return false;
        if(this.x != other.x)
            return false;
        if(this.y != other.y)
            return false;
        if(this.z != other.z)
            return false;
        return true;
    }
}
