package dev.diagram.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import dev.diagram.model.AbstractConnectionModel;
import dev.diagram.model.ContentsModel;
import dev.diagram.model.ElementModel;

/**
 * ����������
 * 
 * @author ľľ
 * 
 */
public class PartFactory implements EditPartFactory {
	/**
	 * ģ������ƹ���
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		// �õ�������
		EditPart part = getPartForElement(model);
		// ����ģ���������
		part.setModel(model);
		return part;
	}

	/**
	 * ���ݲ�ͬ��ģ�ͷ��ز�ͬ�Ŀ�����
	 * 
	 * @param modelElement
	 * @return
	 */
	private EditPart getPartForElement(Object modelElement) {
		// ����ģ�Ϳ�����
		if (modelElement instanceof ElementModel)
			return new ElementEditPart();
		// ���ݿ�����
		else if (modelElement instanceof ContentsModel)
			return new ContentsModelPart();
		// �߿�����
		else if (modelElement instanceof AbstractConnectionModel)
			return new ConnectionEditPart();

		throw new RuntimeException("Can't create part for model element: "
				+ ((modelElement != null) ? modelElement.getClass().getName()
						: "null"));
	}

}
