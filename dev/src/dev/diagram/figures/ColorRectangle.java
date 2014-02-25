package dev.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
/**
 * 异常和补偿模型的图形
 * @author 木木
 *
 */
public class ColorRectangle extends RectangleFigure implements MyFigure
{
	//图形的名称
	private String text="异常和补偿";
	/**
	 * 图形填充为矩形
	 */
	@Override
	protected void fillShape(Graphics graphics)
	{
		
		super.fillShape(graphics);
		Rectangle rec = getBounds();
		graphics.setBackgroundColor(ColorConstants.lightBlue);
		graphics.fillRectangle(rec.x, rec.y, rec.width, 25);
		graphics.drawString(text, rec.getTop().translate(-30, 4));
	}
	/**
	 * 图形轮廓为矩形
	 */
	@Override
	protected void outlineShape(Graphics graphics)
	{
		
		super.outlineShape(graphics);
		Rectangle rec = getBounds();
		//去掉纹理
		graphics.setAntialias(SWT.ON);
		graphics.drawRectangle(rec.x, rec.y, rec.width, 25);

	}
	public void setText(String text)
	{
		
		this.text=text;
		repaint();
	}
	@Override
	public String getText()
	{
		// TODO Auto-generated method stub
		return null;
	}
}