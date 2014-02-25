/* 文件名：       NewServerWizardPage6.java
 * 描述：           该文件定义了类NewServerWizardPage6，该类实现了在新建服务程序向导中，
 *         让用户输入个性依赖宏定义的功能。
 * 创建人：       rxy
 * 创建时间：   2013.11.27
 */

package dev.wizards.newServer;

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

/**
 * NewServerWizardPage6类，提供了用户在新建服务程序向导中，输入个性依赖宏定义的功能
 * @author rxy
 */
public class NewServerWizardPage6 extends WizardPage
{
    private ISelection selection;
    private Text serverSpecMarcoText;
    private List serverSpecMarcoList;
    private Button delButton;
    private Button upButton;
    private Button downButton;

    public ISelection getSelection()
    {
        return selection;
    }

    public List getServerSpecMarcoList()
    {
        return serverSpecMarcoList;
    }

    public NewServerWizardPage6(ISelection selection)
    {
        super("NewServerWizardPage6");
        setTitle("新建服务程序向导");
        setDescription("这个向导将指导你完成GOLP服务程序的创建。" 
                       + System.getProperty("line.separator") 
                       + "格式：标识符=值，若值为字符串，" 
                       + "则格式为为：标识符=\"值\",使用英文双引号。");
        this.selection = selection;
    }

    @Override
    public void createControl(Composite parent)
    {
        
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        container.setLayout(new GridLayout(3, false));

        Label serverSpecMarcoLabel = new Label(container, SWT.NONE);
        serverSpecMarcoLabel.setText("服务程序个性依赖宏定义：");

        serverSpecMarcoText = new Text(container, SWT.BORDER);
        serverSpecMarcoText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 1, 1));
        serverSpecMarcoText.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if((serverSpecMarcoText.getText().trim().isEmpty() == false)
                    && (e.keyCode == SWT.CR))
                {
                    serverSpecMarcoList.add(serverSpecMarcoText.getText());
                    serverSpecMarcoText.setText("");
                    dialogChanged();
                }
            }
        });

        Button addButton = new Button(container, SWT.NONE);
        GridData gd_addButton = new GridData(SWT.LEFT, SWT.CENTER, false,
                false, 1, 1);
        gd_addButton.widthHint = 60;
        addButton.setLayoutData(gd_addButton);
        addButton.setText("添加");
        addButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (!serverSpecMarcoText.getText().isEmpty())
                {

                    serverSpecMarcoList.add(serverSpecMarcoText.getText());
                    serverSpecMarcoText.setText("");
                    dialogChanged();
                }
            }
        });
        new Label(container, SWT.NONE);

        serverSpecMarcoList = new List(container, SWT.BORDER | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.MULTI);
        serverSpecMarcoList.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
                true, true, 1, 3));
        serverSpecMarcoList.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (serverSpecMarcoList.getSelectionIndices().length > 0)
                {
                    delButton.setEnabled(true);
                    if (serverSpecMarcoList.getSelectionIndices().length == 1)
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
        delButton.setText("移除");
        new Label(container, SWT.NONE);

        delButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                int[] indices = serverSpecMarcoList.getSelectionIndices();
                serverSpecMarcoList.remove(indices);
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
        upButton.setText("上移");
        new Label(container, SWT.NONE);
        upButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                int index = serverSpecMarcoList.getSelectionIndex();
                if (index > 0)
                {
                    String tmp = serverSpecMarcoList.getItem(index);
                    serverSpecMarcoList.setItem(index,
                            serverSpecMarcoList.getItem(index - 1));
                    serverSpecMarcoList.setItem(index - 1, tmp);
                    serverSpecMarcoList.setSelection(index - 1);
                }
            }
        });

        downButton = new Button(container, SWT.NONE);
        downButton.setEnabled(false);
        GridData gd_downButton = new GridData(SWT.LEFT, SWT.TOP, false, false,
                1, 1);
        gd_downButton.widthHint = 60;
        downButton.setLayoutData(gd_downButton);
        downButton.setText("下移");
        downButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                int index = serverSpecMarcoList.getSelectionIndex();
                if (index < serverSpecMarcoList.getItemCount() - 1)
                {
                    String tmp = serverSpecMarcoList.getItem(index);
                    serverSpecMarcoList.setItem(index,
                            serverSpecMarcoList.getItem(index + 1));
                    serverSpecMarcoList.setItem(index + 1, tmp);
                    serverSpecMarcoList.setSelection(index + 1);
                }
            }
        });

    }

    // 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法
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
