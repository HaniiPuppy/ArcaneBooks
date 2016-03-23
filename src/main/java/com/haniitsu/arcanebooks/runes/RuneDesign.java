package com.haniitsu.arcanebooks.runes;

import com.haniitsu.arcanebooks.misc.geometry.Line;
import com.haniitsu.arcanebooks.misc.geometry.PointInt2d;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuneDesign
{
    public RuneDesign(List<? extends Line<PointInt2d>> lines)
    { this.lines = Collections.unmodifiableList(new ArrayList<Line<PointInt2d>>()); }
    
    protected final List<Line<PointInt2d>> lines;
    
    public List<Line<PointInt2d>> getLines()
    { return lines; }
}