package dev.diagram.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.viewers.TextCellEditor;

import dev.diagram.figures.Circle;
import dev.diagram.figures.Circle_End;
import dev.diagram.figures.ColorRectangle;
import dev.diagram.figures.DoubleRectangle;
import dev.diagram.figures.IEllipse;
import dev.diagram.figures.IRectangleFigure;
import dev.diagram.figures.MyFigure;
import dev.diagram.figures.Rhombus;
import dev.diagram.inputDialog.BlockConfigureDialog;
import dev.diagram.inputDialog.ConditionBlockConfigurDialong;
import dev.diagram.inputDialog.ExceptionAndCompsationDialog;
import dev.diagram.inputDialog.StartAndEndDialog;
import dev.diagram.inputDialog.TfmCongigureDialog;
import dev.diagram.model.AopModel;
import dev.diagram.model.CommonModel;
import dev.diagram.model.ConditionModel;
import dev.diagram.model.ContentsModel;
import dev.diagram.model.ElementModel;
import dev.diagram.model.EndModel;
import dev.diagram.model.ExAndComModel;
import dev.diagram.model.ReturnModel;
import dev.diagram.model.StartModel;
import dev.diagram.model.TfmModel;
import dev.diagram.policies.CustomComponentEditPolicy;
import dev.diagram.policies.CustomDirectEditPolicy;
import dev.diagram.policies.CustomGraphicalNodeEditPolicy;

/**
 * ����ģ�ͣ�ElementModel��������
 * 
 * @author ľľ
 * 
 */
