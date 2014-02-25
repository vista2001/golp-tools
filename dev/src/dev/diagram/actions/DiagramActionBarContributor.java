package dev.diagram.actions;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.LabelRetargetAction;

public class DiagramActionBarContributor extends ActionBarContributor
{
	/**
	 * 创建action
	 */
	@Override
	protected void buildActions()
	{
		// 撤销acion
		addRetargetAction(new UndoRetargetAction());
		// 回复action
		addRetargetAction(new RedoRetargetAction());
		// 删除action
		addRetargetAction(new DeleteRetargetAction());
		// 自动布局action
		addRetargetAction(new LabelRetargetAction(GraphAutoLayout.ID, null));
		// 放大action
		addRetargetAction(new ZoomInRetargetAction());
		// 缩小action
		addRetargetAction(new ZoomOutRetargetAction());
		// 写入数据库action
		addRetargetAction(new LabelRetargetAction(GenerateCodeAction.ID, null));
	}

	@Override
	protected void declareGlobalActionKeys()
	{

		addGlobalActionKey(GraphAutoLayout.ID);
		addGlobalActionKey(GenerateCodeAction.ID);
	}

	/**
	 * action加入工具栏中
	 */
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager)
	{
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
		toolBarManager.add(new ZoomComboContributionItem(getPage()));
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(GraphAutoLayout.ID));
		toolBarManager.add(getAction(GenerateCodeAction.ID));
	}

}
