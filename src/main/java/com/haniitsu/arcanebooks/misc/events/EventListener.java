package com.haniitsu.arcanebooks.misc.events;

import com.haniitsu.arcanebooks.misc.events.args.EventArgs;

public interface EventListener<T extends EventArgs>
{ void onEvent(Object sender, T args); }