package dev.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * 开始模型的图形
 * 
 * @author 木木
 * 
 */
public class Circle extends Shape implements MyFigure {
	// 图形的名称
	private String text = "开始";

	/**
	 * 图形的填充为一个圆
	 */
	@Override
	protected void fillShape(Graphics graphics) {

		Rectangle rec = getBounds();
		int r = (rec.width > rec.height) ? rec.height : rec.width;
		graphics.fillOval(rec.x + rec.width / 2 - r / 2, rec.y, r, r);
		graphics.setBackgroundColor(ColorConstants.white);
		graphics.setLineStyle(SWT.LINE_SOLID);
	}

	// 图形的轮廓是圆
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
