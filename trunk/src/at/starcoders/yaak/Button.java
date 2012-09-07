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

import android.graphics.Bitmap;
import at.starcoders.yaak.R;

public class Button {
	private String text;
	private Bitmap icon;
	private int bgColor;
	private int fontColor;
	private String action;
	
	private boolean selected;
	

	/** default constructor */
	public Button() {
		// just smile, not more
	}
	
	// constructor only for text, the rest has to be set
	public Button(String text) {
		this.text = text;
	}
	
	
	/*
	 * is called when the button is activated/should run it's action
	 */
	public void onPress() {
		if (action != null)
			Logger.log(action);
		else
			Logger.log("no action defined for this button!");
	}
	
	
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Bitmap getIcon() {
		return icon;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}
	
	

	
}
