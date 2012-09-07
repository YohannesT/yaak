package at.starcoders.yaak.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import at.starcoders.yaak.Button;
import at.starcoders.yaak.Keyboard;

/**
 * Draws just text, ignores icon.
 */
public class TextPainter implements Painter {
	
	private Keyboard kb;
	private Paint paint;
	
	public TextPainter(Keyboard kb) {
		this.kb = kb;
		paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
	}

	@Override
	public void drawKeyboard(Canvas canvas) {
		if (kb == null) {
			paint.setTextSize(100f);
			canvas.drawText("no keyboard", 100, 100, paint);
			return;
		}
		
		int i, j, rows, cols, h, w, dh, dw;
		rows = kb.getRows();
		cols = kb.getColumns();
		h = canvas.getHeight();
		w = canvas.getWidth();
		dh = h / rows;
		dw = w / cols;
		
		Button b;
		String t;
		for (i = 0; i < rows; i++) {
			for (j = 0; j < cols; j++) {
				b = kb.getButton(i, j);
				t = b.getText();
				
				if (b.isSelected())
					t = "->" + t + "<-";
					
				canvas.drawText(t, dh * j, dw * i + 20, paint);
			}
		}
	}

}
