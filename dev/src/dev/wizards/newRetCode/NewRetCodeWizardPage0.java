/* �ļ�����       NewRetCodeWizardPage0.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.12
 * �޸����ݣ�   1.��ԭ����NewRetCodeWizardPage1.java�еġ��������̡��ŵ���ǰ��ǰҳ�У�
 *         2.���ӡ���Ӧ�뼶��һ�
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
        setTitle("�½���Ӧ����");
        setDescription("����򵼽�ָ�������GOLP��Ӧ��Ĵ���");
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
        retCodeUpProjectLabel.setText("*�������̣�");

        retCodeUpProjectCombo = new Combo(container, SWT.READ_ONLY);
        retCodeUpProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 1, 1));
        initRetCodeUpProjectCombo();
        addModifyListener(retCodeUpProjectCombo);

        Label retCodeLevelLabel = new Label(container, SWT.NONE);
        retCodeLevelLabel.setText("*��Ӧ�뼶��");

        retCodeLevelCombo = new Combo(container, SWT.READ_ONLY);
        retCodeLevelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 1, 1));
        retCodeLevelCombo.add("0-APP");
        addModifyListener(retCodeLevelCombo);

        Label retCodeIdLabel = new Label(container, SWT.NONE);
        retCodeIdLabel.setText("*��Ӧ���ʶ����");

        retCodeIdText = new Text(container, SWT.BORDER);
        retCodeIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));
        addModifyListener(retCodeIdText);

        Label retCodeValueLabel = new Label(container, SWT.NONE);
        retCodeValueLabel.setText("*��Ӧ��ֵ��");

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
                    setErrorMessage("��Ӧ��ֵΪ5λ����");
                }
            }
        });
        retCodeValueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));
        addModifyListener(retCodeValueText);

        Label retCodeDescLabel = new Label(container, SWT.NONE);
        retCodeDescLabel.setText("*������");

        retCodeDescText = new Text(container, SWT.BORDER | SWT.WRAP
                | SWT.V_SCROLL | SWT.MULTI);
        retCodeDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 1, 1));
        addModifyListener(retCodeDescText);
    }

    /**
     * Ϊ�ؼ����addModifyListener��
     * 
     * @param control
     *            Ҫ���addModifyListener�Ŀؼ�
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
     * �÷�������ർ����ͼ�е����й���ID����ʾ�ڡ��������̡������б��С�
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

    // �˴�������Ϊtrue�������ǻ�����±ߵ�canFlipToNextPage()����
    private void dialogChanged()
    {
        setPageComplete(true);
    }

    /**
     * �жϱ������Ƿ������롣
     * 
     * @return ������������룬����true�����򷵻�flase��
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
