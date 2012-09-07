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

public class RowScanner extends Scanner {

	private int rowIndex;
	private int colIndex;
	private int rounds = 0;
	
	
	private enum States {ROW_SCANNING,SIMPLE_COLUMN_SCANNING, REPEAT_SCANNING};
	private States state;
	
	public RowScanner(Keyboard keyboard, int scanTime, int repeatTime) {
		super(keyboard, scanTime, repeatTime);
		rowIndex = 0;
		colIndex = 0;
		state = States.ROW_SCANNING;
	}
	
	public RowScanner(Keyboard keyboard, int scanTime, int repeatTime,int timeoutRounds) {
		super(keyboard, scanTime, repeatTime,timeoutRounds);
		rowIndex = 0;
		colIndex = 0;
		state = States.ROW_SCANNING;
	}
	
	public RowScanner(View v,Keyboard keyboard, int scanTime, int repeatTime) {
		super(v,keyboard, scanTime, repeatTime);
		rowIndex = 0;
		colIndex = 0;
	}
	
	public RowScanner(View v,Keyboard keyboard, int scanTime, int repeatTime, int timeoutRounds) {
		super(v,keyboard, scanTime, repeatTime, timeoutRounds);
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
				switch (state) {
					case ROW_SCANNING:
						keyboard.selectRow(rowIndex);
						view.postInvalidate();
						Thread.sleep(scanTime);
						rowIndex++;
						if (rowIndex >= keyboard.getRows())
							rowIndex = 0;
						break;
					case SIMPLE_COLUMN_SCANNING:
						keyboard.unselectAll();
						keyboard.selectButton(rowIndex, colIndex);
						view.postInvalidate();
						Thread.sleep(scanTime);
						colIndex++;
						if (colIndex >= keyboard.getColumns()) {
							colIndex = 0;
							rounds++;
						}
						if (rounds == timeoutRounds) {
							state = States.ROW_SCANNING;
							rowIndex = 0;
							colIndex = 0;
							rounds = 0;
						}
						break;
					case REPEAT_SCANNING:
						Thread.sleep(repeatTime);
						rowIndex = 0;
						colIndex = 0;
						state = States.ROW_SCANNING;
						break;
				}
			} catch (InterruptedException e) {
				if (exit)
					return;
				switch (state) {
					case ROW_SCANNING:
						colIndex = 0;
						rounds = 0;
						state = States.SIMPLE_COLUMN_SCANNING;
						break;
					case SIMPLE_COLUMN_SCANNING:
						Button b = keyboard.getButton(rowIndex, colIndex);
						Logger.log("Action triggered '" + b.getAction() + "'");
						fireButtonPressEvent(b);
						state = States.REPEAT_SCANNING;
						break;
					case REPEAT_SCANNING:
						Button b1 = keyboard.getButton(rowIndex, colIndex);
						Logger.log("Repeat Action triggered '"+ keyboard.getButton(rowIndex, colIndex).getAction() + "'");
						fireButtonPressEvent(b1);
						break;
					default:
						e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void trigger() {	
		this.interrupt();
	}
}
