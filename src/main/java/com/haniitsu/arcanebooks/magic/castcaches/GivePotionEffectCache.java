package com.haniitsu.arcanebooks.magic.castcaches;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.potion.PotionEffect;

public class GivePotionEffectCache extends CastCache
{
    public GivePotionEffectCache(List<PotionEffect> potionEffects)
    { this.potionEffects = potionEffects; }
    
    protected List<PotionEffect> potionEffects;
    
    public List<PotionEffect> getFreshPotionEffects()
    {
        List<PotionEffect> newEffects = new ArrayList<PotionEffect>();
        
        for(PotionEffect effect : potionEffects)
            newEffects.add(new PotionEffect(effect));
        
        return newEffects;
    }
}