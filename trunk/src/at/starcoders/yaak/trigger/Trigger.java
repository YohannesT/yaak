package at.starcoders.yaak.trigger;

import java.util.ArrayList;

import at.starcoders.yaak.Logger;

public abstract class Trigger {
	
	private ArrayList<TriggerListener> listeners;
	
	public Trigger() {
		listeners = new ArrayList<TriggerListener>();
	}
	
	public void addTriggerListener(TriggerListener l) {
		listeners.add(l);
	}
	
	public void fireTriggerEvent() {
		for (TriggerListener l : listeners) {
			Logger.log("Trigger.fireTriggerEvent(): trigger for " + l.getClass().toString());
			l.trigger();
		}
	}
	
	public void clearTriggerListeners() {
		listeners.clear();
	}
	
	public abstract boolean analyzeEvent(Object event);
	
	
	
	
}
