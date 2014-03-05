package dev.diagram.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

/**
 * ֱ�ӱ༭��λ��
 * 
 * @author ľľ
 * 
 */
public class CustomCellEditLocator implements CellEditorLocator {
	// ֱ�ӱ༭ͼ�ζ���
	private IFigure figure;

	public CustomCellEditLocator(IFigure f) {
		figure = f;
	}

	/**
	 * ֱ�ӱ༭��λ������λ�úʹ�С
	 */
	@Override
	public void relocate(CellEditor celleditor) {

		Text text = (Text) celleditor.getControl();

		Rectangle rect = figure.getBounds().getCopy();
		text.setBounds(rect.x, rect.y, rect.width, rect.height);
	}

}
