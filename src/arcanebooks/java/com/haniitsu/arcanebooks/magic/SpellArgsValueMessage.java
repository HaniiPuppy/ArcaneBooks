package com.haniitsu.arcanebooks.magic;

public class SpellArgsValueMessage<T> extends SpellArgsMessage
{
    public SpellArgsValueMessage(String name, T value)
    {
        super(name);
        this.value = value;
    }
    
    final T value;
    
    public T getValue()
    { return value; }
}