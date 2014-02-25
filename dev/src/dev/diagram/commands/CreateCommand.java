package dev.diagram.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.PlatformUI;

import dev.diagram.helper.UniqueCheck;
import dev.diagram.inputDialog.BlockConfigureDialog;
import dev.diagram.inputDialog.ConditionBlockConfigurDialong;
import dev.diagram.inputDialog.StartAndEndDialog;
import dev.diagram.inputDialog.TfmCongigureDialog;
import dev.diagram.model.CommonModel;
import dev.diagram.model.ContentsModel;
import dev.diagram.model.ElementModel;

/**
 * ������ģ������
 * 
 * @author ľľ
 * 
 */
public class CreateCommand extends Command
{
	// ģ�͵ĸ�ģ��
	private ContentsModel contentsModel;
	// Ҫ������ģ��
	private CommonModel commonModel;

	public void execute()
	{
		Dialog dialog = null;
		// ����ģ�͵����ͣ�������ģ�ͶԻ���
		switch (commonModel.getTypeId())
		{
		// �����Ի���
		case 0:
			dialog = new ConditionBlockConfigurDialong(PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getShell(),
					commonModel, contentsModel);
			break;
		// ���أ�AOP�Ի���
		case 1:
		case 2:
			dialog = new BlockConfigureDialog(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), commonModel,
					contentsModel);
			break;
		// TFM�Ի���
		case 3:
			dialog = new TfmCongigureDialog(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), commonModel,
					contentsModel);
			break;
		// ��ʼ�������Ի���
		case 4:
		case 5:
			dialog = new StartAndEndDialog(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), commonModel,
					contentsModel);
			break;

		default:
			break;
		}
		if (dialog.open() == IDialogConstants.OK_ID)
			contentsModel.addChild(commonModel);
		else
		{
			ElementModel.setNumber(commonModel.getId() - 1);
			return;
		}
	}

	/**
	 * ���ø�ģ��
	 * 
	 * @param model
	 */
	public void setContentsModel(Object model)
	{
		contentsModel = (ContentsModel) model;
	}

	/**
	 * ����ģ��
	 * 
	 * @param model
	 */
	public void setModel(Object model)
	{
		commonModel = (CommonModel) model;

	}

	/**
	 * ����
	 */
	public void undo()
	{
		contentsModel.removeChild(commonModel);
	}

	/**
	 * ����ǰ�ж��Ƿ����ִ�У��������ݲ鿴UniqueCheck��
	 */
	@Override
	public boolean canExecute()
	{

		if (commonModel instanceof CommonModel)
		{
			if (!UniqueCheck.check((CommonModel) commonModel, contentsModel))
			{
				ElementModel.setNumber(commonModel.getId() - 1);
				return false;
			}
		}
		commonModel.setContentModel(contentsModel);
		return true;
	}

}
