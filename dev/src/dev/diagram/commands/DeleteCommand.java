package dev.diagram.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import dev.diagram.model.AbstractConnectionModel;
import dev.diagram.model.CommonModel;
import dev.diagram.model.ContentsModel;
import dev.diagram.model.ElementModel;
import dev.diagram.model.ExAndComModel;

/**
 * 节点删除命令
 * 
 * @author 木木
 * 
 */
public class DeleteCommand extends Command {
	private ContentsModel parentModel;
	private ElementModel elementModel;
	private List<AbstractConnectionModel> sourceConnections = new ArrayList<AbstractConnectionModel>();
	private List<AbstractConnectionModel> targetConnections = new ArrayList<AbstractConnectionModel>();

	public void execute() {
		// 在删除一个模型对象前，这个模型对象连接的source和target被记录
		sourceConnections.addAll(elementModel.getModelSourceConnections());
		targetConnections.addAll(elementModel.getModelTargetConnections());
		// 删除该模型对象对应的source
		for (int i = 0; i < sourceConnections.size(); i++) {
			AbstractConnectionModel model = (AbstractConnectionModel) sourceConnections
					.get(i);
			model.detachSource();
			model.detachTarget();
		}

		// 删除该模型对象对应的target
		for (int i = 0; i < targetConnections.size(); i++) {
			AbstractConnectionModel model = (AbstractConnectionModel) targetConnections
					.get(i);
			model.detachSource();
			model.detachTarget();
		}

		// 模型被删除
		parentModel.removeChild(elementModel);
	}

	public void setContentsModel(Object model) {
		parentModel = (ContentsModel) model;
	}

	public void setModel(Object model) {
		if (CommonModel.class.isInstance(model))
			elementModel = (CommonModel) model;
		elementModel = (ElementModel) model;
	}

	// Override
	public void undo() {
		// undo
		parentModel.addChild(elementModel);

		// --------- undo the connection -------------
		for (int i = 0; i < sourceConnections.size(); i++) {
			AbstractConnectionModel model = (AbstractConnectionModel) sourceConnections
					.get(i);
			model.attachSource();
			model.attachTarget();
		}
		for (int i = 0; i < targetConnections.size(); i++) {
			AbstractConnectionModel model = (AbstractConnectionModel) targetConnections
					.get(i);
			model.attachSource();
			model.attachTarget();
		}
		// 清楚纪录，这些记录用于恢复
		sourceConnections.clear();
		targetConnections.clear();
		// --------- undo the connection -------------
	}

	@Override
	public boolean canExecute() {

		if (elementModel instanceof ExAndComModel)
			return false;
		return super.canExecute();
	}

}
