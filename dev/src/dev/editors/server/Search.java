package dev.editors.server;

import java.sql.SQLException;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import dev.db.service.EditorsDaoServiceImpl;
import dev.model.base.ResourceLeafNode;
/**
 * 2013.11.7
 * 1.修改Search类，在“查询”按钮的响应中，增加判断，
 * 若在查找到指定的记录后，若要在当前编辑器中显示，且当前编辑器为dirty，
 * 则询问用户是否先保存当前修改或者取消，若是，则先保存当前的修改，
 * 然后显示查询到的记录，若否，则不保存当前修改，直接显示查询到的记录，
 * 若取消，则什么也不做，对“查询”按钮的响应就此返回。
 * 2.修改Search类，在查找到指定的记录后，将对setSaveButtonEnable方法的调用，
 * 替换为对setDirty方法的调用。
 * 2013.11.1
 * 该类实现了在编辑中显示查询所涉及的控件，
 * 以及完成查询功能的方法，通过在编辑器中使用该类的实例，
 * 即可以完成编辑器所需要的查询功能，
 * 从而不需要在编辑器定义查询所涉及的控件，以及实现查询方法。
 * 使用方法：
 * 1.编辑器需要实现ISearch接口；
 * 2.给编辑器类新增一个私有的Search类型的成员变量；（以下假定该成员变量的名字为search）
 * 3.在编辑器类的createPartControl()方法中依次调用：
 * search = new Search();
 * search.setUpProject();
 * search.setEditorId();
 * search.setEditor();
 * search.createPartControl();
 * 注意：需要将编辑器中所使用的map中的诸如“serverid”的键改为“id”，
 * 将诸如“servername”的键改为“name”。
 */
public class Search 
{
	private ISearch editor;
	private Text upProjectText;
	private Text searchText;
	private Button searchByIdRadioButton;
	private Button searchByNameRadioButton;
	private String upProject;
	private Button searchButton;
	private String editorId;
	private HashMap<String, String> map;
	private String editorType;
	/**
	 * 设置Search对象所嵌入的编辑器的ID。通过该方法可以让Search对象知道当前所嵌入的编辑器时哪一类交易资源的编辑器
	 * @param editorId 如ServerEditor.ID
	 */
	public void setEditorId(String editorId)
	{
		this.editorId = editorId;
	}
	/**
	 * 设置所属工程。目的是让Search对象知道当前编辑器中的交易资源是属于哪一个工程，知道了这一点，在搜索时，就只会搜索同一工程下的交易资源
	 * @param upProject
	 */
	public void setUpProject(String upProject)
	{
		this.upProject = upProject;
	}
	/**
	 * 将当前的编辑器对象传进来，传进来后，即可在Search对象中调用该编辑器中实现的ISearch接口的方法
	 * @param editor
	 */
	public void setEditor(ISearch editor)
	{
		this.editor = editor;
	}
	/**
	 * 返回所属工程
	 * @return
	 */
	public String getUpProject()
	{
		return upProject;
	}
	/**
	 * 该方法中创建了搜索group中需要的控件，并且为搜索按钮添加了监听器
	 * @param group 指定将这些搜索需要的控件放入哪个group
	 */
	public void createPartControl(Group group)
	{
		Label upProjectLabel = new Label(group, SWT.NONE);
		upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		upProjectLabel.setText("所属工程");

		upProjectText = new Text(group, SWT.BORDER);
		upProjectText.setEnabled(false);
		GridData gd_serverUpProjectText = new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1);
		gd_serverUpProjectText.widthHint = 70;
		upProjectText.setLayoutData(gd_serverUpProjectText);
		upProjectText.setText(upProject);

		Label searchTypeLabel = new Label(group, SWT.NONE);
		searchTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		searchTypeLabel.setText("查询方式");

		searchByIdRadioButton = new Button(group, SWT.RADIO);
		searchByIdRadioButton.setSelection(true);
		searchByIdRadioButton.setText("标识");

