package at.starcoders.yaak.painter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import at.starcoders.yaak.Button;
import at.starcoders.yaak.Keyboard;
import at.starcoders.yaak.Logger;

/**
 * Paints borders around Buttons when selected.
 */
public class BorderPainter implements Painter {
	
	/** Icon to text ratio. */
	private static final float I2TR = 0.75f;
	
	private static int BORDER_COLOR;
	private static float BORDER_SIZE;
	private static int BGCOLOR;
	
	private Keyboard kb;
	private Paint paint;
	
	public BorderPainter(Keyboard kb, int borderColor, float borderSize, int bgColor) {
		this.kb = kb;
		paint = new Paint();
		BORDER_COLOR = borderColor;
		BORDER_SIZE = borderSize;
		BGCOLOR = bgColor;
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
		canvas.drawColor(BGCOLOR);
		
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
		/* testing paint.setStrokeWidth()
		paint.setColor(BORDER_COLOR);
		paint.setStrokeWidth(10f);
		canvas.drawLine(0f, 240f, 100f, 240f, paint);
		paint.setStrokeWidth(0f);
		canvas.drawLine(102f, 235f, 102f, 245f, paint);
		*/
	}
	
	private void drawOnlyIcon(Canvas canvas, Button b, Rect dst, int col, int dc, int row, int dr) {
		dst.left = col * dc;
		dst.right = dst.left + dc;
		dst.top = row * dr;
		dst.bottom = dst.top + dr;
		if (b.isSelected()) {
			drawBorder(canvas, col, dc, row, dr);
			/*
			paint.setColor(BORDER_COLOR);
			paint.setStrokeWidth(BORDER_SIZE);
			float pts[] = {	dst.left, dst.top, dst.left, dst.bottom, 
							dst.left, dst.bottom, dst.right, dst.bottom,
							dst.right, dst.bottom, dst.right, dst.top, 
							dst.right, dst.top, dst.left, dst.top};
			canvas.drawLines(pts, 0, pts.length, paint);
			*/
		}
		canvas.drawBitmap(b.getIcon(), null, dst, paint);
	}
	
	private void drawOnlyText(Canvas canvas, Button b, int col, int dc, int row, int dr) {
		float x, y, tw, th;
		paint.setColor(b.getFontColor());
		th = 100f; //0.5f * dr;
		paint.setTextSize(th);
		String text = b.getText();
		tw = paint.measureText(text);
		if (tw > dc) {
			text = cropText(text, paint, dc);
			tw = paint.measureText(text);
		}
		x = col * dc + 0.5f * (dc - tw );
		y = (row + 1) * dr - 0.5f * (dr - th);
		canvas.drawText(text, x, y, paint);
		
		Rect dst = new Rect();
		dst.left = col * dc;
		dst.right = dst.left + dc;
		dst.top = row * dr;
		dst.bottom = dst.top + dr;
		if (b.isSelected()) {
			drawBorder(canvas, col, dc, row, dr);
		}
	}
	
	private void drawIconAndText(Canvas canvas, Button b, Rect dst, int col, int dc, int row, int dr) {
		float itb = row * dr + I2TR * dr;  // icon text border
		
		// draw icon
		dst.left = col * dc;
		dst.right = dst.left + dc;
		dst.top = row * dr;
		dst.bottom = (int) itb;
		canvas.drawBitmap(b.getIcon(), null, dst, paint);
		
		// draw text
		float x, y, tw, th;
		paint.setColor(b.getFontColor());
		th = 0.8f * (1f - I2TR) * dr;
		paint.setTextSize(th);
		String text = b.getText();
		tw = paint.measureText(text);
		if (tw > dc) {
			text = cropText(text, paint, dc);
			tw = paint.measureText(text);
		}
		x = col * dc + 0.5f * (dc - tw );
		y = (row + 1) * dr - 0.5f * ((1 - I2TR) * dr - th);
		canvas.drawText(text, x, y, paint);
		
		dst.left = col * dc;
		dst.right = dst.left + dc;
		dst.top = row * dr;
		dst.bottom = dst.top + dr;
		if (b.isSelected()) {
			drawBorder(canvas, col, dc, row, dr);
		}
	}
	
	private void drawBorder(Canvas canvas, int col, int dc, int row, int dr) {
		float lf, rg, tp, bt, bsh;
		bsh = 0.5f * BORDER_SIZE;
		lf = col * dc + bsh;
		rg = lf + dc - bsh;
		tp = row * dr + bsh;
		bt = tp + dr - bsh;
		paint.setColor(BORDER_COLOR);
		paint.setStrokeWidth(BORDER_SIZE);
		float pts[] = {	lf, tp - bsh, lf, bt + bsh, 
						lf - bsh, bt, rg + bsh, bt,
						rg, bt + bsh, rg, tp - bsh, 
						rg + bsh, tp, lf - bsh, tp};
		canvas.drawLines(pts, 0, pts.length, paint);
	}
	
	private String cropText(String text, Paint paint, int dc) {
		while (paint.measureText(text + "...") > dc) {
			text = text.substring(0, text.length() - 2);
		}
		text = text + "...";
		return text;
	}

}
