package com.haniitsu.arcanebooks.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

// TO DO: Write, then document. The constructors need to take the phrases to be burst and the spellcast the phrases
//        are being burst as.

/**
 * The spell projectile, for containing spell phrases to be burst that use the projectile SpellTarget.
 */
public class EntitySpellProjectile extends EntityThrowable
{
    public EntitySpellProjectile(World world)
    { super(world); }
    
    public EntitySpellProjectile(World world, EntityLivingBase source)
    { super(world, source); }
    
    public EntitySpellProjectile(World world, double sourceX, double sourceY, double sourceZ)
    { super(world, sourceX, sourceY, sourceZ); }
    
    @Override
    protected void onImpact(MovingObjectPosition ImpactLocation)
    {
        // TO DO: Write.
        System.out.println("*Fizzle*");

    }
}