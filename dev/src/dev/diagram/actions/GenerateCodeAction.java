package dev.diagram.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import dev.Application;
import dev.diagram.model.ContentsModel;

/**
 * 生成插入数据库的动作，并在插入前进行校验
 */
public class GenerateCodeAction extends Action
{
	//动作唯一标识ID
	//生成XML和数据库的原数据contentsModel
	public final static String ID = "RUNACTION";
	private ContentsModel contentModel;

	public GenerateCodeAction(ContentsModel contentModel)
	{
		//设置action的Id
		setId(GenerateCodeAction.ID);
		this.contentModel = contentModel;
		ImageDescriptor descriptor = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						"icons/logo.gif");
		setImageDescriptor(descriptor);
	}

	@Override
	public String getId()
	{
		return ID;
	}

	@Override
	public void run()
	{
		// 插入前进行判断，图的信息是否完整
		if (contentModel.isComplete())
		{
			//先生成xml代码
			//然后将contentsModel里面的信息插入数据库
			WriteToXML.writeToXML(contentModel);
			WriteToDB.writeToDB(contentModel);
		}
	}

	@Override
	public void setImageDescriptor(ImageDescriptor newImage)
	{

		super.setImageDescriptor(newImage);
	}

	@Override
	public void setToolTipText(String toolTipText)
	{

		super.setToolTipText(toolTipText);
	}

}
