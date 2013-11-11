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
 * 1.�޸�Search�࣬�ڡ���ѯ����ť����Ӧ�У������жϣ�
 * ���ڲ��ҵ�ָ���ļ�¼����Ҫ�ڵ�ǰ�༭������ʾ���ҵ�ǰ�༭��Ϊdirty��
 * ��ѯ���û��Ƿ��ȱ��浱ǰ�޸Ļ���ȡ�������ǣ����ȱ��浱ǰ���޸ģ�
 * Ȼ����ʾ��ѯ���ļ�¼�������򲻱��浱ǰ�޸ģ�ֱ����ʾ��ѯ���ļ�¼��
 * ��ȡ������ʲôҲ�������ԡ���ѯ����ť����Ӧ�ʹ˷��ء�
 * 2.�޸�Search�࣬�ڲ��ҵ�ָ���ļ�¼�󣬽���setSaveButtonEnable�����ĵ��ã�
 * �滻Ϊ��setDirty�����ĵ��á�
 * 2013.11.1
 * ����ʵ�����ڱ༭����ʾ��ѯ���漰�Ŀؼ���
 * �Լ���ɲ�ѯ���ܵķ�����ͨ���ڱ༭����ʹ�ø����ʵ����
 * ��������ɱ༭������Ҫ�Ĳ�ѯ���ܣ�
 * �Ӷ�����Ҫ�ڱ༭�������ѯ���漰�Ŀؼ����Լ�ʵ�ֲ�ѯ������
 * ʹ�÷�����
 * 1.�༭����Ҫʵ��ISearch�ӿڣ�
 * 2.���༭��������һ��˽�е�Search���͵ĳ�Ա�����������¼ٶ��ó�Ա����������Ϊsearch��
 * 3.�ڱ༭�����createPartControl()���������ε��ã�
 * search = new Search();
 * search.setUpProject();
 * search.setEditorId();
 * search.setEditor();
 * search.createPartControl();
 * ע�⣺��Ҫ���༭������ʹ�õ�map�е����硰serverid���ļ���Ϊ��id����
 * �����硰servername���ļ���Ϊ��name����
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
	 * ����Search������Ƕ��ı༭����ID��ͨ���÷���������Search����֪����ǰ��Ƕ��ı༭��ʱ��һ�ཻ����Դ�ı༭��
	 * @param editorId ��ServerEditor.ID
	 */
	public void setEditorId(String editorId)
	{
		this.editorId = editorId;
	}
	/**
	 * �����������̡�Ŀ������Search����֪����ǰ�༭���еĽ�����Դ��������һ�����̣�֪������һ�㣬������ʱ����ֻ������ͬһ�����µĽ�����Դ
	 * @param upProject
	 */
	public void setUpProject(String upProject)
	{
		this.upProject = upProject;
	}
	/**
	 * ����ǰ�ı༭�����󴫽������������󣬼�����Search�����е��øñ༭����ʵ�ֵ�ISearch�ӿڵķ���
	 * @param editor
	 */
	public void setEditor(ISearch editor)
	{
		this.editor = editor;
	}
	/**
	 * ������������
	 * @return
	 */
	public String getUpProject()
	{
		return upProject;
	}
	/**
	 * �÷����д���������group����Ҫ�Ŀؼ�������Ϊ������ť����˼�����
	 * @param group ָ������Щ������Ҫ�Ŀؼ������ĸ�group
	 */
	public void createPartControl(Group group)
	{
		Label upProjectLabel = new Label(group, SWT.NONE);
		upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		upProjectLabel.setText("��������");

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
		searchTypeLabel.setText("��ѯ��ʽ");

		searchByIdRadioButton = new Button(group, SWT.RADIO);
		searchByIdRadioButton.setSelection(true);
		searchByIdRadioButton.setText("��ʶ");

		searchByNameRadioButton = new Button(group, SWT.RADIO);
		searchByNameRadioButton.setText("����");

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
		searchButton.setText("��ѯ");
		
		searchButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				//������������Ϊ�գ��򵯳��Ի��������ʾ��Ȼ��ú�������
				if(searchText.getText().isEmpty())
				{
					editor.showMessage(SWT.ICON_INFORMATION | SWT.YES, "��ʾ", "��ѯ���ݲ���Ϊ��");
					return;
				}
				//����forѭ�������ж�����ѯ�ķ�������Ƿ����ɶ�Ӧ�ı༭���򿪣����ǣ��򽫸ñ༭���ŵ�ǰ����ʾ��Ȼ��ú�������
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
				//���´��������ݿ��в�����������Ϣ
				EditorsDaoServiceImpl editorsDaoServiceImpl = new EditorsDaoServiceImpl();
				try
				{
					switch(editorId)
					{
					case ServerEditor.ID:
						editorType = "�������";
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
				//���ҵ�
				if(map != null)
				{
					if(((EditorPart)editor).isDirty())
					{
						int choice = editor.showMessage(SWT.ICON_INFORMATION|SWT.YES|SWT.NO|SWT.ICON_CANCEL, "��ʾ", "�Ƿ��ȱ��浱ǰ�޸ģ�");
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
					//Ϊ�ؼ���ֵ
					editor.setControlsText();
//					editor.setSaveButtonEnable(false);
					editor.setDirty(false);
					//���øñ༭����ǩ�ı���
					editor.setMyPartName(editorType + " " + map.get("ID") + " " + "��������" + " " + upProject);
					//���ù��ߵı���
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setText("GOLP TOOL" + " " + editorType + " " + map.get("ID") + " " + "��������" + " " + upProject);
					ResourceLeafNode myNode = new ResourceLeafNode(map.get("NAME"),map.get("ID"),editor.getThisNode().getParent());
					//����thisNode
					editor.setThisNode(myNode);
					//����input
					editor.setInputNode(myNode);
					//���µ�����ͼ�Ľ���
					editor.setSelectNode(myNode);
					//�൱�ڵ���ˡ���������ť
					editor.setEnable(false);
					editor.setUnLockButtonText("�༭");
				}
				//��δ�ҵ����򵯳��Ի�����ʾ
				else
				{
					editor.showMessage(SWT.ICON_INFORMATION | SWT.YES, "��Ǹ", "δ�ҵ���¼");
				}
			}
		});
	}

}
