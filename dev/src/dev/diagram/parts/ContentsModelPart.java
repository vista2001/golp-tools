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
 * ���ݿ�������ģ������ͼ�ı仯��ͨ����������ͬ��
 * 
 * @author ľľ
 * 
 */
public class ContentsModelPart extends EditPartWithListener {
	/**
	 * ����ͼ�Σ�����ֻ��һ���㣬��Ϊ����ͼ�εĸ��ף��㲢����ʾ����ͼ��
	 */
	@Override
	protected IFigure createFigure() {

		Figure figure = new Layer();
		return figure;

	}

	// �Զ����ֹ�����
	LayoutManager graphLayout = new GraphLayoutManager(this);
	// xy���ֹ�����
	LayoutManager xyLayout = new XYLayout();

	/**
	 * ˢ����ͼ��ʵ��ˢ��ǰ�ж�ʹ�ú��ֲ��ֹ�����
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
	 * ��װ����
	 */
	@Override
	protected void createEditPolicies() {
		// ��װXY���ֹ����������ֹ���������ͼ�ε�λ�úʹ�С�����ı�
		installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new CustomXYLayoutEditPolicy());

	}

	/**
	 * ������ģ�������Զ����ã���ʾ��ͼ��ʱ��ͨ�����ص���ģ��������ʾ��ͼ��
	 */
	@Override
	protected List<?> getModelChildren() {
		return ((ContentsModel) getModel()).getChildren();
	}

	/**
	 * ���������ִ�к���
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// ������ģ����Ӻ�ɾ���ı仯��ˢ���ӿ������ı���ͼ
		if (e.getPropertyName().equals(ContentsModel.CHILDREN)) {
			// ˢ�º��ӿ�����
			refreshChildren();
		}
		// ���ֱ仯
		if (e.getPropertyName().equals(ContentsModel.LAYOUT))
			refreshVisuals();
	}

	/**
	 * �й��Զ����ֹ����������ڵ�ͼ��ת��Ϊϵͳ�Դ�ͼ���node����
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
	 * ��ת��ϵͳ�Դ�ͼ��ı�
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
