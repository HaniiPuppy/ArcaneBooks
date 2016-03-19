package com.haniitsu.arcanebooks.magic;

/**
 * A message, carrying an object, to be passed through a SpellArgs object from one spell effect definition onto any
 * future one that's looking for any messages with the same name.
 * @param <T> The type of the object being passed along.
 */
public class SpellArgsValueMessage<T> extends SpellMessage
{
    /**
     * Creates a message with the passed name and object.
     * @param name The name of the message.
     * @param value The object to be passed along with the message.
     */
    public SpellArgsValueMessage(String name, T value)
    {
        super(name);
        this.value = value;
    }
    
    /** The object passed along with the message. */
    final T value;
    
    /**
     * Gets the object passed along with the message.
     * @return The object passed along with the message.
     */
    public T getValue()
    { return value; }
}

// the object passed along with the message, object passed along with the message, ject passed along with the message,
// passed along with the message, along with the message, long with the message, with the message, the message,
// message, sage. Echo. Cho.