package dev.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * 结束模型对应的图形
 * 
 * @author 木木
 * 
 */
public class Circle_End extends Circle implements MyFigure
{
	// 图形名称
	private String text = "结束";

	/**
	 * 图形填充为俩个同心圆
	 */
	@Override
	protected void fillShape(Graphics graphics)
	{

		super.fillShape(graphics);
		Rectangle rec = getBounds();
		graphics.setAntialias(SWT.ON);
		int r = (rec.width > rec.height) ? rec.height : rec.width;
		// graphics.setBackgroundColor(ColorConstants.black);
		graphics.fillOval(
				rec.getCenter().getTranslated(-3 * r / 8, -3 * r / 8).x, rec
						.getCenter().getTranslated(-3 * r / 8, -3 * r / 8).y,
				3 * r / 4, 3 * r / 4);

	}

	/**
	 * 图像轮廓是圆
	 */
	@Override
	protected void outlineShape(Graphics graphics)
	{

		Rectangle rec = getBounds();
		int r = (rec.width > rec.height) ? rec.height : rec.width;
		// graphics.setForegroundColor(ColorConstants.green);
		graphics.setAntialias(SWT.ON);
		graphics.drawOval(
				rec.getCenter().getTranslated(-3 * r / 8, -3 * r / 8).x, rec
						.getCenter().getTranslated(-3 * r / 8, -3 * r / 8).y,
				3 * r / 4, 3 * r / 4);
		graphics.drawOval(rec.x + rec.width / 2 - r / 2, rec.y + rec.height / 2
				- r / 2, r, r);
		graphics.drawText(text, rec.getCenter().getTranslated(-12, -10));
	}

	@Override
	public void setText(String text)
	{

		this.text = text;
		repaint();
	}

}
