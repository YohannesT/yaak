package at.starcoders.yaak.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import at.starcoders.yaak.Button;
import at.starcoders.yaak.Keyboard;
import at.starcoders.yaak.Logger;

/**
 * Draws just icons, ignores text.
 */
public class IconPainter implements Painter {
	
	private Keyboard kb;
	private Paint paint;
	
	public IconPainter(Keyboard kb) {
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
		
		int r, c, rows, cols, h, w, dr, dc;
		rows = kb.getRows();
		cols = kb.getColumns();
		h = canvas.getHeight();
		w = canvas.getWidth();
		dr = h / rows;
		dc = w / cols;
		
		Button b;
		Rect dst = new Rect();
		for (r = 0; r < rows; r++) {
			for (c = 0; c < cols; c++) {
				b = kb.getButton(r, c);
				if (b.getIcon() != null) {
					dst.left = c * dc;
					dst.right = dst.left + dc;
					dst.top = r * dr;
					dst.bottom = dst.top + dr;
					if (b.isSelected()) {
						paint.setARGB(255, 0, 255, 0);
						canvas.drawRect(dst, paint);
					}
					canvas.drawBitmap(b.getIcon(), null, dst, paint);
				} else {
					// draw text
					canvas.drawText(b.getText(), dr * c, dc * r, paint);
				}
			}
		}
		
		/*
		int x = 0;
		int y = h;
		paint.setARGB(255, 255, 0, 0);
		canvas.drawCircle(x, y, 20f, paint);
		paint.setARGB(255, 255, 255, 255);
		canvas.drawCircle(x, y, 18f, paint);
		paint.setARGB(255, 255, 0, 0);
		canvas.drawLine(x, y, x + 15, y - 15, paint);
		*/
	}

}
