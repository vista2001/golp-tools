package dev.diagram.actions;

import java.io.IOException;

import dev.diagram.model.ContentsModel;
import dev.diagram.ui.GenerateXml;
/**
 * 流程图配置的信息写入xml
 * @author 木木
 *
 */
public class WriteToXML
{
	public static Boolean writeToXML(ContentsModel contentsModel)
	{
		//xml文件的名称，其中包括路径信息，打开编辑器的时候生成
		String fileName = contentsModel.fileName;
		//writeTfmXml将流程图模型信息写入fileName中
		try
		{
			GenerateXml.writeTfmXml(fileName, contentsModel.getTfmBean());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
