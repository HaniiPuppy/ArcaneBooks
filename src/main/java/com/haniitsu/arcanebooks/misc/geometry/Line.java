package com.haniitsu.arcanebooks.misc.geometry;

import com.haniitsu.arcanebooks.misc.UtilMethods;

/**
 * A generic class representing a line between two given points.
 * @param <T> The type of the points this is a line between.
 */
public class Line<T extends Point2d>
{
    /** Thrown when performing some operation requiring two lines to be in-line (such as merging them) and they
     * aren't. */
    public static class LinesDontAlignException extends RuntimeException
    {
        public LinesDontAlignException() { super(); }
        public LinesDontAlignException(String message) { super(message); }
        public LinesDontAlignException(Throwable source) { super(source); }
        public LinesDontAlignException(String message, Throwable source) { super(message, source); }
    }
    
    /** Thrown when performing some operation requiring two lines to have some connection or overlap (such as merging
     * them) and they don't. */
    public static class LinesDontConnectException extends RuntimeException
    {
        public LinesDontConnectException() { super(); }
        public LinesDontConnectException(String message) { super(message); }
        public LinesDontConnectException(Throwable source) { super(source); }
        public LinesDontConnectException(String message, Throwable source) { super(message, source); }
    }
    
    /**
     * Creates a line between the given start and end points.
     * @param start The start point of the line.
     * @param end The end point of the line.
     */
    public Line(T start, T end)
    {
        this.start = start;
        this.end = end;
    }
    
    /** The start point. The object represents a line between the start and end points. */
    protected final T start;
    
    /** The end point. The object represents a line between the start and end points. */
    protected final T end;
    
    /**
     * Gets the line's start point.
     * @return The line's start point.
     */
    public T getStart()
    { return start; }
    
    /**
     * Gets the line's end point.
     * @return The line's end point.
     */
    public T getEnd()
    { return end; }
    
    /**
     * Gets the length of the line, in whatever unit the points are ordinated in.
     * @return The length of the line in the same units as the points.
     */
    public double getLength()
    { return Math.sqrt(getLengthSquared()); }
    
    /**
     * Gets the angle of the line, as a (double) floating point value from 0.0 to 1.0, where 0.0 would represent 0° and
     * 1.0 would represent 360°. x and x + 0.5 would be parallel in opposite directions.
     * @return The angle, from 0 to 1, of the line.
     */
    public double getAngle()
    {
        double startX = start.getX().doubleValue(), startY = start.getY().doubleValue();
        double endX   = end  .getX().doubleValue(), endY   = end  .getY().doubleValue();
        double relX = endX - startX, relY = endY - startY;
        
        if(startX == endX)
            return endY < startY ? 0 : 0.5;
        
        if(startY == endY)
            return endX > startX ? 0.25 : 0.75;
        
        if(endX > startX)
        {
            if(endY > startY)
                return relX == relY ? 0.125 : (relX < relY ? relX / relY : relY / relX + 1) / 8;
            else // if(endY < startY)
            {
                relY *= -1;
                return relX == relY ? 0.375 : (relY < relX ? relY / relX + 2 : relX / relY + 3) / 8;
            }
        }
        else // if(endX < startX)
        {
            if(endY < startY)
            {
                relY *= -1;
                relX *= -1;
                return relX == relY ? 0.625 : (relX < relY ? relX / relY + 4 : relY / relX + 5) / 8;
            }
            else // if(endY > startY)
            {
                relX *= -1;
                return relX == relY ? 0.875 : (relY < relX ? relY / relX + 6 : relX / relY + 7) / 8;
            }
        }
    }
    
    /**
     * Checks whether or not a given point is on this line.
     * @note Uses a margin of error of 10^-12, to account for possible precision errors in the double numeric type.
     * @param point The point to check whether or not its on the line.
     * @return True if the point is on the line. Otherwise, false.
     */
    public boolean hasPoint(T point)
    { return UtilMethods.pointIsOnLine(point, this); }
    
