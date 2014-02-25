/* �ļ�����       NewTfmWizardPage0.java
 * ������           ���ļ���������NewTfmWizardPage0������ʵ�����½�����ͼ�򵼵���ҳ��
 * �����ˣ�       rxy
 * ����ʱ�䣺   2013.12.10
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
 * ���ඨ�����½�����ͼ�򵼵���ҳ��
 */
public class NewTfmWizardPage0 extends WizardPage
{
    /**
     *  ������������ͼ�������̵������б�
     */
    private Combo tfmUpProjectCombo;

    /**
     * ������������ͼ���͵������б�
     */
    private Combo tfmTypeCombo;

    /**
     *  ������������ͼ��ʶ�����ı���
     */
    private Text tfmIdText;

    /**
     *  ������������ͼ���Ƶ��ı���
     */
    private Text tfmNameText;

    /**
     *  ������������ͼ������Ϣ���ı���
     */
    private Text tfmDescText;

    private ISelection selection;

    /**
     *  ������ʾ�󶨽��׵��ı���
     */
    private Text bindingTradeIdText;
    
    /**
     * �������ð󶨽��׵İ�ť
     */
    private Button bindingTradeIdButton;

    public NewTfmWizardPage0(ISelection selection)
    {
        super("NewTfmWizardPage0");
        setTitle("�½�����ͼ��");
        setDescription("����򵼽�ָ�������GOLP����ͼ�Ĵ���");
        this.selection = selection;
    }

    public Combo getTfmUpProjectCombo()
    {
        return tfmUpProjectCombo;
    }

    /**
     * ���á��������̡������б�����ʾ�����ݡ� ����������ͼ�еĹ��̵�id������Щid��ӵ����������̡������б��С�
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
        tfmUpProjectLabel.setText("*�������̣�");

        tfmUpProjectCombo = new Combo(container, SWT.READ_ONLY);
        tfmUpProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                                                     true, false, 1, 1));
        setTfmUpProjectCombo();
        new Label(container, SWT.NONE);

        Label bindingTradeIdLabel = new Label(container, SWT.NONE);
        bindingTradeIdLabel.setText("*���װ󶨣�");
        
        bindingTradeIdText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        bindingTradeIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        bindingTradeIdButton = new Button(container, SWT.NONE);
        bindingTradeIdButton.setEnabled(false);
        bindingTradeIdButton.setText("...");

        Label tfmTypeLabel = new Label(container, SWT.NONE);
        tfmTypeLabel.setText("*����ͼ���ͣ�");

        tfmTypeCombo = new Combo(container, SWT.READ_ONLY);
        tfmTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                                                false, 1, 1));
        tfmTypeCombo.add("0-������");
        tfmTypeCombo.add("1-������");
        tfmTypeCombo.add("2-������");
        new Label(container, SWT.NONE);

        Label tfmIdLabel = new Label(container, SWT.NONE);
        tfmIdLabel.setText("*����ͼ��ʶ����");

        tfmIdText = new Text(container, SWT.BORDER);
        tfmIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                                             1, 1));
        new Label(container, SWT.NONE);

        Label tfmNameLabel = new Label(container, SWT.NONE);
        tfmNameLabel.setText("*����ͼ���ƣ�");

        tfmNameText = new Text(container, SWT.BORDER);
        tfmNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                                               false, 1, 1));
        new Label(container, SWT.NONE);

        Label tfmDescLabel = new Label(container, SWT.NONE);
        tfmDescLabel.setText("*������");

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
     * ���û�����������������б仯ʱ������ø÷�����
     */
    private void dialogChanged()
    {
        // �˴�������Ϊtrue�������ǻ�����±ߵ�canFlipToNextPage()����
        setPageComplete(true);
    }

    /**
     * �жϱ������Ƿ������롣
     * 
     * @return ������������룬����true�����򷵻�flase��
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
