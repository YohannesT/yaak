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


package at.starcoders.yaak;

import at.starcoders.yaak.R;


public class Keyboard {

	private final int rows;
	private final int columns;
	private Button[][] buttons;
	private int bgColor;
	
	/**
	 * Keyboard has to know number of rows and columns at beginning.
	 */
	public Keyboard(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		buttons = new Button[rows][columns];
	}

	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public Button getButton(int row, int col) {
		return buttons[row][col];
	}
	
	public Button[][] getButtons() {
		return buttons;
	}
	
	public void selectButton(int row, int column) {
		unselectAll();
		buttons[row][column].setSelected(true);
	}
	
	public void selectColumn(int column) {
		unselectAll();
		if (column < 0 || column >= columns)
			return;
		for (int i = 0; i < rows; i++) {
			Button b =buttons[i][column];
			if (b != null)
				b.setSelected(true);
		}
	}
	
	public void selectRow(int row) {
		unselectAll();
		if (row < 0 || row >= rows)
			return;
		for (int i = 0; i < columns; i++) 
			buttons[row][i].setSelected(true);
	}
	
	public void setButton(int row, int col, Button b) {
		buttons[row][col] = b;
	}
	
	public int getBgColor() {
		return bgColor;
	}


	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	/* 
	 * used before selecting something, 
	 * to be sure nothing else is selected afterwards.
	 */
	public void unselectAll() {
		int i, j;
		for (i = 0; i < rows; i++) {
			for (j = 0; j < columns; j++) {
				if (buttons != null) {
					Button b = buttons[i][j];
					if (b != null)
						b.setSelected(false);
				}
			}
		}
	}
	

	
}
