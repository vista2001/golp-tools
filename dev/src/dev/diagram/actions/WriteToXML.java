package dev.diagram.actions;

import java.io.IOException;

import dev.diagram.model.ContentsModel;
import dev.diagram.ui.GenerateXml;
/**
 * ����ͼ���õ���Ϣд��xml
 * @author ľľ
 *
 */
public class WriteToXML
{
	public static Boolean writeToXML(ContentsModel contentsModel)
	{
		//xml�ļ������ƣ����а���·����Ϣ���򿪱༭����ʱ������
		String fileName = contentsModel.fileName;
		//writeTfmXml������ͼģ����Ϣд��fileName��
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
