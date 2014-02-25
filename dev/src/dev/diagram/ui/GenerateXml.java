package dev.diagram.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import dev.diagram.beans.TfmBean;
import dev.diagram.beans.TfmBlock;
import dev.diagram.beans.TfmCommDeal;
import dev.diagram.beans.TfmCompersation;
import dev.diagram.beans.TfmEdge;
import dev.diagram.beans.TfmException;
import dev.diagram.beans.TfmExtendAop;

public class GenerateXml
{
	/**
	 * 读取保存流程图的xml文件
	 * 
	 * @param filename
	 *            绝对路径文件名称
	 * @return
	 * @throws DocumentException
	 */
	public static TfmBean readTfmXml(String filename) throws DocumentException
	{
		File file = new File(filename);
		TfmBean tfmBean = new TfmBean();
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding("UTF-8");
		Document document = saxReader.read(file);
		Element root = document.getRootElement();
		tfmBean.setTfmId(root.elementText("id"));
		tfmBean.setTfmType(root.elementText("type"));
		tfmBean.setTfmdesc(root.elementText("desc"));
		tfmBean.setTradeId(root.elementText("tradeid"));
		// 准备新建的TfmBean中的List项目
		List<TfmBlock> blockList = tfmBean.getTfmBlockList();
		List<TfmException> exceptionList = tfmBean.getTfmExceptionList();
		List<TfmCompersation> compersationList = tfmBean
				.getTfmCompersationList();
		List<TfmEdge> edgesList = tfmBean.getTfmEdgesList();
		// 准备被解析的节点

		List<?> compersationsElement = root.element("compersations").elements(
				"compersation");
		List<?> exceptionsElement = root.element("exceptions").elements(
				"exception");
		List<?> edgesElement = root.element("edges").elements("edge");
		List<?> nodesElement = root.element("nodes").elements("node");
		// 解析流程图的节点block
		for (Object object : nodesElement)
		{
			// nodePropertyElement 名称为node节点下的一级节点
			// 遍历流程图的节点
			// for (Object object : nodePropertyElement) {
			Element nodeElement = (Element) object;
			// 获取的扩展点列表的element节点
			List<?> extendaopElements = nodeElement.element("extendaops")
					.elements("extendaop");
			// 创建一个节点
			TfmBlock block = new TfmBlock();
			// 包含的扩展点列表
			List<TfmExtendAop> tfmExtendAopsList = block.getTfmExtendAopsList();

			block.setTfmId(nodeElement.elementText("tfmid"));
			block.setNodeId(nodeElement.elementText("nodeid"));
			block.setName(nodeElement.elementText("name"));
			block.setNodeType(nodeElement.elementText("nodetype"));
			block.setAopName(nodeElement.elementText("aopname"));
			block.setCondition(nodeElement.elementText("condition"));
			block.setDllId(nodeElement.elementText("dllid"));
			block.setDesc(nodeElement.elementText("desc"));
			block.setNestedtfm(nodeElement.elementText("nestedtfm"));
			// 遍历extendaops节点的每一个extendaop节点
			for (Object extendaop : extendaopElements)
			{
				TfmExtendAop tfmExtendAop = new TfmExtendAop();
				// TfmCommDeal tfmCommDeal=tfmExtendAop;
				// 访问extendaop节点的内容
				Element extendaopElement = (Element) extendaop;
				// getTfmCommDealDetail((TfmCommDeal)tfmExtendAop,extendaopElement);
				tfmExtendAop.setName(extendaopElement.elementText("name"));
				tfmExtendAop.setDesc(extendaopElement.elementText("desc"));
				tfmExtendAop.setTfmId(extendaopElement.elementText("tfmid"));
				tfmExtendAop.setSeqNo(extendaopElement.elementText("seqno"));
				tfmExtendAop.setFuncname(extendaopElement
						.elementText("funcname"));
				tfmExtendAop.setDllId(extendaopElement.elementText("dllid"));
				tfmExtendAop.setNodeid(extendaopElement.elementText("nodeid"));
				// 向扩展点列表添加一个扩展点信息
				tfmExtendAopsList.add(tfmExtendAop);
			}
			block.setLocationX(nodeElement.elementText("locationX"));
			block.setLocationY(nodeElement.elementText("locationY"));
			block.setWidth(nodeElement.elementText("width"));
			block.setHeight(nodeElement.elementText("height"));
			// 向节点列表中添加一个节点
			blockList.add(block);
			// }
		}
		// 解析edge节点
		for (Object edge : edgesElement)
		{
			Element edgeElement = (Element) edge;
			TfmEdge tfmEdge = new TfmEdge();
			tfmEdge.setTfmId(edgeElement.elementText("tfmid"));
			tfmEdge.setSourceid(edgeElement.elementText("sourceid"));
			tfmEdge.setTargetid(edgeElement.elementText("targetid"));
			tfmEdge.setWeight(edgeElement.elementText("weight"));
			edgesList.add(tfmEdge);
		}
		// 解析exception节点
		for (Object exception : exceptionsElement)
		{
			Element exceptionElement = (Element) exception;
			TfmException tfmException = new TfmException();
			// TfmCommDeal tfmCommDeal=tfmException.getTfmCommDeal();
			getTfmCommDealDetail(tfmException, exceptionElement);
			exceptionList.add(tfmException);
		}
		// 解析compersation节点
		for (Object compersation : compersationsElement)
		{
			Element compersationElement = (Element) compersation;
			TfmCompersation tfmCompersation = new TfmCompersation();
			// TfmCommDeal tfmCommDeal=tfmCompersation.getTfmCommDeal();
			getTfmCommDealDetail(tfmCompersation, compersationElement);
			compersationList.add(tfmCompersation);
		}
		tfmBean.setTfmBlockList(blockList);
		tfmBean.setTfmCompersationList(compersationList);
		tfmBean.setTfmExceptionList(exceptionList);
		tfmBean.setTfmEdgesList(edgesList);
		return tfmBean;
	}

