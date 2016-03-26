package com.haniitsu.arcanebooks.runes;

import static com.haniitsu.arcanebooks.misc.UtilMethods.*;
import com.haniitsu.arcanebooks.misc.geometry.Line;
import com.haniitsu.arcanebooks.misc.geometry.PointInt2d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.NotImplementedException;

public class RuneDesignBuilder
{
    public RuneDesignBuilder()
    { this(0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE); }
    
    public RuneDesignBuilder(List<? extends Line<PointInt2d>> lines)
    { this(0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, lines); }
    
    public RuneDesignBuilder(RuneDesign design)
    { this(0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, design); }
    
    public RuneDesignBuilder(int maxX, int maxY)
    { this(0, maxX, 0, maxY); }
    
    public RuneDesignBuilder(int maxX, int maxY, List<? extends Line<PointInt2d>> lines)
    { this(0, maxX, 0, maxY, lines); }
    
    public RuneDesignBuilder(int maxX, int maxY, RuneDesign design)
    { this(0, maxX, 0, maxY, design); }
    
    public RuneDesignBuilder(int minX, int maxX, int minY, int maxY)
    {
        checkMinMaxArgs(minX, maxX, "minX", "maxX");
        checkMinMaxArgs(minY, maxY, "minY", "maxY");
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    
    public RuneDesignBuilder(int minX, int maxX, int minY, int maxY, List<? extends Line<PointInt2d>> lines)
    {
        checkMinMaxArgs(minX, maxX, "minX", "maxX");
        checkMinMaxArgs(minY, maxY, "minY", "maxY");
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.lines.addAll(lines);
    }
    
    public RuneDesignBuilder(int minX, int maxX, int minY, int maxY, RuneDesign design)
    { this(minX, maxX, minY, maxY, design.getLines()); }
    
    protected List<Line<PointInt2d>> lines = new ArrayList<Line<PointInt2d>>();
    protected int minX, maxX, minY, maxY;
    
    public RuneDesign make()
    {
        consolidate();
        Collections.sort(lines, new Comparator<Line<PointInt2d>>()
        {
            @Override
            public int compare(Line<PointInt2d> o1, Line<PointInt2d> o2)
            {
                int o1Hash = o1.hashCode();
                int o2Hash = o2.hashCode();
                int o1minuso2 = o1Hash - o2Hash;
                return (o1minuso2 < 0) ? -1 : (o1minuso2 > 0) ? 1 : 0;
            }
        });
        return RuneDesign.fromLines(lines);
    }
    
    protected RuneDesignBuilder consolidate()
    {
        List<Line<PointInt2d>> newLines = new ArrayList<Line<PointInt2d>>();
        
        for(Line<PointInt2d> line : lines)
        {
            Line<PointInt2d> currentLine = line;
            
            for(;;)
            {
                Line<PointInt2d> newLinesMemberToRemove = null;
                
                for(Line<PointInt2d> newLinesMember : newLines)
                {
                    if(newLinesMember.overlapsWith(newLinesMember)
                    && (   currentLine.getAngle() == newLinesMember.getAngle()
                        || currentLine.getAngle() == newLinesMember.getAngle() + 0.5
                        || currentLine.getAngle() == newLinesMember.getAngle() - 0.5))
                    {
                        currentLine = currentLine.mergeWith(newLinesMember);
                        newLinesMemberToRemove = newLinesMember;
                    }
                }
                
                if(newLinesMemberToRemove != null)
                    newLines.remove(newLinesMemberToRemove);
                else
                    break;
            }
            
            newLines.add(currentLine);
        }
        
        lines.clear();
        lines.addAll(newLines);
        return this;
    }
    
    public RuneDesignBuilder addLine(Line<PointInt2d> line)
    { lines.add(line); return this; }
    
    public RuneDesignBuilder addLine(PointInt2d start, PointInt2d end)
    { return addLine(new Line<PointInt2d>(start, end)); }
    
    public RuneDesignBuilder addLine(int startX, int startY, int endX, int endY)
    { return addLine(new PointInt2d(startX, startY), new PointInt2d(endX, endY)); }
    
    public RuneDesignBuilder addLines(Line<PointInt2d>... lines)
    { addLines(Arrays.asList(lines)); return this; }
    
    public RuneDesignBuilder addLines(Collection<? extends Line<PointInt2d>> lines)
    { this.lines.addAll(new ArrayList<Line<PointInt2d>>(lines)); return this; }
    
    public RuneDesignBuilder addRandomLine()
    { return addRandomLine(minX, maxX, minY, maxY); }
    
    public RuneDesignBuilder addRandomLine(int maxX, int maxY)
    { return addRandomLine(minX, maxX, minY, maxY); }
    
    public RuneDesignBuilder addRandomLine(int minX, int maxX, int minY, int maxY)
    {
        checkMinMaxArgs(minX, maxX, "minX", "maxX");
        checkMinMaxArgs(minY, maxY, "minY", "maxY");
        Random rand = new Random();
        PointInt2d start = new PointInt2d(rand.nextInt(maxX - minX) + minX, rand.nextInt(maxY - minY) + minY);
        PointInt2d end   = new PointInt2d(rand.nextInt(maxX - minX) + minX, rand.nextInt(maxY - minY) + minY);
        return addLine(start, end);
    }
    
    public RuneDesignBuilder addRandomLines(int amount)
    { return addRandomLines(amount, minX, maxX, minY, maxY); }
    
    public RuneDesignBuilder addRandomLines(int amount, int maxX, int maxY)
    { return addRandomLines(amount, minX, maxX, minY, maxY); }
    
    public RuneDesignBuilder addRandomLines(int amount, int minX, int maxX, int minY, int maxY)
    { return addRandomLines(amount, amount, minX, maxX, minY, maxY); }
    
    public RuneDesignBuilder addRandomLines(int minAmount, int maxAmount)
    { return addRandomLines(minAmount, maxAmount, minX, maxX, minY, maxY); }
    
    public RuneDesignBuilder addRandomLines(int minAmount, int maxAmount, int maxX, int maxY)
    { return addRandomLines(minAmount, maxAmount, minX, maxX, minY, maxY); }
    
    public RuneDesignBuilder addRandomLines(int minAmount, int maxAmount, int minX, int maxX, int minY, int maxY)
    {
        checkMinMaxArgs(minAmount, maxAmount, "minAmount", "maxAmount");
        checkMinMaxArgs(minX, maxX, "minX", "maxX");
        checkMinMaxArgs(minY, maxY, "minY", "maxY");
        
        Random rand = new Random();
        Collection<Line<PointInt2d>> newLines = new HashSet<Line<PointInt2d>>();
        int amount = minAmount == maxAmount ? minAmount : rand.nextInt(maxAmount - minAmount) + minAmount;
        
        for(int i = 0; i < amount; i++)
        {
            PointInt2d start = new PointInt2d(rand.nextInt(maxX - minX) + minX, rand.nextInt(maxY - minY) + minY);
            PointInt2d end   = new PointInt2d(rand.nextInt(maxX - minX) + minX, rand.nextInt(maxY - minY) + minY);
            newLines.add(new Line<PointInt2d>(start, end));
        }
        
        return addLines(newLines);
    }
    
    public RuneDesignBuilder rotate90()
    { return rotate90(false); }
    
    public RuneDesignBuilder rotate90(boolean retainOldLinesAsWell)
    {
        // If applicable area height is odd and width is even, or vice versa. XNOR.
        if(((maxX - minX) % 2 != 0) != ((minY - maxY) % 2 != 0))
            throw new IllegalArgumentException("rotate90 and rotate270 will only work where the area being rotated "
                                             + "has an odd height and width, or an even height and width. These "
                                             + "methods may not be called there a combination of the two is true, as "
                                             + "it'll result in all points having coördinates of ?.5, ?.5, which "
                                             + "obviously isn't representable as a PointInt2d, or a pair of integers. "
                                             + "The " + ((maxX - minX) % 2 != 0 ? "width" : "height") + " was odd, "
                                             + "while the other dimension was even.");
        
        List<Line<PointInt2d>> newLines = new ArrayList<Line<PointInt2d>>();
        PointInt2d midpoint = new PointInt2d(((maxX - minX) / 2) + minX, ((maxY - minY) / 2) + minY);
        boolean midIsBetweenInts = (maxX - minX) % 2 != 0; // if odd.
        
        for(Line<PointInt2d> i : this.lines)
        {
            int startX = i.getStart().getX();
            int startY = i.getStart().getY();
            int endX   = i.getEnd()  .getX();
            int endY   = i.getEnd()  .getY();
            
            // midpoint deviation; deviation where it should be, at a diagonal with (0, 0), to get these sums to work.
            int midDevn = midpoint.getX() - midpoint.getY();
            
            int newStartX = midpoint.getY() - (startY - midpoint.getY()) + midDevn + (midIsBetweenInts ? 1 : 0);
            int newEndX   = midpoint.getY() - (endY   - midpoint.getY()) + midDevn + (midIsBetweenInts ? 1 : 0);
            int newStartY = startX + midDevn;
            int newEndY   = endX   + midDevn;
            
            PointInt2d newStart = new PointInt2d(newStartX, newStartY);
            PointInt2d newEnd   = new PointInt2d(newEndX,   newEndY  );
            
            newLines.add(new Line<PointInt2d>(newStart, newEnd));
        }
        
        if(!retainOldLinesAsWell)
            lines.clear();
        
        lines.addAll(newLines);
        return this;
    }
    
    public RuneDesignBuilder rotate180()
    { return rotate180(false); }
    
    public RuneDesignBuilder rotate180(boolean retainOldLinesAsWell)
    {
        List<Line<PointInt2d>> newLines = new ArrayList<Line<PointInt2d>>();
        PointInt2d midpoint = new PointInt2d(((maxX - minX) / 2) + minX, ((maxY - minY) / 2) + minY);
        boolean addHalfToX = (maxX - minX) % 2 != 0; // if odd.
        boolean addHalfToY = (maxY - minY) % 2 != 0;
        
        for(Line<PointInt2d> i : this.lines)
        {
            int startX = i.getStart().getX();
            int startY = i.getStart().getY();
            int endX   = i.getEnd()  .getX();
            int endY   = i.getEnd()  .getY();
            
            PointInt2d newStart = new PointInt2d(midpoint.getX() - (startX - midpoint.getX()) + (addHalfToX ? 1 : 0),
                                                 midpoint.getY() - (startY - midpoint.getY()) + (addHalfToY ? 1 : 0));
            
            PointInt2d newEnd   = new PointInt2d(midpoint.getX() - (endX   - midpoint.getX()) + (addHalfToX ? 1 : 0),
                                                 midpoint.getY() - (endY   - midpoint.getY()) + (addHalfToY ? 1 : 0));
            
            newLines.add(new Line<PointInt2d>(newStart, newEnd));
        }
        
        if(!retainOldLinesAsWell)
            lines.clear();
        
        lines.addAll(newLines);
        return this;
    }
    
    public RuneDesignBuilder rotate270()
    { return rotate270(false); }
    
    public RuneDesignBuilder rotate270(boolean retainOldLinesAsWell)
    {
        // If applicable area height is odd and width is even, or vice versa. XNOR.
        if(((maxX - minX) % 2 != 0) != ((minY - maxY) % 2 != 0))
            throw new IllegalArgumentException("rotate90 and rotate270 will only work where the area being rotated "
                                             + "has an odd height and width, or an even height and width. These "
                                             + "methods may not be called there a combination of the two is true, as "
                                             + "it'll result in all points having coördinates of ?.5, ?.5, which "
                                             + "obviously isn't representable as a PointInt2d, or a pair of integers. "
                                             + "The " + ((maxX - minX) % 2 != 0 ? "width" : "height") + " was odd, "
                                             + "while the other dimension was even.");
        
        List<Line<PointInt2d>> newLines = new ArrayList<Line<PointInt2d>>();
        PointInt2d midpoint = new PointInt2d(((maxX - minX) / 2) + minX, ((maxY - minY) / 2) + minY);
        boolean midIsBetweenInts = (maxX - minX) % 2 != 0; // if odd.
        
        for(Line<PointInt2d> i : this.lines)
        {
            int startX = i.getStart().getX();
            int startY = i.getStart().getY();
            int endX   = i.getEnd()  .getX();
            int endY   = i.getEnd()  .getY();
            
            // midpoint deviation; deviation where it should be, at a diagonal with (0, 0), to get these sums to work.
            int midDevn = midpoint.getX() - midpoint.getY();
            
            int newStartX = startY + midDevn;
            int newEndX   = endY + midDevn;
            int newStartY = midpoint.getX() - (startX - midpoint.getX()) + midDevn + (midIsBetweenInts ? 1 : 0);
            int newEndY   = midpoint.getX() - (endX   - midpoint.getX()) + midDevn + (midIsBetweenInts ? 1 : 0);
            
            PointInt2d newStart = new PointInt2d(newStartX, newStartY);
            PointInt2d newEnd   = new PointInt2d(newEndX,   newEndY  );
            
            newLines.add(new Line<PointInt2d>(newStart, newEnd));
        }
        
        if(!retainOldLinesAsWell)
            lines.clear();
        
        lines.addAll(newLines);
        return this;
    }
    
    public RuneDesignBuilder rotate360()
    { return rotate360(false); }
    
    public RuneDesignBuilder rotate360(boolean retainOldLinesAsWell)
    { /* ~Magic~ */ return this; }
    
    public RuneDesignBuilder flipVertically()
    { return flipVertically(false); }
    
    public RuneDesignBuilder flipVertically(boolean retainOldLinesAsWell)
    {
        List<Line<PointInt2d>> newLines = new ArrayList<Line<PointInt2d>>();
        int midY = ((maxY - minY) / 2) + minY;
        boolean midIsBetweenYValues = (maxY - minY) % 2 != 0; // if odd.
        
        for(Line<PointInt2d> i : this.lines)
        {
            int startX = i.getStart().getX();
            int startY = i.getStart().getY();
            int endX   = i.getEnd()  .getX();
            int endY   = i.getEnd()  .getY();
            
            int newStartY = midY - (startY - midY) + (midIsBetweenYValues ? 1 : 0);
            int newEndY   = midY - (endY   - midY) + (midIsBetweenYValues ? 1 : 0);
            
            PointInt2d newStart = new PointInt2d(startX, newStartY);
            PointInt2d newEnd   = new PointInt2d(endX,   newEndY);
            
            newLines.add(new Line<PointInt2d>(newStart, newEnd));
        }
        
        if(!retainOldLinesAsWell)
            lines.clear();
        
        lines.addAll(newLines);
        return this;
    }
    
    public RuneDesignBuilder flipHorizontally()
    { return flipHorizontally(false); }
    
    public RuneDesignBuilder flipHorizontally(boolean retainOldLinesAsWell)
    {
        List<Line<PointInt2d>> newLines = new ArrayList<Line<PointInt2d>>();
        int midX = ((maxX - minX) / 2) + minX;
        boolean midIsBetweenXValues = (maxY - minY) % 2 != 0; // if odd.
        
        for(Line<PointInt2d> i : this.lines)
        {
            int startX = i.getStart().getX();
            int startY = i.getStart().getY();
            int endX   = i.getEnd()  .getX();
            int endY   = i.getEnd()  .getY();
            
            int newStartX = midX - (startX - midX) + (midIsBetweenXValues ? 1 : 0);
            int newEndX   = midX - (endX   - midX) + (midIsBetweenXValues ? 1 : 0);
            
            PointInt2d newStart = new PointInt2d(newStartX, startY);
            PointInt2d newEnd   = new PointInt2d(newEndX,   endY);
            
            newLines.add(new Line<PointInt2d>(newStart, newEnd));
        }
        
        if(!retainOldLinesAsWell)
            lines.clear();
        
        lines.addAll(newLines);
        return this;
    }
    
    public RuneDesignBuilder clear()
    { lines.clear(); return this; }
}