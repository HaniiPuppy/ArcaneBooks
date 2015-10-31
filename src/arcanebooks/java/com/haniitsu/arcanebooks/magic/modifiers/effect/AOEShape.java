package com.haniitsu.arcanebooks.magic.modifiers.effect;

import com.haniitsu.arcanebooks.misc.Direction;
import com.haniitsu.arcanebooks.misc.Location;

public abstract class AOEShape implements SpellEffectModifier
{
    public AOEShape()
    { this(1); }
    
    public AOEShape(double AOESizeModifier)
    { this.AOESizeModifier = AOESizeModifier; }
    
    protected final double AOESizeModifier;
    
    public static final AOEShape around = new AOEShape()
    {
        @Override
        public boolean coversLocation(double    AOESize,
                                      Location  burstLocation,
                                      Direction burstDirection,
                                      Location  checkLocation)
        {
            double distanceX = checkLocation.getX() - burstLocation.getX();
            double distanceY = checkLocation.getY() - burstLocation.getY();
            double distanceZ = checkLocation.getZ() - burstLocation.getZ();
            
            // Squaring that extra time is cheaper than square-rooting the XYZ distance.
            return ((distanceX * distanceX) + (distanceY * distanceY) + (distanceZ * distanceZ)) <= (AOESize * AOESize);
        }
    };
    
    public double getAOESizeModifier()
    { return AOESizeModifier; }
    
    public abstract boolean coversLocation(double    AOESize,
                                           Location  burstLocation,
                                           Direction burstDirection,
                                           Location  checkLocation);
}