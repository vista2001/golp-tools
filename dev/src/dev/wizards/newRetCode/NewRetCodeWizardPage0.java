/* 文件名：       NewRetCodeWizardPage0.java
 * 修改人：       rxy
 * 修改时间：   2013.12.12
 * 修改内容：   1.将原本在NewRetCodeWizardPage1.java中的“所属工程”放到当前当前页中；
 *         2.增加“响应码级别”一项。
 */

package dev.wizards.newRetCode;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.util.RegExpCheck;
import dev.views.NavView;

public class NewRetCodeWizardPage0 extends WizardPage
{
    private Combo retCodeUpProjectCombo;
    private Combo retCodeLevelCombo;
    private Text retCodeIdText;
    private Text retCodeValueText;
    private Text retCodeDescText;
    private ISelection selection;

    public NewRetCodeWizardPage0(ISelection selection)
    {
        super("NewRetCodeWizardPage0");
        setTitle("新建响应码向导");
        setDescription("这个向导将指导你完成GOLP响应码的创建");
        this.selection = selection;
    }

    public Combo getRetCodeUpProjectCombo()
    {
        return retCodeUpProjectCombo;
    }

    public Combo getRetCodeLevelCombo()
    {
        return retCodeLevelCombo;
    }

    public Text getRetCodeIdText()
    {
        return retCodeIdText;
    }

    public Text getRetCodeValueText()
    {
        return retCodeValueText;
    }

    public Text getRetCodeDescText()
    {
        return retCodeDescText;
    }

    @Override
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        container.setLayout(new GridLayout(2, false));

        Label retCodeUpProjectLabel = new Label(container, SWT.NONE);
        retCodeUpProjectLabel.setText("*所属工程：");

        retCodeUpProjectCombo = new Combo(container, SWT.READ_ONLY);
        retCodeUpProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 1, 1));
        initRetCodeUpProjectCombo();
        addModifyListener(retCodeUpProjectCombo);

        Label retCodeLevelLabel = new Label(container, SWT.NONE);
        retCodeLevelLabel.setText("*响应码级别：");

        retCodeLevelCombo = new Combo(container, SWT.READ_ONLY);
        retCodeLevelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 1, 1));
        retCodeLevelCombo.add("0-APP");
        addModifyListener(retCodeLevelCombo);

        Label retCodeIdLabel = new Label(container, SWT.NONE);
        retCodeIdLabel.setText("*响应码标识符：");

        retCodeIdText = new Text(container, SWT.BORDER);
        retCodeIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));
        addModifyListener(retCodeIdText);

        Label retCodeValueLabel = new Label(container, SWT.NONE);
        retCodeValueLabel.setText("*响应码值：");

        retCodeValueText = new Text(container, SWT.BORDER);
        retCodeValueText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                if(RegExpCheck.isRetCodeValue(retCodeValueText.getText()))
                {
                    setErrorMessage(null);
                }
                else
                {
                    setErrorMessage("响应码值为5位整数");
                }
            }
        });
        retCodeValueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));
        addModifyListener(retCodeValueText);

        Label retCodeDescLabel = new Label(container, SWT.NONE);
        retCodeDescLabel.setText("*描述：");

        retCodeDescText = new Text(container, SWT.BORDER | SWT.WRAP
                | SWT.V_SCROLL | SWT.MULTI);
        retCodeDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 1, 1));
        addModifyListener(retCodeDescText);
    }

    /**
     * 为控件添加addModifyListener。
     * 
     * @param control
     *            要添加addModifyListener的控件
     */
    private void addModifyListener(Control control)
    {
        if (control instanceof Text)
        {
            ((Text) control).addModifyListener(new ModifyListener()
            {
                public void modifyText(ModifyEvent e)
                {
                    dialogChanged();
                }
            });
        } else if (control instanceof Combo)
        {
            ((Combo) control).addModifyListener(new ModifyListener()
            {
                public void modifyText(ModifyEvent e)
                {
                    dialogChanged();
                }
            });
        }
    }

    /**
     * 该方法将左侧导航视图中的所有工程ID，显示在“所属工程”下拉列表中。
     */
    private void initRetCodeUpProjectCombo()
    {
        IViewPart viewPart = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage()
                .findView(NavView.ID);
        if (viewPart != null)
        {
            NavView v = (NavView) viewPart;
            TreeViewer treeViewer = v.getTreeViewer();
            RootNode root = (RootNode) treeViewer.getInput();
            List<TreeNode> projectNodes = root.getChildren();
            for (TreeNode treeNode : projectNodes)
            {
                retCodeUpProjectCombo.add(treeNode.getName());
            }
        }
    }

    // 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法
    private void dialogChanged()
    {
        setPageComplete(true);
    }

    /**
     * 判断必填项是否都已填入。
     * 
     * @return 若必填项都已填入，返回true，否则返回flase。
     */
    public boolean isInputValid()
    {
        if (retCodeUpProjectCombo.getText().length() == 0
                || retCodeLevelCombo.getText().length() == 0
                || retCodeIdText.getText().length() == 0
                || RegExpCheck.isRetCodeValue(retCodeValueText.getText()) == false
                || retCodeDescText.getText().length() == 0)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean canFlipToNextPage()
    {
        return false;
    }

}
