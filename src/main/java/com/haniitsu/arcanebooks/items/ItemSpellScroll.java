package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.ArcaneBooks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.NotImplementedException;

public class ItemSpellScroll extends CommonItem
{
    public ItemSpellScroll()
    {
        super(ArcaneBooks.Strings.itemId_spellScroll);
        this.setTextureName(this.getUnlocalizedNameWithoutModName());
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setMaxStackSize(1);
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer entityPlayer)
    {
        throw new NotImplementedException("Not implemented yet.");
    }
}