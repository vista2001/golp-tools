package dev.diagram.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

/**
 * 直接编辑定位器
 * 
 * @author 木木
 * 
 */
public class CustomCellEditLocator implements CellEditorLocator {
	// 直接编辑图形对象
	private IFigure figure;

	public CustomCellEditLocator(IFigure f) {
		figure = f;
	}

	/**
	 * 直接编辑定位，包括位置和大小
	 */
	@Override
	public void relocate(CellEditor celleditor) {

		Text text = (Text) celleditor.getControl();

		Rectangle rect = figure.getBounds().getCopy();
		text.setBounds(rect.x, rect.y, rect.width, rect.height);
	}

}
