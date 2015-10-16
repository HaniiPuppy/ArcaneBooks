package com.haviitsu.arcanebooks.items;

import com.haviitsu.arcanebooks.ArcaneIndex;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Tatsu on 10/16/2015.
 */

public class itemSpellBook extends commonItem
{
    //this item launches projectiles similar to the bow.
    //right click and it spawns the projectile in front of the player.

    public itemSpellBook()
    {
        super();
        this.setUnlocalizedName(ArcaneIndex.SPELLBOOK_UNLOCALIZED);
    }


    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {



        return false;
    }

}
