package dev.diagram.helper;

import dev.diagram.model.CommonModel;
import dev.diagram.model.ContentsModel;

/**
 * Ψһ�Լ�飬����ʼ�ͽ��ܽڵ㣨�飩ֻ���Ҹ���һ��
 * 
 * @author ľľ
 * 
 */
public class UniqueCheck
{
	public static boolean check(CommonModel child, ContentsModel contentsModel)
	{
		// �ж��Ƿ��ǿ�ʼ������ڵ�
		if (child.getTypeId() == 4 || child.getTypeId() == 5)
		{
			// ɨ�赱ǰͼ�����нڵ㣬�Ƿ��Ѿ������������ڵ�
			for (int i = 1; i < contentsModel.getChildren().size(); i++)
			{
				if (contentsModel.getChildren().get(i) instanceof CommonModel)
				{
					if (child.getTypeId() == ((CommonModel) contentsModel
							.getChildren().get(i)).getTypeId())
					{
						// MessageDialog dig = new MessageDialog(UIPlugin
						// .getDefault().getWorkbench().getDisplay()
						// .getActiveShell(), "����", null,
						// ((CommonModel) child).getText()
						// + "����������ֻ��һ��,���Ѿ����ڣ���ѡ����������Ŀ� ~_~",
						// MessageDialog.WARNING, new String[] { "ȷ��" }, 0);
						// dig.open();
						return false;
					}
				}
			}
		}
		return true;
	}

}
