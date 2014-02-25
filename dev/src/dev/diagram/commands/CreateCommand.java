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
 * 创建新模型命令
 * 
 * @author 木木
 * 
 */
public class CreateCommand extends Command
{
	// 模型的父模型
	private ContentsModel contentsModel;
	// 要创建的模型
	private CommonModel commonModel;

	public void execute()
	{
		Dialog dialog = null;
		// 根据模型的类型，打开配置模型对话框
		switch (commonModel.getTypeId())
		{
		// 条件对话框
		case 0:
			dialog = new ConditionBlockConfigurDialong(PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getShell(),
					commonModel, contentsModel);
			break;
		// 返回，AOP对话框
		case 1:
		case 2:
			dialog = new BlockConfigureDialog(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), commonModel,
					contentsModel);
			break;
		// TFM对话框
		case 3:
			dialog = new TfmCongigureDialog(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), commonModel,
					contentsModel);
			break;
		// 开始，结束对话框
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
	 * 设置父模型
	 * 
	 * @param model
	 */
	public void setContentsModel(Object model)
	{
		contentsModel = (ContentsModel) model;
	}

	/**
	 * 设置模型
	 * 
	 * @param model
	 */
	public void setModel(Object model)
	{
		commonModel = (CommonModel) model;

	}

	/**
	 * 撤销
	 */
	public void undo()
	{
		contentsModel.removeChild(commonModel);
	}

	/**
	 * 创建前判断是否可以执行，具体内容查看UniqueCheck类
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
