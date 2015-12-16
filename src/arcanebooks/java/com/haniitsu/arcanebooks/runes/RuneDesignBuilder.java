package com.haniitsu.arcanebooks.runes;

import com.haniitsu.arcanebooks.misc.geometry.Line;
import com.haniitsu.arcanebooks.misc.geometry.PointInt2d;
import com.haniitsu.arcanebooks.runes.RuneDesign.LineFlavour;
import java.util.Collection;
import java.util.Map;

public class RuneDesignBuilder
{
    public RuneDesignBuilder()
    {  }
    
    public RuneDesignBuilder(Collection<? extends Line<PointInt2d>> liens)
    {  }
    
    public RuneDesignBuilder(Map<? extends Line<PointInt2d>, LineFlavour> lines)
    {  }
    
    public RuneDesignBuilder(RuneDesign design)
    {  }
    
    public RuneDesignBuilder(int maxX, int maxY)
    {  }
    
    public RuneDesignBuilder(int maxX, int maxY, Collection<? extends Line<PointInt2d>> liens)
    {  }
    
    public RuneDesignBuilder(int maxX, int maxY, Map<? extends Line<PointInt2d>, LineFlavour> lines)
    {  }
    
    public RuneDesignBuilder(int maxX, int maxY, RuneDesign design)
    {  }
    
    public RuneDesignBuilder(int minX, int maxX, int minY, int maxY)
    {  }
    
    public RuneDesignBuilder(int minX, int maxX, int minY, int maxY, Collection<? extends Line<PointInt2d>> liens)
    {  }
    
    public RuneDesignBuilder(int minX, int maxX, int minY, int maxY, Map<? extends Line<PointInt2d>, LineFlavour> lines)
    {  }
    
    public RuneDesignBuilder(int minX, int maxX, int minY, int maxY, RuneDesign design)
    {  }
    
    protected Map<Line<PointInt2d>, LineFlavour> lines;
    
    public RuneDesign make()
    {  }
    
    public RuneDesignBuilder addLine(Line<PointInt2d> line)
    {  }
    
    public RuneDesignBuilder addLine(Line<PointInt2d> line, LineFlavour flavour)
    {  }
    
    public RuneDesignBuilder addLine(int start, int end)
    {  }
    
    public RuneDesignBuilder addLine(int start, int end, LineFlavour flavour)
    {  }
    
    public RuneDesignBuilder addLines(Line<PointInt2d>... lines)
    {  }
    
    public RuneDesignBuilder addLines(Collection<? extends Line<PointInt2d>> lines)
    {  }
    
    public RuneDesignBuilder addLines(Map<? extends Line<PointInt2d>, LineFlavour> lines)
    {  }
    
    public RuneDesignBuilder addRandomLine()
    {  }
    
    public RuneDesignBuilder addRandomLine(int maxX, int maxY)
    {  }
    
    public RuneDesignBuilder addRandomLine(int minX, int maxX, int minY, int maxY)
    {  }
    
    public RuneDesignBuilder addRandomLines(int amount)
    {  }
    
    public RuneDesignBuilder addRandomLines(int amount, int maxX, int maxY)
    {  }
    
    public RuneDesignBuilder addRandomLines(int amount, int minX, int maxX, int minY, int maxY)
    {  }
    
    public RuneDesignBuilder addRandomLines(int minAmount, int maxAmount)
    {  }
    
    public RuneDesignBuilder addRandomLines(int minAmount, int maxAmount, int maxX, int maxY)
    {  }
    
    public RuneDesignBuilder addRandomLines(int minAmount, int maxAmount, int minX, int maxX, int minY, int maxY)
    {  }
    
    public RuneDesignBuilder rotate90()
    { return rotate90(false); }
    
    public RuneDesignBuilder rotate90(boolean retainOldLinesAsWell)
    {  }
    
    public RuneDesignBuilder rotate180()
    { return rotate180(false); }
    
    public RuneDesignBuilder rotate180(boolean retainOldLinesAsWell)
    {  }
    
    public RuneDesignBuilder rotate270()
    { return rotate270(false); }
    
    public RuneDesignBuilder rotate270(boolean retainOldLinesAsWell)
    {  }
    
    public RuneDesignBuilder rotate360()
    { return rotate360(false); }
    
    public RuneDesignBuilder rotate360(boolean retainOldLinesAsWell)
    { /* ~Magic~ */ return this; }
    
    public RuneDesignBuilder flipVertically()
    {  }
    
    public RuneDesignBuilder flipVertically(boolean retainOldLinesAsWell)
    {  }
    
    public RuneDesignBuilder flipHorizontally()
    {  }
    
    public RuneDesignBuilder flipHorizontally(boolean retainOldLinesAsWell)
    {  }
    
    public RuneDesignBuilder clear()
    {  }
}