    /**
     * Checks whether a passed line overlaps or connects with this one.
     * @param other The other line to check with this one.
     * @return True if the two lines overlap or connect. Otherwise, false. False if the lines aren't parallel.
     */
    public boolean overlapsWith(Line<T> other)
    {
        if(connectsTo(other))
            if(getStart().equals(other.getStart()) || getStart().equals(other.getEnd())
            || getEnd().equals(other.getStart())   || getEnd().equals(other.getEnd()))
            {
                double thisAngle = this.getAngle(), otherAngle = other.getAngle();
                return thisAngle == otherAngle || thisAngle + 0.5 == otherAngle || thisAngle - 0.5 == otherAngle;
            }
        
        if(encloses(other) || isEnclosedBy(other))
            return true;
        
        if((hasPoint(other.getStart())) || hasPoint(other.getEnd())
        && (other.hasPoint(getStart())  || other.hasPoint(getEnd())))
            return true;
        
        return false;
    }
    
    /**
     * Checks whether or not a passed line is entirely encloses within this one.
     * @param other The line that may be enclosed by this one.
     * @return True if this line entirely encloses the other line. Otherwise, false. False if the lines aren't parallel.
     */
    public boolean encloses(Line<T> other)
    { return hasPoint(other.start) && hasPoint(other.end); }
    
    /**
     * Checks whether or not a passed line entirely encloses this one.
     * @param other The line that may enclose this one.
     * @return True if this line is entirely enclosed by the other line. Otherwise, false. False if the lines aren't
     * parallel.
     */
    public boolean isEnclosedBy(Line<T> other)
    { return other.hasPoint(start) && other.hasPoint(end); }
    
    /**
     * Checks whether or not a passed line shares an end point with this line.
     * @param other The line which may share an endpoint with this one.
     * @return True if the two lines share at least one end point. Otherwise, false.
     */
    public boolean connectsTo(Line<T> other)
    {
        return start.equals(other.start) && !end.equals(other.end)
            || end.equals(other.end) && !start.equals(other.start);
    }
    
    /**
     * Checks whether or not a passed line both is connected to this one, and aligns with it.
     * @param other The line which may connect and be aligned with this line.
     * @return True if the two lines are both connected, and parallel.
     */
    public boolean connectsToAligned(Line<T> other)
    {
        if(!connectsTo(other))
            return false;
        
        double thisAngle = this.getAngle(), otherAngle = other.getAngle();
        return thisAngle == otherAngle || thisAngle + 0.5 == otherAngle || thisAngle - 0.5 == otherAngle;
    }
    
    /**
     * Gets the length of this line squared. Useful for operations that require comparing a line's length against the
     * length of another line or another recorded length squared - where the actual length value itself isn't needed,
     * as square-root can be a very expensive operation under certain circumstances.
     * @return The length of this line ^ 2.
     */
    public double getLengthSquared()
    {
        Double xDist = start.getX().doubleValue() - end.getX().doubleValue();
        xDist = xDist * xDist;
        
        Double yDist = start.getY().doubleValue() - end.getY().doubleValue();
        yDist = yDist * yDist;
        
        return xDist + yDist;
    }
    
