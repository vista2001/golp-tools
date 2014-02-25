package dev.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
/**
 * TFM模型的图形
 * @author 木木
 *
 */
public class DoubleRectangle extends RoundedRectangle implements MyFigure
{
	//图形名称
	private String text="";
	/**
	 * 图形填充为俩个圆角矩形
	 */
	@Override
	protected void fillShape(Graphics graphics)
	{
		
		// super.fillShape(graphics);
		Rectangle rec = getBounds();
		Rectangle r = new Rectangle(rec.x + 3, rec.y + 3, rec.width - 7,
				rec.height - 6);
		graphics.drawRoundRectangle(r, 10, 10);
	}
	/**
	 * 图形轮廓为矩形
	 */
	@Override
	protected void outlineShape(Graphics graphics)
	{
		
		// super.outlineShape(graphics);
		graphics.setAntialias(SWT.ON);
		graphics.drawText(text, getBounds().getCenter()
				.getTranslated(-12, -10));
	}
	@Override
	public void setText(String text)
	{
		
		this.text = text;
		repaint();
	}
	@Override
	public String getText()
	{
		return text;
	}
}
