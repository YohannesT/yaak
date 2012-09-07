/*
 * 
 * |\     /|(  ___  )(  ___  )| \    /\
 * ( \   / )| (   ) || (   ) ||  \  / /
 *  \ (_) / | (___) || (___) ||  (_/ / 
 *   \   /  |  ___  ||  ___  ||   _ (  
 *    ) (   | (   ) || (   ) ||  ( \ \ 
 *    | |   | )   ( || )   ( ||  /  \ \
 *    \_/   |/     \||/     \||_/    \/
 * 
 * Yet another Accessible Keyboard
 * Author: Sebastian & David
 * Coding Event SS12
 */

package at.starcoders.yaak.scanning;

import java.util.ArrayList;

import android.view.View;
import at.starcoders.yaak.Button;
import at.starcoders.yaak.Keyboard;
import at.starcoders.yaak.Logger;
import at.starcoders.yaak.trigger.TriggerListener;
// TODO: thread guy
public abstract class Scanner extends Thread implements TriggerListener {
	
	protected Keyboard keyboard;
	protected boolean running;
	protected int repeatTime, scanTime, timeoutRounds;
	protected View view;
	protected boolean exit = false;
	
	private ArrayList<ButtonPressListener> bpListener;
	
	public abstract void trigger();
	
	public Scanner(Keyboard keyboard, int scanTime, int repeatTime) {
		this.view = null;
		this.keyboard = keyboard;
		this.scanTime = scanTime;
		this.repeatTime = repeatTime;
		timeoutRounds = -1;
		running = false;
		bpListener = new ArrayList<ButtonPressListener>();
		deselectAll();
	}
	
	public Scanner(Keyboard keyboard, int scanTime, int repeatTime,int timeoutRounds) {
		this.view = null;
		this.keyboard = keyboard;
		this.scanTime = scanTime;
		this.repeatTime = repeatTime;
		this.timeoutRounds = timeoutRounds;
		running = false;
		bpListener = new ArrayList<ButtonPressListener>();
		deselectAll();
	}
	
	public void addButtonPressListener(ButtonPressListener bpl) {
		bpListener.add(bpl);
	}
	
	public void removeButtonPressListener(ButtonPressListener bpl) {
		bpListener.remove(bpl);
	}
	
	protected void fireButtonPressEvent(Button b) {
		Logger.log("Scanner.fireButtonPressEvent(), number of listeners = " + bpListener.size());
		for (ButtonPressListener bpl : bpListener) {
			bpl.buttonPressed(b);
		}
	}
	
	public Scanner(View v, Keyboard keyboard, int scanTime, int repeatTime) {
		this.view = v;
		this.keyboard = keyboard;
		this.scanTime = scanTime;
		this.repeatTime = repeatTime;
		running = false;
		deselectAll();
	}
	

	public Scanner(View v, Keyboard keyboard, int scanTime, int repeatTime, int timeoutRounds) {
		this.view = v;
		this.keyboard = keyboard;
		this.scanTime = scanTime;
		this.repeatTime = repeatTime;
		this.timeoutRounds = timeoutRounds;
		running = false;
		deselectAll();
	}
	
	protected void deselectAll() {
		if (keyboard == null)
			return;
		for (int c = 0; c < keyboard.getColumns(); c++) {
			for (int r = 0; r < keyboard.getRows(); r++) {
				Button b = keyboard.getButton(r,c);
				if (b != null)
					b.setSelected(false);
			}
		}
	}

	public void close() {
		exit = true;
		this.interrupt();
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	
	
}
