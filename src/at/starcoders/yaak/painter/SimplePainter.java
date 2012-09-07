package at.starcoders.yaak.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import at.starcoders.yaak.Button;
import at.starcoders.yaak.Keyboard;

/**
 * Paints text and icons, but very simple.
 */
public class SimplePainter implements Painter {
	
	private Keyboard kb;
	private Paint paint;
	
	public SimplePainter(Keyboard kb) {
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
		
		int rows, cols, h, w, dr, dc;
		rows = kb.getRows();
		cols = kb.getColumns();
		h = canvas.getHeight();
		w = canvas.getWidth();
		dr = h / rows;
		dc = w / cols;
		
		Button b;
		String s;
		Rect dst = new Rect();
		int r, c;
		for (r = 0; r < rows; r++) {
			for (c = 0; c < cols; c++) {
				b = kb.getButton(r, c);
				// draw icon and text
				if (( s = b.getText() ) != null && s.length() > 0 && b.getIcon() != null) {
					drawIconAndText(canvas, b, dst, c, dc, r, dr);
				// draw text only
				} else if (s != null && s.length() > 0) {
					drawOnlyText(canvas, b, c, dc, r, dr);
				// draw icon only
				} else if (b.getIcon() != null) {
					drawOnlyIcon(canvas, b, dst, c, dc, r, dr);
				// no icon and no text found -> button is empty
				} else {
					canvas.drawText("empty", dr * c, dc * r, paint);
				}
			}
		}
	}
	
	private void drawOnlyIcon(Canvas canvas, Button b, Rect dst, int col, int dc, int row, int dr) {
		dst.left = col * dc;
		dst.right = dst.left + dc;
		dst.top = row * dr;
		dst.bottom = dst.top + dr;
		if (b.isSelected()) {
			paint.setColor(b.getBgColor());
			canvas.drawRect(dst, paint);
		}
		canvas.drawBitmap(b.getIcon(), null, dst, paint);
	}
	
	private void drawOnlyText(Canvas canvas, Button b, int col, int dc, int row, int dr) {
		float x, y, tw, th;
		paint.setColor(b.getFontColor());
		th = 0.25f * dr;  // TODO 3: adapt the value
		paint.setTextSize(th);
		tw = paint.measureText(b.getText());
		// TODO 4: if tw > dc crop text
		x = col * dc + 0.5f * (dc - tw );
		y = (row + 1) * dr - 0.5f * (dr - th);
		canvas.drawText(b.getText(), x, y, paint);
	}
	
	private void drawIconAndText(Canvas canvas, Button b, Rect dst, int col, int dc, int row, int dr) {
		float itb = row * dr + 0.75f * dr;  // icon text border
		
		// draw icon
		dst.left = col * dc;
		dst.right = dst.left + dc;
		dst.top = row * dr;
		dst.bottom = (int) itb;
		if (b.isSelected()) {
			paint.setColor(b.getBgColor());
			canvas.drawRect(dst, paint);
		}
		canvas.drawBitmap(b.getIcon(), null, dst, paint);
		
		// draw text
		float x, y, tw, th;
		paint.setColor(b.getFontColor());
		th = 0.125f * dr;
		paint.setTextSize(th);
		tw = paint.measureText(b.getText());
		x = col * dc + 0.5f * (dc - tw );
		y = (row + 1) * dr - 0.5f * (0.25f * dr - th);
		canvas.drawText(b.getText(), x, y, paint);
	}

}
