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

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import at.starcoders.yaak.R;
import at.starcoders.yaak.painter.*;

/*
 * Is the keyboard view, which is displayed in full screen.
 * Paints the current keyboard.
 */
public class YaakView extends View {
	
	private Painter painter;
	
	
	public YaakView(Context context, Painter p) {
		super(context);
		this.painter = p;
	}
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		painter.drawKeyboard(canvas);		
	}
		
	public void setPainter(Painter p) {
		this.painter = p;
	}
}
