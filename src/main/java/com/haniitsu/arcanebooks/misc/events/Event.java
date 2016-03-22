package com.haniitsu.arcanebooks.misc.events;

import com.haniitsu.arcanebooks.misc.Converger;
import com.haniitsu.arcanebooks.misc.events.args.EventArgs;
import java.util.Collection;
import java.util.Map;

public interface Event<T extends EventArgs>
{
    Collection<EventListener<T>> getListeners();
    
    Collection<EventListener<T>> getWeakListeners();
    
    Collection<EventListener<T>> getStrongListeners();
    
    Collection<Event<? extends EventArgs>> getDependentEvents();
    
    Collection<Event<? extends EventArgs>> getWeaklyDependentEvents();
    
    Collection<Event<? extends EventArgs>> getStronglyDependentEvents();
    
    Map<Event<? extends EventArgs>, Converger<Object, T, ? extends EventArgs>> getDependentEventsAndGetters();
    
    Map<Event<? extends EventArgs>, Converger<Object, T, ? extends EventArgs>> getWeaklyDependentEventsAndGetters();
    
    Map<Event<? extends EventArgs>, Converger<Object, T, ? extends EventArgs>> getStronglyDependentEventsAndGetters();
    
    void raise(Object sender, T args);
    
    void registerListener(EventListener<T> listener);
    
    void registerListenerWeakly(EventListener<T> listener);
    
    void deregisterListener(EventListener<T> listener);
    
    <listeningT extends EventArgs> void registerListeningEvent(Event<listeningT> event, Converger<Object, T, listeningT> argsGetter);
    
    <listeningT extends EventArgs> void registerListeningEventWeakly(Event<listeningT> event, Converger<Object, T, listeningT> argsGetter);
    
    void deregisterListeningEvent(Event<? extends EventArgs> event);
}