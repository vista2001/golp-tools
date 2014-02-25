package dev.diagram.parts;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Text;

import dev.diagram.model.ElementModel;

/**
 * 直接编辑管理类
 * 
 * @author 木木
 * 
 */
public class CustomDirectEditManager extends DirectEditManager
{
	// 直接编辑的对象模型，目的是得到当前文本
	private ElementModel model;

	// 初始化模型
	public CustomDirectEditManager(GraphicalEditPart source,
			Class<?> editorType, CellEditorLocator locator)
	{
		super(source, editorType, locator);
		model = (ElementModel) source.getModel();
	}

	/**
	 * 初始化直接编辑的初始值
	 */
	@Override
	protected void initCellEditor()
	{

		getCellEditor().setValue(model.getText());

		Text text = (Text) getCellEditor().getControl();
		text.selectAll();
	}

}
