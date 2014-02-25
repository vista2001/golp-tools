/* �ļ�����       LoadProjectWizardPage0.java
 * ������           ���ļ���������LoadProjectWizardPage0������Ϊ���빤���򵼵���ҳ��
 * �����ˣ�       rxy
 * ����ʱ�䣺   2013.12.26
 */

package dev.wizard.loadProject;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * ��LoadProjectWizardPage0��Ϊ���빤���򵼵���ҳ��
 * @author rxy
 */
public class LoadProjectWizardPage0 extends WizardPage
{
    private ISelection selection;
    
    /**
     * �����������ݿ��ַ���ı���
     */
    private Text dbAddressText;
    
    /**
     * �����������ݿ�˿ڵ��ı���
     */
    private Text dbPortText;
    
    /**
     * �����������ݿ�ʵ�����ı���
     */
    private Text dbInstanceText;
    
    /**
     * �����������ݿ��û������ı���
     */
    private Text dbUserText;
    
    /**
     * �����������ݿ�������ı���
     */
    private Text dbPwdText;
    
    /**
     * ��List�������ø���ҳ�е������ı���
     */
    private List<Text> list = new ArrayList<Text>();
    
    /**
     * ���������������ַ���ı���
     */
    private Text remoteAddressText;
    
    /**
     * ��������������û����ı���
     */
    private Text remoteUserText;
    
    /**
     * �������������������ı���
     */
    private Text remotePwdText;

    public ISelection getSelection()
    {
        return selection;
    }

    public Text getDbAddressText()
    {
        return dbAddressText;
    }

    public Text getDbPortText()
    {
        return dbPortText;
    }

    public Text getDbInstanceText()
    {
        return dbInstanceText;
    }

    public Text getDbUserText()
    {
        return dbUserText;
    }

    public Text getDbPwdText()
    {
        return dbPwdText;
    }

    public Text getRemoteAddressText()
    {
        return remoteAddressText;
    }

    public Text getRemoteUserText()
    {
        return remoteUserText;
    }

    public Text getRemotePwdText()
    {
        return remotePwdText;
    }

    public LoadProjectWizardPage0(ISelection selection)
    {
        super("LoadProjectWizardPage0");
        setTitle("���빤��");
        setDescription("������������Ĺ��̵����ݿ�������Ϣ");
        this.selection = selection;
    }

    @Override
    /**������ҳ��Ŀؼ�*/
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        container.setLayout(new GridLayout(2, false));

        Label dbAddressLabel = new Label(container, SWT.NONE);
        dbAddressLabel.setText("*���ݿ��ַ��");

        dbAddressText = new Text(container, SWT.BORDER);
        dbAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));

        Label dbPortLabel = new Label(container, SWT.NONE);
        dbPortLabel.setText("*���ݿ�˿ڣ�");

        dbPortText = new Text(container, SWT.BORDER);
        dbPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));

        Label dbInstanceLabel = new Label(container, SWT.NONE);
        dbInstanceLabel.setText("*���ݿ�ʵ����");

        dbInstanceText = new Text(container, SWT.BORDER);
        dbInstanceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));

        Label dbUserLabel = new Label(container, SWT.NONE);
        dbUserLabel.setText("*���ݿ��û���");

        dbUserText = new Text(container, SWT.BORDER);
        dbUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));

        Label dbPwdLabel = new Label(container, SWT.NONE);
        dbPwdLabel.setText("*���ݿ���");

        dbPwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
        dbPwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));

        list.add(dbAddressText);
        list.add(dbInstanceText);
        list.add(dbPortText);
        list.add(dbUserText);
        list.add(dbPwdText);
        
        Label remoteAddressLabel = new Label(container, SWT.NONE);
        remoteAddressLabel.setText("��������ַ��");
        
        remoteAddressText = new Text(container, SWT.BORDER);
        remoteAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label remoteUserLabel = new Label(container, SWT.NONE);
        remoteUserLabel.setText("�������û���");
        
        remoteUserText = new Text(container, SWT.BORDER);
        remoteUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label remotePwdLabel = new Label(container, SWT.NONE);
        remotePwdLabel.setText("���������");
        
        remotePwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
        remotePwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        addModifyListener();
    }

    /**
     * Ϊ��Ա����list�е�����Ԫ�أ����ModifyListener��
     */
    private void addModifyListener()
    {
        for (Text text : list)
        {
            text.addModifyListener(new ModifyListener()
            {
                public void modifyText(ModifyEvent e)
                {
                    setPageComplete(true);
                }
            });
        }
    }

    // �ж��Ƿ���Խ�����һҳ
    @Override
    public boolean canFlipToNextPage()
    {
        return false;
    }

    /**
     * �÷��������ж����б������Ƿ������롣
     * @return �����б���������룬�򷵻�true�����򷵻�false��
     */
    public boolean isInputValid()
    {
        for(Text text : list)
        {
            if(text.getText().trim().isEmpty())
            {
                return false;
            }
        }
        return true;
    }
}
