package com.haniitsu.arcanebooks.misc;

import com.haniitsu.arcanebooks.misc.geometry.Line;
import com.haniitsu.arcanebooks.misc.geometry.Point2d;
import java.util.ArrayList;
import java.util.List;

public class UtilMethods
{
    public static boolean pointIsOnLine(Point2d point, Line<? extends Point2d> line)
    { return pointIsOnLine(point, line, 0.000000000001); }
    
    public static boolean pointIsOnLine(Point2d point, Line<? extends Point2d> line, double precisionErrorMargin)
    {
        double firstHalfDistance = new Line<Point2d>(line.getStart(), point).getLength();
        double secondHalfDistance = new Line<Point2d>(point, line.getEnd()).getLength();
        double totalDistance = line.getLength();
        return totalDistance <= firstHalfDistance + secondHalfDistance + precisionErrorMargin;
    }
    
    
    
    public static void checkMinMaxArgs(int min, int max, String minName, String maxName)
    {
        if(min > max)
            throw new IllegalArgumentException(maxName + " (" + max + ") must be greater than " + minName + " (" + min + ").");
    }
    
    
    
    public static List<String> splitCSVLine(String toSplit)
    {
        List<String> entries = new ArrayList<String>();
        boolean nextCharIsEscaped = false;
        boolean lastCharIsEscapingQuote = false;
        StringBuilder entryBuilder = new StringBuilder();
        List<PositionState> positionStates = new ArrayList<PositionState>();
        
        for(int i = 0; i < toSplit.length(); i++)
        {
            char iChar = toSplit.charAt(i);
            boolean printCharacter = true; // Whether or not the character should be printed into the current entry.
            boolean thisCharShouldBeEscaped = nextCharIsEscaped;
            nextCharIsEscaped = false;
            
            switch(iChar)
            {
                case '\\':
                {
                    if(!thisCharShouldBeEscaped)
                    {
                        printCharacter = false;
                        nextCharIsEscaped = true;
                    }
                } break;
                    
                case ',':
                {
                    if(!thisCharShouldBeEscaped && positionStates.isEmpty())
                    {
                        entries.add(entryBuilder.toString());
                        entryBuilder = new StringBuilder();
                        printCharacter = false;
                    }
                } break;
                    
                case '\'':
                {
                    if(!thisCharShouldBeEscaped)
                    {
                        boolean isOpening = true;

                        for(int j = positionStates.size() - 1; j >= 0; j--)
                            if(positionStates.get(j) == PositionState.inInvertedCommas)
                            {
                                isOpening = false;
                                
                                for(int k = positionStates.size() - 1; k >= j; k--)
                                    positionStates.remove(k);
                                
                                break;
                            }
                        
                        if(isOpening)
                            positionStates.add(PositionState.inInvertedCommas);
                    }
                } break;
                    
                case '"':
                {
                    if(thisCharShouldBeEscaped)
                        break;
                    
                    if(lastCharIsEscapingQuote)
                        lastCharIsEscapingQuote = false;
                    else if(i < toSplit.length() - 1 && toSplit.charAt(i + 1) == '"')
                        lastCharIsEscapingQuote = true;
                    else
                    {
                        boolean isOpening = true;

                        for(int j = positionStates.size() - 1; j >= 0; j--)
                            if(positionStates.get(j) == PositionState.inQuotes)
                            {
                                isOpening = false;

                                for(int k = positionStates.size() - 1; k >= j; k--)
                                    positionStates.remove(k);

                                break;
                            }

                        if(isOpening)
                            positionStates.add(PositionState.inQuotes);
                    }
                } break;
                    
                case '(':
                {
                    if(!thisCharShouldBeEscaped)
                        positionStates.add(PositionState.inBrackets);
                } break;
                    
                case ')':
                {
                    if(!thisCharShouldBeEscaped)
                        for(int j = positionStates.size() - 1; j >= 0; j--)
                            if(positionStates.get(j) == PositionState.inBrackets)
                            {
                                for(int k = positionStates.size() - 1; k >= j; k--)
                                    positionStates.remove(k);

                                break;
                            }
                } break;
                    
                case '[':
                {
                    if(!thisCharShouldBeEscaped)
                        positionStates.add(PositionState.inSquareBrackets);
                } break;
                    
                case ']':
                {
                    if(!thisCharShouldBeEscaped)
                        for(int j = positionStates.size() - 1; j >= 0; j--)
                            if(positionStates.get(j) == PositionState.inSquareBrackets)
                            {
                                for(int k = positionStates.size() - 1; k >= j; k--)
                                    positionStates.remove(k);

                                break;
                            }
                } break;
                    
                case '{':
                {
                    if(!thisCharShouldBeEscaped)
                        positionStates.add(PositionState.inCurlyBrackets);
                } break;
                    
                case '}':
                {
                    if(!thisCharShouldBeEscaped)
                        for(int j = positionStates.size() - 1; j >= 0; j--)
                            if(positionStates.get(j) == PositionState.inCurlyBrackets)
                            {
                                for(int k = positionStates.size() - 1; k >= j; k--)
                                    positionStates.remove(k);

                                break;
                            }
                } break;
            }
            
            if(printCharacter)
                entryBuilder.append(iChar);
        }
        
        entries.add(entryBuilder.toString());
        handleQuotes(entries);
        return entries;
    }
    
