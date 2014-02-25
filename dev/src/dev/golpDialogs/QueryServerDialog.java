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

import dev.db.pojo.TServer;
import dev.db.service.CommonDialogServiceImpl;
import dev.golpEvent.InformDialogEvent;
import dev.golpEvent.InformDialogListener;

import org.eclipse.swt.widgets.Text;

public class QueryServerDialog extends TitleAreaDialog
{
    List listInDialog;
    java.util.List<String> listForReturn = null;
    java.util.List<InformDialogListener> listeners = new ArrayList<InformDialogListener>();
    
    /**
     * 所属工程Id
     */
    private String prjId;

    private String[] serverInfo;
    private Text serverInfoText;
    
    public java.util.List<String> getListForReturn() {
        return listForReturn;
    }

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public QueryServerDialog(Shell parentShell, Object obj, String prjId) {
        super(parentShell);
        this.prjId = prjId;
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        new Label(area, SWT.NONE);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayout(new GridLayout(2, true));
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        listInDialog = new List(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        listInDialog.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        
        serverInfoText = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        serverInfoText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        listInDialog.addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                serverInfoText.setText(serverInfo[listInDialog.getSelectionIndex()]);
            }

        });
        initData(prjId);
        this.setMessage("请选择所属服务程序");

        return area;

    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected void okPressed() {
        String[] aops = listInDialog.getSelection();
        listForReturn = new ArrayList<String>();
        for (int i = 0; i < aops.length; i++) {
            listForReturn.add(aops[i]);
        }
        notifyInformDialogEvent();
        super.okPressed();
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(450, 300);
    }
    
    private void initData(String prjId)
    {
        CommonDialogServiceImpl commonDialogServiceImpl = new CommonDialogServiceImpl();
        try
        {
            java.util.List<TServer> servers = commonDialogServiceImpl.serverDialogQuery(prjId);
            if(servers.isEmpty() == false)
            {
                serverInfo = new String[servers.size()];
                int i = 0;
                for(TServer server : servers)
                {
                    listInDialog.add(server.getServerId());
                    serverInfo[i] = server.toString();
                    i++;
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void addInformDialogListener(InformDialogListener dl) {
        listeners.add(dl);
    }

    public void notifyInformDialogEvent() {
        for (InformDialogListener idl : listeners) {
            idl.handleEvent(new InformDialogEvent(this));
        }
    }
}
