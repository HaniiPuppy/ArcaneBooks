package com.haniitsu.arcanebooks.magic.castcaches;

import com.haniitsu.arcanebooks.magic.SpellArgs;
import com.haniitsu.arcanebooks.util.ArcaneSpellEntityDamageSource;
import com.haniitsu.arcanebooks.util.ArcaneSpellGeneralDamageSource;
import net.minecraft.util.DamageSource;

public class DamageCache extends CastCache
{
    public DamageCache(double baseDamage,    double percentOfDamageAtEdge,
                       boolean ignoreArmour, boolean ignoreBuffs,   boolean ignoreSpellStrength,
                       boolean isFireDamage, boolean isMagicDamage, boolean isExplosionDamage, Boolean isProjectile)
    {
        this.baseDamage            = baseDamage;
        this.percentOfDamageAtEdge = percentOfDamageAtEdge;
        this.ignoreArmour          = ignoreArmour;
        this.ignoreBuffs           = ignoreBuffs;
        this.ignoreSpellStrength   = ignoreSpellStrength;
        this.isFireDamage          = isFireDamage;
        this.isMagicDamage         = isMagicDamage;
        this.isExplosionDamage     = isExplosionDamage;
        this.isProjectile          = isProjectile;
    }
    
    final protected double baseDamage;
    final protected double percentOfDamageAtEdge; // i.e. as a percent, how much damage is taken by those at the max distance.
    
    final protected boolean ignoreArmour;
    final protected boolean ignoreBuffs;
    final protected boolean ignoreSpellStrength;
    
    final protected boolean isFireDamage;
    final protected boolean isMagicDamage;
    final protected boolean isExplosionDamage;
    final protected Boolean isProjectile;

    // isProjectile will be null if whether this should be true or false should be left up to whether the spell is
    // cast as a projectile or not.
    
    public double getBaseDamage()
    { return baseDamage; }
    
    public double getPercentOfDamageAtEdge()
    { return percentOfDamageAtEdge; }
    
    public boolean ignoreArmour()
    { return ignoreArmour; }
    
    public boolean ignoreBuffs()
    { return ignoreBuffs; }
    
    public boolean ignoreSpellStrength()
    { return ignoreSpellStrength; }
    
    public boolean isFireDamage()
    { return isFireDamage; }
    
    public boolean isMagicDamage()
    { return isMagicDamage; }
    
    public boolean isExplosionDamage()
    { return isExplosionDamage; }
    
    public Boolean isProjectile()
    { return isProjectile; }
    
    public DamageSource getEntityDamageSource(SpellArgs spellArgs, boolean isActuallyProjectile)
    {
        DamageSource src = new ArcaneSpellEntityDamageSource(spellArgs);
        applyThingsToDamageSource(src, isActuallyProjectile);
        return src;
        
    }
    
    public DamageSource getGeneralDamageSource(SpellArgs spellArgs, boolean isActuallyProjectile)
    {
        DamageSource src = new ArcaneSpellGeneralDamageSource(spellArgs);
        applyThingsToDamageSource(src, isActuallyProjectile);
        return src;
    }
    
    protected void applyThingsToDamageSource(DamageSource src, boolean isActuallyProjectile)
    {
        if(ignoreArmour)         src.setDamageBypassesArmor();
        if(ignoreBuffs)          src.setDamageIsAbsolute();
        if(isFireDamage)         src.setFireDamage();
        if(isMagicDamage)        src.setMagicDamage();
        if(isExplosionDamage)    src.setExplosion();
        if(isActuallyProjectile) src.setProjectile();
    }
}