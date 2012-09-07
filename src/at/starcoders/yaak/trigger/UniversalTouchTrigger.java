package at.starcoders.yaak.trigger;

import android.view.MotionEvent;

public class UniversalTouchTrigger extends Trigger {

	private long lastDownTime = 0;
	
	@Override
	public boolean analyzeEvent(Object event) {
		if (event instanceof MotionEvent) {
			MotionEvent me = (MotionEvent) event;
			if (me.getAction() == MotionEvent.ACTION_UP)
				return true;
			if (lastDownTime != me.getDownTime()) {
				lastDownTime = me.getDownTime();
				fireTriggerEvent();
				return true;
			}	
		}
		return false;
	}
}
