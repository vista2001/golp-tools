/* 文件名：       NewServerWizardPage1.java
 * 修改人：       rxy
 * 修改时间：   2013.11.29
 * 修改内容：    1.initData中sql语句中的DLL改为aopdll;
 *         2.在此向导页中增加对AopRetVal（原子交易返回值）的处理。
 */

package dev.wizards.newAop;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
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

import dev.db.pojo.TAopDll;
import dev.db.service.CommonDialogServiceImpl;
import dev.golpDialogs.InputDataItemDialog;
import dev.golpDialogs.OutputDataItemDialog;
import dev.golpEvent.InformDialogEvent;
import dev.golpEvent.InformDialogListener;

public class NewAopWizardPage1 extends WizardPage
{

    private ISelection selection;

    private Text inputData;
    private Text outPutData;
    private Text aopPreConditionText;
    private Text aopPostConditionText;
    private Combo aopErrRecoverCombo;
    private Combo upDllCombo;
    private Text aopRetValText;
    
    public Text getAopRetValText()
    {
        return aopRetValText;
    }

    public ISelection getSelection()
    {
        return selection;
    }

    public Text getInputData()
    {
        return inputData;
    }

    public Text getOutPutData()
    {
        return outPutData;
    }

    public Text getAopPreConditionText()
    {
        return aopPreConditionText;
    }

    public Text getAopPostConditionText()
    {
        return aopPostConditionText;
    }

    public Combo getAopErrRecoverCombo()
    {
        return aopErrRecoverCombo;
    }

    public Combo getUpDllCombo()
    {
        return upDllCombo;
    }

    public NewAopWizardPage1(ISelection selection)
    {
        super("NewAopWizardPage1");
        setTitle("新建原子交易向导");
        setDescription("这个向导将指导你完成GOLP原子交易的创建");
        this.selection = selection;
    }

    @Override
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        container.setLayout(new GridLayout(3, false));

        Label upDllComboLabel = new Label(container, SWT.NONE);
        upDllComboLabel.setText("*所属动态库：");
        upDllCombo = new Combo(container, SWT.READ_ONLY);
        upDllCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));
        upDllCombo.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        new Label(container, SWT.NONE);

        Label aopErrRecoverLabel = new Label(container, SWT.NONE);
        aopErrRecoverLabel.setText("*AOP恢复机制：");
        aopErrRecoverCombo = new Combo(container, SWT.READ_ONLY);
        aopErrRecoverCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 1, 1));
        aopErrRecoverCombo.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        aopErrRecoverCombo.setItems(new String[] { "0-第一种", "1-第二种" });

        new Label(container, SWT.NONE);

        Label inputDataLabel = new Label(container, SWT.NONE);
        inputDataLabel.setText("*输入数据项：");
        inputData = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        inputData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
        inputData.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        Button inputDataBtn = new Button(container, SWT.NONE);
        inputDataBtn.setText("...");
        inputDataBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProjectId = ((NewAopWizardPage0)getWizard().getPage("NewAopWizardPage0")).getUpProjectCombo().getText();
                InputDataItemDialog inputDataItemDialog = new InputDataItemDialog(
                        e.display.getActiveShell(), e.getSource(),
                        inputData.getText(), upProjectId);
                inputDataItemDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((InputDataItemDialog) dm
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
                                inputData.setText(s);
                            }
                        });
                inputDataItemDialog.open();
            }
        });

        Label outputDataLabel = new Label(container, SWT.NONE);
        outputDataLabel.setText("*输出数据项：");
        outPutData = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        outPutData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));
        outPutData.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        Button outputDataBtn = new Button(container, SWT.NONE);
        outputDataBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProjectId = ((NewAopWizardPage0)getWizard().getPage("NewAopWizardPage0")).getUpProjectCombo().getText();
                OutputDataItemDialog outputDataItemDialog = new OutputDataItemDialog(
                        e.display.getActiveShell(), e.getSource(), outPutData.getText(), upProjectId);
                outputDataItemDialog.addInformDialogListener(new InformDialogListener()
                {

                    @Override
                    public void handleEvent(InformDialogEvent dm)
                    {
                        java.util.List<String> l = ((OutputDataItemDialog) dm
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
                        outPutData.setText(s);
                    }
                });
                outputDataItemDialog.open();
            }
        });
        outputDataBtn.setText("...");

        Label aopRetValLabel = new Label(container, SWT.NONE);
        aopRetValLabel.setText("*原子交易返回值：");
        aopRetValLabel.setToolTipText("返回值必须是整数，值之间用竖线分隔，默认值放在第一个");

        aopRetValText = new Text(container, SWT.BORDER);
        aopRetValText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));
        new Label(container, SWT.NONE);
        aopRetValText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });

        Label aopPreConditionLabel = new Label(container, SWT.NONE);
        aopPreConditionLabel.setText("前置条件：");
        aopPreConditionText = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_text.heightHint = 58;
        aopPreConditionText.setLayoutData(gd_text);
        new Label(container, SWT.NONE);

        Label aopPostConditionLabel = new Label(container, SWT.NONE);
        aopPostConditionLabel.setText("后置条件：");
        aopPostConditionText = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        GridData gd_aopPostConditionText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
                1);
        gd_aopPostConditionText.heightHint = 68;
        aopPostConditionText.setLayoutData(gd_aopPostConditionText);
        new Label(container, SWT.NONE);

        // 控件创建后初始化数据
//        initData();
    }

    public boolean validInput()
    {
        if (getAopErrRecoverCombo().getText().length() == 0
                || getInputData().getText().length() == 0
                || getOutPutData().getText().length() == 0
                || getUpDllCombo().getText().length() == 0
                || aopRetValText.getText().length() == 0)
            return false;
        return true;
    }

    /*
     * @Override public boolean canFlipToNextPage() { //return validInput();
     * return false; }
     */

    // 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法
    private void dialogChanged()
    {
        setPageComplete(true);
    }

    protected void initData(String prjId)
    {
        upDllCombo.removeAll();
        CommonDialogServiceImpl commonDialogServiceImpl = new CommonDialogServiceImpl();
        try
        {
            List<TAopDll> aopDlls = commonDialogServiceImpl.aopDllQuery(prjId);
            for(TAopDll aopDll : aopDlls)
            {
                upDllCombo.add(aopDll.getAopDllId() + "");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
}
