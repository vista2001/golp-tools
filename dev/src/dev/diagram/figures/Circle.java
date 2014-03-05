package dev.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * ��ʼģ�͵�ͼ��
 * 
 * @author ľľ
 * 
 */
public class Circle extends Shape implements MyFigure {
	// ͼ�ε�����
	private String text = "��ʼ";

	/**
	 * ͼ�ε����Ϊһ��Բ
	 */
	@Override
	protected void fillShape(Graphics graphics) {

		Rectangle rec = getBounds();
		int r = (rec.width > rec.height) ? rec.height : rec.width;
		graphics.fillOval(rec.x + rec.width / 2 - r / 2, rec.y, r, r);
		graphics.setBackgroundColor(ColorConstants.white);
		graphics.setLineStyle(SWT.LINE_SOLID);
	}

	// ͼ�ε�������Բ
	@Override
	protected void outlineShape(Graphics graphics) {

		Rectangle rec = getBounds();
		int r = (rec.width > rec.height) ? rec.height : rec.width;
		// graphics.setLineStyle(SWT.LINE_SOLID);
		graphics.setAntialias(SWT.ON);
		graphics.drawOval(rec.x + rec.width / 2 - r / 2, rec.y, r, r);
		graphics.drawText(text, rec.getCenter().getTranslated(-12, -10));

	}

	public void setText(String text) {

		this.text = text;
		repaint();
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return text;
	}
}
