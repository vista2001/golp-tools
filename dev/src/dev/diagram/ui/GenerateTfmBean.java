package dev.diagram.ui;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;
import org.eclipse.draw2d.geometry.Rectangle;

import dev.diagram.beans.TfmBean;
import dev.diagram.beans.TfmBlock;
import dev.diagram.beans.TfmEdge;
import dev.diagram.model.AopModel;
import dev.diagram.model.CommonModel;
import dev.diagram.model.ConditionModel;
import dev.diagram.model.ContentsModel;
import dev.diagram.model.EndModel;
import dev.diagram.model.LineConnectionModel;
import dev.diagram.model.ReturnModel;
import dev.diagram.model.StartModel;
import dev.diagram.model.TfmModel;

/**
 * ��������ͼ��JavaBean�࣬����ģ��
 * 
 * @author ľľ
 * 
 */
public class GenerateTfmBean {
	/**
	 * ��xml�л������ͼ��JavaBean������JavaBean��������ͼ����д�뵽�����ģ�Ͳ���contentsModel
	 * 
	 * @param contentsModel
	 * @param fileName
	 * @throws DocumentException
	 */
	public static void BeanToModel(ContentsModel contentsModel, String fileName)
			throws DocumentException {
		final TfmBean tfmBean;
		// ��xml�ļ��ж���JavaBean��������ͼ
		tfmBean = GenerateXml.readTfmXml(fileName);
		// ģ��������ͼ�Ĳ�������
		contentsModel.getTfmBean().setTfmCompersationList(
				tfmBean.getTfmCompersationList());
		// ģ��������ͼ���쳣����
		contentsModel.getTfmBean().setTfmExceptionList(
				tfmBean.getTfmExceptionList());
		// ģ��������ͼ������
		contentsModel.getTfmBean().setTfmType(tfmBean.getTfmType());
		// ģ��������ͼ������
		contentsModel.getTfmBean().setTfmdesc(tfmBean.getTfmdesc());
		// ģ��������ͼ�ı�ţ�Id��
		contentsModel.getTfmBean().setTfmId(tfmBean.getTfmId());
		// �ڵ��ź;����ģ�͵Ĺ���
		Map<String, CommonModel> mapTfmBlock = new HashMap<String, CommonModel>();
		CommonModel model;
		// ���ݽڵ�������½�ģ��
		for (TfmBlock tfmBlock : tfmBean.getTfmBlockList()) {
			switch (tfmBlock.getNodeType()) {
			// ����ģ��
			case "0":
				model = new ConditionModel();
				break;
			// ����ģ��
			case "1":
				model = new ReturnModel();
				break;
			// AOPģ��
			case "2":
				model = new AopModel();
				break;
			// TFMģ��
			case "3":
				model = new TfmModel();
				break;
			// ��ʼģ��
			case "4":
				model = new StartModel();
				break;
			// ����ģ��
			case "5":
				model = new EndModel();
				break;
			default:
				continue;
			}
			// x����
			int x = Integer.parseInt(tfmBlock.getLocationX());
			// y����
			int y = Integer.parseInt(tfmBlock.getLocationY());
			// ��
			int width = Integer.parseInt(tfmBlock.getWidth());
			// ��
			int height = Integer.parseInt(tfmBlock.getHeight());
			// ����ģ�͵����꣨λ�úʹ�С��
			model.setConstraint(new Rectangle(x, y, width, height));
			// ����ģ��������ͼ�ڵ���Ϣ
			model.setTfmBlock(tfmBlock);
			// �ڵ��ź�ģ�͹���
			mapTfmBlock.put(tfmBlock.getNodeId(), model);
			// ��ģ�ͼ��������ģ��contentsModel��
			model.setContentModel(contentsModel);
			contentsModel.addChild(model);

		}
		for (TfmEdge tfmEdge : tfmBean.getTfmEdgesList()) {
			// �½���ʵ��
			LineConnectionModel l = new LineConnectionModel();
			// ���ñߵĿ�ʼģ��
			l.setSource(mapTfmBlock.get(tfmEdge.getSourceid()));
			// ���ñߵĽ���ģ��
			l.setTarget(mapTfmBlock.get(tfmEdge.getTargetid()));
			// ���뿪ʼģ������
			l.attachSource();
			// �������ģ������
			l.attachTarget();
			// ���ñ�Ȩֵ
			l.setWeight(tfmEdge.getWeight());
		}
	}
}
