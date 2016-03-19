package com.haniitsu.arcanebooks.runes;

import com.haniitsu.arcanebooks.misc.geometry.Line;
import com.haniitsu.arcanebooks.misc.geometry.PointInt2d;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RuneDesign
{
    public static enum LineFlavour
    {
        Straight, Round;
    
        public static LineFlavour getDefault()
        { return Straight; }
    }
    
    public RuneDesign(Collection<? extends Line<PointInt2d>> lines)
    {
        Map<Line<PointInt2d>, LineFlavour> map = new HashMap<Line<PointInt2d>, LineFlavour>();
        
        for(Line<PointInt2d> line : lines)
            map.put(line, LineFlavour.getDefault());
        
        this.lines = Collections.unmodifiableMap(map);
    }
    
    public RuneDesign(Map<? extends Line<PointInt2d>, LineFlavour> lines)
    { this.lines = Collections.unmodifiableMap(new HashMap<Line<PointInt2d>, LineFlavour>()); }
    
    protected final Map<Line<PointInt2d>, LineFlavour> lines;
    
    public Collection<Line<PointInt2d>> getLines()
    { return lines.keySet(); }
    
    public Map<Line<PointInt2d>, LineFlavour> getLinesAndFlavours()
    { return lines; }
}