package dev.wizards.newTrade;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.wizards.newAop.InformDialogEvent;
import dev.wizards.newAop.InformDialogListener;
import dev.wizards.newAop.QueryDataItemDialog;
import dev.wizards.newAop.QueryTradeDialog;

import org.eclipse.swt.widgets.Button;

public class NewTradeWizardPage1 extends WizardPage{
	private ISelection selection;
	private Combo tradeUpServerCombo;
	private Combo tradeModelCombo;
	private Combo tradeServerModelCombo;
	private Text inputDataText;
	private Text outputDataText;
	private Text tradeCallServiceText;
	private Text tradePreConditionText;
	private Text tradePostConditionText;

	public Combo getTradeUpServerCombo() {
		return tradeUpServerCombo;
	}


	public Combo getTradeModelCombo() {
		return tradeModelCombo;
	}


	public Combo getTradeServerModelCombo() {
		return tradeServerModelCombo;
	}


	public Text getInputDataText() {
		return inputDataText;
	}


	public Text getOutputDataText() {
		return outputDataText;
	}


	public Text getTradeCallServiceText() {
		return tradeCallServiceText;
	}


	public Text getTradePreConditionText() {
		return tradePreConditionText;
	}


	public Text getTradePostConditionText() {
		return tradePostConditionText;
	}


	public NewTradeWizardPage1(ISelection selection) {
		super("wizardPage");
		setTitle("�½�������");
		setDescription("����򵼽�ָ�������GOLP���׵Ĵ���");
		this.selection = selection;
	}


	@Override
	/**������ҳ��Ŀؼ�*/
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label tradeUpServerLabel = new Label(container, SWT.NONE);
		tradeUpServerLabel.setText("*�����������");
		
		tradeUpServerCombo = new Combo(container, SWT.READ_ONLY);
		tradeUpServerCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		Label tradeModelLabel = new Label(container, SWT.NONE);
		tradeModelLabel.setText("*��������");
		
		tradeModelCombo = new Combo(container, SWT.READ_ONLY);
		tradeModelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tradeModelCombo.setItems(new String[]{"0-����ͼ","1-�ֹ�����"});
		new Label(container, SWT.NONE);
		
		Label tradeServerModelLabel = new Label(container, SWT.NONE);
		tradeServerModelLabel.setText("*����ģʽ");
		
		tradeServerModelCombo = new Combo(container, SWT.READ_ONLY);
		tradeServerModelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tradeServerModelCombo.setItems(new String[]{"0-ģʽ0","1-ģʽ1"});
		new Label(container, SWT.NONE);
		
		Label inputDataLabel = new Label(container, SWT.NONE);
		inputDataLabel.setText("*����������");
		
		inputDataText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		inputDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button inputDataBtn = new Button(container, SWT.NONE);
		inputDataBtn.setText("...");
		inputDataBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				QueryDataItemDialog queryAop=new QueryDataItemDialog(e.display.getActiveShell(),e.getSource());
				queryAop.addInformDialogListener(new InformDialogListener() {
					
					@Override
					public void handleEvent(InformDialogEvent dm) {
						java.util.List<String> l=((QueryDataItemDialog)dm.getdm()).getListForReturn();
						String s="";
						for (String string : l) {
							if(s.equals("")){
								s+=string;
							}else{
								s+="|"+string;
							}
						}
						inputDataText.setText(s);
					}
				});
				queryAop.open();
			}
		});
		Label outputDataLabel = new Label(container, SWT.NONE);
		outputDataLabel.setText("*���������");
		
		outputDataText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		outputDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button outputDataBtn = new Button(container, SWT.NONE);
		outputDataBtn.setText("...");
		outputDataBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				QueryDataItemDialog queryAop=new QueryDataItemDialog(e.display.getActiveShell(),e.getSource());
				queryAop.addInformDialogListener(new InformDialogListener() {
						
					@Override
					public void handleEvent(InformDialogEvent dm) {
						java.util.List<String> l=((QueryDataItemDialog)dm.getdm()).getListForReturn();
						String s="";
						for (String string : l) {
							if(s.equals("")){
								s+=string;
							}else{
								s+="|"+string;
							}
						}
						outputDataText.setText(s);
					}
				});
				queryAop.open();
			}
		});
		
		Label tradeCallServiceLabel = new Label(container, SWT.NONE);
		tradeCallServiceLabel.setText("���õĽ���");
		
		tradeCallServiceText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		tradeCallServiceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button tradeCallServiceBtn = new Button(container, SWT.NONE);
		tradeCallServiceBtn.setText("...");
		
		Label tradePreConditionLabel = new Label(container, SWT.NONE);
		tradePreConditionLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		tradePreConditionLabel.setText("ǰ������");
		
		tradePreConditionText = new Text(container, SWT.BORDER | SWT.MULTI);
		tradePreConditionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		new Label(container, SWT.NONE);
		
		Label tradePostConditionLabel = new Label(container, SWT.NONE);
		tradePostConditionLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		tradePostConditionLabel.setText("��������");
		
		tradePostConditionText = new Text(container, SWT.BORDER | SWT.MULTI);
		tradePostConditionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		new Label(container, SWT.NONE);
		tradeCallServiceBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				QueryTradeDialog queryTradeDialog=new QueryTradeDialog(e.display.getActiveShell(),e.getSource());
				queryTradeDialog.addInformDialogListener(new InformDialogListener() {
					
					@Override
					public void handleEvent(InformDialogEvent dm) {
						java.util.List<String> l=((QueryTradeDialog)dm.getdm()).getListForReturn();
						String s="";
						for (String string : l) {
							if(s.equals("")){
								s+=string;
							}else{
								s+="|"+string;
							}
						}
						tradeCallServiceText.setText(s);
					}
				});
				queryTradeDialog.open();
			}
			
		});
		//��ʼ������
		initData();
	}

	// ����״̬
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	@Override
	public boolean canFlipToNextPage() {
		//if(getTradeDescText().getText().isEmpty()||getTradeIdText().getText().isEmpty()||getTradeNameText().getText().isEmpty())
			//return false;
		return true;
	}
	private void initData(){
		// ��ʼ�����������������
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn();
		ResultSet rs = null;
		try {
			rs = dbConnectImpl.retrive("select * from server order by serverid");
			while (rs.next()) {
				tradeUpServerCombo.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				dbConnectImpl.closeConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
