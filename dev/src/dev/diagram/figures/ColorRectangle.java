package dev.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
/**
 * �쳣�Ͳ���ģ�͵�ͼ��
 * @author ľľ
 *
 */
public class ColorRectangle extends RectangleFigure implements MyFigure
{
	//ͼ�ε�����
	private String text="�쳣�Ͳ���";
	/**
	 * ͼ�����Ϊ����
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
	 * ͼ������Ϊ����
	 */
	@Override
	protected void outlineShape(Graphics graphics)
	{
		
		super.outlineShape(graphics);
		Rectangle rec = getBounds();
		//ȥ������
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