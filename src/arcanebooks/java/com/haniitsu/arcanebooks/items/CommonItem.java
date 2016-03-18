package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.ArcaneBooks;
import net.minecraft.item.Item;

// TO DO: Document this class.
public class CommonItem extends Item
{
    public CommonItem(String uniqueItemName)
    {
        super();
        setMaxStackSize(1);
        setUnlocalizedName(ArcaneBooks.Strings.modId + ":" + uniqueItemName);
    }
    
    String unlocalisedName = null;
    
    @Override
    public Item setUnlocalizedName(String id)
    {
        super.setUnlocalizedName(id);
        this.unlocalisedName = id;
        return this;
    }
}
