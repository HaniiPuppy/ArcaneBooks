package com.haniitsu.arcanebooks.misc;

import com.haniitsu.arcanebooks.misc.geometry.Line;
import com.haniitsu.arcanebooks.misc.geometry.Point2d;

public class UtilMethods
{
    public static boolean pointIsOnLine(Point2d point, Line<? extends Point2d> line)
    { return pointIsOnLine(point, line, 0.000000000001); }
    
    public static boolean pointIsOnLine(Point2d point, Line<? extends Point2d> line, double precisionErrorMargin)
    {
        double firstHalfDistance = new Line<Point2d>(line.getStart(), point).getLength();
        double secondHalfDistance = new Line<Point2d>(point, line.getEnd()).getLength();
        double totalDistance = line.getLength();
        return totalDistance <= firstHalfDistance + secondHalfDistance + precisionErrorMargin;
    }
    
    
    
    public static void checkMinMaxArgs(int min, int max, String minName, String maxName)
    {
        if(min > max)
            throw new IllegalArgumentException(maxName + " (" + max + ") must be greater than " + minName + " (" + min + ").");
    }
}