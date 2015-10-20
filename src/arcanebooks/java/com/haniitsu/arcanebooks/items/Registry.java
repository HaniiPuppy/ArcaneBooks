package com.haniitsu.arcanebooks.items;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by Tatsu on 10/16/2015.
 */
public class Registry {
    public static commonItem spellBook;


    public static void registerItems()
    {
        spellBook = new itemSpellBook();
        GameRegistry.registerItem(spellBook, "SpellBook");
    }

}
