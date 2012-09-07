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
import android.view.View;
import at.starcoders.yaak.Button;
import at.starcoders.yaak.Keyboard;
import at.starcoders.yaak.Logger;

public class SingleScanner extends Scanner {

	private int rowIndex;
	private int colIndex;
	private boolean repeating = false;
	private boolean triggered = false;
	
	public SingleScanner(Keyboard keyboard, int scanTime, int repeatTime) {
		super(keyboard, scanTime, repeatTime);
		rowIndex = 0;
		colIndex = 0;
	}
	
	public SingleScanner(View v,Keyboard keyboard, int scanTime, int repeatTime) {
		super(v,keyboard, scanTime, repeatTime);
		rowIndex = 0;
		colIndex = 0;
	}

	@Override
	public void run() {
		if (keyboard == null)
			return;
		running = true;
		while (running) {
			try {
				if (repeating == false) {
					keyboard.unselectAll();
					keyboard.selectButton(rowIndex, colIndex);
					if (view != null)
						view.postInvalidate();
					Thread.sleep(scanTime);
					colIndex++;
					if (colIndex >= keyboard.getColumns()) {
						colIndex = 0;
						rowIndex++;
					}
					if (rowIndex >= keyboard.getRows()) {
						rowIndex = 0;
						colIndex = 0;
					}
				} else {
					Thread.sleep(repeatTime);
					rowIndex = 0;
					colIndex = 0;
					view.postInvalidate();
					Logger.log("Repeat Time is over now");
					repeating = false;
				}
			} catch (InterruptedException e) {
				if (exit)
					return;
				if (triggered) {
					Button b = keyboard.getButton(rowIndex, colIndex);
					if (!repeating) {
						Logger.log("Action '" + b.getAction() + "' got triggered");
						fireButtonPressEvent(b);
					} else {
						Logger.log("Repeated Action '" + keyboard.getButton(rowIndex, colIndex).getAction() + "' got triggered");
						fireButtonPressEvent(b);
					}
					repeating = true;
					triggered = false;
				} else {
					Logger.log("inside SingleScanner.run():catch InterruptedException e");
					e.printStackTrace();
				}
					
			}
		}
	}

	@Override
	public void trigger() {	
		triggered = true;
		this.interrupt();  // why should it be interrupted??
	}
	
}
