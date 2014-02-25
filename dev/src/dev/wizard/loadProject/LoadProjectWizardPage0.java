/* 文件名：       LoadProjectWizardPage0.java
 * 描述：           该文件定义了类LoadProjectWizardPage0，该类为载入工程向导的向导页。
 * 创建人：       rxy
 * 创建时间：   2013.12.26
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
 * 类LoadProjectWizardPage0，为载入工程向导的向导页。
 * @author rxy
 */
public class LoadProjectWizardPage0 extends WizardPage
{
    private ISelection selection;
    
    /**
     * 用于输入数据库地址的文本框。
     */
    private Text dbAddressText;
    
    /**
     * 用于输入数据库端口的文本框。
     */
    private Text dbPortText;
    
    /**
     * 用于输入数据库实例的文本框。
     */
    private Text dbInstanceText;
    
    /**
     * 用于输入数据库用户名的文本框。
     */
    private Text dbUserText;
    
    /**
     * 用于输入数据库密码的文本框。
     */
    private Text dbPwdText;
    
    /**
     * 该List用于引用该向导页中的所有文本框。
     */
    private List<Text> list = new ArrayList<Text>();
    
    /**
     * 用于输入服务器地址的文本框。
     */
    private Text remoteAddressText;
    
    /**
     * 用于输入服务器用户的文本框。
     */
    private Text remoteUserText;
    
    /**
     * 用于输入服务器口令的文本框。
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
        setTitle("载入工程");
        setDescription("请输入欲载入的工程的数据库连接信息");
        this.selection = selection;
    }

    @Override
    /**创建向导页面的控件*/
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        setControl(container);
        container.setLayout(new GridLayout(2, false));

        Label dbAddressLabel = new Label(container, SWT.NONE);
        dbAddressLabel.setText("*数据库地址：");

        dbAddressText = new Text(container, SWT.BORDER);
        dbAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));

        Label dbPortLabel = new Label(container, SWT.NONE);
        dbPortLabel.setText("*数据库端口：");

        dbPortText = new Text(container, SWT.BORDER);
        dbPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));

        Label dbInstanceLabel = new Label(container, SWT.NONE);
        dbInstanceLabel.setText("*数据库实例：");

        dbInstanceText = new Text(container, SWT.BORDER);
        dbInstanceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));

        Label dbUserLabel = new Label(container, SWT.NONE);
        dbUserLabel.setText("*数据库用户：");

        dbUserText = new Text(container, SWT.BORDER);
        dbUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));

        Label dbPwdLabel = new Label(container, SWT.NONE);
        dbPwdLabel.setText("*数据库口令：");

        dbPwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
        dbPwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));

        list.add(dbAddressText);
        list.add(dbInstanceText);
        list.add(dbPortText);
        list.add(dbUserText);
        list.add(dbPwdText);
        
        Label remoteAddressLabel = new Label(container, SWT.NONE);
        remoteAddressLabel.setText("服务器地址：");
        
        remoteAddressText = new Text(container, SWT.BORDER);
        remoteAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label remoteUserLabel = new Label(container, SWT.NONE);
        remoteUserLabel.setText("服务器用户：");
        
        remoteUserText = new Text(container, SWT.BORDER);
        remoteUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        Label remotePwdLabel = new Label(container, SWT.NONE);
        remotePwdLabel.setText("服务器口令：");
        
        remotePwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
        remotePwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        addModifyListener();
    }

    /**
     * 为成员变量list中的所有元素，添加ModifyListener。
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

    // 判断是否可以进入下一页
    @Override
    public boolean canFlipToNextPage()
    {
        return false;
    }

    /**
     * 该方法用于判断所有必填项是否都已输入。
     * @return 若所有必填项都已输入，则返回true，否则返回false。
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
