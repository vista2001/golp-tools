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
 * 流程图配置的信息写入xml
 * 
 * @author 木木
 * 
 */
public class WriteToXML
{
	public static Boolean writeToXML(ContentsModel contentsModel)
	{
		// xml文件的名称，其中包括路径信息，打开编辑器的时候生成
		String fileName = contentsModel.fileName;
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		// writeTfmXml将流程图模型信息写入fileName中
		try
		{
			GenerateXml.writeTfmXml(fileName, contentsModel.getTfmBean());
			// 打开数据库的连接，xml插入数据库表T_tfm中的tfmxml字段
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
				// 得到数据库表中的存储xml的二进制字段
				BLOB blob = (BLOB) rs.getBlob(1);
				// 打开外存xml文件
				File file = new File(fileName);
				// 数据库表中的存储xml的二进制字段作为输出流
				OutputStream outStream = blob.getBinaryOutputStream();
				// xml文件作为输入流
				FileInputStream fin = new FileInputStream(file);
				// 缓存区大小
				byte[] b = new byte[blob.getBufferSize()];
				int len = 0;
				// 写入数据库字段
				while ((len = fin.read(b)) != -1)
					outStream.write(b, 0, len);
				// 关闭输入流
				fin.close();
				// 刷新输出流
				outStream.flush();
				// 输出流关闭
				outStream.close();
				// 提交数据库
				dbConnectImpl.commit();
			}

		} catch (IOException|SQLException e)
		{
			e.printStackTrace();
		}
		return true;
	}
}