public class ElementEditPart extends EditPartWithListener implements
		NodeEditPart
{
	/**
	 * ���ݲ�ͬ��ģ�ʹ�����ͬ��ͼ��
	 */
	@Override
	protected IFigure createFigure()
	{
		ElementModel model = (ElementModel) getModel();
		IFigure figure = null;
		// �쳣�벹��ģ��
		// �쳣�벹��ģ���Ǹ����ͼ�Σ���ͼ���Ǹ����Σ���ͼ���Ǹ�button
		if (model instanceof ExAndComModel)
		{
			// �쳣�벹��ͼ��ͼ��
			figure = new ColorRectangle();
			// ��ͼ�񲼾ֹ�����
			XYLayout xyly = new XYLayout();
			figure.setLayoutManager(xyly);
			// ��ͼ��button
			Button exceptions = new Button("�쳣�Ͳ���");
			exceptions.setBorder(new LineBorder(ColorConstants.orange));
			// button��ӵ���ͼ�������
			figure.add(exceptions);
			// Ϊ��ͼ������λ�úʹ�С��Ϣ
			figure.setConstraint(exceptions, new Rectangle(25, 35, 100, 30));
			ButtonModel buttonModelEx = new ButtonModel();
			exceptions.setModel(buttonModelEx);
			// Ϊbutton��Ӽ���
			buttonModelEx.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					// �������һ���쳣�벹���Ի���
					ExceptionAndCompsationDialog dialog = new ExceptionAndCompsationDialog(
							getViewer().getControl().getShell(),
							(ContentsModel) ((ContentsModelPart) getParent())
									.getModel());
					dialog.open();
				}
			});
			return figure;
		}
		// aopģ��
		else if (model instanceof AopModel)
			figure = new IRectangleFigure();
		// ����ģ��
		else if (model instanceof ReturnModel)
			figure = new IEllipse();
		// ��ʼģ��
		else if (model instanceof StartModel)
			figure = new Circle();
		// ����ģ��
		else if (model instanceof ConditionModel)
			figure = new Rhombus();
		// ����ģ��
		else if (model instanceof EndModel)
			figure = new Circle_End();
		// Tfmģ��
		else if (model instanceof TfmModel)
			figure = new DoubleRectangle();
		// Ϊͼ��������ʾ����
		((MyFigure) figure).setText(model.getText());
		// mouseOver��ʾ��Ϣ
		figure.setToolTip(new Label(model.getDesc()));
		return figure;
	}

	/**
	 * ��װ����
	 */
	@Override
	protected void createEditPolicies()
	{
		// ComponentEditPolicy���ԣ�����ģ�͵�ɾ��
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new CustomComponentEditPolicy());
		// ֱ�ӱ༭ģ�Ͳ��ԣ����ڵ�����༭ģ����ʾ����
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new CustomDirectEditPolicy());
		// GraphicalNodeEditPolicy���ԣ��������ӱ�
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new CustomGraphicalNodeEditPolicy());

	}

	/**
	 * ˢ����ͼ����ʾģ�Ͷ�Ӧ��ͼ��
	 */
	@Override
	protected void refreshVisuals()
	{
		Rectangle cts = ((ElementModel) getModel()).getConstraint();
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), cts);
	}

	/**
	 * ���Ըı䴥���������ִ�ж���
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event)
	{
		// event�����Ըı��¼�
		// λ�úʹ�С���Ըı��ˢ����ͼ
		if (event.getPropertyName().equals(ElementModel.CONSTRAINT))
			refreshVisuals();
		// ���Ƹı䣨ֱ�ӱ༭����ı���ͼ�е�����
		else if (event.getPropertyName().equals(ElementModel.NAME))
		{
			MyFigure myFigure = (MyFigure) getFigure();
			myFigure.setText((String) event.getNewValue());
		}
		// ģ���������˱ߣ��˱���ģ��Ϊ��㣬��ˢ��
		else if (event.getPropertyName().equals(
				ElementModel.P_SOURCE_CONNECTION))
		{
			// AbstractGraphicalEditPart���еķ���
			refreshSourceConnections();
		}
		// ģ���������˱ߣ��˱���ģ��Ϊ�յ㣬��ˢ��
		else if (event.getPropertyName().equals(
				ElementModel.P_TARGET_CONNECTION))
		{
			// AbstractGraphicalEditPart���еķ���
			refreshTargetConnections();
		}
	}

	/**
	 * ����ģ������ģ��Ϊ���ı�ģ������������
	 * <p>
	 * refreshSourceConnections()
	 * </p>
	 * ����
	 */
	@Override
	protected List<?> getModelSourceConnections()
	{
		return ((ElementModel) getModel()).getModelSourceConnections();
	}

	/**
	 * ����ģ������ģ��Ϊ���ı�ģ������������
	 * <p>
	 * refreshTargetConnections()
	 * </p>
	 * ����
	 */
	@Override
	public List<?> getModelTargetConnections()
	{
		return ((ElementModel) getModel()).getModelTargetConnections();
	}

	private CustomDirectEditManager directManager = null;

	/**
	 * ������˫��������
	 */
	@Override
	public void performRequest(Request req)
	{
		// ������ִ��ֱ�ӱ༭����
		if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT))
		{
			// ֱ�ӱ༭
			performDirectEdit();
			((CommonModel)getModel()).setText(((MyFigure)getFigure()).getText());
		}
		// ˫������ݲ�ͬģ�ʹ򿪲�ͬ��ģ���������öԻ���
		else if (req.getType().equals(RequestConstants.REQ_OPEN))
		{
			// �쳣�Ͳ��� ʵ�ֵ��Ǵ����۵�
			if (getModel() instanceof ExAndComModel)
			{
				Rectangle rec = getFigure().getBounds();
				if (rec.height <= 25)
				{
					((ElementModel) getModel()).setConstraint(new Rectangle(
							rec.x, rec.y, rec.width, 80));
				} else
				{
					((ElementModel) getModel()).setConstraint(new Rectangle(
							rec.x, rec.y, rec.width, 25));
				}
			} else if (getModel() instanceof CommonModel)
			{
				 ContentsModel contentsModel=((CommonModel)getModel()).getContentModel();
				// ����ģ��
				if (((CommonModel) getModel()).getTypeId() == 0)
				{
					// ������ģ�ͶԻ���
					ConditionBlockConfigurDialong IDialog = new ConditionBlockConfigurDialong(
							getViewer().getControl().getShell(),
							(CommonModel) getModel(),contentsModel);
					IDialog.open();
				}
				// AOP������ģ��
				else if (!(((CommonModel) getModel()).getTypeId() == 4 || ((CommonModel) getModel())
						.getTypeId() == 5))
				{
					// ��ģ�����ԶԻ���
					BlockConfigureDialog IDialog = new BlockConfigureDialog(
							getViewer().getControl().getShell(),
							(CommonModel) getModel(),contentsModel);
					IDialog.open();
				}
				// TFM
				else if(((CommonModel) getModel()).getTypeId() == 3)
				{
					// �򿪿�ʼ�����ģ�����ԶԻ���
					TfmCongigureDialog IDialog = new TfmCongigureDialog(
							getViewer().getControl().getShell(),
							(CommonModel) getModel(),contentsModel);
					IDialog.open();
				}
				//��ʼ�����ģ��
				else
				{
					// �򿪿�ʼ�����ģ�����ԶԻ���
					StartAndEndDialog IDialog = new StartAndEndDialog(
							getViewer().getControl().getShell(),
							(CommonModel) getModel(),contentsModel);
					IDialog.open();
				}
			}
		}
	}

	/**
	 * ֱ�ӱ༭
	 */
	private void performDirectEdit()
	{

		if (directManager == null)
		{
			// �½�ֱ�ӱ༭������
			directManager = new CustomDirectEditManager(this,
					TextCellEditor.class,
					new CustomCellEditLocator(getFigure()));
		}
		directManager.show();
		
	}

	/**
	 * ���رߵ����ģ�͵�����ê��
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection)
	{

		return new ChopboxAnchor(getFigure());
	}
	/**
	 * ���رߵ��յ�ģ�͵�����ê��
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection)
	{

		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request)
	{

		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request)
	{

		return new ChopboxAnchor(getFigure());
	}

}
