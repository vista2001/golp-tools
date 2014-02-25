/* �ļ�����       NewServerWizardPage7.java
 * ������           ���ļ���������NewServerWizardPage7������ʵ�������½�����������У�
 *         ���û������������Ŀ���ļ��Ĺ��ܡ�
 * �����ˣ�       rxy
 * ����ʱ�䣺   2013.11.27
 */

package dev.wizards.newServer;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import dev.remote.RemoteDialog;

/**
 * NewServerWizardPage7�࣬�ṩ���û����½�����������У������������Ŀ���ļ��Ĺ��ܡ�
 * @author rxy
 */
public class NewServerWizardPage7 extends WizardPage
{
    private ISelection selection;
    private Text serverSpecObjText;
    private List serverSpecObjList;
    private Button delButton;
    private Button upButton;
    private Button downButton;

    public ISelection getSelection()
    {
        return selection;
    }

    public List getServerSpecObjList()
    {
        return serverSpecObjList;
    }

    public NewServerWizardPage7(ISelection selection)
    {
        super("NewServerWizardPage7");
        setTitle("�½����������");
        setDescription("����򵼽�ָ�������GOLP�������Ĵ���");
        this.selection = selection;
    }

    @Override
    public void createControl(Composite parent)
    {
        
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        container.setLayout(new GridLayout(3, false));

        Label serverSpecObjLabel = new Label(container, SWT.NONE);
        serverSpecObjLabel.setText("��������������Ŀ���ļ���");

        serverSpecObjText = new Text(container, SWT.BORDER);
        serverSpecObjText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 1, 1));
        serverSpecObjText.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if((serverSpecObjText.getText().trim().isEmpty() == false)
                    && (e.keyCode == SWT.CR))
                {
                    serverSpecObjList.add(serverSpecObjText.getText());
                    serverSpecObjText.setText("");
                    dialogChanged();
                }
            }
        });

        Button addButton = new Button(container, SWT.NONE);
        GridData gd_addButton = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_addButton.widthHint = 60;
        addButton.setLayoutData(gd_addButton);
        addButton.setText("���");
        addButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProject = ((NewServerWizardPage0) getWizard().getPage(
                        "NewServerWizardPage0")).getUpProjectCombo().getText();
                ArrayList<String> paths = new ArrayList<String>();
                RemoteDialog remoteDialog = new RemoteDialog(getShell(),
                        upProject, null, RemoteDialog.REMOTEDIALOG_FILE,
                        RemoteDialog.REMOTEDIALOG_MULTI, paths);
                remoteDialog.open();
                for (String str : paths)
                {
                    serverSpecObjList.add(str);
                }
                dialogChanged();
            }
        });
        new Label(container, SWT.NONE);

        serverSpecObjList = new List(container, SWT.BORDER | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.MULTI);
        serverSpecObjList.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
                true, true, 1, 3));
        serverSpecObjList.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (serverSpecObjList.getSelectionIndices().length > 0)
                {
                    delButton.setEnabled(true);
                    if (serverSpecObjList.getSelectionIndices().length == 1)
                    {
                        upButton.setEnabled(true);
                        downButton.setEnabled(true);
                    } else
                    {
                        upButton.setEnabled(false);
                        downButton.setEnabled(false);
                    }
                }
            }
        });

        delButton = new Button(container, SWT.NONE);
        delButton.setEnabled(false);
        GridData gd_delButton = new GridData(SWT.LEFT, SWT.TOP, false, false,
                1, 1);
        gd_delButton.widthHint = 60;
        delButton.setLayoutData(gd_delButton);
        delButton.setText("�Ƴ�");
        new Label(container, SWT.NONE);

        delButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                int[] indices = serverSpecObjList.getSelectionIndices();
                serverSpecObjList.remove(indices);
                delButton.setEnabled(false);
                upButton.setEnabled(false);
                downButton.setEnabled(false);
                dialogChanged();
            }
        });

        upButton = new Button(container, SWT.NONE);
        upButton.setEnabled(false);
        GridData gd_upButton = new GridData(SWT.LEFT, SWT.TOP, false, false, 1,
                1);
        gd_upButton.widthHint = 60;
        upButton.setLayoutData(gd_upButton);
        upButton.setText("����");
        new Label(container, SWT.NONE);
        upButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                int index = serverSpecObjList.getSelectionIndex();
                if (index > 0)
                {
                    String tmp = serverSpecObjList.getItem(index);
                    serverSpecObjList.setItem(index,
                            serverSpecObjList.getItem(index - 1));
                    serverSpecObjList.setItem(index - 1, tmp);
                    serverSpecObjList.setSelection(index - 1);
                }
            }
        });

        downButton = new Button(container, SWT.NONE);
        downButton.setEnabled(false);
        GridData gd_downButton = new GridData(SWT.LEFT, SWT.TOP, false, false,
                1, 1);
        gd_downButton.widthHint = 60;
        downButton.setLayoutData(gd_downButton);
        downButton.setText("����");
        downButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                int index = serverSpecObjList.getSelectionIndex();
                if (index < serverSpecObjList.getItemCount() - 1)
                {
                    String tmp = serverSpecObjList.getItem(index);
                    serverSpecObjList.setItem(index,
                            serverSpecObjList.getItem(index + 1));
                    serverSpecObjList.setItem(index + 1, tmp);
                    serverSpecObjList.setSelection(index + 1);
                }
            }
        });

    }

    // �˴�������Ϊtrue�������ǻ�����±ߵ�canFlipToNextPage()����
    private void dialogChanged()
    {
        setPageComplete(true);
    }

    @Override
    public boolean canFlipToNextPage()
    {
        return true;
    }
}
