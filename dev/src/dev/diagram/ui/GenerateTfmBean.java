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
 * 根据流程图的JavaBean类，创建模型
 * 
 * @author 木木
 * 
 */
public class GenerateTfmBean {
	/**
	 * 从xml中获得流程图的JavaBean，根据JavaBean，将流程图内容写入到传入的模型参数contentsModel
	 * 
	 * @param contentsModel
	 * @param fileName
	 * @throws DocumentException
	 */
	public static void BeanToModel(ContentsModel contentsModel, String fileName)
			throws DocumentException {
		final TfmBean tfmBean;
		// 从xml文件中读入JavaBean，即流程图
		tfmBean = GenerateXml.readTfmXml(fileName);
		// 模型中流程图的补偿设置
		contentsModel.getTfmBean().setTfmCompersationList(
				tfmBean.getTfmCompersationList());
		// 模型中流程图的异常设置
		contentsModel.getTfmBean().setTfmExceptionList(
				tfmBean.getTfmExceptionList());
		// 模型中流程图的类型
		contentsModel.getTfmBean().setTfmType(tfmBean.getTfmType());
		// 模型中流程图的描述
		contentsModel.getTfmBean().setTfmdesc(tfmBean.getTfmdesc());
		// 模型中流程图的编号（Id）
		contentsModel.getTfmBean().setTfmId(tfmBean.getTfmId());
		// 节点编号和具体的模型的关联
		Map<String, CommonModel> mapTfmBlock = new HashMap<String, CommonModel>();
		CommonModel model;
		// 根据节点的类型新建模型
		for (TfmBlock tfmBlock : tfmBean.getTfmBlockList()) {
			switch (tfmBlock.getNodeType()) {
			// 条件模型
			case "0":
				model = new ConditionModel();
				break;
			// 返回模型
			case "1":
				model = new ReturnModel();
				break;
			// AOP模型
			case "2":
				model = new AopModel();
				break;
			// TFM模型
			case "3":
				model = new TfmModel();
				break;
			// 开始模型
			case "4":
				model = new StartModel();
				break;
			// 结束模型
			case "5":
				model = new EndModel();
				break;
			default:
				continue;
			}
			// x坐标
			int x = Integer.parseInt(tfmBlock.getLocationX());
			// y坐标
			int y = Integer.parseInt(tfmBlock.getLocationY());
			// 宽
			int width = Integer.parseInt(tfmBlock.getWidth());
			// 高
			int height = Integer.parseInt(tfmBlock.getHeight());
			// 设置模型的坐标（位置和大小）
			model.setConstraint(new Rectangle(x, y, width, height));
			// 设置模型中流程图节点信息
			model.setTfmBlock(tfmBlock);
			// 节点编号和模型关联
			mapTfmBlock.put(tfmBlock.getNodeId(), model);
			// 将模型加入的内容模型contentsModel中
			model.setContentModel(contentsModel);
			contentsModel.addChild(model);

		}
		for (TfmEdge tfmEdge : tfmBean.getTfmEdgesList()) {
			// 新建边实例
			LineConnectionModel l = new LineConnectionModel();
			// 设置边的开始模型
			l.setSource(mapTfmBlock.get(tfmEdge.getSourceid()));
			// 设置边的结束模型
			l.setTarget(mapTfmBlock.get(tfmEdge.getTargetid()));
			// 边与开始模型连接
			l.attachSource();
			// 边与结束模型连接
			l.attachTarget();
			// 设置边权值
			l.setWeight(tfmEdge.getWeight());
		}
	}
}
