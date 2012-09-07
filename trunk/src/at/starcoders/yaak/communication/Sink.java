package at.starcoders.yaak.communication;

import java.util.ArrayList;

import at.starcoders.yaak.Button;
import at.starcoders.yaak.scanning.ButtonPressListener;
import at.starcoders.yaak.trigger.TriggerListener;

public abstract class Sink implements Runnable{

	
	private ArrayList<TriggerListener> listeners;
	
	public Sink() {
		listeners = new ArrayList<TriggerListener>();
	}
	
	public void addTriggerListener(TriggerListener l) {
		listeners.add(l);
	}
	
	public void removeListener(TriggerListener l) {
		listeners.remove(l);
	}
	
	public void clearListeners() {
		listeners.clear();
	}
	
	public void fireTriggerEvent() {
		for (TriggerListener l : listeners)
			l.trigger();
	}
	
	public abstract void close();
	
	public abstract void sendMessage(String action);
}