	/**
	 * 写入流程图内容到xml文件
	 * 
	 * @param filename
	 *            文件名称
	 * @param tfmBean
	 *            TfmBean对象
	 * @throws IOException
	 */
	public static void writeTfmXml(String filename, TfmBean tfmBean)
			throws IOException
	{
		File wfile = new File(filename);
		Document wdocument = DocumentHelper.createDocument();
		Element wroot = wdocument.addElement("tfm");
		Element wname = wroot.addElement("name");
		wname.setText(tfmBean.getTfmId());
		Element wid = wroot.addElement("id");
		wid.setText(tfmBean.getTfmId());
		Element wtype = wroot.addElement("type");
		wtype.setText(tfmBean.getTfmType());
		Element wtradeid = wroot.addElement("tradeid");
		wtradeid.setText(tfmBean.getTradeId());
		Element wdesc = wroot.addElement("desc");
		wdesc.setText(tfmBean.getTfmdesc());
		Element wexceptions = wroot.addElement("exceptions");
		Element wcompersations = wroot.addElement("compersations");
		Element wnodes = wroot.addElement("nodes");
		Element wedges = wroot.addElement("edges");
		List<?> exceptionsList = tfmBean.getTfmExceptionList();
		for (Object object : exceptionsList)
		{
			TfmException tfmException = (TfmException) object;
			// TfmCommDeal tfmCommDeal=tfmException.getTfmCommDeal();
			Element exception = wexceptions.addElement("exception");
			getTfmCommDealNode(exception, tfmException);
		}
		List<?> wcompersationsList = tfmBean.getTfmCompersationList();
		for (Object object : wcompersationsList)
		{
			TfmCompersation compersation1 = (TfmCompersation) object;
			// TfmCommDeal tfmCommDeal=compersation1.getTfmCommDeal();
			Element compersation = wcompersations.addElement("compersation");
			getTfmCommDealNode(compersation, compersation1);
		}
		List<?> wedgesList = tfmBean.getTfmEdgesList();
		for (Object object : wedgesList)
		{
			TfmEdge edge = (TfmEdge) object;
			Element tfmEdge = wedges.addElement("edge");
			Element tfmid = tfmEdge.addElement("tfmid");
			Element sourceid = tfmEdge.addElement("sourceid");
			Element targetid = tfmEdge.addElement("targetid");
			Element weight = tfmEdge.addElement("weight");

			tfmid.setText(edge.getTfmId());
			sourceid.setText(edge.getSourceid());
			targetid.setText(edge.getTargetid());
			weight.setText(edge.getWeight());
		}
		List<?> wnodeList = tfmBean.getTfmBlockList();
		for (Object object : wnodeList)
		{
			TfmBlock tfmBlock = (TfmBlock) object;
			Element node = wnodes.addElement("node");
			Element name = node.addElement("name");
			name.setText(tfmBlock.getName());
			Element tfmid = node.addElement("tfmid");
			tfmid.setText(tfmBlock.getTfmId());
			Element nodeid = node.addElement("nodeid");
			nodeid.setText(tfmBlock.getNodeId());
			Element nodeType = node.addElement("nodetype");
			nodeType.setText(tfmBlock.getNodeType());
			Element aopname = node.addElement("aopname");
			aopname.setText(tfmBlock.getAopName());
			Element nestedtfm = node.addElement("nestedtfm");
			nestedtfm.setText(tfmBlock.getNestedtfm());
			Element condition = node.addElement("condition");
			condition.setText(tfmBlock.getCondition());
			Element dllid = node.addElement("dllid");
			dllid.setText(tfmBlock.getDllId());
			Element desc = node.addElement("desc");
			desc.setText(tfmBlock.getDesc());
			Element locationX = node.addElement("locationX");
			locationX.setText(tfmBlock.getLocationX());
			Element locationY = node.addElement("locationY");
			locationY.setText(tfmBlock.getLocationY());
			Element height = node.addElement("height");
			height.setText(tfmBlock.getHeight());
			Element width = node.addElement("width");
			width.setText(tfmBlock.getWidth());
			Element extendaops = node.addElement("extendaops");
			List<?> extendAops = tfmBlock.getTfmExtendAopsList();
			for (Object extendAop : extendAops)
			{
				TfmExtendAop tfmExtendAop = (TfmExtendAop) extendAop;
				Element tfmaop = extendaops.addElement("extendaop");
				Element exname = tfmaop.addElement("name");
				exname.setText(tfmExtendAop.getName());
				Element exdesc = tfmaop.addElement("desc");
				exdesc.setText(tfmExtendAop.getDesc());
				Element tfmid1 = tfmaop.addElement("tfmid");
				tfmid1.setText(tfmExtendAop.getTfmId());
				Element seqno = tfmaop.addElement("seqno");
				seqno.setText(tfmExtendAop.getSeqNo());
				Element funcname = tfmaop.addElement("funcname");
				funcname.setText(tfmExtendAop.getFuncname());
				Element dllid1 = tfmaop.addElement("dllid");
				dllid1.setText(tfmExtendAop.getDllId());
				Element ofnodeid = tfmaop.addElement("nodeid");
				ofnodeid.setText(tfmExtendAop.getNodeid());
			}

		}
		OutputFormat format = new OutputFormat("\t", true);
		format.setEncoding("UTF-8");
		XMLWriter writer = new XMLWriter(new FileOutputStream(wfile), format);
		writer.write(wdocument);
		writer.close();
		// return false;
	}

	private static void getTfmCommDealDetail(TfmCommDeal tfmCommDeal,
			Element element)
	{
		tfmCommDeal.setName(element.elementText("name"));
		tfmCommDeal.setDesc(element.elementText("desc"));
		tfmCommDeal.setTfmId(element.elementText("tfmid"));
		tfmCommDeal.setSeqNo(element.elementText("seqno"));
		tfmCommDeal.setFuncname(element.elementText("funcname"));
		tfmCommDeal.setDllId(element.elementText("dllid"));
	}

	private static Element getTfmCommDealNode(Element node,
			TfmCommDeal tfmCommDeal)
	{
		Element name = node.addElement("name");
		name.setText(tfmCommDeal.getName());
		Element desc = node.addElement("desc");
		desc.setText(tfmCommDeal.getDesc());
		Element tfmid1 = node.addElement("tfmid");
		tfmid1.setText(tfmCommDeal.getTfmId());
		Element seqno = node.addElement("seqno");
		seqno.setText(tfmCommDeal.getSeqNo());
		Element funcname = node.addElement("funcname");
		funcname.setText(tfmCommDeal.getFuncname());
		Element dllid1 = node.addElement("dllid");
		dllid1.setText(tfmCommDeal.getDllId());
		return node;
	}
}