		searchByNameRadioButton = new Button(group, SWT.RADIO);
		searchByNameRadioButton.setText("名称");

		searchText = new Text(group, SWT.BORDER);
		GridData gd_searchText = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_searchText.widthHint = 70;
		searchText.setLayoutData(gd_searchText);

		searchButton = new Button(group, SWT.NONE);
		GridData gd_searchButton = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_searchButton.widthHint = 80;
		searchButton.setLayoutData(gd_searchButton);
		searchButton.setText("查询");
		
		searchButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				//若搜索框内容为空，则弹出对话框进行提示，然后该函数返回
				if(searchText.getText().isEmpty())
				{
					editor.showMessage(SWT.ICON_INFORMATION | SWT.YES, "提示", "查询内容不能为空");
					return;
				}
				//以下for循环用于判断所查询的服务程序是否已由对应的编辑器打开，若是，则将该编辑器放到前端显示，然后该函数返回
				IEditorReference[] editorReference = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditors(null,editorId,IWorkbenchPage.MATCH_ID);
				for(IEditorReference tmp : editorReference)
				{
					ISearch searchEditor = (ISearch)tmp.getEditor(false);
					if(searchEditor.getSearch().getUpProject().equals(upProject))
					{
						if(searchByIdRadioButton.getSelection())
						{
							if(searchEditor.getTargetId().equals(searchText.getText()))
							{
								PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().bringToTop((IWorkbenchPart)searchEditor);
								return;
							}
						}
						else
						{
							if(searchEditor.getTargetName().equals(searchText.getText()))
							{
								PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().bringToTop((IWorkbenchPart)searchEditor);
								return;
							}
						}
					}
				}
				//以下代码在数据库中查找搜索的信息
				EditorsDaoServiceImpl editorsDaoServiceImpl = new EditorsDaoServiceImpl();
				try
				{
					switch(editorId)
					{
					case ServerEditor.ID:
						editorType = "服务程序";
						if(searchByIdRadioButton.getSelection())
						{
							map = editorsDaoServiceImpl.queryServerByIdOrName(searchText.getText(), 0, editor.getPs());
						}
						else
						{
							map = editorsDaoServiceImpl.queryServerByIdOrName(searchText.getText(), 1, editor.getPs());
						}
						break;
					}
					
				} catch (SQLException e2)
				{
					e2.printStackTrace();
				}
				//若找到
				if(map != null)
				{
					if(((EditorPart)editor).isDirty())
					{
						int choice = editor.showMessage(SWT.ICON_INFORMATION|SWT.YES|SWT.NO|SWT.ICON_CANCEL, "提示", "是否先保存当前修改？");
						if(choice == SWT.YES)
						{
							editor.mySave();
						}
						else if(choice == SWT.CANCEL)
						{
							return;
						}
					}
					editor.setTargetMap(map);
					//为控件赋值
					editor.setControlsText();
//					editor.setSaveButtonEnable(false);
					editor.setDirty(false);
					//设置该编辑器标签的标题
					editor.setMyPartName(editorType + " " + map.get("ID") + " " + "所属工程" + " " + upProject);
					//设置工具的标题
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setText("GOLP TOOL" + " " + editorType + " " + map.get("ID") + " " + "所属工程" + " " + upProject);
					ResourceLeafNode myNode = new ResourceLeafNode(map.get("NAME"),map.get("ID"),editor.getThisNode().getParent());
					//更改thisNode
					editor.setThisNode(myNode);
					//更改input
					editor.setInputNode(myNode);
					//更新导航视图的焦点
					editor.setSelectNode(myNode);
					//相当于点击了“锁定”按钮
					editor.setEnable(false);
					editor.setUnLockButtonText("编辑");
				}
				//若未找到，则弹出对话框提示
				else
				{
					editor.showMessage(SWT.ICON_INFORMATION | SWT.YES, "抱歉", "未找到记录");
				}
			}
		});
	}

}
