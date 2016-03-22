package com.haniitsu.arcanebooks.misc.events.args;

import com.haniitsu.arcanebooks.misc.events.Event;

public interface EventArgs
{
    public static interface TechnicalAccessor
    {
        void setEvent(Event<? extends EventArgs> event);
    }
    
    Event<? extends EventArgs> getEvent();
    
    TechnicalAccessor getTechnicalAccessor();
}