package com.haniitsu.arcanebooks.runes;

import com.google.common.primitives.Doubles;
import com.haniitsu.arcanebooks.misc.geometry.Line;
import com.haniitsu.arcanebooks.misc.geometry.PointInt2d;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** The rune design used where runes are needed to represent spellwords, such as on the runestone item. */
public class RuneDesign
{
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a new rune design from a list of rune lines.
     * @param lines The lines that should make up this rune design.
     */
    public RuneDesign(List<? extends RuneLine> lines)
    { this.lines = Collections.unmodifiableList(new ArrayList<RuneLine>(lines)); }
    
    //<editor-fold defaultstate="collapsed" desc="From methods">
    /**
     * Creates a new rune design from a list of lines
     * @param lines The lines that should make up the new rune design.
     * @return The new rune design.
     */
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
    
    /**
     * Creates a new rune design from a string, such as the ones returned by this class' .toString method.
     * @param runeDesignString The string to turn into a rune design object.
     * @return The new rune design.
     */
    public static RuneDesign fromString(String runeDesignString)
    { return new RuneDesignBuilder(getLineListFromString(runeDesignString)).make(); }
    //</editor-fold>
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    /** The rune lines that make up this rune design. */
    protected final List<RuneLine> lines;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Methods">
    //<editor-fold defaultstate="collapsed" desc="Accessors">
    /**
     * Gets the lines that make up this rune design, as Line objects.
     * @return A list of the lines that make up this rune design.
     */
    public List<Line<PointInt2d>> getLines()
    {
        List<Line<PointInt2d>> convertedLines = new ArrayList<Line<PointInt2d>>();
        
        for(RuneLine i : lines)
            convertedLines.add(i.toLine());
        
        return convertedLines;
    }
    
    /**
     * Gets the lines that make up this rune design, as RuneLine members.
     * @return A list of the lines that make up this rune design.
     */
    public List<RuneLine> getRuneLines()
    { return lines; }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="To methods">
    /**
     * Creates a parsable string representing all of the lines in this rune design.
     * @example 2,3>3,1_0,0>1,2_3,0>2,1
     * @return The string representation of this rune design.
     */
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Internal methods">
    /**
     * Gets a string representation of the passed Line object, in the format used by this class' .toString method.
     * @param line The line to convert.
     * @return The string representation of the passed line.
     */
    private static String lineToString(Line<PointInt2d> line)
    { return pointToString(line.getStart()) + ">" + pointToString(line.getEnd()); }
    
    /**
     * Gets a string representation of the passed Point object, in the format used by this class' .toString method.
     * @param point The point to convert.
     * @return The string representation of the passed point.
     */
    private static String pointToString(PointInt2d point)
    { return point.getX() + "," + point.getY(); }
    
    /**
     * Gets a new list of lines from the passed string, where the passed string is in the format returned by this class'
     * .toString method.
     * @param s The string to get a list of lines from.
     * @return The list of lines parsed from the passed string.
     */
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
    
    /**
     * Gets a new Line object from the passed string, where the passed string is in the format returned by this class'
     * .lineToString method.
     * @param s The string to get a Line from.
     * @return The line parsed from the passed string.
     */
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
    
    /**
     * Gets a new Point object from the passed string, where the passed string is in the format return by this class'
     * .pointToString method.
     * @param s The string to get a Point from.
     * @return The point parsed from the passed string.
     */
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
    //</editor-fold>
    //</editor-fold>
}