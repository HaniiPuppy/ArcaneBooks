package com.haniitsu.arcanebooks.magic.castcaches;

public class BreakBlockCache extends CastCache
{
    public BreakBlockCache(boolean stopNormalDrops, boolean dropExactItem, boolean silkTouch, int fortuneLevel)
    {
        this.stopNormalDrops = stopNormalDrops;
        this.dropExactItem   = dropExactItem;
        this.silkTouch       = silkTouch;
        this.fortuneLevel    = fortuneLevel;
    }
    
    final protected boolean stopNormalDrops;
    final protected boolean dropExactItem;
    final protected boolean silkTouch;
    final protected int     fortuneLevel;
    
    public boolean stopNormalDrops()
    { return stopNormalDrops; }
    
    public boolean dropExactItem()
    { return dropExactItem; }
    
    public boolean useSilkTouch()
    { return silkTouch; }
    
    public int getFortuneLevel()
    { return fortuneLevel; }
}