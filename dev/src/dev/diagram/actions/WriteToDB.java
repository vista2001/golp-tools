package dev.diagram.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.sql.BLOB;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.diagram.beans.TfmBean;
import dev.diagram.beans.TfmBlock;
import dev.diagram.beans.TfmCompersation;
import dev.diagram.beans.TfmEdge;
import dev.diagram.beans.TfmException;
import dev.diagram.beans.TfmExtendAop;
import dev.diagram.model.ContentsModel;
import dev.util.CommonUtil;
import dev.util.DevLogger;

/**
 * 将参数contentModel的内容写入数据库表
 */
public class WriteToDB {
	public static Boolean writeToDB(ContentsModel contentsModel) {
		// 从内容模型中获得流程图的JavaBean
		TfmBean tfmBean = contentsModel.getTfmBean();
		// 数据库的连接
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		// 插入数据库前先删除当前流程图的所有信息
		String presql = "delete from T_TFM where tfmid ="
				+ contentsModel.diagramId;
		try {
			dbConnectImpl.openConn(CommonUtil.initPs(contentsModel.projectId));
			dbConnectImpl.setAutoCommit(false);
			dbConnectImpl.retrive(presql);

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		// 插入t_tfm表一条流程图信息
		String sql = "insert into t_tfm(tfmid,tfmname,tfmdesc,type,tradeid) values("
				+ contentsModel.diagramId
				+ ",'"
				+ tfmBean.getTfmName()
				+ "','"
				+ tfmBean.getTfmdesc()
				+ "',"
				+ tfmBean.getTfmType()
				+ ","
				+ Integer.parseInt(tfmBean.getTradeId()) + ")";
		try {

			dbConnectImpl.retrive(sql);
			// 异常信息插入t_exceptions表
			sql = "insert into t_exceptions values(?,?,?,?)";
			for (TfmException curr : tfmBean.getTfmExceptionList()) {
				dbConnectImpl.setPrepareSql(sql);
				dbConnectImpl.setPreparedInt(1,
						Integer.parseInt(curr.getTfmId()));
				dbConnectImpl.setPreparedInt(2,
						Integer.parseInt(curr.getSeqNo()));
				dbConnectImpl.setPreparedString(3, curr.getFuncname());
				dbConnectImpl.setPreparedInt(4,
						Integer.parseInt(curr.getDllId()));
				dbConnectImpl.executeExceptPreparedQuery();
			}
			// 补偿信息插入T_compersations表
			sql = "insert into t_compersations values(?,?,?,?)";
			for (TfmCompersation curr : tfmBean.getTfmCompersationList()) {
				dbConnectImpl.setPrepareSql(sql);
				dbConnectImpl.setPreparedInt(1,
						Integer.parseInt(curr.getTfmId()));
				dbConnectImpl.setPreparedInt(2,
						Integer.parseInt(curr.getSeqNo()));
				dbConnectImpl.setPreparedString(3, curr.getFuncname());
				dbConnectImpl.setPreparedInt(4,
						Integer.parseInt(curr.getDllId()));
				dbConnectImpl.executeExceptPreparedQuery();
			}
			// 块信息插入T_Blockoftfm表
			sql = "insert into T_blockoftfm values(?,?,?,?,?,?,?)";
			for (TfmBlock curr : tfmBean.getTfmBlockList()) {
				dbConnectImpl.setPrepareSql(sql);
				dbConnectImpl.setPreparedInt(1,
						Integer.parseInt(curr.getTfmId()));
				dbConnectImpl.setPreparedInt(2,
						Integer.parseInt(curr.getNodeId()));
				dbConnectImpl.setPreparedString(3, curr.getNodeType());

				dbConnectImpl.setPreparedInt(5,
						Integer.parseInt(curr.getNestedtfm()));
				dbConnectImpl.setPreparedString(6, curr.getCondition());
				if (!curr.getDllId().equals("")) {
					dbConnectImpl.setPreparedString(4, curr.getAopName());
					dbConnectImpl.setPreparedInt(7,
							Integer.parseInt(curr.getDllId()));
				} else {
					dbConnectImpl.setPreparedString(4, "-1");
					dbConnectImpl.setPreparedInt(7, -1);
				}
				dbConnectImpl.executeExceptPreparedQuery();
				// 扩展节点信息插入T_extendofblock表
				String _sql = "insert into T_extendofblock values(?,?,?,?,?)";
				for (TfmExtendAop temp : curr.getTfmExtendAopsList()) {
					dbConnectImpl.setPrepareSql(_sql);
					dbConnectImpl.setPreparedInt(1,
							Integer.parseInt(curr.getTfmId()));
					dbConnectImpl.setPreparedInt(2,
							Integer.parseInt(temp.getNodeid()));
					dbConnectImpl.setPreparedInt(3,
							Integer.parseInt(temp.getSeqNo()));
					dbConnectImpl.setPreparedString(4, temp.getFuncname());
					dbConnectImpl.setPreparedInt(5,
							Integer.parseInt(temp.getDllId()));
					dbConnectImpl.executeExceptPreparedQuery();
				}
			}
			// 边信息插入T_edgeOfTFM
			String _sql = "insert into T_edgeOfTFM values(?,?,?,?)";
			for (TfmEdge curr : tfmBean.getTfmEdgesList()) {
				dbConnectImpl.setPrepareSql(_sql);
				dbConnectImpl.setPreparedInt(1,
						Integer.parseInt(curr.getTfmId()));
				dbConnectImpl.setPreparedInt(2,
						Integer.parseInt(curr.getSourceid()));
				dbConnectImpl.setPreparedInt(3,
						Integer.parseInt(curr.getTargetid()));
				dbConnectImpl.setPreparedInt(4,
						Integer.parseInt(curr.getWeight()));
				dbConnectImpl.executeExceptPreparedQuery();
			}
			// 关闭数据库的连接
			// 弹出对话框提示插入完成
			dbConnectImpl.closeConn();
			MessageDialog.openInformation(PlatformUI.getWorkbench()
					.getDisplay().getActiveShell(), "消息对话框", "已生成完成！");
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
			return false;
		}

		try {
			// 打开数据库的连接，xml插入数据库表T_tfm中的tfmxml字段
			dbConnectImpl.openConn(CommonUtil.initPs(contentsModel.projectId));
			dbConnectImpl
					.retrive("update t_tfm set tfmxml=empty_blob() where tfmid = "
							+ contentsModel.diagramId);
			ResultSet rs = dbConnectImpl
					.retrive("select tfmxml from t_tfm where tfmid = "
							+ contentsModel.diagramId + " for update");
			String fileName = contentsModel.fileName;
			if (rs.next()) {
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
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}

		return true;
	}

}