    /**
     * Gets a line that's this line merged with another line that aligns with this one and connects or overlaps with it.
     * @param other The line to merge with this one to get the resulting line.
     * @return A line that's the combined result of both this line and the passed line.
     * @throws LinesDontAlignException If the lines aren't parallel and thus can't be merged into a single line.
     * @throws LinesDontConnectException If the lines don't overlap/connect and thus can't be merged into a single line.
     */
    public Line<T> mergeWith(Line<T> other)
    {
        if(this.encloses(other))
            return this;
        
        if(this.isEnclosedBy(other))
            return other;
        
        double thisAngle = getAngle();
        double otherAngle = getAngle();
        
        if(thisAngle != otherAngle && thisAngle + 0.5 != otherAngle && thisAngle - 0.5 != otherAngle)
            throw new LinesDontAlignException();
        
        // Don't need to check if the point not checked is on the other line - all situations where this is possible
        // is covered by the .encloses and .isEnclosedBy checks.
        
        if(getStart().equals(other.getStart()))
            return new Line<T>(getEnd(), other.getEnd());
        
        if(getStart().equals(other.getEnd()))
            return new Line<T>(other.getStart(), getEnd());
        
        if(getEnd().equals(other.getStart()))
            return new Line<T>(getStart(), other.getEnd());
        
        if(getEnd().equals(other.getEnd()))
            return new Line<T>(getStart(), other.getStart());
        
        // Don't need to make sure the point not checked is on the other line - the lines are ensured to line up by the
        // angle check, and if one of this line's points are one the other line, we've already established that this
        // line isn't totally enclosed in the other line, meaning that we can be certain the two lines overlap without
        // one totally enclosing the other.
        
        if(other.hasPoint(getStart()))
        {
            // This check is cheaper than doing hasPoint on the other's ends. The shorter distance specifies which of
            // the other points is on this line. Also, no need to sqrt here.
            double endToOtherStartDistSq = new Line<T>(getEnd(), other.getStart()).getLengthSquared();
            double endToOtherEndDistSq = new Line<T>(getEnd(), other.getEnd()).getLengthSquared();
            return new Line<T>(endToOtherStartDistSq > endToOtherEndDistSq ? other.getStart() : other.getEnd(), getEnd());
        }
        
        if(other.hasPoint(getEnd()))
        {
            double startToOtherStartDistSq = new Line<T>(getStart(), other.getStart()).getLengthSquared();
            double startToOtherEndDistSq = new Line<T>(getStart(), other.getEnd()).getLengthSquared();
            return new Line<T>(getStart(), startToOtherStartDistSq > startToOtherEndDistSq ? other.getStart() : other.getEnd());
        }
        
        throw new LinesDontConnectException();
    }
    
    /**
     * Splits this line into two lines, at the passed point.
     * @param splitPoint The point at which to split this line.
     * @param ensureSplitIsOnLine Whether or not to ensure that the passed split point is on this line. If it's not, the
     * resulting lines may not align or overlap with this one, and will just be from each of this line's endpoints to
     * the splitpoint.
     * @return An array containing the resulting two lines from the split. Will be from the endpoints of the line to
     * the split point if ensureSplitIsOnLine is false. If it's true, then this array will only contain a single line -
     * this one - when the split point isn't found on this line.
     */
    public Line<T>[] splitAt(T splitPoint, boolean ensureSplitIsOnLine)
    {
        if(ensureSplitIsOnLine && !hasPoint(splitPoint))
            return (Line<T>[])new Line[]{this};
        
        return (Line<T>[])new Line[]{ new Line<T>(getStart(), splitPoint), new Line<T>(splitPoint, getEnd()) };
    }
    
    /**
     * Splits this line into two lines, at the passed point.
     * @param splitPoint The point at which to split this line.
     * @return An array containing the resulting two lines from the split, or just a single line (this) if the point
     * isn't on this line.
     */
    public Line<T>[] splitAt(T splitPoint)
    { return splitAt(splitPoint, true); }
    
