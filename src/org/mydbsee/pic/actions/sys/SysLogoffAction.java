package org.mydbsee.pic.actions.sys;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.mydbsee.pic.common.sys.IAppConstants;
import org.mydbsee.pic.common.sys.IImageKey;

public class SysLogoffAction extends Action {

	public SysLogoffAction() {
		setId("cn.edu.jfcs.actions.logoff");
		setText("�û�ע��[&R]");
		setToolTipText("�û�ע��");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				IAppConstants.APPLICATION_ID,IImageKey.LOGOFF));
	}

	public void run() {	
		PlatformUI.getWorkbench().restart();
	}
}
