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
 * ��ͼ�༭��
 * 
 * @author ľľ
 * 
 */
public class DiagramEditor extends GraphicalEditorWithFlyoutPalette
// implements ISaveablePart2
{
	// �༭��ID
	public static final String ID = "dev.diagram.ui.DiagramEditor";
	// ��ͼ����ʾ���ݣ�����ģ��
	private ContentsModel model;
	// �����Ĺ���Id,�������ݿ������
	private String projectId;
	// ����ͼ��Id
	private String diagramId;
	// ����ͼxml�ļ�����
	private String fileName;
	// ������ͼ
	GraphicalViewer viewer;

	public DiagramEditor() {
		// ���ñ༭��
		setEditDomain(new DefaultEditDomain(this));

	}

	@Override
	protected void setPartName(String partName) {
		//setPartName("AAA");
		//super.setPartName(partName);
	}

	/**
	 * ������ͼ
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		// ScalableRootEditPart�����������й�����������
		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		viewer = getGraphicalViewer();
		// ���ø�������
		viewer.setRootEditPart(rootEditPart);
		// ���ÿ���������
		viewer.setEditPartFactory(new PartFactory());
		// ��������ɼ�
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, true);
		// ���ü�������
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, true);
		getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED,
				true);
		// ���zoomManager
		ZoomManager manager = rootEditPart.getZoomManager();
		// ���÷ǰٷֱ�����
		ArrayList<String> zoomcontributions = new ArrayList<String>();
		zoomcontributions.add(ZoomManager.FIT_ALL);
		zoomcontributions.add(ZoomManager.FIT_HEIGHT);
		zoomcontributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(zoomcontributions);
		// ע��Ŵ�IAction
		IAction action = new ZoomInAction(manager);
		getActionRegistry().registerAction(action);
		// ע����СIAction
		action = new ZoomOutAction(manager);
		getActionRegistry().registerAction(action);

		getGraphicalViewer().setKeyHandler(
				new GraphicalViewerKeyHandler(getGraphicalViewer())
						.setParent(getCommonKeyHandler()));
	}

	/**
	 * ��ʼ����ͼ�༭��
	 */
	protected void initializeGraphicalViewer() {
		// ��ʼ��ģ��
		// initModel();
		// ����ContentsModelģ������ͼ����ʾ����

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
	 * MyContentOutlinePage����ʵ�ִ�ٺ�ӥ����
	 * 
	 * @author ľľ
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
	 * ����IAction
	 */
	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		IAction action;
		action = new DirectEditAction((IWorkbenchPart) this);
		registry.registerAction(action);
		// ע���Զ�����action
		action = new GraphAutoLayout(this, model);
		action.setId(GraphAutoLayout.ID);
		registry.registerAction(action);
		// getSelectionActions().add(action.getId());
		// ע��д�����ݿ�action
		action = new GenerateCodeAction(model);
		registry.registerAction(action);
		// ע��SaveAction
		action = new SaveAction(model, this);
		registry.registerAction(action);
	}

	/**
	 * ��ʼ�� ���̱�ţ�id��projectId,����ͼ��ţ�id��diagramId���洢��xml�ļ�·��������fileName
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		// �������̱�ţ�Id��
		projectId = ((DiagramEditorInput) input).getProjectId();
		// ����ͼ�ı�ţ�Id��
		diagramId = ((DiagramEditorInput) input).getDiagramId();
		// �������ƣ�Ŀ���ǿ��Դ������еõ��洢xml��·��
		String projectName = ((DiagramEditorInput) input).getProjectName();
		// xml�ļ�������·�������ƣ��洢�ڹ�����xml�ļ�����
		fileName = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(projectName).getLocationURI().toString()
				+ "/XML/" + diagramId + ".xml";
		fileName = fileName.substring(6);
		File file = new File(fileName);
		File parent = file.getParentFile();
		// ·���Ƿ�����������������·����
		if (parent != null && !file.exists())
			parent.mkdir();
		if (file.exists())
			file.delete();
		try {
			// �½��ļ�
			file.createNewFile();

		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
		model = new ContentsModel(projectId, diagramId, fileName);
		super.init(site, input);
	}

	/**
	 * ��ʼ������ģ���е�����ͼ��Ϣ
	 * 
	 * @param contentsModel
	 */
	public void initModel() {
		String tfmName;
		String tfmDesc;
		String tfmType;
		String tfmTradeId;
		// ���ݿ�����
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

		dbConnectImpl.setAutoCommit(false);
		// �����ݿ���ȡ������ͼ����Ϣ
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
				// ��������ģ����
				model.init(tfmName, tfmDesc, tfmType, tfmTradeId);
				// ������ݿ�������ͼ��xml�ļ��Ķ������ļ�
				Blob blob = rs.getBlob(5);
				// �������ļ�Ϊ��ֱ���˳�
				if (blob == null) {
					return;
				}
				// ������
				InputStream ins = blob.getBinaryStream();
				File file = new File(fileName);
				// �����
				OutputStream fout = new FileOutputStream(file);
				// �����С
				byte[] b = new byte[1024];
				int len = 0;
				// �������Ƶ�xml�ļ�д�����
				while ((len = ins.read(b)) != -1)
					fout.write(b, 0, len);
				// �ر������
				fout.close();
				// �ر������
				ins.close();
				// ���д����ļ���Ϊ�գ����Ǵ��Ѿ����ڵ�����ͼ��������ͼ����Ϣд��ģ��
				if (file.length() != -1)
					GenerateTfmBean.BeanToModel(model, fileName);
				// ���ݿ��ύ
				dbConnectImpl.commit();
				// �ر����ݿ�����
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
	// "Close Virtual Terminal", "�����Ѿ��޸ģ���Ҫ������?");
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
