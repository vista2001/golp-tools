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
 * ������contentModel������д�����ݿ��
 */
public class WriteToDB {
	public static Boolean writeToDB(ContentsModel contentsModel) {
		// ������ģ���л������ͼ��JavaBean
		TfmBean tfmBean = contentsModel.getTfmBean();
		// ���ݿ������
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		// �������ݿ�ǰ��ɾ����ǰ����ͼ��������Ϣ
		String presql = "delete from T_TFM where tfmid ="
				+ contentsModel.diagramId;
		try {
			dbConnectImpl.openConn(CommonUtil.initPs(contentsModel.projectId));
			dbConnectImpl.setAutoCommit(false);
			dbConnectImpl.retrive(presql);

		} catch (SQLException e1) {

			e1.printStackTrace();
		}
		// ����t_tfm��һ������ͼ��Ϣ
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
			// �쳣��Ϣ����t_exceptions��
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
			// ������Ϣ����T_compersations��
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
			// ����Ϣ����T_Blockoftfm��
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
				// ��չ�ڵ���Ϣ����T_extendofblock��
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
			// ����Ϣ����T_edgeOfTFM
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
			// �ر����ݿ������
			// �����Ի�����ʾ�������
			dbConnectImpl.closeConn();
			MessageDialog.openInformation(PlatformUI.getWorkbench()
					.getDisplay().getActiveShell(), "��Ϣ�Ի���", "��������ɣ�");
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
			return false;
		}

		try {
			// �����ݿ�����ӣ�xml�������ݿ��T_tfm�е�tfmxml�ֶ�
			dbConnectImpl.openConn(CommonUtil.initPs(contentsModel.projectId));
			dbConnectImpl
					.retrive("update t_tfm set tfmxml=empty_blob() where tfmid = "
							+ contentsModel.diagramId);
			ResultSet rs = dbConnectImpl
					.retrive("select tfmxml from t_tfm where tfmid = "
							+ contentsModel.diagramId + " for update");
			String fileName = contentsModel.fileName;
			if (rs.next()) {
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
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}

		return true;
	}

}
