package com.haniitsu.arcanebooks.runes;

import com.google.common.primitives.Doubles;
import com.haniitsu.arcanebooks.misc.geometry.Line;
import com.haniitsu.arcanebooks.misc.geometry.PointInt2d;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuneDesign
{
    public RuneDesign(List<? extends RuneLine> lines)
    { this.lines = Collections.unmodifiableList(new ArrayList<RuneLine>(lines)); }
    
    public static RuneDesign fromLines(List<? extends Line<PointInt2d>> lines)
    {
        List<RuneLine> runeLines = new ArrayList<RuneLine>();
        
        for(Line<PointInt2d> line : lines)
        {
            RuneLine current = RuneLine.fromLine(line);
            
            if(current != null)
                runeLines.add(current);
        }
        
        return new RuneDesign(runeLines);
    }
    
    public static RuneDesign fromString(String runeDesignString)
    { return new RuneDesignBuilder(getLineListFromString(runeDesignString)).make(); }
    
    protected final List<RuneLine> lines;
    
    public List<Line<PointInt2d>> getLines()
    {
        List<Line<PointInt2d>> convertedLines = new ArrayList<Line<PointInt2d>>();
        
        for(RuneLine i : lines)
            convertedLines.add(i.toLine());
        
        return convertedLines;
    }
    
    public List<RuneLine> getRuneLines()
    { return lines; }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        if(lines.isEmpty())
            return "[blank]";
        
        boolean first = true;
        
        for(RuneLine line : lines)
        {
            if(first) first = false;
            else      sb.append('_');
            
            sb.append(lineToString(line.toLine()));
        }
        
        return sb.toString();
    }
    
    private static String lineToString(Line<PointInt2d> line)
    { return pointToString(line.getStart()) + ">" + pointToString(line.getEnd()); }
    
    private static String pointToString(PointInt2d point)
    { return point.getX() + "," + point.getY(); }
    
    private static List<Line<PointInt2d>> getLineListFromString(String s)
    {
        if(s == null || s.trim().equalsIgnoreCase("[blank]"))
            return new ArrayList<Line<PointInt2d>>();
        
        String[] sParts = s.trim().split("_");
        List<Line<PointInt2d>> lineList = new ArrayList<Line<PointInt2d>>();
        
        for(String i : sParts)
        {
            Line<PointInt2d> line = getLineFromString(i);
            
            if(line != null)
                lineList.add(line);
        }
        
        return lineList;
    }
    
    private static Line<PointInt2d> getLineFromString(String s)
    {
        if(s == null)
            return null;
        
        String[] sParts = s.trim().split(">");
        
        if(sParts.length < 2)
            return null;
        
        PointInt2d start = getPointFromString(sParts[0]);
        PointInt2d end = getPointFromString(sParts[1]);
        
        if(start == null || end == null)
            return null;
        
        return new Line<PointInt2d>(start, end);
    }
    
    private static PointInt2d getPointFromString(String s)
    {
        if(s == null)
            return null;
        
        String[] sParts = s.trim().split(",");
        
        if(sParts.length < 2)
            return null;
        
        Double x = Doubles.tryParse(sParts[0].trim());
        Double y = Doubles.tryParse(sParts[1].trim());
        
        if(x == null || y == null)
            return null;
        
        return new PointInt2d(x.intValue(), y.intValue());
    }
}