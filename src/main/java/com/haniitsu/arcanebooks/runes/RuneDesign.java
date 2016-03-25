package com.haniitsu.arcanebooks.runes;

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
}