package com.haviitsu.arcanebooks.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

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