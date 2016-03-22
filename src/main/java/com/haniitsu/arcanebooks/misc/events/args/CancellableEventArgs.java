package com.haniitsu.arcanebooks.misc.events.args;

public interface CancellableEventArgs extends EventArgs
{
    public boolean isCancelled();
    
    public void setCancelled(boolean cancelled);
}