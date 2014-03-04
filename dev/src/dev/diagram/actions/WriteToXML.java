package dev.diagram.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.sql.BLOB;
import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.diagram.model.ContentsModel;
import dev.diagram.ui.GenerateXml;
import dev.util.CommonUtil;

/**
 * ����ͼ���õ���Ϣд��xml
 * 
 * @author ľľ
 * 
 */
public class WriteToXML
{
	public static Boolean writeToXML(ContentsModel contentsModel)
	{
		// xml�ļ������ƣ����а���·����Ϣ���򿪱༭����ʱ������
		String fileName = contentsModel.fileName;
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		// writeTfmXml������ͼģ����Ϣд��fileName��
		try
		{
			GenerateXml.writeTfmXml(fileName, contentsModel.getTfmBean());
			// �����ݿ�����ӣ�xml�������ݿ��T_tfm�е�tfmxml�ֶ�
			dbConnectImpl.openConn(CommonUtil.initPs(contentsModel.projectId));
			dbConnectImpl
					.retrive("update t_tfm set tfmxml=empty_blob() where tfmid = "
							+ contentsModel.diagramId);
			ResultSet rs = dbConnectImpl
					.retrive("select tfmxml from t_tfm where tfmid = "
							+ contentsModel.diagramId + " for update");
			fileName = contentsModel.fileName;
			if (rs.next())
			{
				// �õ����ݿ���еĴ洢xml�Ķ������ֶ�
				BLOB blob = (BLOB) rs.getBlob(1);
				// �����xml�ļ�
				File file = new File(fileName);
				// ���ݿ���еĴ洢xml�Ķ������ֶ���Ϊ�����
				OutputStream outStream = blob.getBinaryOutputStream();
				// xml�ļ���Ϊ������
				FileInputStream fin = new FileInputStream(file);
				// ��������С
				byte[] b = new byte[blob.getBufferSize()];
				int len = 0;
				// д�����ݿ��ֶ�
				while ((len = fin.read(b)) != -1)
					outStream.write(b, 0, len);
				// �ر�������
				fin.close();
				// ˢ�������
				outStream.flush();
				// ������ر�
				outStream.close();
				// �ύ���ݿ�
				dbConnectImpl.commit();
			}

		} catch (IOException|SQLException e)
		{
			e.printStackTrace();
		}
		return true;
	}
}
