package dev.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.swt.SWT;
/**
 * Aopģ�͵�ͼ��
 * @author ľľ
 *
 */
public class IRectangleFigure extends RectangleFigure implements MyFigure
{
	//ͼ������
	private String text = "aop";
	@Override
	protected void outlineShape(Graphics graphics)
	{
		
		graphics.setAntialias(SWT.ON);
		super.outlineShape(graphics);
		//������ʾ
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
