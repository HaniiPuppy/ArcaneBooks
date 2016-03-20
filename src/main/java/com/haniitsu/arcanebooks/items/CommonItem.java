package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.ArcaneBooks;
import net.minecraft.item.Item;

// TO DO: Document this class.
public class CommonItem extends Item
{
    public CommonItem(String uniqueItemName)
    {
        super();
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
    
    public String getUnlocalizedNameWithoutPrefix()
    { return this.getUnlocalizedName().substring(5); }
    
    public String getUnlocalizedNameWithoutModName()
    { return unlocalisedName; }
}
