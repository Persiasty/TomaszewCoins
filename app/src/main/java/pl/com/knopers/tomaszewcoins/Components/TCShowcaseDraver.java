package pl.com.knopers.tomaszewcoins.Components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import com.github.amlcurran.showcaseview.ShowcaseDrawer;

public class TCShowcaseDraver implements ShowcaseDrawer
{

	private float width;
	private float height;
	private final Paint eraserPaint;
	private final Paint basicPaint;
	private final RectF renderRect;

	public TCShowcaseDraver()
	{
		PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
		eraserPaint = new Paint();
		eraserPaint.setColor(0xFFFFFF);
		eraserPaint.setAlpha(0);
		eraserPaint.setXfermode(xfermode);
		eraserPaint.setAntiAlias(true);
		basicPaint = new Paint();
		renderRect = new RectF();
	}

	@Override
	public void setShowcaseColour(int color) {}

	@Override
	public void drawShowcase(Bitmap buffer, float x, float y, float scaleMultiplier)
	{
		Canvas bufferCanvas = new Canvas(buffer);
		renderRect.left = x - width / 2f;
		renderRect.right = x + width / 2f;
		renderRect.top = y - height / 2f;
		renderRect.bottom = y + height / 2f;
		bufferCanvas.drawRect(renderRect, eraserPaint);
	}

	@Override
	public int getShowcaseWidth()
	{
		return (int) width;
	}

	@Override
	public int getShowcaseHeight()
	{
		return (int) height;
	}

	@Override
	public float getBlockedRadius()
	{
		return width;
	}

	@Override
	public void setBackgroundColour(int backgroundColor) {}

	@Override
	public void erase(Bitmap bitmapBuffer)
	{
		bitmapBuffer.eraseColor(0xcc000000);
	}

	public void setSize(int width, int height)
	{
		this.width = Math.max(width, 200) + 10;
		this.height = height + 1;
	}

	@Override
	public void drawToCanvas(Canvas canvas, Bitmap bitmapBuffer)
	{
		canvas.drawBitmap(bitmapBuffer, 0, 0, basicPaint);
	}

}