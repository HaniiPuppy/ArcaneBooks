package com.haniitsu.arcanebooks.registries;

import com.haniitsu.arcanebooks.magic.SpellArgs;

/** Repository for technical and utility methods used by default SpellEffectDefinitions. (As defined in DefaultDefs) */
public class DefaultDefsUtilMethods
{
    /**
     * Gets the boolean result of a basic "if" string, given the SpellArgs object as context.
     * @param toEval The string to evaluate into a true or false value.
     * @param spellArgs The spell args object carrying the information for the spell effect burst this is testing.
     * @return The result of the if string.
     */
    public static boolean evaluateIf(String toEval, SpellArgs spellArgs)
    {
        toEval = toEval.trim();
        String spellMsgPrefix = "%";
        
        int orPosition = -1, andPosition = -1;
        boolean precededByOrChar = false, precededByAndChar = false;
        int bracketDepth = 0;
        
        for(int i = 0; i < toEval.length(); i++)
        {
            char current = toEval.charAt(i);
            
            if(current == '(')
            {
                bracketDepth++;
                precededByOrChar = false;
                precededByAndChar = false;
            }
            else if(current == ')')
            {
                bracketDepth--;
                precededByOrChar = false;
                precededByAndChar = false;
            }
            else if(bracketDepth <= 0)
            {
                if(current == '|')
                {
                    if(orPosition < 0)
                    {
                        if(precededByOrChar)
                            orPosition = i;
                        else
                        {
                            precededByOrChar = true;
                            precededByAndChar = false;
                        }
                    }
                }
                else if(current == '&')
                {
                    if(precededByAndChar)
                    {
                        andPosition = i;
                        break;
                    }
                    else
                    {
                        precededByAndChar = true;
                        precededByOrChar = false;
                    }
                }
                else
                {
                    precededByOrChar = false;
                    precededByAndChar = false;
                }
            }
        }
        
        if(andPosition >= 0)
        {
            String left = toEval.substring(0, andPosition - 1);
            String right = toEval.substring(andPosition + 1);
            return evaluateAnd(left, right, spellArgs);
        }
        
        if(orPosition >= 0)
        {
            String left = toEval.substring(0, orPosition - 1);
            String right = toEval.substring(orPosition + 1);
            return evaluateOr(left, right, spellArgs);
        }
        
        if(toEval.startsWith("!"))
            return evaluateNot(toEval.substring(1), spellArgs);
        
        if(toEval.startsWith("(") && toEval.endsWith(")"))
            return evaluateIf(toEval.substring(1, toEval.length() - 1), spellArgs);
        
        return toEval.startsWith(spellMsgPrefix) ? spellArgs.getCast().getMessage(toEval.substring(1).toUpperCase()) != null
                                                 : spellArgs.getMessage(toEval.toUpperCase()) != null ;
    }
    
    /**
     * Evaluates the result of a basic "not" string, given the SpellArgs object as context.
     * @param toEval The string to evaluate into a true or false value.
     * @param spellArgs The spell args object carrying the information for the spell effect burst this is testing.
     * @return The result of this not string. That is, the opposite of evaluating this as an if string.
     */
    public static boolean evaluateNot(String toEval, SpellArgs spellArgs)
    { return !evaluateIf(toEval, spellArgs); }
    
    /**
     * Evaluates the result of two strings combined in an "and" gate, given the SpellArgs object as context.
     * @param left The first value to evaluate and pass into the and gate.
     * @param right The second value to evaluate and pass into the and gate.
     * @param spellArgs The spell args object carrying the information for the spell effect burst this is testing.
     * @return True if both strings evaluate to true as if strings. Otherwise, false.
     */
    public static boolean evaluateAnd(String left, String right, SpellArgs spellArgs)
    { return evaluateIf(left, spellArgs) && evaluateIf(right, spellArgs); }
    
    /**
     * Evaluates the result of two strings combined in an "or" gate, given the SpellArgs object as context.
     * @param left The first value to evaluate and pass into the or gate.
     * @param right The second value to evaluate and pass into the or gate.
     * @param spellArgs The spell args object carrying the information for the spell effect burst this is testing.
     * @return True if either of the strings evaluate to true as if strings. Otherwise, false.
     */
    public static boolean evaluateOr(String left, String right, SpellArgs spellArgs)
    { return evaluateIf(left, spellArgs) || evaluateIf(right, spellArgs); }
}