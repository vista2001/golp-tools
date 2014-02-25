/* �ļ�����       QueryTradeDialog.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.11
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������ 
 */

package dev.golpDialogs;

import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dev.db.pojo.TTrade;
import dev.db.service.CommonDialogServiceImpl;
import dev.golpEvent.InformDialogEvent;
import dev.golpEvent.InformDialogListener;
import dev.util.Constants;

public class QueryTradeDialog extends TitleAreaDialog
{
    List listInDialog;
    java.util.List<String> listForReturn = null;
    java.util.List<InformDialogListener> listeners = new ArrayList<InformDialogListener>();

    /**
     * �ڲ�ѯ���Ľ����Ҫ�ų��Ľ���Id
     */
    private int selfId;

    /**
     * ��������Id
     */
    private String prjId;

    /**
     * ��ѯ���׵����ͣ������������������ǲ�ѯ���еĽ��ף�����ֻ��ѯ������ͼʵ�ֵ���δ������ͼ�󶨵Ľ���
     */
    private int type;

    /**
     * ������ʾ������ϸ��Ϣ���ı���
     */
    private Text tradeInfoText;

    /**
     * �����洢���в�ѯ���Ľ��׵���ϸ��Ϣ���ַ������飬�������һ��Ԫ�ض�Ӧһ�����׵���ϸ��Ϣ
     */
    private String[] tradeInfo;

    public java.util.List<String> getListForReturn()
    {
        return listForReturn;
    }

    /**
     * ����һ��QueryTradeDialog���󣬸öԻ�����������ĳ���ף�����a�����õ��������ף��öԻ������ʾĳ�����µ����н��ף���a����Id��
     * 
     * @param parentShell
     * @param obj
     * @param prjId Ҫ��ѯ�Ľ��������Ĺ���
     * @param selfId �ڲ�ѯ���Ľ����Ҫ�ų��Ľ���Id����ǰ��a������selfIdΪ-1������ʾ���ų��κν���Id����ʾ���н��׵�Id��
     */
    public QueryTradeDialog(Shell parentShell, Object obj, String prjId,
            int selfId, int type)
    {
        super(parentShell);
        this.prjId = prjId;
        this.selfId = selfId;
        this.type = type;
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite area = (Composite) super.createDialogArea(parent);
        new Label(area, SWT.NONE);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayout(new GridLayout(2, true));
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
                                             1));

        if (type == Constants.TFM_BINDING)
        {
            listInDialog = new List(container, SWT.BORDER | SWT.H_SCROLL
                                               | SWT.V_SCROLL | SWT.SINGLE);
        }
        else
        {
            listInDialog = new List(container, SWT.BORDER | SWT.H_SCROLL
                                               | SWT.V_SCROLL | SWT.MULTI);
        }
        listInDialog.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
                                                1, 1));
        listInDialog.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if ((listInDialog.getSelectionCount() == 1)
                    && (listInDialog.getSelection()[0].equals("����") == false))
                {
                    tradeInfoText.setText(tradeInfo[listInDialog.getSelectionIndex()]);
                }
                else
                {
                    tradeInfoText.setText("");
                }
            }
        });

        tradeInfoText = new Text(container, SWT.BORDER | SWT.READ_ONLY
                                            | SWT.WRAP | SWT.V_SCROLL
                                            | SWT.MULTI);
        tradeInfoText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
                                                 false, 1, 1));
        initData(prjId);
        this.setMessage("��ѡ����");

        return area;

    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                true);
        createButton(parent, IDialogConstants.CANCEL_ID,
                IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected void okPressed()
    {
        // DebugOut.println(listInDialog.isDisposed());
        String[] aops = listInDialog.getSelection();
        listForReturn = new ArrayList<String>();
        for (int i = 0; i < aops.length; i++)
        {
            listForReturn.add(aops[i]);
            // DebugOut.println(aops[i]);
        }
        notifyInformDialogEvent();
        super.okPressed();
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize()
    {
        return new Point(550, 400);
    }

    private void initData(String prjId)
    {
        CommonDialogServiceImpl commonDialogServiceImpl = new CommonDialogServiceImpl();
        try
        {
            java.util.List<TTrade> trades = commonDialogServiceImpl.tradeDialogQuery(
                    prjId, type);
            if (trades.isEmpty() == false)
            {
                tradeInfo = new String[trades.size()];
                int i = 0;
                for (TTrade trade : trades)
                {
                    if (trade.getTradeId() != selfId)
                    {
                        listInDialog.add(trade.getTradeId() + "");
                        tradeInfo[i] = trade.toString();
                        i++;
                    }
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        if (type == Constants.TFM_BINDING)
        {
            listInDialog.add("����");
        }
    }

    public void addInformDialogListener(InformDialogListener dl)
    {
        listeners.add(dl);
        // DebugOut.println("demo add");
    }

    public void notifyInformDialogEvent()
    {
        for (InformDialogListener idl : listeners)
        {
            idl.handleEvent(new InformDialogEvent(this));
            // DebugOut.println(idl.toString() + " is informed");
        }
    }
}
