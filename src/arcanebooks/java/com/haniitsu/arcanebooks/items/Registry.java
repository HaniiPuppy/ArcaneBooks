package com.haniitsu.arcanebooks.items;

import cpw.mods.fml.common.registry.GameRegistry;

// TO DO: Fill out javadoc for this class.
public class Registry
{
    public static CommonItem spellBook;

    public static void registerItems()
    {
        spellBook = new ItemSpellBook();
        GameRegistry.registerItem(spellBook, "SpellBook");
    }
}
