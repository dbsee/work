package org.mydbsee.pic.sys.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.mydbsee.pic.sys.common.IAppConstants;
import org.mydbsee.pic.sys.common.IImageKey;

public class SysLogoffAction extends Action {

	public SysLogoffAction() {
		setId("cn.edu.jfcs.actions.logoff");
		setText("用户注销[&R]");
		setToolTipText("用户注销");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				IAppConstants.APPLICATION_ID,IImageKey.LOGOFF));
	}

	public void run() {	
		PlatformUI.getWorkbench().restart();
	}
}
