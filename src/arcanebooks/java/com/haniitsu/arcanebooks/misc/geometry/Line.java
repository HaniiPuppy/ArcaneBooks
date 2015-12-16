package com.haniitsu.arcanebooks.misc.geometry;

public class Line<T extends Point2d>
{
    public static class LinesDontAlignException extends RuntimeException
    {
        public LinesDontAlignException() { super(); }
        public LinesDontAlignException(String message) { super(message); }
        public LinesDontAlignException(Throwable source) { super(source); }
        public LinesDontAlignException(String message, Throwable source) { super(message, source); }
    }
    
    public static class LinesDontConnectException extends RuntimeException
    {
        public LinesDontConnectException() { super(); }
        public LinesDontConnectException(String message) { super(message); }
        public LinesDontConnectException(Throwable source) { super(source); }
        public LinesDontConnectException(String message, Throwable source) { super(message, source); }
    }
    
    public Line(T start, T end)
    {
        this.start = start;
        this.end = end;
    }
    
    protected final T start;
    protected final T end;
    
    public T getStart()
    { return start; }
    
    public T getEnd()
    { return end; }
    
    public double getLength()
    { return Math.sqrt(getLengthSquared()); }
    
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
    
    public boolean hasPoint(T point)
    {
        double firstHalfDistance = Math.round(new Line<Point2d>(this.getStart(), point).getLength() * 100000000.00) / 100000000.00;
        double secondHalfDistance = Math.round(new Line<Point2d>(point, this.getEnd()).getLength() * 100000000.00) / 100000000.00;
        double totalDistance = Math.round(this.getLength() * 100000000.00) / 100000000.00;
        return totalDistance == firstHalfDistance + secondHalfDistance;
    }
    
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
    
    public boolean encloses(Line<T> other)
    { return hasPoint(other.start) && hasPoint(other.end); }
    
    public boolean isEnclosedBy(Line<T> other)
    { return other.hasPoint(start) && other.hasPoint(end); }
    
    public boolean connectsTo(Line<T> other)
    {
        return start.equals(other.start) && !end.equals(other.end)
            || end.equals(other.end) && !start.equals(other.start);
    }
    
    public boolean connectsToAligned(Line<T> other)
    {
        if(!connectsTo(other))
            return false;
        
        double thisAngle = this.getAngle(), otherAngle = other.getAngle();
        return thisAngle == otherAngle || thisAngle + 0.5 == otherAngle || thisAngle - 0.5 == otherAngle;
    }
    
    public double getLengthSquared()
    {
        Double xDist = start.getX().doubleValue() - end.getX().doubleValue();
        xDist = xDist * xDist;
        
        Double yDist = start.getY().doubleValue() - end.getY().doubleValue();
        yDist = yDist * yDist;
        
        return xDist + yDist;
    }
    
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
    
    public Line<T>[] splitAt(T splitPoint, boolean ensureSplitIsOnLine)
    {
        if(ensureSplitIsOnLine && !hasPoint(splitPoint))
            return (Line<T>[])new Line[]{this};
        
        return (Line<T>[])new Line[]{ new Line<T>(getStart(), splitPoint), new Line<T>(splitPoint, getEnd()) };
    }
    
    public Line<T>[] splitAt(T splitPoint)
    { return splitAt(splitPoint, true); }
    
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
    
    public Line<T>[] exclude(Line<T> other, boolean ensureOtherIsInline)
    { return exclude(other, true, false); }
    
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