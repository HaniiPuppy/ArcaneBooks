package com.haniitsu.arcanebooks.magic;

public class SpellArgsMessage
{
    public SpellArgsMessage(String name)
    { this.name = name.trim().toLowerCase(); }
    
    final String name;
    
    public String getName()
    { return name; }
}