    /**
     * Gets a line that is this line with a section removed corresponding to the passed line.
     * @param other The line to remove from this line to get the resulting line.
     * @param ensureOtherIsInline Whether or not to make sure that the other line is aligned with and overlapping this
     * line. If false, then the resulting lines will connect to the end(s) of the other line regardless of where the
     * other line is - the resulting lines may not align with this one or eachother.
     * @param nullInsteadOfLinesDontAlignException When ensureOtherIsInLine is true, whether or not to return a null
     * from this method rather than throw a LinesDontAlignException when the other line doesn't align with this one.
     * @return An array with no, one, or two lines. If the other line was on this line (or ensureOtherIsInLine is
     * false), the resulting two lines will be from one point to one of the other line's points, then from the other
     * line's other point to this line's other point. If the other line partially overlaps this line, the resulting line
     * will be from the end of the other line within this line to the end of this line not within the other line. If
     * this line is entirely enclosed by the other line, the array will be empty. Null will be returned if
     * nullInsteadOfLinesDontAlignException is true and the two lines don't align (and ensureOtherIsInLine is true).
     * @throws LinesDontAlignException If this operation is impossible, as the two lines don't align, and
     * ensureOtherIsInLine is set to true. But not if nullInsteadOfLinesDontAlignException is false.
     */
    public Line<T>[] exclude(Line<T> other, boolean ensureOtherIsInline, boolean nullInsteadOfLinesDontAlignException)
    {
        // TO DO: Shorten?
        // Yes, I sat and worked all this out on paper, including which points to use for the starts and ends of
        // returned lines. I tried doing it purely mentally at first, but I kept getting different lines and cases
        // mixed up. This almost certainly could be shortened, but it'll do for now. I'm tired of thinking about lines.
        
        if(ensureOtherIsInline)
        {
            if(getStart().equals(other.getStart()))
            {
                if(getEnd().equals(other.getEnd()))
                    return new Line[0];
                
                if(hasPoint(other.getEnd()))
                    return new Line[]{ new Line<T>(other.getEnd(), getEnd()) };
                
                if(other.hasPoint(getEnd()))
                    return new Line[0];
                
                if(nullInsteadOfLinesDontAlignException)
                    return null;
                
                throw new LinesDontAlignException("Lines connect at starts, but don't align.");
            }
            
            if(getStart().equals(other.getEnd()))
            {
                if(getEnd().equals(other.getStart()))
                    return new Line[0];
                
                if(hasPoint(other.getStart()))
                    return new Line[]{ new Line<T>(other.getStart(), getEnd()) };
                
                if(other.hasPoint(getEnd()))
                    return new Line[0];
                
                if(nullInsteadOfLinesDontAlignException)
                    return null;
                
                throw new LinesDontAlignException("This line's start connects to that line's end, but they don't align.");
            }
            
            if(getEnd().equals(other.getStart()))
            {
                if(hasPoint(other.getEnd()))
                    return new Line[]{ new Line<T>(getStart(), other.getEnd()) };
                
                if(other.hasPoint(getStart()))
                    return new Line[0];
                
                if(nullInsteadOfLinesDontAlignException)
                    return null;
                
                throw new LinesDontAlignException("This line's end connects to that line's start, but they don't align.");
            }
            
            if(getEnd().equals(other.getEnd()))
            {
                if(hasPoint(other.getStart()))
                    return new Line[]{ new Line<T>(getStart(), other.getStart()) };
                
                if(other.hasPoint(getStart()))
                    return new Line[0];
                
                if(nullInsteadOfLinesDontAlignException)
                    return null;
                
                throw new LinesDontAlignException("Lines connect at ends, but don't align.");
            }
            
            if(hasPoint(other.getStart()))
            {
                if(hasPoint(other.getEnd()))
                    return exclude(other, false, nullInsteadOfLinesDontAlignException);
                
                if(other.hasPoint(getEnd()))
                    return new Line[]{ new Line<T>(getStart(), other.getStart()) };
                
                if(other.hasPoint(getStart()))
                    return new Line[]{ new Line<T>(other.getStart(), getEnd()) };
                
                if(nullInsteadOfLinesDontAlignException)
                    return null;
                
                throw new LinesDontAlignException("Other starts within this line, but doesn't align with it.");
            }
            
            if(hasPoint(other.getEnd()))
            {
                if(other.hasPoint(getStart()))
                    return new Line[]{ new Line<T>(other.getEnd(), getEnd()) };
                
                if(other.hasPoint(getEnd()))
                    return new Line[]{ new Line<T>(getStart(), other.getEnd()) };
                
                if(nullInsteadOfLinesDontAlignException)
                    return null;
                
                throw new LinesDontAlignException("Other line ends within this line, but doesn't align with it.");
            }
            
            if(nullInsteadOfLinesDontAlignException)
                    return null;
                
            throw new LinesDontAlignException("Lines don't align.");
        }
        
        if(getStart().equals(other.getStart()))
        {
            if(getEnd().equals(other.getEnd()))
                return new Line[0];
            
            return new Line[]{ new Line<T>(other.getEnd(), getEnd()) };
        }
        
        if(getStart().equals(other.getEnd()))
        {
            if(getEnd().equals(other.getStart()))
                return new Line[0];
            
            return new Line[]{ new Line<T>(other.getStart(), getEnd()) };
        }
        
        if(getEnd().equals(other.getStart()))
            return new Line[]{ new Line<T>(getStart(), other.getEnd()) };
        
        if(getEnd().equals(other.getEnd()))
            return new Line[]{ new Line<T>(getStart(), other.getEnd()) };
        
        T otherPointClosestToStart = getStart().getDistanceFromSquared(other.getStart()) < getStart().getDistanceFromSquared(other.getEnd()) ? other.getStart() : other.getEnd();
        T otherPointClosestToEnd = other.getStart() == otherPointClosestToStart ? other.getEnd() : other.getStart();
        return new Line[]{ new Line<T>(getStart(), otherPointClosestToStart), new Line<T>(otherPointClosestToEnd, getEnd()) };
    }
    
