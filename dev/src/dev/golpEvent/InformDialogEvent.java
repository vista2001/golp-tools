/* 文件名：       InformDialogEvent.java
 * 修改人：       rxy
 * 修改时间：   2013.12.11
 * 修改内容：   用DebugOut.println方法替换System.out.println方法。 
 */

package dev.golpEvent;

import org.eclipse.jface.dialogs.Dialog;

import dev.util.DebugOut;

public class InformDialogEvent extends java.util.EventObject {   
    private Object a;
	public InformDialogEvent(Object source) {   
      super(source);//source—事件源对象—如在界面上发生的点击按钮事件中的按钮   
       //所有 Event 在构造时都引用了对象 "source"，在逻辑上认为该对象是最初发生有关 Event 的对象
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