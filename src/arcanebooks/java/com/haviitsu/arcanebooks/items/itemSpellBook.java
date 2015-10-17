package com.haviitsu.arcanebooks.items;

import com.haviitsu.arcanebooks.ArcaneIndex;
import com.haviitsu.arcanebooks.projectiles.EntitySpellProjectile;
import net.minecraft.entity.player.EntityPlayer;
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

    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer entityPlayer)
    {
        world.playSoundAtEntity(entityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if(world.isRemote)
        {
                world.spawnEntityInWorld(new EntitySpellProjectile(world, entityPlayer));
        }

        return item;
    }


}
