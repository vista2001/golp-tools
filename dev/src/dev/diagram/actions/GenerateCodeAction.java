package dev.diagram.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import dev.Application;
import dev.diagram.model.ContentsModel;

/**
 * ���ɲ������ݿ�Ķ��������ڲ���ǰ����У��
 */
public class GenerateCodeAction extends Action
{
	//����Ψһ��ʶID
	//����XML�����ݿ��ԭ����contentsModel
	public final static String ID = "RUNACTION";
	private ContentsModel contentModel;

	public GenerateCodeAction(ContentsModel contentModel)
	{
		//����action��Id
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
		// ����ǰ�����жϣ�ͼ����Ϣ�Ƿ�����
		if (contentModel.isComplete())
		{
			//������xml����
			//Ȼ��contentsModel�������Ϣ�������ݿ�
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
