package com.haniitsu.arcanebooks.misc.geometry;

public interface Point3d <T extends Number> extends Point2d<T>
{
    T getZ();
}