package dev.diagram.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.gef.EditPolicy;

import dev.diagram.model.AbstractConnectionModel;
import dev.diagram.model.ContentsModel;
import dev.diagram.policies.CustomXYLayoutEditPolicy;

/**
 * 内容控制器，模型与视图的变化都通过控制器来同步
 * 
 * @author 木木
 * 
 */
public class ContentsModelPart extends EditPartWithListener {
	/**
	 * 创建图形，这里只建一个层，作为所有图形的父亲，层并不显示在视图中
	 */
	@Override
	protected IFigure createFigure() {

		Figure figure = new Layer();
		return figure;

	}

	// 自动布局管理器
	LayoutManager graphLayout = new GraphLayoutManager(this);
	// xy布局管理器
	LayoutManager xyLayout = new XYLayout();

	/**
	 * 刷新视图，实现刷新前判断使用何种布局管理器
	 * 
	 */
	public void refreshVisuals() {
		ContentsModel contentsModel = (ContentsModel) getModel();
		if (contentsModel.isAutoLayout())
			figure.setLayoutManager(graphLayout);
		else
			figure.setLayoutManager(xyLayout);
	}

	/**
	 * 安装策略
	 */
	@Override
	protected void createEditPolicies() {
		// 安装XY布局管理器，这种管理器容许图形的位置和大小发生改变
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new CustomXYLayoutEditPolicy());

	}

	/**
	 * 返回子模型链表，自动调用；显示视图的时候通过返回的子模型链表显示子图形
	 */
	@Override
	protected List<?> getModelChildren() {
		return ((ContentsModel) getModel()).getChildren();
	}

	/**
	 * 激活触发器后执行函数
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// 发生子模型添加和删除的变化后刷新子控制器改变视图
		if (e.getPropertyName().equals(ContentsModel.CHILDREN)) {
			// 刷新孩子控制器
			refreshChildren();
		}
		// 布局变化
		if (e.getPropertyName().equals(ContentsModel.LAYOUT))
			refreshVisuals();
	}

	/**
	 * 有关自动布局管理器，将节点图形转换为系统自带图类的node类型
	 * 
	 * @param graph
	 * @param map
	 */
	public void contributeNodesToGraph(DirectedGraph graph, Map map) {
		for (int i = 1; i < getChildren().size(); i++) {
			ElementEditPart modelP = (ElementEditPart) getChildren().get(i);
			org.eclipse.draw2d.graph.Node node = new org.eclipse.draw2d.graph.Node(
					modelP);
			node.width = modelP.getFigure().getPreferredSize().width;
			node.height = modelP.getFigure().getPreferredSize().height;
			node.setPadding(new Insets(20, 16, 15, 14));
			map.put(modelP, node);
			graph.nodes.add(node);
		}
	}

	/**
	 * 边转换系统自带图类的边
	 * 
	 * @param graph
	 * @param map
	 */
	public void contributeEdgesToGraph(DirectedGraph graph, Map map) {
		for (int i = 1; i < getChildren().size(); i++) {
			ElementEditPart modelP = (ElementEditPart) getChildren().get(i);
			List outgoing = modelP.getSourceConnections();
			for (int j = 0; j < outgoing.size(); j++) {
				ConnectionEditPart conn = (ConnectionEditPart) outgoing.get(j);
				Node source = (Node) map.get(conn.getSource());
				Node target = (Node) map.get(conn.getTarget());
				Edge e = new Edge(conn, source, target);
				e.weight = ((AbstractConnectionModel) conn.getModel())
						.getWeight();
				graph.edges.add(e);
				map.put(conn, e);
			}
		}
	}

	protected void applyGraphResults(DirectedGraph graph, Map map) {
		for (int i = 1; i < getChildren().size(); i++) {
			ElementEditPart modelP = (ElementEditPart) getChildren().get(i);
			Node n = (Node) map.get(modelP);
			modelP.getFigure().setBounds(
					new Rectangle(n.x, n.y, n.width, n.height));
		}
	}
}
