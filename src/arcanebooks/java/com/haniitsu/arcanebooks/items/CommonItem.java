package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.ArcaneIndex;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

// TO DO: Document this class.
public class CommonItem extends Item
{
    public CommonItem()
    {
        super();
        setMaxStackSize(1);

    }

    @Override
    public String getUnlocalizedName()
    {
        return String.format("item.%s%s", ArcaneIndex.RESOURCE_PREFIX, this.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
        //item.ArcaneBooks:UnlocalizedName.name
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return String.format("item.%s%s", ArcaneIndex.RESOURCE_PREFIX, this.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
        //item.ArcaneBooks:UnlocalizedName.name
    }

    public String getUnwrappedUnlocalizedName(String UnlocalizedName)
    {
        return UnlocalizedName.substring(UnlocalizedName.indexOf(".") + 1);
    }
}
