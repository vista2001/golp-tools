package dev.diagram.figures;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.SWT;

public class IEllipse extends Ellipse implements MyFigure {
	// ͼ������
	private String text = "";

	@Override
	protected void outlineShape(Graphics graphics) {

		graphics.setAntialias(SWT.ON);
		super.outlineShape(graphics);
		// ��ʾ����
		graphics.drawText(text, getBounds().getCenter().getTranslated(-12, -10));
	}

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
