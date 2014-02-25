package dev.remote;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dev.util.CommonUtil;

public class RemoteDialog extends Dialog {

	public RemoteDialog(Shell parentShell, String prjId, String defalutPath,
			int filter, int selection, java.util.List<String> tem) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
		this.filter = filter;
		this.tem = tem;
		this.selection = selection;
		path = defalutPath;
		this.prjId = prjId;
		// TODO Auto-generated constructor stub
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("选择文件");
		setShellStyle(SWT.RESIZE);
	}

	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout(1, false));

		Composite displayComposite = new Composite(area, SWT.BORDER);
		GridData gd_displayComposite = new GridData(SWT.FILL, SWT.FILL, true,
				false, 2, 1);
		gd_displayComposite.widthHint = 291;
		displayComposite.setLayoutData(gd_displayComposite);
		displayComposite.setLayout(new GridLayout(2, false));

		Label pathLabel = new Label(displayComposite, SWT.CENTER);
		pathLabel.setText("路径：");

		displayText = new Text(displayComposite, SWT.BORDER);
		displayText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		choseComposite = new Composite(area, SWT.NONE);
		choseComposite.setLayout(new GridLayout(1, false));
		GridData gd_choseComposite = new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1);
		gd_choseComposite.widthHint = 271;
		choseComposite.setLayoutData(gd_choseComposite);
		if (selection == REMOTEDIALOG_SINGLE) {
			list = new List(choseComposite, SWT.BORDER | SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.SINGLE);
		} else {
			list = new List(choseComposite, SWT.BORDER | SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.MULTI);
		}

		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		PreferenceStore ps = CommonUtil.initPs(prjId);

		host = ps.getString("remoteAddress");
		name = ps.getString("remoteUser");
		password = ps.getString("remotePwd");

		try {
			run = new RemoteUtil(host, name, password);
			run.connection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if (path == null) {
			try {
				lines = run.getRemoteDirs("pwd");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			try {
				lines = run.getRemoteDirs("cd "+path+"&&pwd");
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		path = lines.get(0);
		if(path.equals("/"))
			displayText.setText(path);
		else
			displayText.setText(path + "/");
		try {
			lines = run.getRemoteDirs("cd " + path + "&&ls -lXF .&&pwd");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			openMessagebox();
		}
		createList(lines);

		displayText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.keyCode == 13) {
					java.util.List<String> result = new ArrayList<String>();
					try {
						result = run.getRemoteDirs("cd "
								+ displayText.getText() + "&&echo 1||echo 0");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.out.println(result);
					if (result.get(result.size() - 1).equals("0")) {
						list.removeAll();
						list.add("路径错误");
						getButton(IDialogConstants.OK_ID).setEnabled(false);
						list.setEnabled(false);
					} else {
						try {
							lines = run.getRemoteDirs("cd "
									+ displayText.getText() + "&&ls -lXF&&pwd");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						list.removeAll();
						createList(lines);
						displayText.setText(path);
						if (filter == REMOTEDIALOG_FILE) {
							getButton(IDialogConstants.OK_ID).setEnabled(false);
						}
						list.setEnabled(true);
					}
				}

			}
		});

		list.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (filter == REMOTEDIALOG_FILE) {
					if (list.getSelectionCount() == 0) {
						getButton(IDialogConstants.OK_ID).setEnabled(false);
						getButton(IDialogConstants.OK_ID).setText("确定");
						return;
					}
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					for (String name : list.getSelection()) {
						if (!(name.endsWith("/") || name.equals(".."))) {
							getButton(IDialogConstants.OK_ID).setText("确定");
							return;
						}
					}
					getButton(IDialogConstants.OK_ID).setText("打开");
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				List tem = (List) e.getSource();
				if (tem.getSelectionCount() == 0)
					return;
				String next = list.getItem(tem.getSelectionIndex());
				if (next.equals("..")) {
					try {
						lines = run.getRemoteDirs("cd " + path
								+ "&&cd .. &&ls -lXF .&&pwd");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						openMessagebox();
					}
				} else if (next.endsWith("/")) {
					try {
						String Directory = nextDirectory(next);
						lines = run.getRemoteDirs("cd " + path + "&&cd "
								+ Directory + "&& ls -lXF .&& pwd ");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						openMessagebox();
					}
				} else {
					return;
				}
				list.removeAll();
				createList(lines);
				displayText.setText(path);
				if (filter == REMOTEDIALOG_FILE) {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				}
			}
		});

		return parent;
	}

	protected Button createButton(Composite parent, int id, String label,
			boolean defaultButton) {
		return null;
	}

	protected void initializeBounds() {
		// 我们可以利用原有的ID创建按钮,也可以用自定义的ID创建按钮
		// 但是调用的都是父类的createButton方法.
		super.createButton((Composite) getButtonBar(), IDialogConstants.OK_ID,
				"确定", false);
		super.createButton((Composite) getButtonBar(),
				IDialogConstants.CANCEL_ID, "取消", false);
		// 下面这个方法一定要加上,并且要放在创建按钮之后,否则就不会创建按钮
		super.initializeBounds();
		if (filter == REMOTEDIALOG_FILE) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			if (filter == REMOTEDIALOG_FILE) {
				if (list.getSelectionCount() == 1) {
					if (list.getSelection()[0].endsWith("/")
							|| list.getSelection()[0].equals("..")) {
						String Directory = nextDirectory(list.getSelection()[0]);
						try {
							lines = run.getRemoteDirs("cd " + path + "&&cd "
									+ Directory + "&& ls -lXF .&& pwd ");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						list.removeAll();
						createList(lines);
						displayText.setText(path);
						getButton(IDialogConstants.OK_ID).setEnabled(false);
					} else {
						run.disConnetion();
						getAbsolutePath(tem);
						close();
					}
				} else {
					boolean isAllDirectory = true;
					for (String line : list.getSelection()) {
						if (!(line.endsWith("/")||line.equals(".."))) {
							isAllDirectory = false;
							break;
						}
					}
					if (isAllDirectory) {
						String Directory = nextDirectory(list.getSelection()[0]);
						try {
							lines = run.getRemoteDirs("cd " + path + "&&cd "
									+ Directory + "&& ls -lXF .&& pwd ");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						list.removeAll();
						createList(lines);
						displayText.setText(path);
						getButton(IDialogConstants.OK_ID).setEnabled(false);
					} else {
						run.disConnetion();
						getAbsolutePath(tem);

						for (int i = 0; i < tem.size(); i++) {
							if (tem.get(i).endsWith("/")||tem.get(i).endsWith("..")) {
								tem.remove(i);
								i--;
							}
						}
						close();
					}
				}

			} else {
				run.disConnetion();
				getAbsolutePath(tem);
				close();
			}
		}
		if (buttonId == IDialogConstants.CANCEL_ID) {
			run.disConnetion();
			path = null;
			close();
		}
	}

	protected Point getInitialSize() {
		return new Point(300, 300);
	}

	private void createList(java.util.List<String> lines) {
		list.add("..");
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).split(" ").length <= 7) {
				if (i == lines.size() - 1) {
					path = lines.get(i);
					if (!path.equals("/"))
						path += "/";
					break;
				}
				continue;
			}
			String line = lines.get(i);
			String[] tem = line.split("[ ]+");
			if (filter == REMOTEDIALOG_DIRECTORY) {
				if (line.endsWith("/")) {
					if (line.contains("->"))
						list.add(tem[tem.length - 3] + tem[tem.length - 2]
								+ tem[tem.length - 1]);
					else
						list.add(tem[tem.length - 1]);
				}
			} else if (filter == REMOTEDIALOG_FILE) {
				if (line.contains("->"))
					list.add(tem[tem.length - 3] + tem[tem.length - 2]
							+ tem[tem.length - 1]);
				else
					list.add(tem[tem.length - 1]);
			}
		}
	}

	private String nextDirectory(String next) {
		if (next.contains("->")) {
			return next.substring(0, next.indexOf("->"));
		} else {
			return next;
		}
	}

	public void getAbsolutePath(java.util.List<String> tem) {
		if (path == null)
			return;
		if (list.getSelection().length == 0) {
			// if (path.equals("/"))
			tem.add(path);
			// else
			// tem.add(path + "/");
			return;
		}
		for (String name : list.getSelection()) {
			if(name.endsWith("*")){	
				name=name.substring(0, name.length()-2);
			}
			if (name.contains("->")) {
				String fake = name.substring(name.indexOf("->") + 2,
						name.length() - 1);
				if (fake.startsWith("/"))
					tem.add(fake);
				// else if (path.equals("/"))
				// tem.add(path + fake);
				else
					tem.add(path + fake);
			} else {
				// if (path.equals("/"))
				tem.add(path + name);
				// else
				// tem.add(path + "/" + name);
			}
		}
	}

	private void openMessagebox() {
		MessageBox box = new MessageBox(getShell(), SWT.OK);
		box.setText("失败");
		box.setMessage("没有权限打开此目录!");
		box.open();
	}

	public String turnToRelative(String absolute) {
		String relative = absolute;

		return relative;
	}

	Composite choseComposite;
	private Text displayText;
	String host;
	String name;
	String password;
	String path;
	java.util.List<String> lines;
	List list;
	RemoteUtil run;
	java.util.List<String> tem;
	public static final int REMOTEDIALOG_DIRECTORY = 1;
	public static final int REMOTEDIALOG_FILE = 2;
	int filter;
	public static final int REMOTEDIALOG_SINGLE = 1;
	public static final int REMOTEDIALOG_MULTI = 2;
	int selection;
	String prjId;
}
