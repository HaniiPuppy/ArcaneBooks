package com.haniitsu.arcanebooks.misc.events.args;

import com.haniitsu.arcanebooks.misc.events.Event;

public class BasicEventArgs implements EventArgs
{
    protected Event<? extends EventArgs> event;
    protected EventArgs.TechnicalAccessor technicalAccessor = null;
    
    @Override
    public Event<? extends EventArgs> getEvent()
    { return event; }
    
    protected void setEvent(Event<? extends EventArgs> event)
    { this.event = event; }

    @Override
    public EventArgs.TechnicalAccessor getTechnicalAccessor()
    {
        if(technicalAccessor == null)
            technicalAccessor = new EventArgs.TechnicalAccessor()
            {
                @Override
                public void setEvent(Event<? extends EventArgs> event)
                { BasicEventArgs.this.setEvent(event); }
            };
        
        return technicalAccessor;
    }
}