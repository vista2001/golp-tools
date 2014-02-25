package dev.diagram.helper;

import dev.diagram.model.CommonModel;
import dev.diagram.model.ContentsModel;

/**
 * 唯一性检查，即开始和接受节点（块）只有且各有一个
 * 
 * @author 木木
 * 
 */
public class UniqueCheck
{
	public static boolean check(CommonModel child, ContentsModel contentsModel)
	{
		// 判断是否是开始或结束节点
		if (child.getTypeId() == 4 || child.getTypeId() == 5)
		{
			// 扫描当前图的所有节点，是否已经存在这俩个节点
			for (int i = 1; i < contentsModel.getChildren().size(); i++)
			{
				if (contentsModel.getChildren().get(i) instanceof CommonModel)
				{
					if (child.getTypeId() == ((CommonModel) contentsModel
							.getChildren().get(i)).getTypeId())
					{
						// MessageDialog dig = new MessageDialog(UIPlugin
						// .getDefault().getWorkbench().getDisplay()
						// .getActiveShell(), "错误", null,
						// ((CommonModel) child).getText()
						// + "块容许有且只有一个,且已经存在，请选择添加其他的块 ~_~",
						// MessageDialog.WARNING, new String[] { "确认" }, 0);
						// dig.open();
						return false;
					}
				}
			}
		}
		return true;
	}

}
