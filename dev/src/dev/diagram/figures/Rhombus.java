package dev.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * 条件模型的图形显示
 * 
 * @author 木木
 * 
 */
public class Rhombus extends Shape implements MyFigure {
	/**
	 * 图形填充为菱形
	 */
	@Override
	protected void fillShape(Graphics graphics) {

		Rectangle rec = getBounds().getCopy();
		PointList pl = new PointList(4);
		pl.addPoint(rec.x + rec.width / 2, rec.y);
		pl.addPoint(rec.x, rec.y + rec.height / 2);
		pl.addPoint(rec.x + rec.width / 2, rec.y + rec.height);
		pl.addPoint(rec.x + rec.width, rec.y + rec.height / 2);
		graphics.fillPolygon(pl);
		graphics.setBackgroundColor(ColorConstants.white);
	}

	/**
	 * 图形轮廓
	 */
	@Override
	protected void outlineShape(Graphics graphics) {

		Rectangle rec = getBounds().getCopy();
		graphics.setAntialias(SWT.ON);
		PointList pl = new PointList(4);
		pl.addPoint(rec.x + rec.width / 2, rec.y);
		pl.addPoint(rec.x, rec.y + rec.height / 2);
		pl.addPoint(rec.x + rec.width / 2, rec.y + rec.height);
		pl.addPoint(rec.x + rec.width, rec.y + rec.height / 2);
		graphics.drawPolygon(pl);
		graphics.drawText(text, rec.getCenter().getTranslated(-12, -10));
	}

	// 图形名称
	private String text = "";

	@Override
	public void setText(String text) {

		this.text = text;
		repaint();
	}

	@Override
	public String getText() {
		return text;
	}
}
