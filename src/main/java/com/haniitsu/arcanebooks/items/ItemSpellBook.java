package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.ArcaneBooks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.NotImplementedException;

/**
 * The item containing a spell to be cast by right-clicking, possibly made up of multiple phrases, to be cast in
 * succession.
 */
public class ItemSpellBook extends CommonItem
{
    /** Creates a new instance of this item. */
    public ItemSpellBook()
    {
        super(ArcaneBooks.Strings.itemId_spellBook);
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
