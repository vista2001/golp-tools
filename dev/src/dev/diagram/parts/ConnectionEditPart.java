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
 * �߿�����
 * 
 * @author ľľ
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
	 * ������ͼ��
	 */
	protected IFigure createFigure() {
		// �½�һ��ֱ��
		PolylineConnection conn = new PolylineConnection();
		// Ϊֱ����Ӽ�ͷװ��
		conn.setTargetDecoration(new PolygonDecoration());
		// ȡֱ�ߵ��յ�
		MidpointLocator mid = new MidpointLocator(conn, 0);
		Label label = new Label(
				String.valueOf(((LineConnectionModel) getModel()).getWeight()));
		label.setOpaque(true);
		// ��ֱ�ߵ��е����label ��ʾȨֵ
		conn.add(label, mid);
		return conn;
	}

	/**
	 * ��װ����
	 */
	protected void createEditPolicies() {
		// ��װConnectionEditPolicy���ԣ����ڱߵ�ɾ��
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new CustomConnectionEditPolicy());
		// ��װConnectionEndpointEditPolicy���ԣ��ڵ������
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new ConnectionEndpointEditPolicy());
	}

	/**
	 * ������������
	 */
	@Override
	public void performRequest(Request req) {
		// ˫��
		if (req.getType().equals(REQ_OPEN)) {
			// ��Ȩֵ���öԻ���
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