    /**
     * Gets a line that is this line with a section removed corresponding to the passed line.
     * @param other The line to remove from this line to get the resulting line.
     * @param ensureOtherIsInline Whether or not to make sure that the other line is aligned with and overlapping this
     * line. If false, then the resulting lines will connect to the end(s) of the other line regardless of where the
     * other line is - the resulting lines may not align with this one or eachother.
     * @return An array with no, one, or two lines. If the other line was on this line (or ensureOtherIsInLine is
     * false), the resulting two lines will be from one point to one of the other line's points, then from the other
     * line's other point to this line's other point. If the other line partially overlaps this line, the resulting line
     * will be from the end of the other line within this line to the end of this line not within the other line. If
     * this line is entirely enclosed by the other line, the array will be empty.
     * @throws LinesDontAlignException If this operation is impossible, as the two lines don't align, and
     * ensureOtherIsInLine is set to true.
     */
    public Line<T>[] exclude(Line<T> other, boolean ensureOtherIsInline)
    { return exclude(other, true, false); }
    
    /**
     * Gets a line that is this line with a section removed corresponding to the passed line.
     * @param other The line to remove from this line to get the resulting line.
     * @return An array with no, one, or two lines. If the other line was on this line, the resulting two lines will be
     * from one point to one of the other line's points, then from the other line's other point to this line's other
     * point. If the other line partially overlaps this line, the resulting line will be from the end of the other line
     * within this line to the end of this line not within the other line. If this line is entirely enclosed by the
     * other line, the array will be empty.
     * @throws LinesDontAlignException If this operation is impossible, as the two lines don't align.
     */
    public Line<T>[] exclude(Line<T> other)
    { return exclude(other, true); }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null || getClass() != obj.getClass())
            return false;
        
        final Line<?> other = (Line<?>)obj;
        
        return (this.getStart() == other.getStart() || this.getStart().equals(other.getStart()))
            && (this.getEnd()   == other.getEnd()   || this.getEnd()  .equals(other.getEnd()));
    }
    
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 79 * hash + (this.start != null ? this.start.hashCode() : 0);
        hash = 79 * hash + (this.end != null ? this.end.hashCode() : 0);
        return hash;
    }
}