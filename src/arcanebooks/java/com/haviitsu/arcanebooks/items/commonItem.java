package com.haviitsu.arcanebooks.items;

import com.haviitsu.arcanebooks.ArcaneIndex;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Tatsu on 10/16/2015.
 */
public class commonItem extends Item
{
    public commonItem()
    {
        super();
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
