package dev.wizards.newAop;

import org.eclipse.jface.dialogs.Dialog;

public class InformDialogEvent extends java.util.EventObject {   
    private Object a;
	public InformDialogEvent(Object source) {   
      super(source);//source���¼�Դ�������ڽ����Ϸ����ĵ����ť�¼��еİ�ť   
       //���� Event �ڹ���ʱ�������˶��� "source"�����߼�����Ϊ�ö�������������й� Event �Ķ���
      a=source;
    }
    
    public void say() {   
           System.out.println("This is say method...");   
    }
    
/*    public QueryDataItemDialog getdm(){
    	return (QueryDataItemDialog)a;
    }*/
    public Dialog getdm(){
    	return (Dialog)a;
    }
    
}  