package com.haniitsu.arcanebooks.misc.events.args;

import com.haniitsu.arcanebooks.misc.events.Event;

/**
 * Base interface for all event args, passed into events when they're raised to carry information about that particular
 * instance.
 */
public interface EventArgs
{
    /** The methods required for technical reasons to access information within the EventArgs object. */
    public static interface TechnicalAccessor
    {
        /**
         * Sets the event related to the EventArgs object this technical accessor is from.
         * @param event The event being raised this technical accessor's EventArgs is passed into.
         */
        void setEvent(Event<? extends EventArgs> event);
    }
    
    /**
     * Gets the event that was raised, passing this EventArgs into it.
     * @return The EventArg's related Event object.
     */
    Event<? extends EventArgs> getEvent();
    
    /**
     * Gets the technical accessor for this EventArgs. This is used for technical purposes to do with the implementation
     * of the events system.
     * @return This EventArgs object's technical accessor.
     */
    TechnicalAccessor getTechnicalAccessor();
}