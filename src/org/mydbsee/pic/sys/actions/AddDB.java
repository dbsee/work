package org.mydbsee.pic.sys.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;
import org.mydbsee.db.mode.ConnectModel;

/**
 * 添加数据库
 */
public class AddDB extends Action
{
	ConnectModel selection;
	public AddDB(ConnectModel selection)
	{
		setText("添加数据库");
		this.selection = selection;
		if(!selection.getConnControl().isConnected())
		{
			setEnabled(false);
		}
	}
	
	@Override
	public void run()
	{
		AddDBDialog addDBDialog = new AddDBDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().
				getShell(),SWT.CLOSE,selection);
		addDBDialog.open();
	}
}