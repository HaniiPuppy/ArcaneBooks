package com.haniitsu.arcanebooks.runes;

import com.haniitsu.arcanebooks.misc.geometry.Line;
import com.haniitsu.arcanebooks.misc.geometry.PointInt2d;
import net.minecraft.util.IIcon;

/** Enum representation of every possible line in a rune design. */
public enum RuneLine
{
    //<editor-fold defaultstate="collapsed" desc="Members">
    line1_2  (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(0, 1)), "runemark_1-2"),
    line1_3  (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(0, 2)), "runemark_1-3"),
    line1_4  (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(0, 3)), "runemark_1-4"),
    line1_5  (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(1, 0)), "runemark_1-5"),
    line1_6  (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(1, 1)), "runemark_1-6"),
    line1_7  (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(1, 2)), "runemark_1-7"),
    line1_8  (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(1, 3)), "runemark_1-8"),
    line1_9  (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(2, 0)), "runemark_1-9"),
    line1_10 (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(2, 1)), "runemark_1-10"),
    line1_11 (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(2, 2)), "runemark_1-11"),
    line1_12 (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(2, 3)), "runemark_1-12"),
    line1_13 (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(3, 0)), "runemark_1-13"),
    line1_14 (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(3, 1)), "runemark_1-14"),
    line1_15 (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(3, 2)), "runemark_1-15"),
    line1_16 (new Line<PointInt2d>(new PointInt2d(0, 0), new PointInt2d(3, 3)), "runemark_1-16"),
    line2_3  (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(0, 2)), "runemark_2-3"),
    line2_4  (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(0, 3)), "runemark_2-4"),
    line2_5  (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(1, 0)), "runemark_2-5"),
    line2_6  (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(1, 1)), "runemark_2-6"),
    line2_7  (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(1, 2)), "runemark_2-7"),
    line2_8  (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(1, 3)), "runemark_2-8"),
    line2_9  (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(2, 0)), "runemark_2-9"),
    line2_10 (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(2, 1)), "runemark_2-10"),
    line2_11 (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(2, 2)), "runemark_2-11"),
    line2_12 (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(2, 3)), "runemark_2-12"),
    line2_13 (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(3, 0)), "runemark_2-13"),
    line2_14 (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(3, 1)), "runemark_2-14"),
    line2_15 (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(3, 2)), "runemark_2-15"),
    line2_16 (new Line<PointInt2d>(new PointInt2d(0, 1), new PointInt2d(3, 3)), "runemark_2-16"),
    line3_4  (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(0, 3)), "runemark_3-4"),
    line3_5  (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(1, 0)), "runemark_3-5"),
    line3_6  (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(1, 1)), "runemark_3-6"),
    line3_7  (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(1, 2)), "runemark_3-7"),
    line3_8  (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(1, 3)), "runemark_3-8"),
    line3_9  (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(2, 0)), "runemark_3-9"),
    line3_10 (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(2, 1)), "runemark_3-10"),
    line3_11 (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(2, 2)), "runemark_3-11"),
    line3_12 (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(2, 3)), "runemark_3-12"),
    line3_13 (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(3, 0)), "runemark_3-13"),
    line3_14 (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(3, 1)), "runemark_3-14"),
    line3_15 (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(3, 2)), "runemark_3-15"),
    line3_16 (new Line<PointInt2d>(new PointInt2d(0, 2), new PointInt2d(3, 3)), "runemark_3-16"),
    line4_5  (new Line<PointInt2d>(new PointInt2d(0, 3), new PointInt2d(1, 0)), "runemark_4-5"),
    line4_6  (new Line<PointInt2d>(new PointInt2d(0, 3), new PointInt2d(1, 1)), "runemark_4-6"),
    line4_7  (new Line<PointInt2d>(new PointInt2d(0, 3), new PointInt2d(1, 2)), "runemark_4-7"),
    line4_8  (new Line<PointInt2d>(new PointInt2d(0, 3), new PointInt2d(1, 3)), "runemark_4-8"),
    line4_9  (new Line<PointInt2d>(new PointInt2d(0, 3), new PointInt2d(2, 0)), "runemark_4-9"),
    line4_10 (new Line<PointInt2d>(new PointInt2d(0, 3), new PointInt2d(2, 1)), "runemark_4-10"),
    line4_11 (new Line<PointInt2d>(new PointInt2d(0, 3), new PointInt2d(2, 2)), "runemark_4-11"),
    line4_12 (new Line<PointInt2d>(new PointInt2d(0, 3), new PointInt2d(2, 3)), "runemark_4-12"),
    line4_13 (new Line<PointInt2d>(new PointInt2d(0, 3), new PointInt2d(3, 0)), "runemark_4-13"),
    line4_14 (new Line<PointInt2d>(new PointInt2d(0, 3), new PointInt2d(3, 1)), "runemark_4-14"),
    line4_15 (new Line<PointInt2d>(new PointInt2d(0, 3), new PointInt2d(3, 2)), "runemark_4-15"),
    line4_16 (new Line<PointInt2d>(new PointInt2d(0, 3), new PointInt2d(3, 3)), "runemark_4-16"),
    line5_6  (new Line<PointInt2d>(new PointInt2d(1, 0), new PointInt2d(1, 1)), "runemark_5-6"),
    line5_7  (new Line<PointInt2d>(new PointInt2d(1, 0), new PointInt2d(1, 2)), "runemark_5-7"),
    line5_8  (new Line<PointInt2d>(new PointInt2d(1, 0), new PointInt2d(1, 3)), "runemark_5-8"),
    line5_9  (new Line<PointInt2d>(new PointInt2d(1, 0), new PointInt2d(2, 0)), "runemark_5-9"),
    line5_10 (new Line<PointInt2d>(new PointInt2d(1, 0), new PointInt2d(2, 1)), "runemark_5-10"),
    line5_11 (new Line<PointInt2d>(new PointInt2d(1, 0), new PointInt2d(2, 2)), "runemark_5-11"),
    line5_12 (new Line<PointInt2d>(new PointInt2d(1, 0), new PointInt2d(2, 3)), "runemark_5-12"),
    line5_13 (new Line<PointInt2d>(new PointInt2d(1, 0), new PointInt2d(3, 0)), "runemark_5-13"),
    line5_14 (new Line<PointInt2d>(new PointInt2d(1, 0), new PointInt2d(3, 1)), "runemark_5-14"),
    line5_15 (new Line<PointInt2d>(new PointInt2d(1, 0), new PointInt2d(3, 2)), "runemark_5-15"),
    line5_16 (new Line<PointInt2d>(new PointInt2d(1, 0), new PointInt2d(3, 3)), "runemark_5-16"),
    line6_7  (new Line<PointInt2d>(new PointInt2d(1, 1), new PointInt2d(1, 2)), "runemark_6-7"),
    line6_8  (new Line<PointInt2d>(new PointInt2d(1, 1), new PointInt2d(1, 3)), "runemark_6-8"),
    line6_9  (new Line<PointInt2d>(new PointInt2d(1, 1), new PointInt2d(2, 0)), "runemark_6-9"),
    line6_10 (new Line<PointInt2d>(new PointInt2d(1, 1), new PointInt2d(2, 1)), "runemark_6-10"),
    line6_11 (new Line<PointInt2d>(new PointInt2d(1, 1), new PointInt2d(2, 2)), "runemark_6-11"),
    line6_12 (new Line<PointInt2d>(new PointInt2d(1, 1), new PointInt2d(2, 3)), "runemark_6-12"),
    line6_13 (new Line<PointInt2d>(new PointInt2d(1, 1), new PointInt2d(3, 0)), "runemark_6-13"),
    line6_14 (new Line<PointInt2d>(new PointInt2d(1, 1), new PointInt2d(3, 1)), "runemark_6-14"),
    line6_15 (new Line<PointInt2d>(new PointInt2d(1, 1), new PointInt2d(3, 2)), "runemark_6-15"),
    line6_16 (new Line<PointInt2d>(new PointInt2d(1, 1), new PointInt2d(3, 3)), "runemark_6-16"),
    line7_8  (new Line<PointInt2d>(new PointInt2d(1, 2), new PointInt2d(1, 3)), "runemark_7-8"),
    line7_9  (new Line<PointInt2d>(new PointInt2d(1, 2), new PointInt2d(2, 0)), "runemark_7-9"),
    line7_10 (new Line<PointInt2d>(new PointInt2d(1, 2), new PointInt2d(2, 1)), "runemark_7-10"),
    line7_11 (new Line<PointInt2d>(new PointInt2d(1, 2), new PointInt2d(2, 2)), "runemark_7-11"),
    line7_12 (new Line<PointInt2d>(new PointInt2d(1, 2), new PointInt2d(2, 3)), "runemark_7-12"),
    line7_13 (new Line<PointInt2d>(new PointInt2d(1, 2), new PointInt2d(3, 0)), "runemark_7-13"),
    line7_14 (new Line<PointInt2d>(new PointInt2d(1, 2), new PointInt2d(3, 1)), "runemark_7-14"),
    line7_15 (new Line<PointInt2d>(new PointInt2d(1, 2), new PointInt2d(3, 2)), "runemark_7-15"),
    line7_16 (new Line<PointInt2d>(new PointInt2d(1, 2), new PointInt2d(3, 3)), "runemark_7-16"),
    line8_9  (new Line<PointInt2d>(new PointInt2d(1, 3), new PointInt2d(2, 0)), "runemark_8-9"),
    line8_10 (new Line<PointInt2d>(new PointInt2d(1, 3), new PointInt2d(2, 1)), "runemark_8-10"),
    line8_11 (new Line<PointInt2d>(new PointInt2d(1, 3), new PointInt2d(2, 2)), "runemark_8-11"),
    line8_12 (new Line<PointInt2d>(new PointInt2d(1, 3), new PointInt2d(2, 3)), "runemark_8-12"),
    line8_13 (new Line<PointInt2d>(new PointInt2d(1, 3), new PointInt2d(3, 0)), "runemark_8-13"),
    line8_14 (new Line<PointInt2d>(new PointInt2d(1, 3), new PointInt2d(3, 1)), "runemark_8-14"),
    line8_15 (new Line<PointInt2d>(new PointInt2d(1, 3), new PointInt2d(3, 2)), "runemark_8-15"),
    line8_16 (new Line<PointInt2d>(new PointInt2d(1, 3), new PointInt2d(3, 3)), "runemark_8-16"),
    line9_10 (new Line<PointInt2d>(new PointInt2d(2, 0), new PointInt2d(2, 1)), "runemark_9-10"),
    line9_11 (new Line<PointInt2d>(new PointInt2d(2, 0), new PointInt2d(2, 2)), "runemark_9-11"),
    line9_12 (new Line<PointInt2d>(new PointInt2d(2, 0), new PointInt2d(2, 3)), "runemark_9-12"),
    line9_13 (new Line<PointInt2d>(new PointInt2d(2, 0), new PointInt2d(3, 0)), "runemark_9-13"),
    line9_14 (new Line<PointInt2d>(new PointInt2d(2, 0), new PointInt2d(3, 1)), "runemark_9-14"),
    line9_15 (new Line<PointInt2d>(new PointInt2d(2, 0), new PointInt2d(3, 2)), "runemark_9-15"),
    line9_16 (new Line<PointInt2d>(new PointInt2d(2, 0), new PointInt2d(3, 3)), "runemark_9-16"),
    line10_11(new Line<PointInt2d>(new PointInt2d(2, 1), new PointInt2d(2, 2)), "runemark_10-11"),
    line10_12(new Line<PointInt2d>(new PointInt2d(2, 1), new PointInt2d(2, 3)), "runemark_10-12"),
    line10_13(new Line<PointInt2d>(new PointInt2d(2, 1), new PointInt2d(3, 0)), "runemark_10-13"),
    line10_14(new Line<PointInt2d>(new PointInt2d(2, 1), new PointInt2d(3, 1)), "runemark_10-14"),
    line10_15(new Line<PointInt2d>(new PointInt2d(2, 1), new PointInt2d(3, 2)), "runemark_10-15"),
    line10_16(new Line<PointInt2d>(new PointInt2d(2, 1), new PointInt2d(3, 3)), "runemark_10-16"),
    line11_12(new Line<PointInt2d>(new PointInt2d(2, 2), new PointInt2d(2, 3)), "runemark_11-12"),
    line11_13(new Line<PointInt2d>(new PointInt2d(2, 2), new PointInt2d(3, 0)), "runemark_11-13"),
    line11_14(new Line<PointInt2d>(new PointInt2d(2, 2), new PointInt2d(3, 1)), "runemark_11-14"),
    line11_15(new Line<PointInt2d>(new PointInt2d(2, 2), new PointInt2d(3, 2)), "runemark_11-15"),
    line11_16(new Line<PointInt2d>(new PointInt2d(2, 2), new PointInt2d(3, 3)), "runemark_11-16"),
    line12_13(new Line<PointInt2d>(new PointInt2d(2, 3), new PointInt2d(3, 0)), "runemark_12-13"),
    line12_14(new Line<PointInt2d>(new PointInt2d(2, 3), new PointInt2d(3, 1)), "runemark_12-14"),
    line12_15(new Line<PointInt2d>(new PointInt2d(2, 3), new PointInt2d(3, 2)), "runemark_12-15"),
    line12_16(new Line<PointInt2d>(new PointInt2d(2, 3), new PointInt2d(3, 3)), "runemark_12-16"),
    line13_14(new Line<PointInt2d>(new PointInt2d(3, 0), new PointInt2d(3, 1)), "runemark_13-14"),
    line13_15(new Line<PointInt2d>(new PointInt2d(3, 0), new PointInt2d(3, 2)), "runemark_13-15"),
    line13_16(new Line<PointInt2d>(new PointInt2d(3, 0), new PointInt2d(3, 3)), "runemark_13-16"),
    line14_15(new Line<PointInt2d>(new PointInt2d(3, 1), new PointInt2d(3, 2)), "runemark_14-15"),
    line14_16(new Line<PointInt2d>(new PointInt2d(3, 1), new PointInt2d(3, 3)), "runemark_14-16"),
    line15_16(new Line<PointInt2d>(new PointInt2d(3, 2), new PointInt2d(3, 3)), "runemark_15-16");
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates a new RuneLine member representing the passed line.
     * @param line The line this RuneLine member represents.
     * @param iconString The name of the image file (minus enclosing folder path and file extension) used for drawing
     * this RuneLine.
     */
    private RuneLine(Line<PointInt2d> line, String iconString)
    {
        this.line = line;
        this.iconString = iconString;
    }
    
    //<editor-fold defaultstate="collapsed" desc="From methods">
    /**
     * Gets the RuneLine representing the passed line.
     * @param line The line to get the RuneLine representing.
     * @return The RuneLine member representing the passed Line, or null if there is none.
     */
    public static RuneLine fromLine(Line<PointInt2d> line)
    {
        for(RuneLine i : RuneLine.values())
            if(i.toLine().equals(line))
                return i;
        
        return null;
    }
    //</editor-fold>
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    /** The represented line. */
    Line<PointInt2d> line;
    
    /** The name of the file (without the enclosing folder path or file extension) to use to draw this RuneLine. */
    String iconString;
    
    /** The icon object used for drawing this rune line. */
    IIcon icon = null;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Methods">
    //<editor-fold defaultstate="collapsed" desc="Accessors">
    /**
     * Gets the name of the file (without the enclosing folder path or file extension) to use to draw this RuneLine.
     * @return The icon string.
     */
    public String getIconString()
    { return iconString; }
    
    /**
     * Gets the icon object used for drawing this rune line.
     * @return The runeline's icon, or null if none has been assigned yet.
     */
    public IIcon getIcon()
    { return icon; }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Mutators">
    /**
     * Assigns an icon object to this RuneLine member.
     * @param icon The icon to assign to this RuneLine.
     */
    public void setIcon(IIcon icon)
    { this.icon = icon; }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="To methods">
    /**
     * Gets this RuneLine member as a Line object.
     * @return The Line represented by this RuneLine.
     */
    public Line<PointInt2d> toLine()
    { return line; }
    //</editor-fold>
    //</editor-fold>
}