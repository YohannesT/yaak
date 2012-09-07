package at.starcoders.yaak.painter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import at.starcoders.yaak.Button;
import at.starcoders.yaak.Keyboard;
import at.starcoders.yaak.Logger;

/**
 * Inverts colors of Button when selected.
 */
public class InvertPainter implements Painter {
	
	/** Icon to text ratio. */
	private static final float I2TR = 0.75f;
	
	private Keyboard kb;
	private Paint paint;
	private Paint invpaint;
	
	public InvertPainter(Keyboard kb) {
		this.kb = kb;
		paint = new Paint();
		invpaint = new Paint();
		
		float invert[] =
			{
				-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 
				0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
				0.0f, 0.0f, -1.0f, 1.0f, 1.0f, 
				0.0f, 0.0f, 0.0f, 1.0f, 0.0f
			};
		
		ColorMatrix cm = new ColorMatrix(invert);
		invpaint.setColorFilter(new ColorMatrixColorFilter(cm));
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
					drawOnlyText(canvas, b, dst, c, dc, r, dr);
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
			invpaint.setColor(kb.getBgColor());
			canvas.drawRect(dst, invpaint);
			canvas.drawBitmap(b.getIcon(), null, dst, invpaint);
		} else {
			canvas.drawBitmap(b.getIcon(), null, dst, paint);
		}
	}
	
	private void drawOnlyText(Canvas canvas, Button b, Rect dst, int col, int dc, int row, int dr) {
		float x, y, tw, th;
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
		if (b.isSelected()) {
			dst.left = col * dc;
			dst.right = dst.left + dc;
			dst.top = row * dr;
			dst.bottom = dst.top + dr;
			invpaint.setColor(kb.getBgColor());
			canvas.drawRect(dst, invpaint);
			invpaint.setColor(b.getFontColor());
			invpaint.setTextSize(th);
			canvas.drawText(text, x, y, invpaint);
		} else {
			paint.setColor(b.getFontColor());
			paint.setTextSize(th);
			canvas.drawText(text, x, y, paint);	
		}
	}
	
	private void drawIconAndText(Canvas canvas, Button b, Rect dst, int col, int dc, int row, int dr) {
		
		float itb = row * dr + I2TR * dr;  // icon text border
		dst.left = col * dc;
		dst.right = dst.left + dc;
		dst.top = row * dr;
		dst.bottom = dst.top + dr;
		
		float x, y, tw, th;
		th = 0.6f * (1f - I2TR) * dr;
		paint.setTextSize(th);
		String text = b.getText();
		tw = paint.measureText(text);
		if (tw > dc) {
			text = cropText(text, paint, dc);
			tw = paint.measureText(text);
		}
		x = col * dc + 0.5f * (dc - tw );
		y = (row + 1) * dr - 0.6f * ((1 - I2TR) * dr - th);
		
		if (b.isSelected()) {
			invpaint.setColor(kb.getBgColor());
			canvas.drawRect(dst, invpaint);
			invpaint.setColor(b.getFontColor());
			invpaint.setTextSize(th);
			dst.bottom = (int) itb;
			canvas.drawBitmap(b.getIcon(), null, dst, invpaint);
			canvas.drawText(text, x, y, invpaint);
		} else {
			paint.setColor(b.getFontColor());
			dst.bottom = (int) itb;
			canvas.drawBitmap(b.getIcon(), null, dst, paint);
			canvas.drawText(text, x, y, paint);	
		}
		
	}
	
	
	private String cropText(String text, Paint paint, int dc) {
		while (paint.measureText(text + "...") > dc) {
			text = text.substring(0, text.length() - 2);
		}
		text = text + "...";
		return text;
	}

}
