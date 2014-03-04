/* �ļ�����       NewTradeWizardPage1.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.3
 * �޸����ݣ�   1.���Ӷ�TradeSrcPath��Դ�ļ�·�����Ĵ���
 *         2.��������������������ѡ���Ƿ����Ĺ��ܣ��������������������ѡ���Ƿ�������������Դ�Ĺ��ܣ�
 *         3.���ӷ���Զ��Ŀ¼�Ĺ��ܡ�
 */

package dev.wizards.newTrade;

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

import dev.golpDialogs.InputDataItemDialog;
import dev.golpDialogs.OutputDataItemDialog;
import dev.golpDialogs.QueryServerDialog;
import dev.golpDialogs.QueryTradeDialog;
import dev.golpDialogs.TradeSrcPathDialog;
import dev.golpEvent.InformDialogEvent;
import dev.golpEvent.InformDialogListener;
import dev.util.Constants;

public class NewTradeWizardPage1 extends WizardPage
{
    private ISelection selection;
    private Combo tradeModelCombo;
    private Combo tradeServerModelCombo;
    private Text inputDataText;
    private Text outputDataText;
    private Text tradeCallServiceText;
    private Label tradeSrcPathLabel;

    // Դ�ļ�·��
    private Text tradeSrcPathText;
    private Button tradeSrcPathBtn;
    private Text tradePreConditionText;
    private Text tradePostConditionText;
    private Text tradeUpServerText;

    public Text getTradeUpServerText()
    {
        return tradeUpServerText;
    }

    public Combo getTradeModelCombo()
    {
        return tradeModelCombo;
    }

    public Combo getTradeServerModelCombo()
    {
        return tradeServerModelCombo;
    }

    public Text getInputDataText()
    {
        return inputDataText;
    }

    public Text getOutputDataText()
    {
        return outputDataText;
    }

    public Text getTradeCallServiceText()
    {
        return tradeCallServiceText;
    }

    public Text getTradePreConditionText()
    {
        return tradePreConditionText;
    }

    public Text getTradePostConditionText()
    {
        return tradePostConditionText;
    }

    public Text getTradeSrcPathText()
    {
        return tradeSrcPathText;
    }

    public NewTradeWizardPage1(ISelection selection)
    {
        super("NewTradeWizardPage1");
        setTitle("�½�������");
        setDescription("����򵼽�ָ�������GOLP���׵Ĵ���");
        this.selection = selection;
    }

    @Override
    /**������ҳ��Ŀؼ�*/
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        container.setLayout(new GridLayout(3, false));

        Label tradeUpServerLabel = new Label(container, SWT.NONE);
        tradeUpServerLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        tradeUpServerLabel.setText("*�����������");
        
