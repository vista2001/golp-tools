/* �ļ�����       InformDialogEvent.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.11
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������ 
 */

package dev.golpEvent;

import org.eclipse.jface.dialogs.Dialog;

import dev.util.DebugOut;

public class InformDialogEvent extends java.util.EventObject {   
    private Object a;
	public InformDialogEvent(Object source) {   
      super(source);//source���¼�Դ�������ڽ����Ϸ����ĵ����ť�¼��еİ�ť   
       //���� Event �ڹ���ʱ�������˶��� "source"�����߼�����Ϊ�ö�������������й� Event �Ķ���
      a=source;
    }
    
    public void say() {   
           DebugOut.println("This is say method...");   
    }
    
/*    public QueryDataItemDialog getdm(){
    	return (QueryDataItemDialog)a;
    }*/
    public Dialog getdm(){
    	return (Dialog)a;
    }
    
}  