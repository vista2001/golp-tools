package dev.diagram.figures;

import org.eclipse.draw2d.IFigure;

/**
 * 图形的接口，用于修改图形的名称
 * 
 * @author 木木
 * 
 */
public interface MyFigure extends IFigure {
	void setText(String text);

	String getText();
}
