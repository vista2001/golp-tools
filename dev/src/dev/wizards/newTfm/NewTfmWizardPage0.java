/* 文件名：       NewTfmWizardPage0.java
 * 描述：           该文件定义了类NewTfmWizardPage0，该类实现了新建流程图向导的向导页。
 * 创建人：       rxy
 * 创建时间：   2013.12.10
 */

package dev.wizards.newTfm;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import dev.golpDialogs.QueryTradeDialog;
import dev.golpEvent.InformDialogEvent;
import dev.golpEvent.InformDialogListener;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.util.Constants;
import dev.views.NavView;

/**
 * 该类定义了新建流程图向导的向导页。
 */
public class NewTfmWizardPage0 extends WizardPage
{
    /**
     *  用于设置流程图所属工程的下拉列表
     */
    private Combo tfmUpProjectCombo;

    /**
     * 用于设置流程图类型的下拉列表
     */
    private Combo tfmTypeCombo;

    /**
     *  用于输入流程图标识符的文本框
     */
    private Text tfmIdText;

    /**
     *  用于输入流程图名称的文本框
     */
    private Text tfmNameText;

    /**
     *  用于输入流程图描述信息的文本框
     */
    private Text tfmDescText;

    private ISelection selection;

    /**
     *  用于显示绑定交易的文本框
     */
    private Text bindingTradeIdText;
    
    /**
     * 用于设置绑定交易的按钮
     */
    private Button bindingTradeIdButton;

    public NewTfmWizardPage0(ISelection selection)
    {
        super("NewTfmWizardPage0");
        setTitle("新建流程图向导");
        setDescription("这个向导将指导你完成GOLP流程图的创建");
        this.selection = selection;
    }

    public Combo getTfmUpProjectCombo()
    {
        return tfmUpProjectCombo;
    }

    /**
     * 设置“所属工程”下拉列表所显示的内容。 遍历导航视图中的工程的id，将这些id添加到“所属工程”下拉列表中。
     */
    private void setTfmUpProjectCombo()
    {
        IViewPart viewPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
                NavView.ID);
        if (viewPart != null)
        {
            NavView v = (NavView) viewPart;
            TreeViewer treeViewer = v.getTreeViewer();
            RootNode root = (RootNode) treeViewer.getInput();
            List<TreeNode> projectNodes = root.getChildren();
            for (TreeNode treeNode : projectNodes)
            {
                tfmUpProjectCombo.add(treeNode.getName());
            }
        }
    }

    public Text getBindingTradeIdText()
    {
        return bindingTradeIdText;
    }

    public Combo getTfmTypeCombo()
    {
        return tfmTypeCombo;
    }

    public Text getTfmIdText()
    {
        return tfmIdText;
    }

    public Text getTfmNameText()
    {
        return tfmNameText;
    }

    public Text getTfmDescText()
    {
        return tfmDescText;
    }

    @Override
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        GridLayout gl_container = new GridLayout(3, false);
        container.setLayout(gl_container);

        Label tfmUpProjectLabel = new Label(container, SWT.NONE);
        tfmUpProjectLabel.setText("*所属工程：");

        tfmUpProjectCombo = new Combo(container, SWT.READ_ONLY);
        tfmUpProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                                                     true, false, 1, 1));
        setTfmUpProjectCombo();
        new Label(container, SWT.NONE);

        Label bindingTradeIdLabel = new Label(container, SWT.NONE);
        bindingTradeIdLabel.setText("*交易绑定：");
        
        bindingTradeIdText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        bindingTradeIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        bindingTradeIdButton = new Button(container, SWT.NONE);
        bindingTradeIdButton.setEnabled(false);
        bindingTradeIdButton.setText("...");

        Label tfmTypeLabel = new Label(container, SWT.NONE);
        tfmTypeLabel.setText("*流程图类型：");

        tfmTypeCombo = new Combo(container, SWT.READ_ONLY);
        tfmTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                                                false, 1, 1));
        tfmTypeCombo.add("0-非事务");
        tfmTypeCombo.add("1-单事务");
        tfmTypeCombo.add("2-多事务");
        new Label(container, SWT.NONE);

        Label tfmIdLabel = new Label(container, SWT.NONE);
        tfmIdLabel.setText("*流程图标识符：");

        tfmIdText = new Text(container, SWT.BORDER);
        tfmIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                                             1, 1));
        new Label(container, SWT.NONE);

        Label tfmNameLabel = new Label(container, SWT.NONE);
        tfmNameLabel.setText("*流程图名称：");

        tfmNameText = new Text(container, SWT.BORDER);
        tfmNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                                               false, 1, 1));
        new Label(container, SWT.NONE);

        Label tfmDescLabel = new Label(container, SWT.NONE);
        tfmDescLabel.setText("*描述：");

        tfmDescText = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
                                          | SWT.MULTI);
        tfmDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
                                               1, 1));
        new Label(container, SWT.NONE);

        tfmUpProjectCombo.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                if (tfmUpProjectCombo.getText().isEmpty())
                {
                    bindingTradeIdButton.setEnabled(false);
                }
                else
                {
                    bindingTradeIdButton.setEnabled(true);
                }
                dialogChanged();
            }
        });
        
        bindingTradeIdText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        
        bindingTradeIdButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProject = tfmUpProjectCombo.getText();
                QueryTradeDialog queryTradeDialog = new QueryTradeDialog(
                        e.display.getActiveShell(), e.getSource(),
                        upProject, -1, Constants.TFM_BINDING);
                queryTradeDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((QueryTradeDialog) dm
                                        .getdm()).getListForReturn();
                                String s = "";
                                for (String string : l)
                                {
                                    if (s.equals(""))
                                    {
                                        s += string;
                                    } else
                                    {
                                        s += "|" + string;
                                    }
                                }
                                bindingTradeIdText.setText(s);
                            }
                        });
                queryTradeDialog.open();
            }
        });

        tfmTypeCombo.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });

        tfmIdText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });

        tfmNameText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });

        tfmDescText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
    }

    /**
     * 当用户在向导中输入的内容有变化时，会调用该方法。
     */
    private void dialogChanged()
    {
        // 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法
        setPageComplete(true);
    }

    /**
     * 判断必填项是否都已填入。
     * 
     * @return 若必填项都已填入，返回true，否则返回flase。
     */
    public boolean isInputValid()
    {
        if (tfmUpProjectCombo.getText().isEmpty()
            || bindingTradeIdText.getText().isEmpty()
            || tfmIdText.getText().isEmpty()
            || tfmNameText.getText().isEmpty()
            || tfmDescText.getText().isEmpty()
            || tfmTypeCombo.getText().isEmpty())
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
