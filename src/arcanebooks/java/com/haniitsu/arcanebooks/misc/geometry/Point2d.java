package com.haniitsu.arcanebooks.misc.geometry;

public interface Point2d <T extends Number>
{
    T getX();
    T getY();
    double getDistanceFrom(Point2d<T> other);
    double getDistanceFromSquared(Point2d<T> other);
}