        tradeUpServerText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        tradeUpServerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        tradeUpServerText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        
        Button tradeUpServerButton = new Button(container, SWT.NONE);
        tradeUpServerButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProjectId = ((NewTradeWizardPage0) getWizard()
                        .getPage("NewTradeWizardPage0"))
                        .getTradeUpProjectCombo().getText();
                QueryServerDialog queryServerDialog = new QueryServerDialog(
                        e.display.getActiveShell(), e.getSource(),
                        upProjectId);
                queryServerDialog.addInformDialogListener(new InformDialogListener()
                {

                    @Override
                    public void handleEvent(InformDialogEvent dm)
                    {

                        java.util.List<String> l = ((QueryServerDialog) dm
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
                        tradeUpServerText.setText(s);
                    }
                });
                queryServerDialog.open();
            }
        });
        tradeUpServerButton.setText("...");

        Label tradeModelLabel = new Label(container, SWT.NONE);
        tradeModelLabel.setText("*�������ͣ�");

        tradeModelCombo = new Combo(container, SWT.READ_ONLY);
        tradeModelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));
        tradeModelCombo.setItems(new String[] { "0-����ͼ", "1-�ֹ�����" });
        tradeModelCombo.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                // ����������Ϊ����ͼ��ʽ
                if (tradeModelCombo.getSelectionIndex() == 0)
                {
                    tradeSrcPathLabel.setEnabled(false);
                    tradeSrcPathText.setText("");
                    tradeSrcPathText.setEnabled(false);
                    tradeSrcPathBtn.setEnabled(false);
                }
                // ����������Ϊ�ֹ����뷽ʽ
                else
                {
                    tradeSrcPathLabel.setEnabled(true);
                    tradeSrcPathText.setEnabled(true);
                    tradeSrcPathBtn.setEnabled(true);
                }
                dialogChanged();
            }
        });
        new Label(container, SWT.NONE);

        Label tradeServerModelLabel = new Label(container, SWT.NONE);
        tradeServerModelLabel.setText("*����ģʽ��");

        tradeServerModelCombo = new Combo(container, SWT.READ_ONLY);
        tradeServerModelCombo.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        tradeServerModelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 1, 1));
        tradeServerModelCombo.setItems(new String[] { "0-2 Wayͬ��ģʽ",
                "1-N Way�Ựģʽ" });
        new Label(container, SWT.NONE);

        Label inputDataLabel = new Label(container, SWT.NONE);
        inputDataLabel.setText("*���������");

        inputDataText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        inputDataText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        inputDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));

        Button inputDataBtn = new Button(container, SWT.NONE);
        inputDataBtn.setText("...");
        inputDataBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProjectId = ((NewTradeWizardPage0) getWizard()
                        .getPage("NewTradeWizardPage0"))
                        .getTradeUpProjectCombo().getText();
                InputDataItemDialog inputDataItemDialog = new InputDataItemDialog(
                        e.display.getActiveShell(), e.getSource(),
                        inputDataText.getText(), upProjectId);
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
                                inputDataText.setText(s);
                            }
                        });
                inputDataItemDialog.open();
            }
        });

        Label outputDataLabel = new Label(container, SWT.NONE);
        outputDataLabel.setText("*��������");

        outputDataText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        outputDataText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        outputDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));

        Button outputDataBtn = new Button(container, SWT.NONE);
        outputDataBtn.setText("...");
        outputDataBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProjectId = ((NewTradeWizardPage0) getWizard()
                        .getPage("NewTradeWizardPage0"))
                        .getTradeUpProjectCombo().getText();
                OutputDataItemDialog outputDataItemDialog = new OutputDataItemDialog(
                        e.display.getActiveShell(), e.getSource(),
                        outputDataText.getText(), upProjectId,inputDataText.getText());
                outputDataItemDialog
                        .addInformDialogListener(new InformDialogListener()
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
                                outputDataText.setText(s);
                            }
                        });
                outputDataItemDialog.open();
            }
        });

        Label tradeCallServiceLabel = new Label(container, SWT.NONE);
        tradeCallServiceLabel.setText("���õĽ��ף�");

        tradeCallServiceText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        tradeCallServiceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 1, 1));

        Button tradeCallServiceBtn = new Button(container, SWT.NONE);
        tradeCallServiceBtn.setText("...");

        tradeSrcPathLabel = new Label(container, SWT.NONE);
        tradeSrcPathLabel.setText("*Դ�ļ�·����");

        tradeSrcPathText = new Text(container, SWT.BORDER);
        tradeSrcPathText.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                dialogChanged();
            }
        });
        tradeSrcPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));

        tradeSrcPathBtn = new Button(container, SWT.NONE);
        tradeSrcPathBtn.setText("...");

        Label tradePreConditionLabel = new Label(container, SWT.NONE);
        tradePreConditionLabel.setText("ǰ��������");

        tradePreConditionText = new Text(container, SWT.BORDER | SWT.MULTI);
        tradePreConditionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 1, 1));
        new Label(container, SWT.NONE);

        Label tradePostConditionLabel = new Label(container, SWT.NONE);
        tradePostConditionLabel.setText("����������");

        tradePostConditionText = new Text(container, SWT.BORDER | SWT.MULTI);
        tradePostConditionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 1, 1));
        new Label(container, SWT.NONE);
        tradeCallServiceBtn.addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProject = ((NewTradeWizardPage0) getWizard().getPage(
                        "NewTradeWizardPage0")).getTradeUpProjectCombo()
                        .getText();
                QueryTradeDialog queryTradeDialog = new QueryTradeDialog(
                        e.display.getActiveShell(), e.getSource(), upProject, -1, Constants.ALL);
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
                                tradeCallServiceText.setText(s);
                            }
                        });
                queryTradeDialog.open();
            }

        });

        tradeSrcPathBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String upProject = ((NewTradeWizardPage0) getWizard().getPage(
                        "NewTradeWizardPage0")).getTradeUpProjectCombo()
                        .getText();
                TradeSrcPathDialog tradeSrcPathDialog = new TradeSrcPathDialog(
                        e.display.getActiveShell(), e.getSource(),
                        tradeSrcPathText.getText().trim(), upProject);
                tradeSrcPathDialog
                        .addInformDialogListener(new InformDialogListener()
                        {

                            @Override
                            public void handleEvent(InformDialogEvent dm)
                            {
                                java.util.List<String> l = ((TradeSrcPathDialog) dm
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
                                tradeSrcPathText.setText(s);
                            }
                        });
                tradeSrcPathDialog.open();
            }
        });

        // ��ʼ������
        // initData();
    }

    // ����״̬
    private void updateStatus(String message)
    {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    @Override
    public boolean canFlipToNextPage()
    {
        // if(getTradeDescText().getText().isEmpty()||getTradeIdText().getText().isEmpty()||getTradeNameText().getText().isEmpty())
        // return false;
        return false;
    }

    private void dialogChanged()
    {
        setPageComplete(true);
    }

    public boolean validInput()
    {
        if (tradeUpServerText.getText().isEmpty()
                || tradeModelCombo.getText().isEmpty()
                || tradeServerModelCombo.getText().isEmpty()
                || inputDataText.getText().isEmpty()
                || outputDataText.getText().isEmpty())
        {
            return false;
        } else
        {
            // �������������ֹ����뷽ʽ
            if (tradeModelCombo.getSelectionIndex() == 1)
            {
                if (tradeSrcPathText.getText().trim().isEmpty())
                {
                    return false;
                } else
                {
                    return true;
                }
            }
            // ����������������ͼ��ʽ
            else
            {
                return true;
            }
        }
    }
}
