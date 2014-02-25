package dev.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.swt.SWT;
/**
 * Aop模型的图形
 * @author 木木
 *
 */
public class IRectangleFigure extends RectangleFigure implements MyFigure
{
	//图形名称
	private String text = "aop";
	@Override
	protected void outlineShape(Graphics graphics)
	{
		
		graphics.setAntialias(SWT.ON);
		super.outlineShape(graphics);
		//名称显示
		graphics.drawText(text, getBounds().getCenter()
				.getTranslated(-12, -10));
	}

	@Override
	public void setText(String text)
	{
		
		this.text=text;
		repaint();
	}

	@Override
	public String getText()
	{
		return text;
	}
}
