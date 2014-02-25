package dev.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * ����ģ�Ͷ�Ӧ��ͼ��
 * 
 * @author ľľ
 * 
 */
public class Circle_End extends Circle implements MyFigure
{
	// ͼ������
	private String text = "����";

	/**
	 * ͼ�����Ϊ����ͬ��Բ
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
	 * ͼ��������Բ
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
