package com.haniitsu.arcanebooks.magic.castcaches;

public class HealCache extends CastCache
{
    public HealCache(double amountToHeal)
    { this.amountToHeal = amountToHeal; }
    
    protected final double amountToHeal;
    
    public double getAmountToHeal()
    { return amountToHeal; }
}