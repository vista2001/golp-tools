package dev.diagram.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

import dev.diagram.inputDialog.EdgeListLimitDialog;
import dev.diagram.model.AbstractConnectionModel;
import dev.diagram.model.CommonModel;
import dev.diagram.model.LineConnectionModel;
import dev.diagram.policies.CustomConnectionEditPolicy;

/**
 * 边控制器
 * 
 * @author 木木
 * 
 */
public class ConnectionEditPart extends AbstractConnectionEditPart implements
		PropertyChangeListener {

	@Override
	public void activate() {
		((AbstractConnectionModel) getModel()).addPropertyChangeListener(this);
		super.activate();
	}

	@Override
	public void deactivate() {
		super.deactivate();
		((AbstractConnectionModel) getModel()).addPropertyChangeListener(this);
	}

	/**
	 * 创建边图形
	 */
	protected IFigure createFigure() {
		// 新建一条直线
		PolylineConnection conn = new PolylineConnection();
		// 为直线添加箭头装饰
		conn.setTargetDecoration(new PolygonDecoration());
		// 取直线的终点
		MidpointLocator mid = new MidpointLocator(conn, 0);
		Label label = new Label(
				String.valueOf(((LineConnectionModel) getModel()).getWeight()));
		label.setOpaque(true);
		// 在直线的中点添加label 显示权值
		conn.add(label, mid);
		return conn;
	}

	/**
	 * 安装策略
	 */
	protected void createEditPolicies() {
		// 安装ConnectionEditPolicy策略，用于边的删除
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new CustomConnectionEditPolicy());
		// 安装ConnectionEndpointEditPolicy策略，节点的连接
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new ConnectionEndpointEditPolicy());
	}

	/**
	 * 特殊命令请求
	 */
	@Override
	public void performRequest(Request req) {
		// 双击
		if (req.getType().equals(REQ_OPEN)) {
			// 打开权值配置对话框
			CommonModel source = (CommonModel) ((AbstractConnectionModel) getModel())
					.getSource();
			EdgeListLimitDialog dialog = new EdgeListLimitDialog(PlatformUI
					.getWorkbench().getActiveWorkbenchWindow().getShell(),
					source);
			if (dialog.open() == Window.OK) {
				((LineConnectionModel) getModel()).setWeight(dialog.getText());
				((Label) getFigure().getChildren().get(1)).setText(dialog
						.getText());
			}
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(AbstractConnectionModel.EDGE))
			((Label) getFigure().getChildren().get(1))
					.setText(((LineConnectionModel) getModel()).getWeight()
							+ "");
	}

}
