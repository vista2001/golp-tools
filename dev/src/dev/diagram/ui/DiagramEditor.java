package dev.diagram.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;

import org.dom4j.DocumentException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.diagram.actions.GenerateCodeAction;
import dev.diagram.actions.GraphAutoLayout;
import dev.diagram.actions.SaveAction;
import dev.diagram.actions.WriteToXML;
import dev.diagram.helper.Palette;
import dev.diagram.model.ContentsModel;
import dev.diagram.outline.TreeEditPartFactory;
import dev.diagram.parts.PartFactory;
import dev.util.CommonUtil;
import dev.util.DevLogger;

/**
 * 视图编辑器
 * 
 * @author 木木
 * 
 */
public class DiagramEditor extends GraphicalEditorWithFlyoutPalette
// implements ISaveablePart2
{
	// 编辑器ID
	public static final String ID = "dev.diagram.ui.DiagramEditor";
	// 视图的显示内容，内容模型
	private ContentsModel model;
	// 所属的工程Id,用于数据库的连接
	private String projectId;
	// 流程图的Id
	private String diagramId;
	// 流程图xml文件名称
	private String fileName;
	// 声明视图
	GraphicalViewer viewer;

	public DiagramEditor() {
		// 设置编辑域
		setEditDomain(new DefaultEditDomain(this));

	}

	@Override
	protected void setPartName(String partName) {
		//setPartName("AAA");
		//super.setPartName(partName);
	}

	/**
	 * 配置视图
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		// ScalableRootEditPart可以伸缩，有滚动条工作区
		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		viewer = getGraphicalViewer();
		// 设置根工作区
		viewer.setRootEditPart(rootEditPart);
		// 设置控制器工厂
		viewer.setEditPartFactory(new PartFactory());
		// 设置网格可见
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, true);
		// 设置激活网格
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, true);
		getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED,
				true);
		// 获得zoomManager
		ZoomManager manager = rootEditPart.getZoomManager();
		// 设置非百分比缩放
		ArrayList<String> zoomcontributions = new ArrayList<String>();
		zoomcontributions.add(ZoomManager.FIT_ALL);
		zoomcontributions.add(ZoomManager.FIT_HEIGHT);
		zoomcontributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(zoomcontributions);
		// 注册放大IAction
		IAction action = new ZoomInAction(manager);
		getActionRegistry().registerAction(action);
		// 注册缩小IAction
		action = new ZoomOutAction(manager);
		getActionRegistry().registerAction(action);

		getGraphicalViewer().setKeyHandler(
				new GraphicalViewerKeyHandler(getGraphicalViewer())
						.setParent(getCommonKeyHandler()));
	}

	/**
	 * 初始化视图编辑器
	 */
	protected void initializeGraphicalViewer() {
		// 初始化模型
		// initModel();
		// 设置ContentsModel模型是视图的显示内容

		initModel();
		viewer.setContents(model);
	}

	public void doSave(IProgressMonitor monitor) {
		WriteToXML.writeToXML(model);
	}

	public void doSaveAs() {

	}

	public boolean isSaveAsAllowed() {

		return false;
	}

	/**
	 * MyContentOutlinePage类是实现大纲和鹰眼类
	 * 
	 * @author 木木
	 * 
	 */
	class MyContentOutlinePage extends
			org.eclipse.gef.ui.parts.ContentOutlinePage {
		private SashForm sash;

		public MyContentOutlinePage() {
			super(new TreeViewer());
		}

		private DisposeListener disposeListerner;
		ScrollableThumbnail thumbnail;

		@Override
		public void createControl(Composite parent) {

			sash = new SashForm(parent, SWT.VERTICAL);
			getViewer().createControl(sash);
			getViewer().setEditDomain(getEditDomain());
			getViewer().setEditPartFactory(new TreeEditPartFactory());
			getViewer().setContents(model);
			getSelectionSynchronizer().addViewer(getViewer());
			Canvas canvas = new Canvas(sash, SWT.BORDER);
			LightweightSystem lws = new LightweightSystem(canvas);
			thumbnail = new ScrollableThumbnail(
					(Viewport) ((ScalableRootEditPart) getGraphicalViewer()
							.getRootEditPart()).getFigure());
			thumbnail.setSource(((ScalableRootEditPart) getGraphicalViewer()
					.getRootEditPart())
					.getLayer(LayerConstants.PRINTABLE_LAYERS));
			lws.setContents(thumbnail);

			disposeListerner = new DisposeListener() {

				@Override
				public void widgetDisposed(DisposeEvent e) {

					if (thumbnail != null) {
						thumbnail.deactivate();
						thumbnail = null;

					}
				}
			};
			getGraphicalViewer().getControl().addDisposeListener(
					disposeListerner);
		}

		@Override
		public Control getControl() {

			return sash;
		}

		@Override
		protected EditPartViewer getViewer() {

			return super.getViewer();
		}

		@Override
		public void dispose() {

			getSelectionSynchronizer().removeViewer(getViewer());
			if (getGraphicalViewer().getControl() != null
					&& !getGraphicalViewer().getControl().isDisposed())
				getGraphicalViewer().getControl().removeDisposeListener(
						disposeListerner);
			super.dispose();
		}

		@Override
		public void init(IPageSite pageSite) {

			super.init(pageSite);
			ActionRegistry registry = getActionRegistry();
			IActionBars bars = pageSite.getActionBars();
			String id = IWorkbenchActionConstants.DELETE;
			bars.setGlobalActionHandler(id, registry.getAction(id));
			bars.updateActionBars();

		}

	}

	@Override
	protected PaletteRoot getPaletteRoot() {

		return Palette.getPaletteRoot();
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// It ignores, when getActivePage() is null
		if (part.getSite().getWorkbenchWindow().getActivePage() == null)
			return;
		super.selectionChanged(part, selection);
	}

	@Override
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class) {
			return ((ScalableRootEditPart) getGraphicalViewer()
					.getRootEditPart()).getZoomManager();

		}
		if (type == IContentOutlinePage.class)
			return new MyContentOutlinePage();
		return super.getAdapter(type);
	}

	/**
	 * 创建IAction
	 */
	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		IAction action;
		action = new DirectEditAction((IWorkbenchPart) this);
		registry.registerAction(action);
		// 注册自动布局action
		action = new GraphAutoLayout(this, model);
		action.setId(GraphAutoLayout.ID);
		registry.registerAction(action);
		// getSelectionActions().add(action.getId());
		// 注册写入数据库action
		action = new GenerateCodeAction(model);
		registry.registerAction(action);
		// 注册SaveAction
		action = new SaveAction(model, this);
		registry.registerAction(action);
	}

	/**
	 * 初始化 工程编号（id）projectId,流程图编号（id）diagramId，存储的xml文件路径和名称fileName
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		// 所属工程编号（Id）
		projectId = ((DiagramEditorInput) input).getProjectId();
		// 流程图的编号（Id）
		diagramId = ((DiagramEditorInput) input).getDiagramId();
		// 工程名称，目的是可以从名称中得到存储xml的路径
		String projectName = ((DiagramEditorInput) input).getProjectName();
		// xml文件的完整路径和名称，存储在工程下xml文件夹下
		fileName = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName).getLocationURI().toString()
				+ "/XML/" + diagramId + ".xml";
		fileName = fileName.substring(6);
		File file = new File(fileName);
		File parent = file.getParentFile();
		// 路径是否完整，不完整则建立路径，
		if (parent != null && !file.exists())
			parent.mkdir();
		if (file.exists())
			file.delete();
		try {
			// 新建文件
			file.createNewFile();

		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
		model = new ContentsModel(projectId, diagramId, fileName);
		super.init(site, input);
	}

	/**
	 * 初始化内容模型中的流程图信息
	 * 
	 * @param contentsModel
	 */
	public void initModel() {
		String tfmName;
		String tfmDesc;
		String tfmType;
		String tfmTradeId;
		// 数据库连接
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

		dbConnectImpl.setAutoCommit(false);
		// 从数据库中取出流程图的信息
		String sql = "select tfmname,tfmdesc,type,tradeid,tfmxml from t_tfm where tfmid="
				+ Integer.parseInt(diagramId);
		ResultSet rs;
		try {
			dbConnectImpl.openConn(CommonUtil.initPs(projectId));
			rs = dbConnectImpl.retrive(sql);
			if (rs.next()) {
				tfmName = rs.getString(1);
				tfmDesc = rs.getString(2);
				tfmType = rs.getInt(3) + "";
				tfmTradeId = rs.getInt(4) + "";
				// 存入内容模型中
				model.init(tfmName, tfmDesc, tfmType, tfmTradeId);
				// 获得数据库中流程图的xml文件的二进制文件
				Blob blob = rs.getBlob(5);
				// 二进制文件为空直接退出
				if (blob == null) {
					return;
				}
				// 输入流
				InputStream ins = blob.getBinaryStream();
				File file = new File(fileName);
				// 输出流
				OutputStream fout = new FileOutputStream(file);
				// 缓存大小
				byte[] b = new byte[1024];
				int len = 0;
				// 将二进制的xml文件写入外存
				while ((len = ins.read(b)) != -1)
					fout.write(b, 0, len);
				// 关闭输出流
				fout.close();
				// 关闭输出流
				ins.close();
				// 如果写入的文件不为空，则是打开已经存在的流程图，将流程图的信息写入模型
				if (file.length() != -1)
					GenerateTfmBean.BeanToModel(model, fileName);
				// 数据库提交
				dbConnectImpl.commit();
				// 关闭数据库连接
				dbConnectImpl.closeConn();
			}
		} catch (SQLException | IOException | DocumentException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
	}

	// @Override
	// public int promptToSaveOnClose()
	// {
	// if (isDirty())
	// {
	// boolean answer = MessageDialog.openQuestion(PlatformUI
	// .getWorkbench().getActiveWorkbenchWindow().getShell(),
	// "Close Virtual Terminal", "内容已经修改，你要保存吗?");
	// if (!answer)
	// {
	// return ISaveablePart2.CANCEL;
	// }
	// else
	// {
	// WriteToXML.writeToXML(model);
	// }
	// }
	// return ISaveablePart2.NO;
	// }

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public void commandStackChanged(EventObject event) {
		super.commandStackChanged(event);
		getCommandStack().isDirty();
		setIsDirty(getCommandStack().isDirty());
	}

	private boolean isDirty = false;

	public void setIsDirty(boolean flag) {
		isDirty = flag;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		return getCommandStack().isDirty();
	}

	KeyHandler sharedKeyHandler;

	protected KeyHandler getCommonKeyHandler() {
		if (sharedKeyHandler == null) {
			sharedKeyHandler = new KeyHandler();
			sharedKeyHandler
					.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
							getActionRegistry().getAction(
									ActionFactory.DELETE.getId()));
		}
		return sharedKeyHandler;
	}
}
