package com.haniitsu.arcanebooks.misc;

import com.haniitsu.arcanebooks.misc.geometry.Line;
import com.haniitsu.arcanebooks.misc.geometry.Point2d;

public class UtilMethods
{
    public static boolean pointIsOnLine(Point2d point, Line<? extends Point2d> line)
    {
        // Should I take into account possible loss of precision of double here, causing this to possibly return false
        // when it should return true? Is this a worry?
        double firstHalfDistance = new Line<Point2d>(line.getStart(), point).getLength();
        double secondHalfDistance = new Line<Point2d>(point, line.getEnd()).getLength();
        double totalDistance = line.getLength();
        return totalDistance == firstHalfDistance + secondHalfDistance;
    }
}