    private static enum PositionState // Because Java doesn't support local enums.
    { inQuotes, inInvertedCommas, inBrackets, inSquareBrackets, inCurlyBrackets, inChevronBrackets }
    
    /**
     * Removes the surrounding quotes from entries that are enclosed in them, and converts adjacent pairs of quotation
     * marks into single ones, where they don't represent an empty field and the first quotation mark isn't escaped.
     * @param entries The list of entries to handle quotes in.
     */
    private static void handleQuotes(List<String> entries)
    {
        for(int i = 0; i < entries.size(); i++)
        {
            String iEntry = entries.get(i);
            String iEntryOriginal = iEntry;
            
            iEntry = iEntry.trim();
            
            if(iEntry.startsWith("\"") && iEntry.endsWith("\""))
                iEntry = iEntry.substring(1, iEntry.length() - 1);
            
            /*
                Go through iEntry looking for double quotes (""). Double-quotes representing empty fields have already
                been taken out with the previous check. Replace them with a single quote (") where the first quote isn't
                escaped. That is, where they aren't preceded by an escape character (\) that isn't itself escaped.
                This can be approximated by checking whether they're preceded by an odd or even number of the escape
                character.
            */
            
            for(int j = 0; j < iEntry.length() - 1; j++)
            {
                if(iEntry.charAt(j) == '"' && iEntry.charAt(j + 1) == '"')
                {
                    int escapeCharacterCount = 0;
                    
                    for(int k = j - 1; k >= 0; k--)
                    {
                        if(iEntry.charAt(k) == '\\')
                            escapeCharacterCount++;
                        else
                            break;
                    }
                    
                    if(escapeCharacterCount % 2 == 0) // if escapeCharacterCount is even or 0
                        iEntry = new StringBuilder(iEntry).deleteCharAt(j + 1).toString();
                }
            }
            
            if(!iEntry.equals(iEntryOriginal))
                entries.set(i, iEntry);
        }
    }
    
    /**
     * Gets a string of all the text in the passed source string both after the first instance of the first character,
     * and before the last instance of the last character.
     * @param source The string containing the desired substring.
     * @param first The character immediately before the start of the desired substring.
     * @param second The character immediately after the end of the desired substring.
     * @return The text between the first instance of the first char and last instance of the second char, or null if
     * one of the two chars aren't found, neither of the chars are found, or the first instance of the first char occurs
     * after the last instance of the second char, or both characters are the same and the first and last occurrence of
     * the character are at the same position.
     */
    public static String getTextBetween(String source, char first, char second)
    {
        int firstCharPosition = source.indexOf(first);
        int secondCharPosition = source.lastIndexOf(second);
        
        if(firstCharPosition < 0 || secondCharPosition < 0 || secondCharPosition <= firstCharPosition)
            return null;
        
        return source.substring(firstCharPosition + 1, secondCharPosition);
    }
}