package com.haniitsu.arcanebooks.items;

import com.haniitsu.arcanebooks.ArcaneBooks;
import com.haniitsu.arcanebooks.magic.SpellWord;
import com.haniitsu.arcanebooks.runes.RuneDesign;
import com.haniitsu.arcanebooks.runes.RuneLine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;

/**
 * The common class inherited by all runestone items in-game.
 */
public class ItemRuneStone extends CommonItem
{
    public ItemRuneStone()
    {
        super(ArcaneBooks.Strings.itemId_runeStone);
        this.setTextureName(this.getUnlocalizedNameWithoutModName());
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setMaxStackSize(1);
    }
    
    public RuneDesign getRuneDesign(ItemStack stack)
    {
        throw new NotImplementedException("Not implemented yet.");
    }
    
    // Temporary. getRuneDesign will later pull from a rune design registry.
    public RuneDesign setRuneDesign(ItemStack stack)
    {
        throw new NotImplementedException("Not implemented yet.");
    }
    
    public SpellWord getSpellWord(ItemStack stack)
    {
        throw new NotImplementedException("Not implemented yet.");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister IconRegister)
    {
        super.registerIcons(IconRegister);
        
        for(RuneLine i : RuneLine.values())
            i.setIcon(IconRegister.registerIcon(i.getIconString()));
    }
}