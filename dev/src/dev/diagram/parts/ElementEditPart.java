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
 * 基本模型（ElementModel）控制器
 * 
 * @author 木木
 * 
 */
public class ElementEditPart extends EditPartWithListener implements
		NodeEditPart
{
	/**
	 * 根据不同的模型创建不同的图形
	 */
	@Override
	protected IFigure createFigure()
	{
		ElementModel model = (ElementModel) getModel();
		IFigure figure = null;
		// 异常与补偿模型
		// 异常与补偿模型是个多层图形，父图像是个矩形，子图像是个button
		if (model instanceof ExAndComModel)
		{
			// 异常与补偿图像父图像
			figure = new ColorRectangle();
			// 父图像布局管理器
			XYLayout xyly = new XYLayout();
			figure.setLayoutManager(xyly);
			// 子图像button
			Button exceptions = new Button("异常和补偿");
			exceptions.setBorder(new LineBorder(ColorConstants.orange));
			// button添加到父图像矩形中
			figure.add(exceptions);
			// 为子图像设置位置和大小信息
			figure.setConstraint(exceptions, new Rectangle(25, 35, 100, 30));
			ButtonModel buttonModelEx = new ButtonModel();
			exceptions.setModel(buttonModelEx);
			// 为button添加监听
			buttonModelEx.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent event)
				{
					// 单击后打开一个异常与补偿对话框
					ExceptionAndCompsationDialog dialog = new ExceptionAndCompsationDialog(
							getViewer().getControl().getShell(),
							(ContentsModel) ((ContentsModelPart) getParent())
									.getModel());
					dialog.open();
				}
			});
			return figure;
		}
		// aop模型
		else if (model instanceof AopModel)
			figure = new IRectangleFigure();
		// 返回模型
		else if (model instanceof ReturnModel)
			figure = new IEllipse();
		// 开始模型
		else if (model instanceof StartModel)
			figure = new Circle();
		// 条件模型
		else if (model instanceof ConditionModel)
			figure = new Rhombus();
		// 结束模型
		else if (model instanceof EndModel)
			figure = new Circle_End();
		// Tfm模型
		else if (model instanceof TfmModel)
			figure = new DoubleRectangle();
		// 为图形设置显示名称
		((MyFigure) figure).setText(model.getText());
		// mouseOver提示信息
		figure.setToolTip(new Label(model.getDesc()));
		return figure;
	}

	/**
	 * 安装策略
	 */
	@Override
	protected void createEditPolicies()
	{
		// ComponentEditPolicy策略，用于模型的删除
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new CustomComponentEditPolicy());
		// 直接编辑模型策略，用于单击后编辑模型显示名称
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new CustomDirectEditPolicy());
		// GraphicalNodeEditPolicy策略，用于连接边
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new CustomGraphicalNodeEditPolicy());

	}

	/**
	 * 刷新视图，显示模型对应的图像
	 */
	@Override
	protected void refreshVisuals()
	{
		Rectangle cts = ((ElementModel) getModel()).getConstraint();
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), cts);
	}

	/**
	 * 属性改变触发器激活后执行动作
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event)
	{
		// event是属性改变事件
		// 位置和大小属性改变后刷新视图
		if (event.getPropertyName().equals(ElementModel.CONSTRAINT))
			refreshVisuals();
		// 名称改变（直接编辑）后改变视图中的名称
		else if (event.getPropertyName().equals(ElementModel.NAME))
		{
			MyFigure myFigure = (MyFigure) getFigure();
			myFigure.setText((String) event.getNewValue());
		}
		// 模型中增加了边，此边以模型为起点，后刷新
		else if (event.getPropertyName().equals(
				ElementModel.P_SOURCE_CONNECTION))
		{
			// AbstractGraphicalEditPart类中的方法
			refreshSourceConnections();
		}
		// 模型中增加了边，此边以模型为终点，后刷新
		else if (event.getPropertyName().equals(
				ElementModel.P_TARGET_CONNECTION))
		{
			// AbstractGraphicalEditPart类中的方法
			refreshTargetConnections();
		}
	}

	/**
	 * 返回模型中以模型为起点的边模型链表，被方法
	 * <p>
	 * refreshSourceConnections()
	 * </p>
	 * 调用
	 */
	@Override
	protected List<?> getModelSourceConnections()
	{
		return ((ElementModel) getModel()).getModelSourceConnections();
	}

	/**
	 * 返回模型中以模型为起点的边模型链表，被方法
	 * <p>
	 * refreshTargetConnections()
	 * </p>
	 * 调用
	 */
	@Override
	public List<?> getModelTargetConnections()
	{
		return ((ElementModel) getModel()).getModelTargetConnections();
	}

	private CustomDirectEditManager directManager = null;

	/**
	 * 单击和双击请求处理
	 */
	@Override
	public void performRequest(Request req)
	{
		// 单击后执行直接编辑功能
		if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT))
		{
			// 直接编辑
			performDirectEdit();
			((CommonModel)getModel()).setText(((MyFigure)getFigure()).getText());
		}
		// 双击后根据不同模型打开不同的模型属性配置对话框
		else if (req.getType().equals(RequestConstants.REQ_OPEN))
		{
			// 异常和补偿 实现的是打开与折叠
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
				// 条件模型
				if (((CommonModel) getModel()).getTypeId() == 0)
				{
					// 打开条件模型对话框
					ConditionBlockConfigurDialong IDialog = new ConditionBlockConfigurDialong(
							getViewer().getControl().getShell(),
							(CommonModel) getModel(),contentsModel);
					IDialog.open();
				}
				// AOP，返回模型
				else if (!(((CommonModel) getModel()).getTypeId() == 4 || ((CommonModel) getModel())
						.getTypeId() == 5))
				{
					// 打开模型属性对话框
					BlockConfigureDialog IDialog = new BlockConfigureDialog(
							getViewer().getControl().getShell(),
							(CommonModel) getModel(),contentsModel);
					IDialog.open();
				}
				// TFM
				else if(((CommonModel) getModel()).getTypeId() == 3)
				{
					// 打开开始或结束模型属性对话框
					TfmCongigureDialog IDialog = new TfmCongigureDialog(
							getViewer().getControl().getShell(),
							(CommonModel) getModel(),contentsModel);
					IDialog.open();
				}
				//开始或结束模型
				else
				{
					// 打开开始或结束模型属性对话框
					StartAndEndDialog IDialog = new StartAndEndDialog(
							getViewer().getControl().getShell(),
							(CommonModel) getModel(),contentsModel);
					IDialog.open();
				}
			}
		}
	}

	/**
	 * 直接编辑
	 */
	private void performDirectEdit()
	{

		if (directManager == null)
		{
			// 新建直接编辑管理器
			directManager = new CustomDirectEditManager(this,
					TextCellEditor.class,
					new CustomCellEditLocator(getFigure()));
		}
		directManager.show();
		
	}

	/**
	 * 返回边的起点模型的连接锚点
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection)
	{

		return new ChopboxAnchor(getFigure());
	}
	/**
	 * 返回边的终点模型的连接锚点
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
