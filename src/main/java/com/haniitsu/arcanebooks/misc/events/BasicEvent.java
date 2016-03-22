package com.haniitsu.arcanebooks.misc.events;

import com.haniitsu.arcanebooks.misc.Converger;
import com.haniitsu.arcanebooks.misc.events.args.EventArgs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.lang3.NotImplementedException;

public class BasicEvent<T extends EventArgs> implements Event<T>
{
    protected final Collection<EventListener<T>> listeners = new HashSet<EventListener<T>>();
    
    protected final Collection<EventListener<T>> weakListeners
        = Collections.newSetFromMap(new WeakHashMap<EventListener<T>, Boolean>());
    
    protected final Map<Event<?>, Converger<Object, T, ? extends EventArgs>> dependentEvents
        = new HashMap<Event<?>, Converger<Object, T, ? extends EventArgs>>();
    
    protected final Map<Event<?>, Converger<Object, T, ? extends EventArgs>> weaklyDependentEvents
        = new WeakHashMap<Event<?>, Converger<Object, T, ? extends EventArgs>>();
    
    @Override
    public Collection<EventListener<T>> getListeners()
    {
        ArrayList<EventListener<T>> returnListeners = new ArrayList<EventListener<T>>();
        
        synchronized(listeners)
        {
            returnListeners.addAll(listeners);
            returnListeners.addAll(weakListeners);
        }
        
        return returnListeners;
    }
    
    @Override
    public Collection<EventListener<T>> getWeakListeners()
    {
        synchronized(listeners)
        { return new ArrayList<EventListener<T>>(weakListeners); }
    }

    @Override
    public Collection<EventListener<T>> getStrongListeners()
    {
        synchronized(listeners)
        { return new ArrayList<EventListener<T>>(listeners); }
    }
    
    @Override
    public Collection<Event<? extends EventArgs>> getDependentEvents()
    {
        ArrayList<Event<? extends EventArgs>> returnEvents = new ArrayList<Event<? extends EventArgs>>();
        
        synchronized(dependentEvents)
        {
            returnEvents.addAll(dependentEvents.keySet());
            returnEvents.addAll(weaklyDependentEvents.keySet());
        }
        
        return returnEvents;
    }

    @Override
    public Collection<Event<? extends EventArgs>> getWeaklyDependentEvents()
    {
        synchronized(dependentEvents)
        { return new ArrayList<Event<? extends EventArgs>>(weaklyDependentEvents.keySet()); }
    }

    @Override
    public Collection<Event<? extends EventArgs>> getStronglyDependentEvents()
    {
        synchronized(dependentEvents)
        { return new ArrayList<Event<? extends EventArgs>>(dependentEvents.keySet()); }
    }
    
    @Override
    public Map<Event<? extends EventArgs>, Converger<Object, T, ? extends EventArgs>> getDependentEventsAndGetters()
    {
        Map<Event<? extends EventArgs>, Converger<Object, T, ? extends EventArgs>> returnMap
            = new HashMap<Event<? extends EventArgs>, Converger<Object, T, ? extends EventArgs>>();
        
        synchronized(dependentEvents)
        {
            returnMap.putAll(dependentEvents);
            returnMap.putAll(weaklyDependentEvents);
        }
        
        return returnMap;
    }

    @Override
    public Map<Event<? extends EventArgs>, Converger<Object, T, ? extends EventArgs>> getWeaklyDependentEventsAndGetters()
    {
        synchronized(dependentEvents)
        { return new HashMap<Event<? extends EventArgs>, Converger<Object, T, ? extends EventArgs>>(weaklyDependentEvents); }
    }

    @Override
    public Map<Event<? extends EventArgs>, Converger<Object, T, ? extends EventArgs>> getStronglyDependentEventsAndGetters()
    {
        synchronized(dependentEvents)
        { return new HashMap<Event<? extends EventArgs>, Converger<Object, T, ? extends EventArgs>>(dependentEvents); }
    }

    @Override
    public void raise(Object sender, T args)
    {
        synchronized(listeners)
        {
            for(EventListener<T> listener : listeners)
                listener.onEvent(sender, args);
            
            for(EventListener<T> listener : weakListeners)
                listener.onEvent(sender, args);
        }
        
        synchronized(dependentEvents)
        {
            // Type arguments of entry.getKey() and entry.getValue().get(sender, args) is guaranteed to be the same.
            for(Map.Entry<Event<?>, Converger<Object, T, ? extends EventArgs>> entry : dependentEvents.entrySet())
                ((Event<EventArgs>)entry.getKey()).raise(sender, entry.getValue().get(sender, args));
            
            for(Map.Entry<Event<?>, Converger<Object, T, ? extends EventArgs>> entry : weaklyDependentEvents.entrySet())
                ((Event<EventArgs>)entry.getKey()).raise(sender, entry.getValue().get(sender, args));
        }
    }

    @Override
    public void registerListener(EventListener<T> listener)
    {
        synchronized(listeners)
        {
            if(!weakListeners.contains(listener))
                listeners.add(listener);
        }
    }
    
    @Override
    public void registerListenerWeakly(EventListener<T> listener)
    {
        synchronized(listeners)
        {
            if(!listeners.contains(listener))
                weakListeners.add(listener);
        }
    }

    @Override
    public void deregisterListener(EventListener<T> listener)
    {
        synchronized(listeners)
        {
            listeners.remove(listener);
            weakListeners.remove(listener);
        }
    }

    @Override
    public <listeningT extends EventArgs> void registerListeningEvent(Event<listeningT> event, Converger<Object, T, listeningT> argsGetter)
    {
        synchronized(dependentEvents)
        {
            if(!weaklyDependentEvents.containsKey(event))
                dependentEvents.put(event, argsGetter);
        }
    }

    @Override
    public <listeningT extends EventArgs> void registerListeningEventWeakly(Event<listeningT> event, Converger<Object, T, listeningT> argsGetter)
    {
        synchronized(dependentEvents)
        {
            if(!dependentEvents.containsKey(event))
                weaklyDependentEvents.put(event, argsGetter);
        }
    }

    @Override
    public void deregisterListeningEvent(Event<? extends EventArgs> event)
    {
        synchronized(dependentEvents)
        {
            dependentEvents.remove(event);
            weaklyDependentEvents.remove(event);
        }
    }
}