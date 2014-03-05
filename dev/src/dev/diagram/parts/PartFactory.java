package dev.diagram.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import dev.diagram.model.AbstractConnectionModel;
import dev.diagram.model.ContentsModel;
import dev.diagram.model.ElementModel;

/**
 * 控制器工厂
 * 
 * @author 木木
 * 
 */
public class PartFactory implements EditPartFactory {
	/**
	 * 模型与控制关联
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		// 得到控制器
		EditPart part = getPartForElement(model);
		// 关联模型与控制器
		part.setModel(model);
		return part;
	}

	/**
	 * 根据不同的模型返回不同的控制器
	 * 
	 * @param modelElement
	 * @return
	 */
	private EditPart getPartForElement(Object modelElement) {
		// 基本模型控制器
		if (modelElement instanceof ElementModel)
			return new ElementEditPart();
		// 内容控制器
		else if (modelElement instanceof ContentsModel)
			return new ContentsModelPart();
		// 边控制器
		else if (modelElement instanceof AbstractConnectionModel)
			return new ConnectionEditPart();

		throw new RuntimeException("Can't create part for model element: "
				+ ((modelElement != null) ? modelElement.getClass().getName()
						: "null"));
	}

}
