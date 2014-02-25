package dev.diagram.parts;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.widgets.Text;

import dev.diagram.model.ElementModel;

/**
 * ֱ�ӱ༭������
 * 
 * @author ľľ
 * 
 */
public class CustomDirectEditManager extends DirectEditManager
{
	// ֱ�ӱ༭�Ķ���ģ�ͣ�Ŀ���ǵõ���ǰ�ı�
	private ElementModel model;

	// ��ʼ��ģ��
	public CustomDirectEditManager(GraphicalEditPart source,
			Class<?> editorType, CellEditorLocator locator)
	{
		super(source, editorType, locator);
		model = (ElementModel) source.getModel();
	}

	/**
	 * ��ʼ��ֱ�ӱ༭�ĳ�ʼֵ
	 */
	@Override
	protected void initCellEditor()
	{

		getCellEditor().setValue(model.getText());

		Text text = (Text) getCellEditor().getControl();
		text.selectAll();
	}

}
