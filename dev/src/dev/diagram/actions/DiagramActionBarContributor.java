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
	 * ����action
	 */
	@Override
	protected void buildActions()
	{
		// ����acion
		addRetargetAction(new UndoRetargetAction());
		// �ظ�action
		addRetargetAction(new RedoRetargetAction());
		// ɾ��action
		addRetargetAction(new DeleteRetargetAction());
		// �Զ�����action
		addRetargetAction(new LabelRetargetAction(GraphAutoLayout.ID, null));
		// �Ŵ�action
		addRetargetAction(new ZoomInRetargetAction());
		// ��Сaction
		addRetargetAction(new ZoomOutRetargetAction());
		// д�����ݿ�action
		addRetargetAction(new LabelRetargetAction(GenerateCodeAction.ID, null));
	}

	@Override
	protected void declareGlobalActionKeys()
	{

		addGlobalActionKey(GraphAutoLayout.ID);
		addGlobalActionKey(GenerateCodeAction.ID);
	}

	/**
	 * action���빤������
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
